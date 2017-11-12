import java.io.Serializable;

public class MessageClass implements Serializable {

    private int number;
    private String string;

    public MessageClass(int number, String string){
        this.number = number;
        this.string = string;
    }

    @Override
    public String toString() {
        return "MessageClass{" +
                "number=" + number +
                ", string='" + string + '\'' +
                '}';
    }
}