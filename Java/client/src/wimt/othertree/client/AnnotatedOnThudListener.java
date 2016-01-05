package wimt.othertree.client;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by Nick Cuthbert on 05/01/2016.
 */
class AnnotatedOnThudListener<T extends GeneratedMessage> {
    public String typeName="";
    public Version version;
    public ThudListener<T> thudListener;
    private T.Builder builder;

    public AnnotatedOnThudListener(ThudListener<T> thudListener){
        this(thudListener,new Version());
    }

    public AnnotatedOnThudListener(ThudListener<T> thudListener, Version version){
        if(version==null){
            version=new Version();
        }
        this.version=version;
        this.thudListener=thudListener;

        Class<T.Builder> builderClass = T.Builder.class;
        try {
            builder = builderClass.newInstance();
            typeName= this.builder.getDescriptorForType().getFullName();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean applicable(String typeName,Version version){
        return (this.typeName.equals(typeName) && this.version.isCompatibleWith(version));
    }

    public void onThud(byte[] payload) throws InvalidProtocolBufferException {
        builder.mergeFrom(payload);
        T message= (T) builder.build();
        thudListener.onThud(message);
    }
}

