package simplesmart.netty.rpc.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import simplesmart.netty.rpc.model.ClassInfoRequest;

import java.util.Map;

/**
 * Created by asus on 2017/9/6.
 */
public class MessageRecPipeHandler extends SimpleChannelInboundHandler<ClassInfoRequest> {

    private final Map<String,Object> handlerMap ;

    public MessageRecPipeHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    //反射调用，处理请求
    protected void channelRead0(ChannelHandlerContext ctx, ClassInfoRequest request) throws Exception {
        Channel channel = ctx.channel();
        String invokeClass = request.getClassName();
        if(handlerMap.isEmpty() || !handlerMap.containsKey(invokeClass)){
            throw new RuntimeException("the handler class is not exist");
        }
        Object obj = handlerMap.get(invokeClass);
        RpcServerExecuter.submit(new MethodExecutionService(ctx.channel(),request,obj));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //有异常则关闭连接
        ctx.channel().close();
    }
}
