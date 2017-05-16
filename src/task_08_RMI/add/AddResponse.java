package task_08_RMI.add;

import java.io.Serializable;

public class AddResponse implements Serializable {
    Long result;

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public AddResponse(Long result) {

        this.result = result;
    }
}
