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
import java.util.ArrayList;

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
    JButton answer1 = new JButton("Svarsalternativ 1");
    JButton answer2 = new JButton("Svarsalternativ 2");
    JButton answer3 = new JButton("Svarsalternativ 3");
    JButton answer4 = new JButton("Svarsalternativ 4");
    ArrayList<JButton> answerButtonsList = new ArrayList<>();

    InetAddress ip = InetAddress.getLocalHost();
    int port = 44444;
    Socket sock = new Socket(ip, port);
    PrintWriter outToServer = new PrintWriter(sock.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    Answers answerList = new Answers();

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

        answerButtonsList.add(answer1);
        answerButtonsList.add(answer2);
        answerButtonsList.add(answer3);
        answerButtonsList.add(answer4);

        answer1.addActionListener(this);
        answer2.addActionListener(this);
        answer3.addActionListener(this);
        answer4.addActionListener(this);
        nameField.addActionListener(this);

        title.setFont(new Font("Tahoma", Font.PLAIN, 23));
        score.setFont(new Font("Tahoma", Font.PLAIN, 23));

        while (true) {
            String serverResponse;
            if ((serverResponse = in.readLine()) != null) {
                title2.setText(serverResponse);
                frame.revalidate();
                frame.repaint();
                basePanel.add(play);
                play.addActionListener(e -> outToServer.println("startPressed"));
                instructionsFromServer();
                break;
            }
        }
    }

    public void instructionsFromServer() throws IOException {
        while (true) {
            String inFromServer = in.readLine();
            System.out.println("inFromServer: " + inFromServer);
            if (inFromServer.equals("SET CATEGORY")) {
                title.setText("Välj kategori");
                basePanel.remove(title2);
                basePanel.remove(nameField);
                basePanel.remove(play);
                basePanel.add(score);
                basePanel.add(category1);
                basePanel.add(category2);
                basePanel.add(category3);
                basePanel.add(category4);
                category1.addActionListener(e -> outToServer.println("Film"));
                category2.addActionListener(e -> outToServer.println("Musik"));
                category3.addActionListener(e -> outToServer.println("Java-kunskap"));
                category4.addActionListener(e -> outToServer.println("Övrigt"));
                frame.repaint();
                frame.revalidate();
            }
            if (inFromServer.equals("SET QUESTION")) {
                basePanel.add(answer1);
                basePanel.add(answer2);
                basePanel.add(answer3);
                basePanel.add(answer4);

                answer1.setBackground(Color.white);
                answer2.setBackground(Color.white);
                answer3.setBackground(Color.white);
                answer4.setBackground(Color.white);

                basePanel.remove(category1);
                basePanel.remove(category2);
                basePanel.remove(category3);
                basePanel.remove(category4);

                frame.repaint();
                frame.revalidate();
                answerQuestion();
                break;
            }
            if (inFromServer.equals("SET WAIT")) {
                title.setText("Väntar på din tur..");
                basePanel.remove(title2);
                basePanel.remove(nameField);
                basePanel.remove(play);
                basePanel.add(score);
                frame.repaint();
                frame.revalidate();
                break;
            }

        }
    }

    private void answerQuestion() throws IOException {
        while (true) {
            String questionFromServer = in.readLine();
            int answerIndex = Integer.parseInt(in.readLine());
            if (questionFromServer != null) {
                title.setText(questionFromServer);

                answer1.setText(answerList.allAnswers.get(answerIndex).get(0));
                answer2.setText(answerList.allAnswers.get(answerIndex).get(1));
                answer3.setText(answerList.allAnswers.get(answerIndex).get(2));
                answer4.setText(answerList.allAnswers.get(answerIndex).get(3));
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new QuizkampenClient();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == nameField){
            outToServer.println(nameField.getText());
            title2.setText("Väntar på en motspelare..");
            frame.repaint();
            frame.revalidate();
        }else{
            for (JButton jButton : answerButtonsList) {
                if (e.getSource() == jButton) {
                    if (answerList.correctAnswers.contains(jButton.getText())) {
                        jButton.setBackground(Color.green);
                        outToServer.println("CorrectAnswer");
                    } else {
                        jButton.setBackground(Color.red);
                        outToServer.println("WrongAnswer");
                    }
                    frame.repaint();
                    frame.revalidate();

                    ActionListener taskPerformer = e1 -> {
                        try {
                            instructionsFromServer();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    };
                    Timer timer = new Timer(1000, taskPerformer);
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        }
    }
}
