import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class QuizkampenClient implements ActionListener {
    JFrame frame = new JFrame("Quizkampen");
    JPanel basePanel = new JPanel();
    JLabel title = new JLabel("Quizkampen! Enter name:");
    JTextField nameField = new JTextField("",10);
    JLabel messageFromGameServer = new JLabel("X");
    int port = 44444;
    InetAddress ip = InetAddress.getLocalHost();

    public QuizkampenClient() throws UnknownHostException {
        frame.setSize(500,200);
        frame.add(basePanel);
        basePanel.add(title);
        basePanel.add(nameField);
        basePanel.add(messageFromGameServer);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try(Socket sock = new Socket(ip, port)){
            PrintWriter out = new PrintWriter(sock.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            nameField.addActionListener(e -> out.println(nameField.getText()));
            String serverResponse;
            while((serverResponse = in.readLine()) != null){
                messageFromGameServer.setText(serverResponse);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args) throws UnknownHostException {
        new QuizkampenClient();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}