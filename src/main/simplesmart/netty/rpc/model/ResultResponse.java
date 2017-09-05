package simplesmart.netty.rpc.model;

/**
 * Created by asus on 2017/9/4.
 */
public class ResultResponse {

    private Object result;

    private String messageId;

    private String error;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ResultResponse{" +
                "result=" + result +
                ", messageId='" + messageId + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
