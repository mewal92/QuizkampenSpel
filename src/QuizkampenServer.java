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
    Questions questions = new Questions();
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
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestion = questions.categoryList.get(0).get(ranNum);
            outPlayer1.println(randomQuestion);
            outPlayer1.println(ranNum);
        }
        if(player1Choice.equals("Musik")){
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestion = questions.categoryList.get(1).get(ranNum);
            outPlayer1.println(randomQuestion);
        }
        if(player1Choice.equals("Java-kunskap")){
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestion = questions.categoryList.get(2).get(ranNum);
            outPlayer1.println(randomQuestion);
        }
        if(player1Choice.equals("Övrigt")){
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestion = questions.categoryList.get(3).get(ranNum);
            outPlayer1.println(randomQuestion);
        }
    }

    /*
    //Hämta antal omgångar och frågor
    roundsToPlay= settings.getRounds();
    questionsPerRound= settings.getQuestions();
    gameActive = true;
        try {
        player1UserName = inPlayer1.readLine();
        player2UserName = inPlayer2.readLine();
        outPlayer1.println("Välkommen " + player1UserName+". Du kommer att spela mot "+player2UserName+"!");
        outPlayer2.println("Välkommen " + player2UserName+". Du kommer att spela mot "+player1UserName+"!");
    } catch (IOException e) {
        e.printStackTrace();
    }
    //Låt player1 välja kategori.
        while(gameActive){
        //1. player1 väljer kategori
        //2. player2 visa väntskärm
        //3. player1 får slumpad fråga.
        try {
            setCategory();
            chooseCategory();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    p
*/
}