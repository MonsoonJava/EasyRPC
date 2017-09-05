package simplesmart.netty.rpc.serialize;


import io.netty.channel.socket.SocketChannel;

/**
 * Created by asus on 2017/9/5.
 */
public interface SerializeProtFrame {

    public void select(SocketChannel channel, SerializeProtocol protocol);

}
