package wimt.othertree.client;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.NullLogger;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler3;
import microsoft.aspnet.signalr.client.transport.LongPollingTransport;

/**
 * Created by Nick Cuthbert on 05/01/2016.
 */


public class OtherTreeClient implements AutoCloseable {



    protected String url;
    protected String token;
    private HubConnection connection;
    private HubProxy hub;
    private boolean connected=false;
    private List<AnnotatedOnThudListener> onThudListeners;
    private Logger logger;

    public OtherTreeClient(String url, String token){
        this(url, token, true);
    }

    public OtherTreeClient(String url, String token,boolean useDefaultUrl){
        this(url, token, useDefaultUrl, new NullLogger());
    }

    public OtherTreeClient(String url, String token,boolean useDefaultUrl, Logger logger){
        this.logger=logger;
        onThudListeners=new ArrayList<>();
        init(url,token,useDefaultUrl);
    }

    protected OtherTreeClient(){
        this("","",true,new NullLogger());
    }



    protected void  init(String url, String token,boolean useDefaultUrl){
        this.url=url;
        this.token=token;
        HashMap<Object, Object> dict = new HashMap<Object, Object>();
        String query = "&username="+ token;
        connection= new HubConnection(url, query, useDefaultUrl, logger);
    }


    public SignalRFuture<Void> futureConnection(){
        hub = connection.createHubProxy("Bedrock");


        hub.on("Thud", new SubscriptionHandler3<String, Version, String>() {
            @Override
            public void run(String type, Version version, String payload){
                byte[] bytes=toByteArray(payload);
                for (AnnotatedOnThudListener handler: onThudListeners) {
                    if(handler.applicable(type,version)){
                        try {
                            handler.onThud(bytes);
                        } catch (InvalidProtocolBufferException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, String.class, Version.class, String.class);


        SignalRFuture<Void> futureConnectionTask = connection.start(new LongPollingTransport(connection.getLogger()));
        futureConnectionTask = futureConnectionTask.done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {

                synchronized (this) {
                    connected = true;
                }
            }
        });

        return futureConnectionTask;
    }

    byte[] toByteArray(String hexString){
        return Base64.decode(hexString, Base64.DEFAULT);
    }

    public void connect() throws ExecutionException, InterruptedException {
        futureConnection().get();
    }

    private static int[] toIntArray(byte[] bytes){
        final int[] ints = new int[bytes.length];
        for(int i=0;i<ints.length;i++){
            ints[i] = bytes[i]& 0xFF;
        }
        return ints;
    }



    public <T extends com.google.protobuf.GeneratedMessage> SignalRFuture<Void> futureWater(T water, Version version){
        int[] ints= toIntArray(water.toByteArray());
        return hub.invoke("Water",toPascalCase(water.getDescriptorForType().getFullName()), version,ints);
    }

    public <T extends com.google.protobuf.GeneratedMessage> void water (T water,Version version) throws ExecutionException, InterruptedException {
        futureWater(water, version).get();
    }



            public <T extends com.google.protobuf.GeneratedMessage> SignalRFuture<ChargeReference> futureCharge(T charge, Version version){
                int[] ints= toIntArray(charge.toByteArray());
                return hub.invoke(ChargeReference.class, "Charge", toPascalCase(charge.getDescriptorForType().getFullName()), version, ints);
            }
            public <T extends com.google.protobuf.GeneratedMessage> SignalRFuture<ChargeReference> futureCharge(T charge){
                return futureCharge(charge, new Version());
            }

            public SignalRFuture<Void> futureDischarge(ChargeReference reference){
                return hub.invoke("Discharge", reference);
            }


        public <T extends com.google.protobuf.GeneratedMessage> ChargeReference charge (T charge,Version version) throws ExecutionException, InterruptedException {
            return futureCharge(charge,version).get();
        }
        public <T extends com.google.protobuf.GeneratedMessage> ChargeReference charge (T charge) throws ExecutionException, InterruptedException {
            return charge(charge, new Version());
        }

        public  void discharge(ChargeReference reference) throws ExecutionException, InterruptedException {
            futureDischarge(reference).get();
        }


        public <T extends GeneratedMessage> void addOnThudListener(Class<T> expectedType,ThudListener<T> listener,Version version){
            AnnotatedOnThudListener<T> handler = new AnnotatedOnThudListener<>(expectedType,listener, version);
            onThudListeners.add(handler);
        }

        public <T extends GeneratedMessage> void addOnThudListener(Class<T> expectedType,ThudListener<T> listener){
            addOnThudListener(expectedType, listener, new Version());
        }

        static String toPascalCase(String camelCase){
            StringBuilder builder=new StringBuilder();
            String[] strings=camelCase.split("\\.");
            for(int i=0; i<strings.length; ++i){

            if(strings[i].length()>0){
                builder.append(Character.toUpperCase(strings[i].charAt(0)));
                builder.append(strings[i].substring(1));
                if(i!=strings.length-1){
                    builder.append('.');
                }
            }
        }
        return builder.toString();
    }

    public <T extends GeneratedMessage>void removeOnThudListener(ThudListener<T> thudListener){
        for(int i=onThudListeners.size()-1; i>=0; --i){
            if(onThudListeners.get(i).thudListener==thudListener){
                onThudListeners.remove(i);
            }
        }
    }


    public synchronized void disconnect(){
        if(connected){
            connection.disconnect();
            connected=false;
        }
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
