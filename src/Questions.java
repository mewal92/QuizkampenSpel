import java.util.ArrayList;

public class Questions {
    ArrayList<ArrayList<String>>categoryList = new ArrayList<>();
    ArrayList<String>movie = new ArrayList<>();
    ArrayList<String>music = new ArrayList<>();
    ArrayList<String>java = new ArrayList<>();
    ArrayList<String>other = new ArrayList<>();

    String q1 = "Vad heter den kända skådespelaren Pitt i förnamn?";
    String q2 = "Vem spelar huvudrollen i den episka filmen The Good, The Bad and The Ugly?";
    String q3 = "Vem spelar Fat Amy i filmen Perfect Pitch?";

    String q4 = "Vem skrev den episka musiken till The Good, The Bad and The Ugly?";
    String q5 = "Vilket instrument spelar artisten John Mayer?";
    String q6 = "Vad kan man göra med en bastuba?";

    String q7 = "Vem kallas the Godfather of Java?";
    String q8 = "Hur många arv är tillåtna per klass i Java?";
    String q9 = "Vad är Java?";

    String q10 = "Vilken är den bästa läsken?";
    String q11 = "Vad heter hjärnorna bakom det här quizet?";
    String q12 = "Hur högt är Mount Everest?";

    public Questions(){
        movie.add(q1);
        movie.add(q2);
        movie.add(q3);
        categoryList.add(movie);

        music.add(q4);
        music.add(q5);
        music.add(q6);
        categoryList.add(music);

        java.add(q7);
        java.add(q8);
        java.add(q9);
        categoryList.add(java);

        other.add(q10);
        other.add(q11);
        other.add(q12);
        categoryList.add(java);

    }
}
