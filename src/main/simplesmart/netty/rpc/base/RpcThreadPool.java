package simplesmart.netty.rpc.base;

import java.util.concurrent.*;

/**
 * Created by asus on 2017/9/4.
 */
public class RpcThreadPool {

    public static ThreadPoolExecutor getRpcThreadPool(int threadNumber,int queueSize){
        String name = "SimpleSmartRpc";
        return new ThreadPoolExecutor(threadNumber,threadNumber,0, TimeUnit.MILLISECONDS,
                queueSize == 0 ? new SynchronousQueue<Runnable>() :
                        ( queueSize < 0 ? new LinkedBlockingQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(queueSize)),
                new NameThreadFactory(name,true),new AbortRejectPolicy(name)
                );
    }

}
