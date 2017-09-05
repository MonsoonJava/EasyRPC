package simplesmart.netty.rpc.base;


import com.google.common.util.concurrent.*;
import org.junit.Test;
import simplesmart.netty.rpc.client.RpcClientInitTask;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by asus on 2017/9/4.
 */
public class ThreadPoolExecuterTest {

    @Test
    public void testThreadPool(){
        ThreadPoolExecutor rpcThreadPool = RpcThreadPool.getRpcThreadPool(5, 0);
        for (int i =0;i<4;i++){
            rpcThreadPool.execute(new SleepTask());
        }
    }


    @Test(expected = RejectedExecutionException.class)
    public void testThreadPoolException(){
        ThreadPoolExecutor rpcThreadPool = RpcThreadPool.getRpcThreadPool(5, 0);
        for (int i =0;i<10;i++){
            rpcThreadPool.execute(new SleepTask());
        }
    }

    /**
     *   测试onsuccess函数线程的调用线程是哪一个
     */
    @Test
    public void testListeningThreadPool(){
        final MessageHolder messageHolder = new MessageHolder();
        final ReentrantLock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        ListeningExecutorService listeningThreadPool = MoreExecutors.listeningDecorator(RpcThreadPool.getRpcThreadPool(1,-1));
        ListenableFuture listenableFuture = listeningThreadPool.submit(new SleepReValTask(messageHolder));
        Futures.addCallback(listenableFuture, new FutureCallback() {
            public void onSuccess(Object result) {
                lock.lock();
                try{
                    System.out.println("success thread--" + Thread.currentThread().getName());
                    if ( null != messageHolder.getMessage()){
                        condition.signalAll();
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
        System.out.println("main start");
        lock.lock();
        try{
            System.out.println("main thread--" + Thread.currentThread().getName());
            while(null == messageHolder.getMessage()){
                condition.await();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        System.out.println("main finish");
    }



    private static class SleepReValTask implements Callable<Boolean>{

        private MessageHolder messageHolder;

        public SleepReValTask(MessageHolder messageHolder) {
            this.messageHolder = messageHolder;
        }

        public Boolean call() throws Exception {
            try {
                Thread.sleep(1000);
                System.out.println("callable thread--" + Thread.currentThread().getName());
                messageHolder.setMessage("what is now");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                return Boolean.TRUE;
            }
        }
    }

    private static class MessageHolder{
        private String message;

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private static class SleepTask implements Runnable{
        public void run() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
