package task_08_RMI.add;

import java.io.Serializable;
import java.util.List;

public class AddRequest implements Serializable {
    List<Integer> digitsToAdd;

    public List<Integer> getDigitsToAdd() {
        return digitsToAdd;
    }

    public void setDigitsToAdd(List<Integer> digitsToAdd) {
        this.digitsToAdd = digitsToAdd;
    }

    public AddRequest(List<Integer> digitsToAdd) {

        this.digitsToAdd = digitsToAdd;
    }
}
