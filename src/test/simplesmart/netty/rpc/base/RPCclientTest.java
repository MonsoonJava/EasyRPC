package simplesmart.netty.rpc.base;

import org.junit.Test;
import simplesmart.netty.rpc.client.RpcProxyExecutor;
import simplesmart.netty.rpc.handlers.Math;

/**
 * Created by asus on 2017/9/6.
 */
public class RPCclientTest {

    @Test
    public void sendMessage(){
        RpcProxyExecutor executor = new RpcProxyExecutor("127.0.0.1","12542");
        Math proxy = executor.execute(Math.class);
        for(int i = 0;i<10000;i++){
            Integer res = proxy.AddInt(i, 0);
            System.out.println(res);
        }
    }
}
