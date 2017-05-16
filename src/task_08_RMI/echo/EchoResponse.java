package task_08_RMI.echo;

import java.io.Serializable;

public class EchoResponse implements Serializable {
    String message;

    public EchoResponse(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
