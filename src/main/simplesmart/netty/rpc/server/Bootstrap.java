package simplesmart.netty.rpc.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by asus on 2017/9/6.
 */
public class Bootstrap {

    public static void main(String[] args){
        ClassPathXmlApplicationContext re = new ClassPathXmlApplicationContext("spring.xml");
    }
}
