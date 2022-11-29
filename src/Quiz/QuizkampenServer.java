package Quiz;

import Quiz.Questions;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class QuizkampenServer extends Thread {
    Socket player1Socket;
    Socket player2Socket;
    PrintWriter outPlayer1;
    PrintWriter outPlayer2;
    BufferedReader inPlayer1;
    BufferedReader inPlayer2;

    Settings settings = new Settings();

    int rounds = settings.getRounds();
    int questionsPerRound = settings.getQuestions();
    String currentCategory;
    int answeredQuestionsThisRound = 0;
    int roundsPlayed = 0;
    int scorePlayer1;
    int scorePlayer2;
    Questions currentQuestion;

    boolean gameActive = false;

    public QuizkampenServer(Socket player1, Socket player2) throws IOException {
        player1Socket = player1;
        player2Socket = player2;
        try {
            outPlayer1 = new PrintWriter(player1Socket.getOutputStream(), true);
            outPlayer2 = new PrintWriter(player2Socket.getOutputStream(), true);
            inPlayer1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            inPlayer2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String player1UserName = inPlayer1.readLine();
            String player2UserName = inPlayer2.readLine();
            outPlayer1.println("Välkommen " + player1UserName + ". Du kommer att spela mot " + player2UserName + "!");
            outPlayer2.println("Välkommen " + player2UserName + ". Du kommer att spela mot " + player1UserName + "!");
            outPlayer1.println(player2UserName);
            outPlayer2.println(player1UserName);
            gameActive = true;

            //Låt player1 välja kategori.
            while (gameActive) {
                String position2 = "";
                String position = inPlayer1.readLine();
                if (position.equals("vidarePressed")) {
                    System.out.println(position);
                    chooseCategory(inPlayer1);
                }
                else if (position.equals("startPressed")) {
                    while (true){
                        position2 = inPlayer2.readLine();
                        if(position2.equals("startPressed")){
                            setCategoryGui(outPlayer1);
                            setWaitScreen(outPlayer2);
                            break;
                        }
                    }
                } else if (position.equals("CorrectAnswer")) {
                    scorePlayer1++;
                    while (true){
                        position2 = inPlayer2.readLine();
                        //gui vänteskärm
                        if (position2.equals("CorrectAnswer")){
                            //if (answeredQuestionsThisRound == questionsPerRound)
                            //   roundsPlayed++;
                            scorePlayer2++;
                            progressCheck();
                            break;
                        }
                        if(position2.equals("WrongAnswer")){
                        progressCheck();
                        break;
                        }
                    }
                } else if (position.equals("WrongAnswer")) {

                    while (true) {
                        position2 = inPlayer2.readLine();
                        if (position2.equals("CorrectAnswer")){
                            //if (answeredQuestionsThisRound == questionsPerRound)
                            //   roundsPlayed++;
                            scorePlayer2++;
                            progressCheck();
                            break;
                        }
                        if(position2.equals("WrongAnswer")){
                            progressCheck();
                            break;
                        }
                    }
            }
        }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setSummary() throws IOException {
            outPlayer1.println("SET SUMMARY");
            outPlayer2.println("SET SUMMARY");
        outPlayer1.println(scorePlayer1);
        outPlayer1.println(scorePlayer2);
        outPlayer2.println(scorePlayer2);
        outPlayer2.println(scorePlayer1);
            while (true){
                String vidare1 = inPlayer1.readLine();
                if(vidare1.equals("next")){
                    String vidare2 = inPlayer2.readLine();
                    if(vidare2.equals("next")){
                        break;
                    }
                }
            }
    }
    public void setEndscreen(){
        outPlayer1.println("SET ENDSCREEN");
        outPlayer2.println("SET ENDSCREEN");
        outPlayer1.println(scorePlayer1);
        outPlayer1.println(scorePlayer2);
        outPlayer2.println(scorePlayer2);
        outPlayer2.println(scorePlayer1);
        gameActive = false;
    }

    public void setCategoryGui(PrintWriter p) throws IOException {
        answeredQuestionsThisRound=0;
        p.println("SET CATEGORY");
    }

    private void progressCheck() throws IOException, InterruptedException {
            if (answeredQuestionsThisRound == questionsPerRound && roundsPlayed==rounds){
                setEndscreen();
            }
            else if (answeredQuestionsThisRound == questionsPerRound) {
                //outPlayer1.println("SET CATEGORY");
                //här skickas summary till p1 och p2
                setSummary();
                if(roundsPlayed%2==0) {
                    setCategoryGui(outPlayer1);
                    setWaitScreen(outPlayer2);
                    System.out.println("rounds played = " + roundsPlayed);
                }else{
                    setCategoryGui(outPlayer2);
                    setWaitScreen(outPlayer1);
                    System.out.println("rounds played = " + roundsPlayed);
                }
                //Visa väntGUI för player1. Skicka choose category till player 2.
            } else {
                sendQuestionToPlayer(readQuestion(currentCategory));
        }
    }

    public Questions readQuestion(String s) {
        Questions question;
        int längd = countLines(s);
        //räknar linjer i kategorins textfil
        int hopp;
        try (var scan = new Scanner(new File("src/Questions/" + s + ".txt"));) {
            //int ranNum = new Random().nextInt(0, längd/5);
            //int line = frågorRader[ranNum];

            hopp = (new Random().nextInt(0, längd / 5) * 5);
            //hoppar 5 rader random till en fråga
            for (int i = 0; i < hopp; i++) {
                scan.nextLine();
            }
            String fråga = scan.nextLine();
            String rättSvar = scan.nextLine();
            String felSvar1 = scan.nextLine();
            String felSvar2 = scan.nextLine();
            String felSvar3 = scan.nextLine();
            question = new Questions(fråga, rättSvar, felSvar1, felSvar2, felSvar3);
            for (Questions f : Questions.questionList) {
                if (f.getFråga().equals(question.getFråga())) {
                    System.out.println("ja");
                    return readQuestion(currentCategory);
                }
            }Questions.addQuestionList(question);
            return question;


        } catch (
                FileNotFoundException e) {
            System.out.println("ej hittad");
        }
        return null;
    }

    public int countLines(String s) {
        int count = 0;
        try {

            File file = new File("src/Questions/" + s + ".txt");

            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                sc.nextLine();
                count++;
            }
            sc.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return count;
    }

    public void shuffleAnswers(String s, String f1, String f2, String f3) {
        LinkedList<String> list = new LinkedList<>();
//Add values
        list.add(s);
        list.add(f1);
        list.add(f2);
        list.add(f3);
//Random() to shuffle the given list.
        Collections.shuffle(list, new Random());
        outPlayer1.println(list.get(0));
        outPlayer1.println(list.get(1));
        outPlayer1.println(list.get(2));
        outPlayer1.println(list.get(3));
        outPlayer2.println(list.get(0));
        outPlayer2.println(list.get(1));
        outPlayer2.println(list.get(2));
        outPlayer2.println(list.get(3));
    }

    public void chooseCategory(BufferedReader b) throws IOException, InterruptedException {
        String player1Choice = b.readLine();
        System.out.println(player1Choice);
        currentQuestion = readQuestion(player1Choice);
        currentCategory = player1Choice;
        sendQuestionToPlayer(currentQuestion);
        }
        public void sendQuestionToPlayer(Questions q) {
            outPlayer1.println("SET QUESTION");
            outPlayer2.println("SET QUESTION");
            currentQuestion = q;
            outPlayer1.println(currentQuestion.getFråga());
            outPlayer1.println(currentQuestion.getRättSvar());
            outPlayer2.println(currentQuestion.getFråga());
            outPlayer2.println(currentQuestion.getRättSvar());
            shuffleAnswers(currentQuestion.getRättSvar(), currentQuestion.getFelSvar1(),
                    currentQuestion.getFelSvar2(), currentQuestion.getFelSvar3());
            answeredQuestionsThisRound++;
            if (answeredQuestionsThisRound == questionsPerRound)
                roundsPlayed++;
        }
    private void setWaitScreen (PrintWriter p) throws IOException, InterruptedException {
        p.println("SET WAIT");
        if (p==outPlayer1){
            String skräp =inPlayer2.readLine();
            String skräp2 = inPlayer2.readLine();
            chooseCategory(inPlayer2);
        }
    }
}