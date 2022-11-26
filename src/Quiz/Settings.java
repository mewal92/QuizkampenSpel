package Quiz;

import java.io.FileInputStream;
import java.util.Properties;

public class Settings {
    private static int rounds;
    private static int questions;

    public Settings(){
        Properties p = new Properties();
        try{
            p.load(new FileInputStream("src/Quiz/settings.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rounds=Integer.parseInt(p.getProperty("rounds","3"));
        questions=Integer.parseInt(p.getProperty("questions","3"));
    }
    public static int getRounds(){
        return rounds;
    }
    public static int getQuestions(){
        return questions;
    }
}