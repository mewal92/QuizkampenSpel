import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class QuizkampenServer extends Thread{
    Socket player1Socket;
    Socket player2Socket;
    Settings settings = new Settings();
    boolean gameActive = false;

    public QuizkampenServer(Socket player1, Socket player2) throws IOException {
        player1Socket = player1;
        player2Socket = player2;

    }
    public void run(){
        try(PrintWriter outPlayer1 = new PrintWriter(player1Socket.getOutputStream(), true);
            PrintWriter outPlayer2 = new PrintWriter(player2Socket.getOutputStream(), true);
            BufferedReader inPlayer1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            BufferedReader inPlayer2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()))){
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
                String player1Choice = inPlayer1.readLine();
                if(player1Choice.equals("startPressed")){
                    outPlayer1.println("SETCATEGORY");
                }
                if(player1Choice.equals("Film")){
                    //Fortsätt koden här..
                }

            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}