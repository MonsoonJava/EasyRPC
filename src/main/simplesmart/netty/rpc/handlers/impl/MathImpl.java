package simplesmart.netty.rpc.handlers.impl;

import simplesmart.netty.rpc.handlers.Math;
import simplesmart.netty.rpc.server.RpcService;

/**
 * Created by asus on 2017/9/6.
 */
@RpcService(Math.class)
public class MathImpl implements Math{

    public Integer AddInt(Integer val1, Integer val2) {
        return val1 + val2;
    }
}
