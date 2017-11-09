import java.io.Serializable;

public class MessageClass implements Serializable {

    private int number;
    private String string;

    public MessageClass(int number, String string){
        this.number = number;
        this.string = string;
    }

    public void info(){
        System.out.println("number = " + number + " | string = " + string);
    }

}