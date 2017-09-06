package simplesmart.netty.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import simplesmart.netty.rpc.base.NameThreadFactory;
import simplesmart.netty.rpc.base.RpcThreadPool;
import simplesmart.netty.rpc.serialize.SerializeProtocol;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by asus on 2017/9/6.
 */
public class RpcServerExecuter implements ApplicationContextAware,InitializingBean{


    private final Map<String,Object> handerMap = new ConcurrentHashMap<String,Object>();

    private int port;

    private SerializeProtocol protocol;

    private  static ReentrantLock lock = new ReentrantLock();

    private static ThreadPoolExecutor threadPoolExecutor = null;

    public RpcServerExecuter(int port) {
        this(port,SerializeProtocol.PROTOSTUFF);

    }


    public RpcServerExecuter(int port, SerializeProtocol protocol) {
        this.port = port;
        this.protocol = protocol;
    }


    //主要用于执行反射调用任务
    public static void submit(Runnable runnable){
        if(null == threadPoolExecutor){
            lock.lock();
            try {
                if(null == threadPoolExecutor){
                    threadPoolExecutor = RpcThreadPool.getRpcThreadPool(8,0);
                }
            }finally {
                lock.unlock();
            }
        }
        threadPoolExecutor.submit(runnable);
    }

    /**
     * 通过注解，获取RpcService.class，将它放到map中
     */
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> beansWithAnnotation = ctx.getBeansWithAnnotation(RpcService.class);
       if(beansWithAnnotation.size() > 0){
           Set<Map.Entry<String, Object>> entries = beansWithAnnotation.entrySet();
           for(Map.Entry<String,Object> entry :  entries){
               Object bean = entry.getValue();
               String interfaceName = bean.getClass().getAnnotation(RpcService.class).value().getName();
               handerMap.put(interfaceName,bean);
           }
       }
    }

    /**
     * spring 加载启动rpc server
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        NameThreadFactory threadFactory = new NameThreadFactory("NPCServer ThreadFactory",true);
        int paralles = Runtime.getRuntime().availableProcessors() * 2;
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup(paralles,threadFactory);
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,worker).channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerChannelInitializer(protocol,handerMap))
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> future) throws Exception {
                    System.out.println("The RPC Server start!");
                }
            });
            channelFuture.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
