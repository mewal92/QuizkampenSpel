package Quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class QuizkampenClient implements ActionListener {
    GameGUI gameGui = new GameGUI();
    /*
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

     */
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
        /*frame.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
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
*/
        answerButtonsList.add(gameGui.answer1);
        answerButtonsList.add(gameGui.answer2);
        answerButtonsList.add(gameGui.answer3);
        answerButtonsList.add(gameGui.answer4);
        categoryButtonsList.add(gameGui.category1);
        categoryButtonsList.add(gameGui.category2);
        categoryButtonsList.add(gameGui.category3);
        categoryButtonsList.add(gameGui.category4);

        gameGui.answer1.addActionListener(this);
        gameGui.answer2.addActionListener(this);
        gameGui.answer3.addActionListener(this);
        gameGui.answer4.addActionListener(this);
        gameGui.category1.addActionListener(this);
        gameGui.category2.addActionListener(this);
        gameGui.category3.addActionListener(this);
        gameGui.category4.addActionListener(this);
        gameGui.nameField.addActionListener(this);
        gameGui.vidare.addActionListener(this);

    }

        public void game() throws Exception {
            String serverResponse;
            serverResponse = in.readLine();
            if (serverResponse != null) {
                gameGui.setStartScreen2();

                /*title2.setText(serverResponse);
                frame.revalidate();
                frame.repaint();
                bottomHalf.add(play);*/
                gameGui.play.addActionListener(e -> outToServer.println("startPressed"));

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
                    setEndScreen();
                }else if(inFromServer.equals("SET SUMMARY")){
                    //metd med gui för rondresultat
                    setSummary();
                }
            }
        }

    private void setEndScreen() throws IOException {
        gameGui.setEndScreenGUI();
        gameGui.vidare.addActionListener(this);
        gameGui.scorePlayer1.setText("Dina poäng: " + in.readLine()+ "Motståndarens poäng: "+ in.readLine());
    }

    private void setSummary() throws IOException {
        gameGui.setSummaryGUI();
        gameGui.vidare.addActionListener(this);
        gameGui.scorePlayer1.setText("Dina poäng: " + in.readLine());
    }

    public void setCategories () {
        gameGui.setChooseCategoryGui();
        }
        public void setQuestion () {
            gameGui.setQuestionScreenGUI();
        }
        public void waiting() throws IOException {
            gameGui.setWaitScreenGUI();
        }


        private void answerQuestion () throws IOException {
            while (true) {
                String questionFromServer = in.readLine();
                rättSvar = in.readLine();
                if (questionFromServer != null) {
                    gameGui.title.setText(questionFromServer);

                    gameGui.answer1.setText(in.readLine());
                    gameGui.answer2.setText(in.readLine());
                    gameGui.answer3.setText(in.readLine());
                    gameGui.answer4.setText(in.readLine());
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
        if (e.getSource() == gameGui.vidare){
            outToServer.println("next");
        }
        if (e.getSource() == gameGui.nameField) {
                outToServer.println(gameGui.nameField.getText());
                gameGui.title2.setText("Väntar på en motspelare..");
                gameGui.frame.repaint();
                gameGui.frame.revalidate();
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
                        gameGui.frame.repaint();
                        gameGui.frame.revalidate();
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









