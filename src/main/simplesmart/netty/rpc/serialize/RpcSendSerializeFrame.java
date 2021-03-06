package simplesmart.netty.rpc.serialize;


import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import simplesmart.netty.rpc.client.MessageSendPipeHandler;
import simplesmart.netty.rpc.model.ClassInfoRequest;
import simplesmart.netty.rpc.model.ResultResponse;
import simplesmart.netty.rpc.serialize.protosbuff.ProtosbuffDecoder;
import simplesmart.netty.rpc.serialize.protosbuff.ProtosbuffEncoder;

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
            case PROTOSTUFF:{
                pipeline.addLast(new ProtosbuffEncoder(ClassInfoRequest.class));
                pipeline.addLast(new ProtosbuffDecoder(ResultResponse.class));
                pipeline.addLast(new MessageSendPipeHandler());
            }
        }
    }
}
