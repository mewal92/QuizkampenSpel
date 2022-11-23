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
    JFrame frame = new JFrame("Quizkampen");
    BufferedImage backgroundImage = ImageIO.read(new File("background.jpg"));
    JPanel basePanel = new JPanel();
    JLabel title = new JLabel("Välkommen till Quizkampen!");
    JLabel title2 = new JLabel("Skriv ditt namn för att börja spela: ");
    JButton play = new JButton("Starta spel");
    JTextField nameField = new JTextField("",10);
    int port = 44444;
    InetAddress ip = InetAddress.getLocalHost();

    public QuizkampenClient() throws IOException {
        frame.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
        frame.setLayout(new FlowLayout());
        frame.add(basePanel);
        frame.setSize(800, 530);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        basePanel.setLayout(new GridLayout(4,1));
        basePanel.setBorder(new EmptyBorder(100, 100, 20 , 20));
        title.setFont(new Font("Tahoma", Font.PLAIN, 23 ));
        basePanel.add(title);
        basePanel.add(title2);
        basePanel.add(nameField);
        basePanel.setBackground(new Color(0, 0, 0, 0));

        try(Socket sock = new Socket(ip, port)){
            PrintWriter out = new PrintWriter(sock.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            nameField.addActionListener(e -> out.println(nameField.getText()));
            String serverResponse;
            while((serverResponse = in.readLine()) != null){
                title2.setText(serverResponse);
                frame.revalidate();
                frame.repaint();
                basePanel.add(play);
                play.addActionListener(e -> {out.println("startPressed");});
            }
            while(in.readLine().equals("SETCATEGORY")){
                title.setText("Välj kategori");
                frame.remove(title2);
                frame.repaint();
                frame.revalidate();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new QuizkampenClient();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //if (e.getSource() == play) {}
    }
}
