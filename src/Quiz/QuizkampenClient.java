package Quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class QuizkampenClient implements ActionListener {
    GameGUI gameGui = new GameGUI();
    //nu instans av GameGUI klassen. Den handahåller nästan all grafik så koden blir renare

    ArrayList<JButton> answerButtonsList = new ArrayList<>();
    ArrayList<JButton> categoryButtonsList = new ArrayList<>();
    InetAddress ip = InetAddress.getLocalHost();
    int port = 55555;
    Socket sock = new Socket(ip, port);
    PrintWriter outToServer = new PrintWriter(sock.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    String rättSvar = "";
    String opponent = "";
    String player= "";

    public QuizkampenClient() throws IOException {
        //listor skapas av svarsknappar och kategoriknappar för att kunna loopa
        answerButtonsList.add(gameGui.answer1);
        answerButtonsList.add(gameGui.answer2);
        answerButtonsList.add(gameGui.answer3);
        answerButtonsList.add(gameGui.answer4);
        categoryButtonsList.add(gameGui.category1);
        categoryButtonsList.add(gameGui.category2);
        categoryButtonsList.add(gameGui.category3);
        categoryButtonsList.add(gameGui.category4);
        //action listeners kopplas till knapparna
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
        gameGui.geUpp.addActionListener(this);

    }

    public void game() throws Exception {
        String serverResponse;
        serverResponse = in.readLine();
        if (serverResponse != null) {
            //motståndarens namn har mottagits(och sparats), vilket gör att startknappen kan ritas ut
            opponent = in.readLine();
            gameGui.title2.setText(serverResponse);
            gameGui.setStartScreen2();
            gameGui.play.addActionListener(e -> outToServer.println("startPressed"));
        }

        while (true) {
            //loop som lyssnar efter kommandon från servern. Är den igång så är spelet igång
            String inFromServer = in.readLine();
            System.out.println(inFromServer);
            if (inFromServer.equals("SET CATEGORY")) {
                setCategories();
            } else if (inFromServer.equals("SET QUESTION")) {
                setQuestion();
                answerQuestion();
            } else if (inFromServer.equals("SET WAIT")) {
                waiting();
            } else if (inFromServer.equals("SET ENDSCREEN")){
                setEndScreen();
                break;
                //detta kommando gör att spelet avslutas
            }else if(inFromServer.equals("SET SUMMARY")){
                setSummary();
            }
        }
    }

    private void setEndScreen() throws IOException {
        //metod vid slut av spelet. Läser in bådas poäng från servern och anger vinnaren
        String points1=in.readLine();
        String points2=in.readLine();
        if (parseInt(points1) >parseInt(points2))
            gameGui.title2.setText(player + " vann!");
        else if(parseInt(points1) <parseInt(points2))
            gameGui.title2.setText(opponent + " vann, du förlorade tyvärr..");
        else
            gameGui.title2.setText("Oavgjort");
        gameGui.scorePlayer1.setText("Dina slutpoäng: " + points1);
        gameGui.scorePlayer2.setText(opponent + "s slutpoäng: "+ points2);
        gameGui.setEndScreenGUI();
    }

    private void setSummary() throws IOException {
        //när en rond är slut, men inte spelet, så skrivs poängen ut så långt
        gameGui.vidare.addActionListener(this);
        gameGui.scorePlayer1.setText("Dina poäng: " + in.readLine());
        gameGui.scorePlayer2.setText(opponent + "s poäng: "+ in.readLine());
        gameGui.setSummaryGUI();
    }
    //3 GUI metoder nedan som har beskrivande namn och som inte behöver någon indata
    public void setCategories () {
        gameGui.setChooseCategoryGui();
    }
    public void setQuestion () {
        gameGui.setQuestionScreenGUI();
    }
    public void waiting(){
        gameGui.setWaitScreenGUI();
    }


    private void answerQuestion () throws IOException {
        //metod som läser in fråga, rätt svar och svarsalternativ som placeras på knappar
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
        //action för att gå vidare från resultatsskärm
        if (e.getSource() == gameGui.vidare){
            outToServer.println("next");
        }
        if (e.getSource() == gameGui.geUpp){
            outToServer.println("geUppPressed");
        }
        if (e.getSource() == gameGui.nameField) {
            //sparar samt skickar ut spelarens namn
            player = gameGui.nameField.getText();
            outToServer.println(player);
            gameGui.title2.setText("Väntar på en motspelare..");
            gameGui.frame.repaint();
            gameGui.frame.revalidate();
        }
        for (JButton jButton : answerButtonsList) {
            /*Om någon av svarsknapparna trycks, ändras färgen beroende på rätt eller fel
            * fördröjning av eventet sker så man hinner se färgen. rätt eller fel skickas ut
            * till servern som kommando*/
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
        //kommando samt vald kategori från kategoriknappslistan skickas till servern
        for (JButton jButton : categoryButtonsList) {
            if (e.getSource() == jButton) {
                outToServer.println("vidarePressed");
                outToServer.println(jButton.getText());
            }

        }
    }

}




