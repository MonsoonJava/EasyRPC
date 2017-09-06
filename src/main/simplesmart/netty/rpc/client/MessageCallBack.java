package simplesmart.netty.rpc.client;

import simplesmart.netty.rpc.model.ClassInfoRequest;
import simplesmart.netty.rpc.model.ResultResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by asus on 2017/9/5.
 */
public class MessageCallBack {

    private ClassInfoRequest request;
    private ResultResponse response;

    private ReentrantLock lock = new ReentrantLock();
    private Condition finishFlag = lock.newCondition();

    public ClassInfoRequest getRequest() {
        return request;
    }

    public void setRequest(ClassInfoRequest request) {
        this.request = request;
    }

    //在channelRead里调用，使用线程池里的线程去通知主线程，返回值以设置好
    public void setResponse(ResultResponse response) {
        lock.lock();
        try{
            if( null == response ){
                this.response =  response;
                finishFlag.notify();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        this.response = response;
    }

    //获取服务端返回的值
    public Object start(){
        lock.lock();
        try{
            while (null == response ){
                finishFlag.await(1000, TimeUnit.MILLISECONDS);
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
            return response.getResult();
        }
    }
}
