import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class QuizkampenServer extends Thread{
    Socket clientSocket;
    String player;

    public QuizkampenServer(Socket socket, String player) throws IOException {
        clientSocket = socket;
        this.player = player;

    }
    public void run(){
        try(PrintWriter outStream = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){
            while((in.readLine()) != null){
                if(player.equals("player1")){
                    outStream.println("Welcome " + in.readLine()+". Waiting for another player..");
                }else{
                    outStream.println("Welcome " + in.readLine() + ". You have an opponent. Let´s start the game!");
                    //Start game here
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}