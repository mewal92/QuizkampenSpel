package Quiz;

import java.io.FileInputStream;
import java.util.Properties;

public class Settings {
    private int rounds;
    private int questions;

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
    public int getRounds(){
        return rounds;
    }
    public int getQuestions(){
        return questions;
    }


}