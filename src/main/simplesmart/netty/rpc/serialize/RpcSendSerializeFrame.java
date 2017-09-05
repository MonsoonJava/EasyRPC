package simplesmart.netty.rpc.serialize;


import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import simplesmart.netty.rpc.client.MessageSendPipeHandler;

/**
 * Created by asus on 2017/9/5.
 */
public class RpcSendSerializeFrame implements SerializeProtFrame{


    public void select(SocketChannel channel, SerializeProtocol protocol) {
        ChannelPipeline pipeline = channel.pipeline();
        switch (protocol){
            case JDKSERIALIZE:{
                // outbondhandler add to pipe before inboundhandler
               //TODO
                pipeline.addLast(new MessageSendPipeHandler());
            }
            case PROTOCOLBUFF:{
                //TODO
                pipeline.addLast(new MessageSendPipeHandler());
            }
            case PROTOSTUFF:{
                //TODO
                pipeline.addLast(new MessageSendPipeHandler());
            }
        }
    }
}
