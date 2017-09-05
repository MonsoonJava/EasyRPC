package simplesmart.netty.rpc.model;

import java.util.Map;

/**
 * Created by asus on 2017/9/4.
 */
public class InterfaceAndBeanMap {

    private Map<String,Object> interfaceAndBeans = null;

    public Map<String, Object> getInterfaceAndBeans() {
        return interfaceAndBeans;
    }

    public void setInterfaceAndBeans(Map<String, Object> interfaceAndBeans) {
        this.interfaceAndBeans = interfaceAndBeans;
    }
}
