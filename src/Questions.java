import java.util.ArrayList;

public class Questions {
    ArrayList<ArrayList<String>>categoryList = new ArrayList<>();
    ArrayList<String>movie = new ArrayList<>();
    ArrayList<String>music = new ArrayList<>();
    ArrayList<String>java = new ArrayList<>();
    ArrayList<String>other = new ArrayList<>();
    String q1 = "Vem skrev den episka musiken till The Good, The Bad and The Ugly?";
    String q2 = "Vem spelar huvudrollen i den episka filmen The Good, The Bad and The Ugly?";
    String q3 = "Vad heter uppföljaren till den episka filmen The Good, The Bad and The Ugly?";

    public Questions(){
        movie.add(q1);
        movie.add(q2);
        movie.add(q3);
        categoryList.add(movie);
    }
}
