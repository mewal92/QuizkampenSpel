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
        JButton vidare = new JButton("Vidare");

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

            nameField.addActionListener(e -> {
                    out.println(nameField.getText());
            title2.setText("Väntar på en motståndare..");
            frame.repaint();
            frame.revalidate();});
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
                } else if (inFromServer.equals("SLUT"))
                    break;


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public void instructionsPlayer(){
            questionCounter = 0;
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

                    category1.addActionListener(e -> {
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
                    frame.revalidate();

                //Här kommer frågan.

        }

        private void answerQuestion() throws IOException {
                currentCat = in.readLine();
                String questionFromServer = in.readLine();
                rättSvar = in.readLine();
                System.out.println(questionFromServer);

                if (questionFromServer != null) {
                    questionCounter ++;
                    title.setText(questionFromServer);
                    basePanel.add(answer1);
                    basePanel.add(answer2);
                    basePanel.add(answer3);
                    basePanel.add(answer4);
                    frame.add(vidare);
                    vidare.setVisible(false);

                    answer1.setText(in.readLine());
                    answer2.setText(in.readLine());
                    answer3.setText(in.readLine());
                    answer4.setText(in.readLine());

                    answer1.addActionListener(this);
                    answer2.addActionListener(this);
                    answer3.addActionListener(this);
                    answer4.addActionListener(this);
                    vidare.addActionListener(this);

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
                out.println("vidarePressed");
                out.println(currentCat);
            } else {
                roundCounter++;
                basePanel.remove(answer1);
                basePanel.remove(answer2);
                basePanel.remove(answer3);
                basePanel.remove(answer4);
                System.out.println("hit3");
                out.println("startPressed");
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            /*out.println(nameField.getText());
            title2.setText("Väntar på en motståndare..");
            frame.repaint();
            frame.revalidate();*/

            //Kollar om spelaren har tryckt på answer1 och om det svarsalternativet finns i korrektasvar-listan
            //och samma på else if fast knappen blir röd om svaret är fel
            if (e.getSource() == answer1 && answer1.getText().equals(rättSvar)) {
                //om den är korrekt blir knappen grön
                answer1.setBackground(Color.green);
                vidare.setVisible(true);
                frame.repaint();
            } else if (e.getSource() == answer1 && !answer1.getText().equals(rättSvar)) {
                answer1.setBackground(Color.red);
                vidare.setVisible(true);
                frame.repaint();
            }
            //OSV samma logik fast resterande svarsknappar
            else if (e.getSource() == answer2 && answer2.getText().equals(rättSvar)) {
                answer2.setBackground(Color.green);
            } else if (e.getSource() == answer2 && !answer2.getText().equals(rättSvar)) {
                answer2.setBackground(Color.red);
            }
            else if (e.getSource() == answer3 && answer3.getText().equals(rättSvar)) {
                answer3.setBackground(Color.green);
            } else if (e.getSource() == answer3 && !answer3.getText().equals(rättSvar)) {
                answer3.setBackground(Color.red);
            }
            else if (e.getSource() == answer4 && answer4.getText().equals(rättSvar)) {
                answer4.setBackground(Color.green);
            } else if (e.getSource() == answer4 && !answer4.getText().equals(rättSvar)) {
                answer4.setBackground(Color.red);
            }
            if (e.getSource() == vidare){

                frame.remove(vidare);
                try {
                    progressCheck();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }