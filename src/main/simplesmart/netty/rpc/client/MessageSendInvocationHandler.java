package simplesmart.netty.rpc.client;

import com.google.common.reflect.AbstractInvocationHandler;
import io.netty.channel.ChannelFuture;
import simplesmart.netty.rpc.model.ClassInfoRequest;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by asus on 2017/9/4.
 */
public class MessageSendInvocationHandler<T> extends AbstractInvocationHandler {

    protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        //封装请求
        ClassInfoRequest request = new ClassInfoRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParametersType(method.getParameterTypes());
        request.setParams(args);

        //数据发送
        MessageSendPipeHandler handler = RpcClientLoader.getInstance().getHandler();
        MessageCallBack callBack = handler.send(request);
        return callBack.start();
    }
}
