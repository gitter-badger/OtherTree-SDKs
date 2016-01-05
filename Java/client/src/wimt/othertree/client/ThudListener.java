package wimt.othertree.client;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.MessageLite;

/**
 * Created by Nick Cuthbert on 05/01/2016.
 */
public interface ThudListener<T extends GeneratedMessage> {
    void onThud(T fruit);
}
