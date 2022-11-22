import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class QuizkampenServer extends Thread{
    Socket player1Socket;
    Socket player2Socket;

    public QuizkampenServer(Socket player1, Socket player2) throws IOException {
        player1Socket = player1;
        player2Socket = player2;

    }
    public void run(){
        try(PrintWriter outPlayer1 = new PrintWriter(player1Socket.getOutputStream(), true);
            PrintWriter outPlayer2 = new PrintWriter(player2Socket.getOutputStream(), true);
            BufferedReader inPlayer1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            BufferedReader inPlayer2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()))){
            String player1UserName;
            String player2UserName;
            while((player1UserName=inPlayer1.readLine()) != null &&
                    (player2UserName=inPlayer2.readLine()) != null){
                System.out.println("inne i loopen");
                outPlayer1.println("Welcome " + player1UserName+". Let´s play!");
                outPlayer2.println("Welcome " + player2UserName+". Let´s play!");
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}