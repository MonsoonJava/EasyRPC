package simplesmart.netty.rpc.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import simplesmart.netty.rpc.handlers.Math;

/**
 * Created by asus on 2017/9/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
public class ScanRpcServiceTest {

    @Autowired
    private Math math;

    @Test
    public void test(){
        //test compoment scan
        System.out.println(math);
    }
}
