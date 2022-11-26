package Quiz;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class QuizkampenServer extends Thread{
    Socket player1Socket;
    Socket player2Socket;
    PrintWriter outPlayer1;
    PrintWriter outPlayer2;
    BufferedReader inPlayer1;
    BufferedReader inPlayer2;

    Settings settings = new Settings();
    Questions questionsList = new Questions();

    int currentCategory;
    boolean gameActive = false;

    public QuizkampenServer(Socket player1, Socket player2) throws IOException {
        player1Socket = player1;
        player2Socket = player2;
        try{
            outPlayer1 = new PrintWriter(player1Socket.getOutputStream(), true);
            outPlayer2 = new PrintWriter(player2Socket.getOutputStream(), true);
            inPlayer1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            inPlayer2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        try{
            String player1UserName = inPlayer1.readLine();
            String player2UserName = inPlayer2.readLine();
            outPlayer1.println("Välkommen " + player1UserName+". Du kommer att spela mot "+player2UserName+"!");
            outPlayer2.println("Välkommen " + player2UserName+". Du kommer att spela mot "+player1UserName+"!");
            gameActive = true;
            //Hämta antal omgångar och frågor
            settings.getRounds();
            settings.getQuestions();
            //Låt player1 välja kategori.
            while(gameActive){
                //1. player1 väljer kategori
                //2. player2 visa väntskärm
                //3. player1 får slumpad fråga.
                setCategory();
                setWaitScreen();
                chooseCategory();
                seeProgress();
                chooseCategory();

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setWaitScreen() {
        outPlayer2.println("SET WAIT");
    }
    public void seeProgress() throws IOException {//kollar hur långt i spelet man kommit om man svarat på en fråga
        while(true){
            if(inPlayer1.readLine().equals("vidarePressed")){
                chooseCategory();
                break;
            }
        }

    }


    public void setCategory() throws IOException {
        while(true){
            if(inPlayer1.readLine().equals("startPressed")){
                outPlayer1.println("SET CATEGORY");
                break;
            }
        }
    }
    public void chooseCategory() throws IOException {
        String category = inPlayer1.readLine();
        System.out.println("sätter kategori");
        if(category.equals("Film")){
            currentCategory = 0;
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestionFromMovieCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println(category);
            outPlayer1.println(randomQuestionFromMovieCategory);
            outPlayer1.println(ranNum);
        }
        if(category.equals("Musik")){
            currentCategory = 1;
            int ranNum = new Random().nextInt(0, 3);
            outPlayer1.println(category);
            String randomQuestionFromMusicCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println(category);
            outPlayer1.println(randomQuestionFromMusicCategory);
            outPlayer1.println(ranNum+3);

        }
        if(category.equals("Java-kunskap")){
            currentCategory = 2;
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestionFromJavaCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println(category);
            outPlayer1.println(randomQuestionFromJavaCategory);
            outPlayer1.println(ranNum+6);

        }
        if(category.equals("Övrigt")){
            currentCategory = 3;
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestionFromOtherCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println(category);
            outPlayer1.println(randomQuestionFromOtherCategory);
            outPlayer1.println(ranNum+9);

        }
    }
}