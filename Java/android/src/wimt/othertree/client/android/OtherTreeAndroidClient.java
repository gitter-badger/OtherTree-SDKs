package wimt.othertree.client.android;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.*;
import wimt.othertree.client.OtherTreeClient;

/**
 * Created by Nick Cuthbert on 05/01/2016.
 */
public class OtherTreeAndroidClient extends OtherTreeClient{

        public OtherTreeAndroidClient(String url, String token){
           this(url,token,true);
        }

        public OtherTreeAndroidClient(String url, String token, boolean useDefaultUrl){
            super();
            Platform.loadPlatformComponent(new AndroidPlatformComponent());
            super.init(url, token, useDefaultUrl);
        }
}