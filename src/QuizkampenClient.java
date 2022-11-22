import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class QuizkampenClient implements ActionListener {
    JPanel basePanel = new JPanel();
    JLabel title = new JLabel("Välkommen till Quizkampen!");
    JLabel title2 = new JLabel("Skriv ditt namn för att börja spela: ");
    JButton play = new JButton("Starta spel");
    JTextField nameField = new JTextField("",10);
    JLabel messageFromGameServer = new JLabel();
    BufferedImage backgroundImage;
    int port = 44444;
    InetAddress ip = InetAddress.getLocalHost();


    public QuizkampenClient() throws IOException {
        basePanel.setLayout(new GridLayout(4,1));
        basePanel.setBorder(new EmptyBorder(100, 100, 20 , 20));
        title.setFont(new Font("Tahoma", 1, 23 ));
        basePanel.add(title);
        basePanel.add(title2);
        basePanel.add(nameField);
        basePanel.setBackground(new Color(0, 0, 0, 0));
        setFrame();


        try(Socket sock = new Socket(ip, port)){
            PrintWriter out = new PrintWriter(sock.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            nameField.addActionListener(e -> out.println(nameField.getText()));
            String serverResponse;
            while((serverResponse = in.readLine()) != null){
                messageFromGameServer.setText(serverResponse);
                basePanel.remove(title2);
                basePanel.remove(nameField);
                basePanel.add(messageFromGameServer);
                basePanel.add(play);
                play.addActionListener(this);
                basePanel.revalidate();

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    private void setFrame() {
        try {
            JFrame frame = new JFrame("Quizkampen");
            backgroundImage = ImageIO.read(new File("background.jpg"));
            frame.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
            frame.setLayout(new FlowLayout());
            frame.add(basePanel);
            frame.setSize(800, 530);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
        public static void main(String[] args) throws IOException {
        new QuizkampenClient();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == play) {
            new GameProtocol();
        }

    }
}
