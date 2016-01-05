package wimt.othertree.client;
import microsoft.aspnet.signalr.client.*;
import microsoft.aspnet.signalr.client.transport.*;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

import java.io.Console;
import java.util.HashMap;
import java.util.IdentityHashMap;

/**
 * Created by Nick Cuthbert on 05/01/2016.
 */
public class OtherTreeClient {


    protected String url;
    protected String token;
    private HubConnection connection;
    private HubProxy hub;

    public OtherTreeClient(String url, String token){
        init(url,token,true);
    }

    public OtherTreeClient(String url, String token,boolean useDefaultUrl){
        init(url,token,useDefaultUrl);
    }

    protected OtherTreeClient(){
    }


    protected void  init(String url, String token,boolean useDefaultUrl){
        this.url=url;
        this.token=token;
        HashMap<Object, Object> dict = new HashMap<Object, Object>();
        String query = "&username="+ token;
        connection= new HubConnection(url, query, useDefaultUrl, new NullLogger());
    }


    public void connect(){
        hub = connection.createHubProxy("Bedrock");
        connection.start(new AutomaticTransport(connection.getLogger()));
    }

    public <T> void water (T water,Version v){

    }

    public <T> void waterAsync (T water,Version v){

    }


    public <T> void charge (T charge,Version v){

    }
}
