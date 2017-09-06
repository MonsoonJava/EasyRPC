package simplesmart.netty.rpc.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import simplesmart.netty.rpc.serialize.RpcRevSerializeFrame;
import simplesmart.netty.rpc.serialize.RpcSendSerializeFrame;
import simplesmart.netty.rpc.serialize.SerializeProtocol;

import java.util.Map;

/**
 * Created by asus on 2017/9/6.
 */
public class RpcServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private SerializeProtocol serializeProtocol;

    private RpcRevSerializeFrame frame ;

    public RpcServerChannelInitializer(SerializeProtocol serializeProtocol,Map<String,Object> handlerMap) {
        this.serializeProtocol = serializeProtocol;
        this.frame = new RpcRevSerializeFrame(handlerMap);
    }

    protected void initChannel(SocketChannel ch) throws Exception {
        frame.select(ch,serializeProtocol);
    }
}
