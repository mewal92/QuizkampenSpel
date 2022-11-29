package Quiz;

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
    BufferedImage backgroundImage = ImageIO.read(new File("src/Images/background.jpg"));
    JLabel title = new JLabel("Välkommen till Quizkampen!");
    JLabel title2 = new JLabel("Skriv ditt namn för att börja spela: ");
    JLabel scorePlayer1 = new JLabel("Poäng: ");
    JLabel scorePlayer2 = new JLabel("Poäng: ");
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
    JButton vidare = new JButton("Vidare");
    ArrayList<JButton> answerButtonsList = new ArrayList<>();
    ArrayList<JButton> categoryButtonsList = new ArrayList<>();
    InetAddress ip = InetAddress.getLocalHost();
    int port = 44444;
    Socket sock = new Socket(ip, port);
    PrintWriter outToServer = new PrintWriter(sock.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    String currentCat = "";
    JLabel slut = new JLabel("slut");
    String rättSvar = "";

    int questionCounter = 0;
    int roundCounter = 0;

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
        categoryButtonsList.add(category1);
        categoryButtonsList.add(category2);
        categoryButtonsList.add(category3);
        categoryButtonsList.add(category4);

        answer1.addActionListener(this);
        answer2.addActionListener(this);
        answer3.addActionListener(this);
        answer4.addActionListener(this);
        category1.addActionListener(this);
        category2.addActionListener(this);
        category3.addActionListener(this);
        category4.addActionListener(this);
        nameField.addActionListener(this);
        vidare.addActionListener(this);

        title.setFont(new Font("Tahoma", Font.PLAIN, 23));
        scorePlayer1.setFont(new Font("Tahoma", Font.PLAIN, 23));
        scorePlayer2.setFont(new Font("Tahoma", Font.PLAIN, 23));
    }

        public void game() throws Exception {
            String serverResponse;
            serverResponse = in.readLine();
            if (serverResponse != null) {
                title2.setText(serverResponse);
                frame.revalidate();
                frame.repaint();
                basePanel.add(play);
                play.addActionListener(e -> outToServer.println("startPressed"));

            }

            while (true) {
                String inFromServer = in.readLine();
                System.out.println(inFromServer);
                if (inFromServer.equals("SET CATEGORY")) {
                    setCategories();
                } else if (inFromServer.equals("SET QUESTION")) {
                    setQuestion();
                    answerQuestion();
                } else if (inFromServer.equals("SET WAIT")) {
                    waiting();
                } else if (inFromServer.equals("QUIT")) {
                    break;
                }else if (inFromServer.equals("SET ENDSCREEN")){
                    //metod med gui för slutresultat
                    setEndscreen();
                }else if(inFromServer.equals("SET SUMMARY")){
                    //metd med gui för rondresultat
                    setSummary();
                }
            }
        }

    private void setEndscreen() throws IOException {
        basePanel.remove(answer1);
        basePanel.remove(answer2);
        basePanel.remove(answer3);
        basePanel.remove(answer4);
        title.setText("Resultaten");
        vidare.addActionListener(this);
        scorePlayer1.setText("Dina poäng: " + in.readLine()+ "Motståndarens poäng: "+ in.readLine());
        basePanel.add(scorePlayer1);
        frame.repaint();
        frame.revalidate();
    }

    private void setSummary() throws IOException {
        basePanel.remove(answer1);
        basePanel.remove(answer2);
        basePanel.remove(answer3);
        basePanel.remove(answer4);
        title.setText("Ronden är slut");
        basePanel.add(vidare);
        vidare.addActionListener(this);
        scorePlayer1.setText("Dina poäng: " + in.readLine());
        basePanel.add(scorePlayer1);
        frame.repaint();
        frame.revalidate();
    }

    public void setCategories () {
            title.setText("Välj kategori");
        basePanel.remove(vidare);
        basePanel.remove(scorePlayer1);
            basePanel.remove(title2);
            basePanel.remove(nameField);
            basePanel.remove(play);
            basePanel.add(category1);
            basePanel.add(category2);
            basePanel.add(category3);
            basePanel.add(category4);
            basePanel.remove(answer1);
            basePanel.remove(answer2);
            basePanel.remove(answer3);
            basePanel.remove(answer4);
            frame.repaint();
            frame.revalidate();
        }
        public void setQuestion () {
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
            basePanel.remove(scorePlayer1);
            basePanel.remove(vidare);

            frame.repaint();
            frame.revalidate();

        }
        public void waiting() throws IOException {
            title.setText("Väntar på din tur..");
            basePanel.remove(vidare);
            basePanel.remove(title2);
            basePanel.remove(nameField);
            basePanel.remove(play);
            basePanel.remove(answer1);
            basePanel.remove(answer2);
            basePanel.remove(answer3);
            basePanel.remove(answer4);
            basePanel.remove(category1);
            basePanel.remove(category2);
            basePanel.remove(category3);
            basePanel.remove(category4);
            frame.repaint();
            frame.revalidate();
        }


        private void answerQuestion () throws IOException {
            while (true) {
                String questionFromServer = in.readLine();
                rättSvar = in.readLine();
                if (questionFromServer != null) {
                    title.setText(questionFromServer);

                    answer1.setText(in.readLine());
                    answer2.setText(in.readLine());
                    answer3.setText(in.readLine());
                    answer4.setText(in.readLine());
                    break;
                }
            }
        }

        public static void main (String[]args) throws Exception {
            QuizkampenClient client = new QuizkampenClient();
            client.game();
        }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vidare){
            outToServer.println("next");
        }
        if (e.getSource() == nameField) {
                outToServer.println(nameField.getText());
                title2.setText("Väntar på en motspelare..");
                frame.repaint();
                frame.revalidate();
            }
            for (JButton jButton : answerButtonsList) {

                if (e.getSource() == jButton) {
                        if (rättSvar.contains(jButton.getText())) {
                            jButton.setBackground(Color.green);
                            ActionListener taskPerformer = e1 -> {
                                outToServer.println("CorrectAnswer");
                            };
                            Timer timer = new Timer(1000, taskPerformer);
                            timer.setRepeats(false);
                            timer.start();
                        } else {
                            jButton.setBackground(Color.red);
                            ActionListener taskPerformer = e1 -> {
                            outToServer.println("WrongAnswer");
                        };
                            Timer timer = new Timer(1000, taskPerformer);
                            timer.setRepeats(false);
                            timer.start();
                    }
                     frame.repaint();
                     frame.revalidate();
                 }
                }

            for (JButton jButton : categoryButtonsList) {
                if (e.getSource() == jButton) {
                    outToServer.println("vidarePressed");
                    outToServer.println(jButton.getText());
                }

            }
        }

    }









