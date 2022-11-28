
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class QuizkampenClient extends GameGUI implements ActionListener {
    InetAddress ip = InetAddress.getLocalHost();
    int port = 44444;
    Socket sock = new Socket(ip, port);
    PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    Answers answerList = new Answers();

        public QuizkampenClient() throws IOException {
            setStartScreen();
        String serverResponse;
        while (true) {

            if ((serverResponse = in.readLine()) != null) {
                title2.setText(serverResponse);
                frame.revalidate();
                frame.repaint();
                basePanel.add(play);
                play.addActionListener(e -> out.println("startPressed"));
                instructionsPlayer();
                break;
            }
        }
    }

    public void instructionsPlayer() throws IOException {
        while (true) {
            String inFromServer = in.readLine();
            if (inFromServer.equals("SET CATEGORY")) {
                setChooseCategory();
                answerQuestion();
                break;
            }
            if (inFromServer.equals("SET WAIT")) {
               setWaitScreen();
            }
            //Här kommer frågan.
        }
    }

    private void answerQuestion() throws IOException {
        while (true) {
            String questionFromServer = in.readLine();
            System.out.println(questionFromServer);
            int answerIndex = Integer.parseInt(in.readLine());
            System.out.println(answerIndex);
            if (questionFromServer != null) {
                setQuestion();
                title.setText(questionFromServer);
                answer1.setText(answerList.allAnswers.get(answerIndex).get(0));
                answer2.setText(answerList.allAnswers.get(answerIndex).get(1));
                answer3.setText(answerList.allAnswers.get(answerIndex).get(2));
                answer4.setText(answerList.allAnswers.get(answerIndex).get(3));
                answer1.addActionListener(this);
                answer2.addActionListener(this);
                answer3.addActionListener(this);
                answer4.addActionListener(this);

                }
        }
    }
    public static void main(String[] args) throws IOException {
        new QuizkampenClient();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == answer1 && answerList.correctAnswers.contains(answer1.getText())) {
            answer1.setBackground(Color.green);
            title.setText("Rätt svar! Du får 1 poäng.");
            basePanel.add(nextQuestion);
        } else if (e.getSource() == answer1 && !answerList.correctAnswers.contains(answer1.getText())) {
            answer1.setBackground(Color.red);
            title.setText("Fel svar!");
            basePanel.add(nextQuestion);
        }
         else if (e.getSource() == answer2 && answerList.correctAnswers.contains(answer2.getText())) {
            answer2.setBackground(Color.green);
            title.setText("Rätt svar! Du får 1 poäng.");
            basePanel.add(nextQuestion);
        } else if (e.getSource() == answer2 && !answerList.correctAnswers.contains(answer2.getText())) {
            answer2.setBackground(Color.red);
            title.setText("Fel svar!");
            basePanel.add(nextQuestion);
        }
       else if (e.getSource() == answer3 && answerList.correctAnswers.contains(answer3.getText())) {
            answer3.setBackground(Color.green);
            title.setText("Rätt svar! Du får 1 poäng.");
            basePanel.add(nextQuestion);
        } else if (e.getSource() == answer3 && !answerList.correctAnswers.contains(answer3.getText())) {
            answer3.setBackground(Color.red);
            title.setText("Fel svar!");
            basePanel.add(nextQuestion);
        }
       else if (e.getSource() == answer4 && answerList.correctAnswers.contains(answer4.getText())) {
            answer4.setBackground(Color.green);
            title.setText("Rätt svar! Du får 1 poäng.");
            basePanel.add(nextQuestion);
        } else if (e.getSource() == answer4 && !answerList.correctAnswers.contains(answer4.getText())) {
            answer4.setBackground(Color.red);
            title.setText("Fel svar!");
            basePanel.add(nextQuestion);
        }

       if(e.getSource() == nextQuestion) {
           try {
               answerQuestion();
           } catch (IOException ex) {
               throw new RuntimeException(ex);
           }
       }
    }
}