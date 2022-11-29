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
    boolean gameActive2= false;

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
            gameActive = true;

            //Låt player1 välja kategori.
            while (gameActive) {
                String position = inPlayer1.readLine();
                if (position.equals("vidarePressed")) {
                    chooseCategory();
                } else if (position.equals("startPressed")) {
                    setCategoryGui();
                } else if (position.equals("CorrectAnswer")) {
                    scorePlayer1++;
                    while (true) {
                        String position2 = inPlayer2.readLine();
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
                        String position2 = inPlayer2.readLine();
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
    public void endGame() throws IOException {

        outPlayer1.println("QUIT");
        outPlayer2.println("QUIT");
        gameActive = false;
    }
    public void setCategoryGui() throws IOException {
        answeredQuestionsThisRound=0;
        outPlayer1.println("SET CATEGORY");
        setWaitScreen();
    }

    private void progressCheck() throws IOException, InterruptedException {
            if (answeredQuestionsThisRound == questionsPerRound && roundsPlayed==rounds){
                endGame();
            }
            else if (answeredQuestionsThisRound == questionsPerRound) {
                //outPlayer1.println("SET CATEGORY");
                System.out.println("hit");
                setCategoryGui();
                System.out.println("rounds played = "+roundsPlayed);
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
            System.out.println(question.getFråga());
            System.out.println(question.getRättSvar());
            System.out.println(question.getFelSvar1());
            System.out.println(question.getFelSvar2());
            System.out.println(question.getFelSvar3());
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

    public void chooseCategory() throws IOException, InterruptedException {
        String player1Choice = inPlayer1.readLine();
        currentQuestion = readQuestion(player1Choice);
        currentCategory = player1Choice;
        sendQuestionToPlayer(currentQuestion);
        }
        public void sendQuestionToPlayer(Questions q) {
            outPlayer1.println("SET QUESTION");
            outPlayer2.println("SET QUESTION");
            //outPlayer1.println(player1Choice);
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
    private void setWaitScreen () {
        outPlayer2.println("SET WAIT");

    }
}