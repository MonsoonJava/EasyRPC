package simplesmart.netty.rpc.serialize.protosbuff;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * Created by asus on 2017/9/6.
 */
public class ProtosbuffDecoder extends ByteToMessageDecoder{

    private Class<?> decodeClass ;

    public ProtosbuffDecoder(Class<?> decodeClass) {
        this.decodeClass = decodeClass;
    }

    // int 4byte length
    // data
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //没有data数据的长度，返回
        if(in.readableBytes() < 4){
            return ;
        }
        in.markReaderIndex();
        int readIndex = in.readerIndex();
        if (readIndex < 0) {
            ctx.close();
        }
        int length = in.readInt();
        if(in.readableBytes() < length){
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[length];
        in.readBytes(data);
        Object obj = ProtobuffSerializationUtil.deserialize(data, decodeClass);
        out.add(obj);
    }
}
