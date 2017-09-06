package simplesmart.netty.rpc.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by asus on 2017/9/4.
 */
public class InterfaceAndBeanMap {

    private Map<String,Object> handlerMap = null;

    public Map<String, Object> getHandlerMap() {
        return handlerMap;
    }

    public void setHandlerMap(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }
}
