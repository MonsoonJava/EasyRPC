package simplesmart.netty.rpc.base;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by asus on 2017/9/4.
 * desc: 命名线程工厂
 */
public class NameThreadFactory implements ThreadFactory{

    // 记录线程工厂数
    private static final AtomicInteger threadFactoryNumber = new AtomicInteger(1);
    //记录一个工程产生的线程数
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemoThread;

    private final ThreadGroup threadGroup;

    public NameThreadFactory() {
        this( "simplesmartserver",false,defaultThreadGroup());
    }

    public NameThreadFactory(String prefix) {
        this(prefix ,false,defaultThreadGroup());
    }

    public NameThreadFactory(String prefix, boolean daemoThread) {
        this(prefix ,daemoThread,defaultThreadGroup());
    }

    private  static ThreadGroup defaultThreadGroup(){
        return System.getSecurityManager() == null ? Thread.currentThread().getThreadGroup() : System.getSecurityManager().getThreadGroup();
    }

    public NameThreadFactory(String prefix, boolean daemoThread, ThreadGroup threadGroup) {
        this.prefix = "--RPC--threadpool--" + threadFactoryNumber.getAndIncrement() + "--" + prefix + "--thread--";
        this.daemoThread = daemoThread;
        this.threadGroup = threadGroup;
    }

    public Thread newThread(Runnable r) {
        String name = this.prefix + threadNumber.getAndIncrement();
        Thread thread = new Thread(this.threadGroup,r,name,0);
        thread.setDaemon(daemoThread);
        return thread;
    }

    public ThreadGroup getThreadGroup(){
        return threadGroup;
    }

}
