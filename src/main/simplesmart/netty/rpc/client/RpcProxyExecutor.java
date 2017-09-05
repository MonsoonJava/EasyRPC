package simplesmart.netty.rpc.client;

import com.google.common.reflect.Reflection;
import simplesmart.netty.rpc.consts._ConstantConfig;
import simplesmart.netty.rpc.util.PropsUtil;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Properties;

/**
 * Created by asus on 2017/9/4.
 */
public class RpcProxyExecutor {

    private static String DEFAULT_SERVER_IP = null;
    private static String DEFAULT_SERVER_PORT = null;

    private String serverIP;

    private String serverPort;

    private RpcClientLoader loader = RpcClientLoader.getInstance();

    static {
        try {
            Properties clientProperties = PropsUtil.loadProperties("rpcclient.properties");
            DEFAULT_SERVER_IP = clientProperties.getProperty(_ConstantConfig.RPC_SERVER_ADDRESS);
            DEFAULT_SERVER_PORT = clientProperties.getProperty(_ConstantConfig.RPC_SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RpcProxyExecutor() {
        this(DEFAULT_SERVER_IP,DEFAULT_SERVER_PORT);
    }

    public RpcProxyExecutor(String serverIP, String serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        loader.load(serverIP,serverPort);
    }

    public void setAddressAndPort(String serverIP, String serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        loader.load(serverIP,serverPort);
    }
    public <T> T execute(Class<T> rpcInterface){
        return Reflection.newProxy(rpcInterface,new MessageSendInvocationHandler<T>());
    }
}
