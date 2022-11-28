package Quiz;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Questions {
    static LinkedList<Questions> questionList= new LinkedList<>();
    private String fråga;
    private String rättSvar;
    private String felSvar1;
    private String felSvar2;
    private String felSvar3;
    public Questions(String f, String s, String f1, String f2, String f3){
        this.fråga = f;
        this.rättSvar=s;
        this.felSvar1=f1;
        this.felSvar2=f2;
        this.felSvar3 = f3;
    }
    public static void addQuestionList(Questions f) {
        questionList.add(f);
    }
    public String getFråga() {
        return fråga;
    }

    public String getRättSvar() {
        return rättSvar;
    }

    public String getFelSvar1() {
        return felSvar1;
    }

    public String getFelSvar2() {
        return felSvar2;
    }

    public String getFelSvar3() {
        return felSvar3;
    }
}