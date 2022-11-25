package Quiz;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServerListener {
    public GameServerListener() {
        try(ServerSocket listener = new ServerSocket(44444)){
            while(true){
                Socket player1 = listener.accept();
                Socket player2 = listener.accept();
                QuizkampenServer game = new QuizkampenServer(player1, player2);
                game.start();
            }
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        new GameServerListener();
    }
}