package simplesmart.netty.rpc.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import simplesmart.netty.rpc.model.ClassInfoRequest;
import simplesmart.netty.rpc.model.ResultResponse;

import java.lang.reflect.Method;

/**
 * Created by asus on 2017/9/6.
 */
public class MethodExecutionService implements Runnable{

    private Channel channel;

    private ClassInfoRequest request;

    private Object invokeBean;

    public MethodExecutionService(Channel channel, ClassInfoRequest request, Object invokeBean) {
        this.channel = channel;
        this.request = request;
        this.invokeBean = invokeBean;
    }

    public void run() {
        ResultResponse response = new ResultResponse();
        try {
            Class<?> cls = Class.forName(request.getClassName());
            Method method = cls.getMethod(request.getMethodName(),request.getParametersType());
            Object result = method.invoke(invokeBean, request.getParams());
            response.setMessageId(request.getMessageId());
            response.setResult(result);
            response.setError(null);
        } catch (Exception e) {
            response.setError(e.toString());
            e.printStackTrace();
            System.err.printf("RPC Server invoke error!\n");
        }finally {
            ChannelFuture channelFuture = channel.writeAndFlush(response);
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> future) throws Exception {
                    System.out.println("RPC Server Send message-id respone:" + request.getMessageId());
                }
            });
        }
    }
}
