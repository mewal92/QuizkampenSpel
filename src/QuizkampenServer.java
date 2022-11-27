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

    int rounds = settings.getRounds();
    int questionsPerRound = settings.getQuestions();
    int currentCategory;
    int scorePlayer1;
    int scorePlayer2;

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

            //Låt player1 välja kategori.
            while(gameActive){
                setCategoryGui();
                playersCategoryChoice();

                setQuestionGui();
                sendQuestionToPlayer();

                setWaitScreen();
            }
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setCategoryGui() throws IOException {
        while(true){
            String gameStartPressedFrom = inPlayer1.readLine();

            if(gameStartPressedFrom.equals("startPressed")){
                outPlayer1.println("SET CATEGORY");
                break;
            }
        }
    }
    public void playersCategoryChoice() throws IOException {
        while(true){
            String player1Choice = inPlayer1.readLine();
            if(player1Choice.equals("Film")){
                currentCategory = 0;
                break;
            }
            if(player1Choice.equals("Musik")){
                currentCategory = 1;
                break;
            }
            if(player1Choice.equals("Java-kunskap")){
                currentCategory = 2;
                break;
            }
            if(player1Choice.equals("Övrigt")){
                currentCategory = 3;
                break;
            }
        }
    }
    private void setQuestionGui() {
        outPlayer1.println("SET QUESTION");
    }
    private void sendQuestionToPlayer() throws IOException, InterruptedException {
        int ranNum = new Random().nextInt(0, 3);
        String randomQuestionFromMovieCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
        outPlayer1.println(randomQuestionFromMovieCategory);
        outPlayer1.println(ranNum+(currentCategory*3));
        answerFromPlayer();
    }

    private void answerFromPlayer() throws IOException, InterruptedException {
        while(true){
            String answerFromPlayer = inPlayer1.readLine();
            if(answerFromPlayer.equals("CorrectAnswer")){
                scorePlayer1++;
                setQuestionGui();
                sendQuestionToPlayer();
                System.out.println("correct answer received");
                break;

            }else if(answerFromPlayer.equals("WrongAnswer")){
                setQuestionGui();
                sendQuestionToPlayer();
                break;
            }
        }
    }
    private void setWaitScreen() {
        outPlayer2.println("SET WAIT");
    }



}