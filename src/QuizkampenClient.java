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
    JPanel basePanel = new JPanel();
    BufferedImage backgroundImage = ImageIO.read(new File("background.jpg"));
    JLabel title = new JLabel("Välkommen till Quizkampen!");
    JLabel title2 = new JLabel("Skriv ditt namn för att börja spela: ");
    JLabel score = new JLabel("Poäng: ");
    JTextField nameField = new JTextField("", 10);
    JButton play = new JButton("Starta spel");
    JButton category1 = new JButton("Film");
    JButton category2 = new JButton("Musik");
    JButton category3 = new JButton("Java-kunskap");
    JButton category4 = new JButton("Övrigt");

    InetAddress ip = InetAddress.getLocalHost();
    int port = 44444;
    Socket sock = new Socket(ip, port);
    PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

    public QuizkampenClient() throws IOException {
        frame.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
        frame.setLayout(new FlowLayout());
        frame.add(basePanel);
        frame.setSize(800, 530);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        basePanel.setLayout(new GridLayout(4, 1));
        basePanel.setBorder(new EmptyBorder(100, 100, 20, 20));
        basePanel.add(title);
        basePanel.add(title2);
        basePanel.add(nameField);
        basePanel.setBackground(new Color(0, 0, 0, 0));

        nameField.addActionListener(this);
        title.setFont(new Font("Tahoma", Font.PLAIN, 23));
        score.setFont(new Font("Tahoma", Font.PLAIN, 23));

        String serverResponse;
        while (true) {
            if ((serverResponse = in.readLine()) != null) {
                title2.setText(serverResponse);
                frame.revalidate();
                frame.repaint();
                basePanel.add(play);
                play.addActionListener(e -> out.println("startPressed"));
                break;
            }
        }
        while (in.readLine().equals("SETCATEGORY")) {
            title.setText("Välj kategori");
            basePanel.remove(title2);
            basePanel.remove(nameField);
            basePanel.remove(play);
            basePanel.add(score);
            basePanel.add(category1);
            basePanel.add(category2);
            basePanel.add(category3);
            basePanel.add(category4);
            category1.addActionListener(e -> out.println("Film"));
            category2.addActionListener(e -> out.println("Musik"));
            category3.addActionListener(e -> out.println("Java-kunskap"));
            category4.addActionListener(e -> out.println("Övrigt"));
            frame.repaint();
            frame.revalidate();
        }
    }



    public static void main(String[] args) throws IOException {
        new QuizkampenClient();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        out.println(nameField.getText());
        title2.setText("Väntar på en motståndare..");
        frame.repaint();
        frame.revalidate();
    }
}
