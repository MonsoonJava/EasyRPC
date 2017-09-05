package simplesmart.netty.rpc.util;

import java.io.IOException;
import java.util.Properties;

public class PropsUtil {

    public static Properties loadProperties(String propertiesFileName) throws IOException {
        Properties prop = new Properties();
        prop.load(PropsUtil.class.getClassLoader().getResourceAsStream(propertiesFileName));
        return prop;
    }


}
