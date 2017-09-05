package simplesmart.netty.rpc.serialize;

/**
 * Created by asus on 2017/9/4.
 */
public enum SerializeProtocol {

    JDKSERIALIZE("JDKnative"),PROTOCOLBUFF("protocolbuff"),PROTOSTUFF("protostuff");

    private String serializeProtocol;

    private SerializeProtocol(String protocol){
        this.serializeProtocol = protocol;
    }

    @Override
    public String toString() {
        return "SerializeProtocol{" +
                "serializeProtocol='" + serializeProtocol + '\'' +
                '}';
    }

    public String getSerializeProtocol() {
        return serializeProtocol;
    }
}
