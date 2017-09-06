package simplesmart.netty.rpc.serialize.protosbuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by asus on 2017/9/6.
 */
public class ProtobuffSerializationUtil {

    private static final Map<Class<?>,Schema<?>> cacheMap = new ConcurrentHashMap<Class<?>,Schema<?>>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    private ProtobuffSerializationUtil(){
    }

    /**
     * 获取schema类
     * @param cls
     * @param <T>
     * @return
     */
    private static <T> Schema<T> getSchema(Class<T> cls){
        if(cacheMap.containsKey(cls)){
            return (Schema<T>) cacheMap.get(cls);
        }
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(cls);
        if( null != cls){
            cacheMap.put(cls,schema);
        }
        return schema;
    }

    //protostuff 写序列化
    public static <T> byte[] serialize(T obj){
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try{
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj,schema,buffer);
        }catch (Exception e){
            throw new IllegalStateException(e.getMessage(), e);
        }finally {
            buffer.clear();
        }
    }


    //反序列化
    public static <T> T deserialize(byte[] bytes, Class<T> cls){
        try {
            T message = objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
