package simplesmart.netty.rpc.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import simplesmart.netty.rpc.serialize.RpcSendSerializeFrame;
import simplesmart.netty.rpc.serialize.SerializeProtocol;


/**
 * Created by asus on 2017/9/5.
 */
public class RpcClientChannelInitializer extends ChannelInitializer<SocketChannel>{

    private SerializeProtocol serializeProtocol;

    private RpcSendSerializeFrame sendFrame = new RpcSendSerializeFrame();

    public RpcClientChannelInitializer(SerializeProtocol serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    protected void initChannel(SocketChannel ch) throws Exception {
        sendFrame.select(ch,serializeProtocol);
    }
}
