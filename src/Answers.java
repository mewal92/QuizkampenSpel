import java.util.ArrayList;

public class Answers {
    ArrayList<ArrayList<String>> filmAnswers = new ArrayList<>();
    ArrayList<String>q1 = new ArrayList<>();
    ArrayList<String>q2 = new ArrayList<>();
    ArrayList<String>q3 = new ArrayList<>();

    public Answers(){
        String a1 = "Michael";
        String a2 = "Brad";
        String a3 = "George";
        String a4 = "Henry";

        q1.add(a1);
        q1.add(a2);
        q1.add(a3);
        q1.add(a4);

        filmAnswers.add(q1);
    }
}
