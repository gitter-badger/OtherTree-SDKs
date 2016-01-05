using System;
using System.Collections.Generic;
using System.Linq;
using Google.Protobuf;
using System.Runtime;
using System.Threading.Tasks;
using Microsoft.AspNet.SignalR.Client;
using Microsoft.AspNet.SignalR.Client.Hubs;
using Microsoft.AspNet.SignalR.Client.Transports;
using Wimt.OtherTree.References;
using Version = Wimt.OtherTree.Version;

namespace OtherTreeClientSDK
{
    // This project can output the Class library as a NuGet Package.
    // To enable this option, right-click on the project and select the Properties menu item. In the Build tab select "Produce outputs on build".
    public delegate void OnThud<T>(T message) where T: IMessage<T>, new();

    public class TypeDescription
    {
        public string TypeName { get; set; }
        public Version Version { get; set; }

        public Type Type { get; set; }
        protected TypeDescription(Type type, string typeName, Version? version)
        {
            Type = type;
            TypeName = typeName;
            Version = version.HasValue?version.Value: new Version();
        }
    }

    public class TypeDescription<TMessage>: TypeDescription
        where TMessage : IMessage<TMessage>, new ()
    {
        public TypeDescription(Version? version):base(typeof(TMessage),typeof(TMessage).FullName,version)
        {
            
        }

        public TypeDescription() : base(typeof (TMessage), typeof (TMessage).FullName, null)
        {
        }
    }

    public class OtherTreeClient: IDisposable
    {
        private string _url;
        private bool _connected = false;
        private string _token;
        private IList<KeyValuePair<TypeDescription, Delegate>> _handlers;
        private HubConnection _hubConnection;
        private IHubProxy _bedrockProxy;
        public OtherTreeClient(string url, string token,bool useDefaultUrl=true)
        {
            _url = url;
            _token = token;
            var qs = new Dictionary<string, string>();
            qs.Add("username", token);
            _handlers=new List<KeyValuePair<TypeDescription, Delegate>>();            
            _hubConnection = new HubConnection(_url,token,useDefaultUrl);

            //  TODO Add proper auth        
            //  var cache= new CredentialCache();
            //  cache.Add(/*Add credentials here*/);
            //  hubConnection.Credentials = cache;
            
            _bedrockProxy= _hubConnection.CreateHubProxy("Bedrock");          
        }

        public void Connect()
        {
            ConnectAsync().Wait();
        }

        public async Task ConnectAsync()
        {            
            await _hubConnection.Start(new WebSocketTransport());

            _bedrockProxy.On("Thud", (string typeName, Wimt.OtherTree.Version? version, byte[] payload) =>
            {
                foreach (var handler in _handlers)
                {
                    if (typeName == handler.Key.TypeName &&
                        (!version.HasValue || handler.Key.Version.IsCompatableWith(version.Value)))
                    {
                        var sap = (Google.Protobuf.IMessage)(handler.Key.Type.GetConstructor(new Type[] { }).Invoke(new object[] { }));
                        sap.MergeFrom(payload);
                        handler.Value.DynamicInvoke(new object[] { sap });
                    }
                }
            });

            _connected = true;
        }

        public void AddThudHandler<T>(OnThud<T> handler, Version? version = null)
            where T : IMessage<T>,new()
        {
            if (version.HasValue)
            {
                _handlers.Add(new KeyValuePair<TypeDescription, Delegate>(new TypeDescription<T>(version),handler));
            }
            else
            {
                _handlers.Add(new KeyValuePair<TypeDescription, Delegate>(new TypeDescription<T>(), handler));
            }            
        }

        public void RemoveThudHandler<T>(OnThud<T> handler)
            where T : IMessage<T>, new()
        {
            for (int i = _handlers.Count-1; i >=0; --i)
            {
                if (_handlers[i].Value.GetInvocationList().Contains(handler))
                {
                    _handlers.RemoveAt(i);
                }
            }
        }

        public ChargeReference Charge<T>(IMessage<T> charge, Version? chargeVersion = null)
            where T : IMessage<T>, new()            
        {
            var chargeTask = ChargeAsync(charge, chargeVersion);
            chargeTask.Wait();            
            return chargeTask.Result;
        }


        public async Task<ChargeReference> ChargeAsync<T>(IMessage<T> charge, Version? chargeVersion = null)
            where T : IMessage<T>, new()            
        {
            if (!_connected)
            {
                await ConnectAsync();
            }
            
            return await _bedrockProxy.Invoke<ChargeReference>("Charge",typeof (T).FullName,chargeVersion.HasValue ? chargeVersion.Value : new Version(), charge.ToByteArray());
        }

        public void Discharge(ChargeReference reference)
        {
            DischargeAsync(reference).Wait();
        }

        public async  Task DischargeAsync(ChargeReference reference)
        {
            if (!_connected)
            {
                await ConnectAsync();
            }            
            await _bedrockProxy.Invoke("Discharge",reference);
        }


        public void Water<T>(T water, Version? version = null) where T : IMessage<T>, new()
        {
            WaterAsync<T>(water, version).Wait();
        }

        public async Task WaterAsync<T>(T water, Version? version = null) where T : IMessage<T>, new()
        {
            if (!version.HasValue)
            {
                version=new Version();
            }
        
            await _bedrockProxy.Invoke("Water",typeof(T).FullName, version,water.ToByteArray());            
        }

        public void Disconnect()
        {
            if (_connected)
            {
                _hubConnection.Stop();
                _connected = false;
            }
        }
        public void Dispose()
        {
            if (_connected)
            {
                _hubConnection.Dispose();
                _connected = false;
            }
            
        }
    }
}
