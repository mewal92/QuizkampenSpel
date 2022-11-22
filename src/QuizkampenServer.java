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

        try(PrintWriter outPlayer1 = new PrintWriter(player1Socket.getOutputStream(), true);
            PrintWriter outPlayer2 = new PrintWriter(player2Socket.getOutputStream(), true);
            BufferedReader inPlayer1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            BufferedReader inPlayer2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()))){
            String messageFromPLayer;
            while((messageFromPLayer=inPlayer1.readLine()) != null){
                System.out.println("inne i loopen");
                outPlayer1.println("Welcome " + messageFromPLayer+". Let´s play!");
                outPlayer2.println("Welcome " + messageFromPLayer+". Let´s play!");
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){

    }
}

/*&& (inPlayer1.readLine()) != null
outPlayer2.println("Welcome " + inPlayer2.readLine()+". Let´s play!");
 */