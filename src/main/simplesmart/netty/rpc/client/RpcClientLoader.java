package simplesmart.netty.rpc.client;

import com.google.common.util.concurrent.*;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import simplesmart.netty.rpc.base.RpcThreadPool;
import simplesmart.netty.rpc.serialize.SerializeProtocol;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by asus on 2017/9/4.
 */
public class RpcClientLoader {

    //内部静态类单列
    private RpcClientLoader(){
    }

    public static RpcClientLoader getInstance(){
        return  RpcClientLoaderHolder.instance;
    }

    private static class RpcClientLoaderHolder {
        private static final RpcClientLoader instance = new RpcClientLoader();
    }

    private SerializeProtocol serializeProtocol = SerializeProtocol.JDKSERIALIZE;
    private static final int parallel = Runtime.getRuntime().availableProcessors() * 2;
    private static ListeningExecutorService listeningThreadPool = MoreExecutors.listeningDecorator(RpcThreadPool.getRpcThreadPool(16,-1));
    EventLoopGroup group = new NioEventLoopGroup(parallel);

    private MessageSendPipeHandler handler = null;

    private Lock lock = new ReentrantLock();
    private Condition connectStatus = lock.newCondition();
    private Condition handlerStatus = lock.newCondition();

    public void load(String serverIp,String serverPort){
        int port = Integer.parseInt(serverPort);
        final SocketAddress remoteAddress = new InetSocketAddress(serverIp,port);
        //在线程池中初始化netty客户端，并且主要目的是返回netty的channel用于发送数据
        ListenableFuture listenableFuture = listeningThreadPool.submit(new RpcClientInitTask(group, serializeProtocol, remoteAddress));
        //确保handler被正确初始化
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {

            public void onSuccess(Boolean result) {
                lock.lock();
                try{
                    while( null ==  handler){
                        handlerStatus.await();
                    }
                    if(handler != null && result){
                        connectStatus.signalAll();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        },listeningThreadPool);

    }

    public void setHandler(MessageSendPipeHandler handler){
        lock.lock();
        try{
            this.handler = handler;
            handlerStatus.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public MessageSendPipeHandler getHandler(){
        lock.lock();
        try{
            while(null == this.handler){
               connectStatus.await();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return this.handler;
    }

    //系统关闭
    public void unload(){
        if(null != handler){
            handler.shutdown();
        }
        listeningThreadPool.shutdown();
        group.shutdownGracefully();
    }






}
