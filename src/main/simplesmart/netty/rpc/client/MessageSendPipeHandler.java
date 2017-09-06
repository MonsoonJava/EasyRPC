package simplesmart.netty.rpc.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import simplesmart.netty.rpc.model.ClassInfoRequest;
import simplesmart.netty.rpc.model.ResultResponse;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by asus on 2017/9/4.
 */
public class MessageSendPipeHandler extends SimpleChannelInboundHandler<ResultResponse>{


    private final ConcurrentHashMap<String,MessageCallBack> callBackMap = new ConcurrentHashMap<String, MessageCallBack>();

    private volatile Channel channel = null;

    private SocketAddress remoteAddress;

    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void shutdown(){
        if(null != channel){
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    protected void channelRead0(ChannelHandlerContext ctx, ResultResponse response) throws Exception {
        String key = response.getMessageId();
        if(callBackMap.containsKey(key)){
            MessageCallBack callBack = callBackMap.remove(key);
            //异步通知，返回值成功获取
            if(null != callBack){
                callBack.setResponse(response);
            }
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remoteAddress = this.channel.remoteAddress();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        shutdown();
    }

    public MessageCallBack send(ClassInfoRequest request){
        if( null != channel && channel.isActive()){
            channel.writeAndFlush(request);
            MessageCallBack callBack = new MessageCallBack();
            callBack.setRequest(request);
            callBackMap.put(request.getMessageId(),callBack);
            return callBack;
        }else{
            //TODO channel reconnect and send the message
            return null;
        }

    }
}
