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
    int playerOneScore;
    int playerTwoScore;

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
            settings.getRounds();
            settings.getQuestions();
            while(gameActive){
                setCategory();
                setWaitScreen();
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

    public void setCategory() throws IOException {
        while(true){
            if(inPlayer1.readLine().equals("startPressed")){
                outPlayer1.println("SET CATEGORY");
                break;
            }
        }
    }
    public void chooseCategory() throws IOException {
        String player1Choice = inPlayer1.readLine();
        if(player1Choice.equals("Film")){
            currentCategory = 0;
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestionFromMovieCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println(randomQuestionFromMovieCategory);
            outPlayer1.println(ranNum);
            outPlayer2.println(randomQuestionFromMovieCategory);
            outPlayer2.println(ranNum);
        }
        if(player1Choice.equals("Musik")){
            currentCategory = 1;
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestionFromMusicCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println(randomQuestionFromMusicCategory);
            outPlayer1.println(ranNum+3);
            outPlayer2.println(randomQuestionFromMusicCategory);
            outPlayer2.println(ranNum+3);

        }
        if(player1Choice.equals("Java-kunskap")){
            currentCategory = 2;
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestionFromJavaCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println(randomQuestionFromJavaCategory);
            outPlayer1.println(ranNum+6);
            outPlayer2.println(randomQuestionFromJavaCategory);
            outPlayer2.println(ranNum+6);

        }
        if(player1Choice.equals("Övrigt")){
            currentCategory = 3;
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestionFromOtherCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println(randomQuestionFromOtherCategory);
            outPlayer1.println(ranNum+9);
            outPlayer2.println(randomQuestionFromOtherCategory);
            outPlayer2.println(ranNum+9);

        }
    }
}