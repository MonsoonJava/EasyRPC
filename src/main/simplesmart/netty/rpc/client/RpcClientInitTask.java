package simplesmart.netty.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import simplesmart.netty.rpc.serialize.SerializeProtocol;

import java.net.SocketAddress;
import java.util.concurrent.Callable;

/**
 * Created by asus on 2017/9/4.
 */
public class RpcClientInitTask implements Callable<Boolean>{

    private EventLoopGroup group;
    private SerializeProtocol protocol;
    private SocketAddress remoteAdreess;

    public RpcClientInitTask(EventLoopGroup group, SerializeProtocol protocol, SocketAddress remoteAdreess) {
        this.group = group;
        this.protocol = protocol;
        this.remoteAdreess = remoteAdreess;
    }

    public Boolean call() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new RpcClientChannelInitializer(protocol))
                .option(ChannelOption.SO_KEEPALIVE,true).option(ChannelOption.TCP_NODELAY,true);
        final ChannelFuture channelFuture = bootstrap.connect(remoteAdreess).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = channelFuture.channel();
                MessageSendPipeHandler handler = channel.pipeline().get(MessageSendPipeHandler.class);
                handler.setChannel(channel);
                RpcClientLoader.getInstance().setHandler(handler);
            }
        });
        return Boolean.TRUE;
    }
}
