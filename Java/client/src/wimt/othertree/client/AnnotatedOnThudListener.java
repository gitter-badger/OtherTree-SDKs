package wimt.othertree.client;

import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import java.lang.reflect.InvocationTargetException;


/**
 * Created by Nick Cuthbert on 05/01/2016.
 */
class AnnotatedOnThudListener<T extends Message> {
    public String typeName="";
    public Version version;
    public ThudListener<T> thudListener;
    private Class<T> expectedType;


    public AnnotatedOnThudListener(Class<T> expectedType,ThudListener<T> thudListener){
        this(expectedType, thudListener, new Version());
    }



    public AnnotatedOnThudListener(Class<T> expectedType, ThudListener<T> thudListener, Version version){
        if(version==null){
            version=new Version();
        }
        this.expectedType=expectedType;

        this.version=version;
        this.thudListener=thudListener;
//

        try {
            Descriptors.Descriptor descriptor = ((Message.Builder)(expectedType.getMethod("newBuilder").invoke(null))).getDescriptorForType();
            this.typeName= OtherTreeClient.toPascalCase(descriptor.getFullName());
        }
        catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean applicable(String typeName,Version version){
        return (this.typeName.equals(typeName) && this.version.isCompatibleWith(version));
    }

    public void onThud(byte[] payload) throws InvalidProtocolBufferException {
        try {
            T.Builder builder=(T.Builder)(expectedType.getMethod("newBuilder").invoke(null));
            thudListener.onThud((T)(builder.mergeFrom(payload).build()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}

