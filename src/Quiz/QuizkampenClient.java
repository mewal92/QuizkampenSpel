package Quiz;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class QuizkampenClient extends GameGUI implements ActionListener {
    ArrayList<JButton> answerButtonsList = new ArrayList<>();
    ArrayList<JButton> categoryButtonsList = new ArrayList<>();
    InetAddress ip = InetAddress.getLocalHost();
    int port = 44444;
    Socket sock = new Socket(ip, port);
    PrintWriter outToServer = new PrintWriter(sock.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    String rättSvar = "";

    int questionCounter = 0;
    int roundCounter = 0;

    public QuizkampenClient() throws IOException {
        answerButtonsList.add(answer1);
        answerButtonsList.add(answer2);
        answerButtonsList.add(answer3);
        answerButtonsList.add(answer4);
        categoryButtonsList.add(category1);
        categoryButtonsList.add(category2);
        categoryButtonsList.add(category3);
        categoryButtonsList.add(category4);
        setStartScreenGUI();
        while (true) {
            String serverResponse;
            nameField.addActionListener(this);
            if ((serverResponse = in.readLine()) != null) {
                title2.setText(serverResponse);
                frame.revalidate();
                frame.repaint();
                play.addActionListener(e -> outToServer.println("startPressed"));
                instructionsFromServer();
                break;
            }
        }
    }

    public void instructionsFromServer() throws IOException {
        basePanel.remove(title2);
        basePanel.remove(nameField);
        basePanel.remove(play);
        setChooseCategoryGui();
        while (true) {
            String inFromServer = in.readLine();
            System.out.println("inFromServer: " + inFromServer);
            if (inFromServer.equals("SET CATEGORY")) {
                category1.addActionListener(this);
                category2.addActionListener(this);
                category3.addActionListener(this);
                category4.addActionListener(this);
            }
            if (inFromServer.equals("SET QUESTION")) {
              setQuestionScreenGUI();
                answer1.addActionListener(this);
                answer2.addActionListener(this);
                answer3.addActionListener(this);
                answer4.addActionListener(this);
                answerQuestion();
                break;
            }
            if (inFromServer.equals("SET WAIT")) {
                setWaitScreenGUI();
                frame.repaint();
                frame.revalidate();
                break;
            }

        }
            }

    private void answerQuestion() throws IOException {
        while (true) {
            setQuestionScreenGUI();
            //currentCat = in.readLine();
            //tillagd för lokal kategori
            String questionFromServer = in.readLine();
            rättSvar = in.readLine();
            if (questionFromServer != null) {
                title.setText(questionFromServer);
                questionCounter ++;
                if(questionCounter == 2)
                    roundCounter++;
                answer1.setText(in.readLine());
                answer2.setText(in.readLine());
                answer3.setText(in.readLine());
                answer4.setText(in.readLine());
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


            for (JButton jButton : answerButtonsList) {
                if (e.getSource() == jButton) {
                    if (rättSvar.contains(jButton.getText())) {
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
            for (JButton jButton: categoryButtonsList){
                if (e.getSource() == jButton){
                    outToServer.println("vidarePressed");
                    outToServer.println(jButton.getText());
                }
            }
        }
    }
}
