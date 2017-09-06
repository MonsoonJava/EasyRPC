package simplesmart.netty.rpc.serialize;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import simplesmart.netty.rpc.client.MessageSendPipeHandler;
import simplesmart.netty.rpc.model.ClassInfoRequest;
import simplesmart.netty.rpc.model.ResultResponse;
import simplesmart.netty.rpc.serialize.protosbuff.ProtosbuffDecoder;
import simplesmart.netty.rpc.serialize.protosbuff.ProtosbuffEncoder;
import simplesmart.netty.rpc.server.MessageRecPipeHandler;

import java.util.Map;

/**
 * Created by asus on 2017/9/6.
 */
public class RpcRevSerializeFrame implements SerializeProtFrame{

    private Map<String,Object> handlerMap;

    public RpcRevSerializeFrame(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public void select(SocketChannel channel, SerializeProtocol protocol) {
        ChannelPipeline pipeline = channel.pipeline();
        switch (protocol){
            case JDKSERIALIZE:{
                // outbondhandler add to pipe before inboundhandler
                //TODO
                pipeline.addLast(new MessageRecPipeHandler(handlerMap));
            }
            case PROTOSTUFF:{
                pipeline.addLast(new ProtosbuffEncoder(ResultResponse.class));
                pipeline.addLast(new ProtosbuffDecoder(ClassInfoRequest.class));
                pipeline.addLast(new MessageRecPipeHandler(handlerMap));
            }
        }
    }
}
