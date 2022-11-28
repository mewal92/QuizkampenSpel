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
    ArrayList<JButton> categoryButtonsList = new ArrayList<>();

        InetAddress ip = InetAddress.getLocalHost();
        int port = 44444;
        Socket sock = new Socket(ip, port);
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        Answers answerList = new Answers();
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
            for (JButton jButton : answerButtonsList)
                jButton.setBackground(Color.LIGHT_GRAY);

            answer1.addActionListener(this);
            answer2.addActionListener(this);
            answer3.addActionListener(this);
            answer4.addActionListener(this);
            category1.addActionListener(this);
            category2.addActionListener(this);
            category3.addActionListener(this);
            category4.addActionListener(this);
            nameField.addActionListener(this);

            /*nameField.addActionListener(e -> {
                    out.println(nameField.getText());
            title2.setText("Väntar på en motståndare..");
            frame.repaint();
            frame.revalidate();});*/
            title.setFont(new Font("Tahoma", Font.PLAIN, 23));
            score.setFont(new Font("Tahoma", Font.PLAIN, 23));

            }

    public void play() throws Exception {
        String serverResponse;
        try {
            serverResponse = in.readLine();
            if (serverResponse != null) {
                title2.setText(serverResponse);
                frame.revalidate();
                frame.repaint();
                basePanel.add(play);
                play.addActionListener(e -> out.println("startPressed"));

            }

            while (true) {
                String inFromServer = in.readLine();
                System.out.println(inFromServer);
                if (inFromServer.equals("SET CATEGORY")) {
                    System.out.println("ny bräda");
                    instructionsPlayer();
                } else if (inFromServer.equals("SET WAIT")) {
                    title.setText("Väntar på din tur..");
                    basePanel.remove(title2);
                    basePanel.remove(nameField);
                    basePanel.remove(play);
                    basePanel.add(score);
                    frame.repaint();
                    frame.revalidate();
                } else if (inFromServer.equals("SET ALTERNATIVES")) {
                    answerQuestion();
                } else if (inFromServer.equals("SLUT")) {
                    JLabel slut = new JLabel("SLUT!!!!!!!!");
                    basePanel.remove(title2);
                    basePanel.remove(nameField);
                    basePanel.remove(play);
                    basePanel.remove(answer1);
                    basePanel.remove(answer2);
                    basePanel.remove(answer3);
                    basePanel.remove(answer4);
                    frame.add(slut);
                    frame.repaint();
                    break;
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public void instructionsPlayer(){
            System.out.println("Väljer kategori");
            title.setText("Välj kategori");
            basePanel.remove(title2);
            basePanel.remove(nameField);
            basePanel.remove(play);
            basePanel.remove(answer1);
            basePanel.remove(answer2);
            basePanel.remove(answer3);
            basePanel.remove(answer4);
            basePanel.add(score);
            basePanel.add(category1);
            basePanel.add(category2);
            basePanel.add(category3);
            basePanel.add(category4);
            frame.revalidate();
            frame.repaint();

                  /*  category1.addActionListener(e -> {
                        out.println("vidarePressed");
                        out.println("Film");
                        });
                    category2.addActionListener(e -> {
                        out.println("vidarePressed");
                        out.println("Musik");
                    });
                    category3.addActionListener(e -> {
                        out.println("vidarePressed");
                        out.println("Java-kunskap");
                    });
                    category4.addActionListener(e -> {
                        out.println("vidarePressed");
                        out.println("Övrigt");
                    });
                    frame.repaint();
                    frame.revalidate();*/

                //Här kommer frågan.

        }

        private void answerQuestion() throws IOException {
                currentCat = in.readLine();
                String questionFromServer = in.readLine();
                rättSvar = in.readLine();

                if (questionFromServer != null) {
                    questionCounter ++;
                    if(questionCounter == 2)
                        roundCounter++;
                    title.setText(questionFromServer);
                    basePanel.add(answer1);
                    basePanel.add(answer2);
                    basePanel.add(answer3);
                    basePanel.add(answer4);

                    answer1.setText(in.readLine());
                    answer2.setText(in.readLine());
                    answer3.setText(in.readLine());
                    answer4.setText(in.readLine());

                    basePanel.remove(category1);
                    basePanel.remove(category2);
                    basePanel.remove(category3);
                    basePanel.remove(category4);

                    frame.repaint();
                    frame.revalidate();
                }
            }
        public static void main(String[] args) throws Exception {
            QuizkampenClient client = new QuizkampenClient();
            client.play();
        }
        public void progressCheck() throws IOException {
            if (questionCounter == 2 && roundCounter ==2) {
                System.out.println("hit1");
                out.println("slut");
                frame.add(slut);
            } else if (questionCounter !=2){
                System.out.println("hit2");
                System.out.println(currentCat);
                for (JButton jButton : answerButtonsList)
                    jButton.setBackground(Color.LIGHT_GRAY);
                out.println("vidarePressed");
                out.println(currentCat);
            } else {
                questionCounter = 0;
                for (JButton jButton : answerButtonsList)
                    jButton.setBackground(Color.LIGHT_GRAY);
                for (JButton jButton : categoryButtonsList) {
                    jButton.setBackground(Color.LIGHT_GRAY);
                    if (jButton.getText().equals(currentCat)) {
                        jButton.setVisible(false);
                    }
                }
                System.out.println("hit3");
                out.println("newRound");
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == nameField){
                out.println(nameField.getText());
                title2.setText("Väntar på en motspelare..");
                frame.repaint();
                frame.revalidate();
            }else{
                for (JButton jButton : answerButtonsList) {
                    if (e.getSource() == jButton) {
                        if (rättSvar.contains(jButton.getText())) {
                            jButton.setBackground(Color.green);
                        } else {
                            jButton.setBackground(Color.red);
                        }
                        frame.repaint();
                        frame.revalidate();

                        ActionListener taskPerformer = e1 -> {
                            try {
                                progressCheck();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        };
                        Timer timer = new Timer(1000, taskPerformer);
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
                for (JButton jButton: categoryButtonsList){
                    if (e.getSource() == jButton){
                        out.println("vidarePressed");
                        out.println(jButton.getText());
                    }
                }
            }

        }
    }