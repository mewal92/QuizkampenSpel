package Quiz;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class QuizkampenServer extends Thread{
    Socket player1Socket;
    Socket player2Socket;
    PrintWriter outPlayer1;
    PrintWriter outPlayer2;
    BufferedReader inPlayer1;
    BufferedReader inPlayer2;

    Settings settings = new Settings();
    Questions currentQuestion;

    int currentCategory;
    boolean gameActive = false;

    public QuizkampenServer(Socket player1, Socket player2) throws IOException {
        player1Socket = player1;
        player2Socket = player2;
        try{
            outPlayer1 = new PrintWriter(player1Socket.getOutputStream(), true);
            outPlayer2 = new PrintWriter(player2Socket.getOutputStream(), true);
            inPlayer1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            inPlayer2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        try{
            String player1UserName = inPlayer1.readLine();
            String player2UserName = inPlayer2.readLine();
            outPlayer1.println("Välkommen " + player1UserName+". Du kommer att spela mot "+player2UserName+"!");
            outPlayer2.println("Välkommen " + player2UserName+". Du kommer att spela mot "+player1UserName+"!");
            gameActive = true;
            //Hämta antal omgångar och frågor
            Settings.getRounds();
            Settings.getQuestions();
            //Låt player1 välja kategori.
            while(gameActive) {
                String position = inPlayer1.readLine();
                if (position.equals("slut")) {
                    endGame();
                } else if (position.equals("vidarePressed")) {
                    chooseCategory();
                } else if (position.equals("startPressed")) {
                    setWaitScreen();
                    setCategory();
                }else if (position.equals("newRound")){
                    setCategory();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void endGame() throws IOException {

                outPlayer1.println("SLUT");
                gameActive = false;
        }
    private void setWaitScreen() {
        outPlayer2.println("SET WAIT");

    }


    public void setCategory() throws IOException {
                outPlayer1.println("SET CATEGORY");
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
                for (int i = 0; i < hopp; i++)
                    scan.nextLine();
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
                    System.out.println(f.getRättSvar());
                    if (f.getFråga().equals(question.getFråga())) {
                        System.out.println("ja");
                        Questions.addQuestionList(question);
                        return null;
                    }
                }Questions.addQuestionList(question);
                return question;

                /*for (Questions f : Questions.questionList) {
                    System.out.println(f.getRättSvar());
                    if (f.getFråga().equals(question.getFråga())) {
                        System.out.println("ja");
                        return null;
                    }
                }*/


        } catch (
                FileNotFoundException e) {
            System.out.println("ej hittad");
        }
        return null;
    }
    public int countLines(String s){
        int count = 0;
        try {

            File file = new File("src/Questions/"+s+".txt");

            Scanner sc = new Scanner(file);

            while(sc.hasNextLine()) {
                sc.nextLine();
                count++;
            }
            sc.close();
        } catch (Exception e) {
            e.getStackTrace();
        }return count;
    }
    public void shuffleAnswers(String s, String f1, String f2, String f3){
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
    }
    public void chooseCategory() throws IOException {
        String player1Choice = inPlayer1.readLine();
        currentQuestion = readQuestion(player1Choice);
           while(currentQuestion ==null) {
                currentQuestion = readQuestion(player1Choice);
            }
            outPlayer1.println("SET ALTERNATIVES");
            outPlayer1.println(player1Choice);
            outPlayer1.println(currentQuestion.getFråga());
            outPlayer1.println(currentQuestion.getRättSvar());
            shuffleAnswers(currentQuestion.getRättSvar(), currentQuestion.getFelSvar1(),
                    currentQuestion.getFelSvar2(), currentQuestion.getFelSvar3());

    }
    /*public void chooseCategory() throws IOException {
        String category = inPlayer1.readLine();
        System.out.println("sätter kategori");
        if(category.equals("Film")){
            currentCategory = 0;
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestionFromMovieCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println("SET ALTERNATIVES");
            outPlayer1.println(category);
            outPlayer1.println(randomQuestionFromMovieCategory);
            outPlayer1.println(ranNum);
        }
        if(category.equals("Musik")){
            currentCategory = 1;
            int ranNum = new Random().nextInt(0, 3);
            outPlayer1.println(category);
            String randomQuestionFromMusicCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println("SET ALTERNATIVES");
            outPlayer1.println(category);
            outPlayer1.println(randomQuestionFromMusicCategory);
            outPlayer1.println(ranNum+3);

        }
        if(category.equals("Java-kunskap")){
            currentCategory = 2;
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestionFromJavaCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println("SET ALTERNATIVES");
            outPlayer1.println(category);
            outPlayer1.println(randomQuestionFromJavaCategory);
            outPlayer1.println(ranNum+6);

        }
        if(category.equals("Övrigt")){
            currentCategory = 3;
            int ranNum = new Random().nextInt(0, 3);
            String randomQuestionFromOtherCategory = questionsList.allQuestions.get(currentCategory).get(ranNum);
            outPlayer1.println("SET ALTERNATIVES");
            outPlayer1.println(category);
            outPlayer1.println(randomQuestionFromOtherCategory);
            outPlayer1.println(ranNum+9);

        }
    }*/
}