package simplesmart.netty.rpc.model;

/**
 * Created by asus on 2017/9/4.
 */
public class ClassInfoRequest {

    private String messageId;

    private String className;

    private String methodName;

    private Class<?>[] parametersType;

    private Object[] params;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParametersType() {
        return parametersType;
    }

    public void setParametersType(Class<?>[] parametersType) {
        this.parametersType = parametersType;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "ClassInfoRequest{" +
                "messageId='" + messageId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
