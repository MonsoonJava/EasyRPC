package simplesmart.netty.rpc.serialize.protosbuff;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by asus on 2017/9/6.
 */
public class ProtosbuffEncoder extends MessageToByteEncoder{

    private Class<?> genericClass;

    public ProtosbuffEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if(null != msg && genericClass.isInstance(msg)){
            byte[] data = ProtobuffSerializationUtil.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
