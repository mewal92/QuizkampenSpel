import java.io.IOException;
import java.net.ServerSocket;

    public class GameServerListener {
        public GameServerListener() {
            try(ServerSocket listener = new ServerSocket(44444)){
                while(true){
                    QuizkampenServer player1 = new QuizkampenServer(listener.accept(), "player1");
                    player1.start();
                    QuizkampenServer player2 = new QuizkampenServer(listener.accept(), "player2");
                    player2.start();
                }
            }catch (IOException e){
                throw new RuntimeException();
            }
        }
        public static void main(String[] args) {
            new GameServerListener();
        }
    }

