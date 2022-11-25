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
    public FrågeObjekt läsFrågor(String s){
        FrågeObjekt question = null;
        int längd = countLines(s);
        int hopp;
        List<Integer> redanSlumpad = new ArrayList<>();
        //int[] frågorRader = new int[längd / 5];
        //for (int i = 0; i < längd / 5; i++)
            //frågorRader[i] = i;
        try (var scan = new Scanner(new File("src\\Frågorna\\"+s+".txt"));)
        {while(true) {
            //int ranNum = new Random().nextInt(0, längd/5);
            //int line = frågorRader[ranNum];
            hopp = (new Random().nextInt(0, längd/5) * 5);

            System.out.println(hopp);
            //skipLines(scan, hopp);
            for (int i = 0; i < hopp; i++)
                scan.nextLine();
            String fråga = scan.nextLine();
            String rättSvar = scan.nextLine();
            String felSvar1 = scan.nextLine();
            String felSvar2 = scan.nextLine();
            String felSvar3 = scan.nextLine();
            question = new FrågeObjekt(fråga, rättSvar, felSvar1, felSvar2, felSvar3);
            for (FrågeObjekt f:FrågeObjekt.frågeLista)
                if (f.getFråga().equals(question.getFråga()))
                    System.out.println("ja");
            FrågeObjekt.addFrågeLista(question);

            return question;
        }

        } catch (
                FileNotFoundException e) {
            System.out.println("ej hittad");
        }
        return null;
    }


        public void run(){
            try{
                String player1UserName = inPlayer1.readLine();
                String player2UserName = inPlayer2.readLine();
                outPlayer1.println("Välkommen " + player1UserName+". Du kommer att spela mot "+player2UserName+"!");
                outPlayer2.println("Välkommen " + player2UserName+". Du kommer att spela mot "+player1UserName+"!");
                gameActive = true;
                //Hämta antal omgångar och frågor
                settings.getRounds();
                settings.getQuestions();
                //Låt player1 välja kategori.
                while(gameActive){
                    //1. player1 väljer kategori
                    setCategory();
                    chooseCategory();
                    //2. player2 visa väntskärm
                    setWaitScreen();
                    //3. player1 får slumpad fråga.
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setWaitScreen() {
            outPlayer2.println("SET WAIT");
        }

        public void setCategory() throws IOException {
            String player1Choice = inPlayer1.readLine();
            if(player1Choice.equals("startPressed")){
                outPlayer1.println("SET CATEGORY");
            }
        }
   /* public static void skipLines(Scanner s,int lineNum){
        for(int i = 0; i < lineNum;i++){
            if(s.hasNextLine())
                s.nextLine();
        }
    }*/
    public int countLines(String s){
        int count = 0;
        try {

            File file = new File("src/Frågorna/"+s+".txt");

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
        public void chooseCategory() throws IOException {
            String player1Choice = inPlayer1.readLine();
            if(player1Choice.equals("Historia")){
                //Välj questionsPerRound slumpmässigt valda frågor ur kategorin.

                FrågeObjekt question = läsFrågor("Historia");

                System.out.println(question.getFråga());
                System.out.println(question.getRättSvar());
                System.out.println(question.getFelSvar1());
                System.out.println(question.getFelSvar2());
                System.out.println(question.getFelSvar3());
                outPlayer1.println(question.getFråga());
            }
            if(player1Choice.equals("Sport")){
                //Välj questionsPerRound slumpmässigt valda frågor ur kategorin.
            }
            if(player1Choice.equals("Java-kunskap")){
                //Välj questionsPerRound slumpmässigt valda frågor ur kategorin.
            }
            if(player1Choice.equals("Övrigt")){
                //Välj questionsPerRound slumpmässigt valda frågor ur kategorin.
            }
        }

    /*
    //Hämta antal omgångar och frågor
    roundsToPlay= settings.getRounds();
    questionsPerRound= settings.getQuestions();
    gameActive = true;
        try {
        player1UserName = inPlayer1.readLine();
        player2UserName = inPlayer2.readLine();
        outPlayer1.println("Välkommen " + player1UserName+". Du kommer att spela mot "+player2UserName+"!");
        outPlayer2.println("Välkommen " + player2UserName+". Du kommer att spela mot "+player1UserName+"!");
    } catch (IOException e) {
        e.printStackTrace();
    }
    //Låt player1 välja kategori.
        while(gameActive){
        //1. player1 väljer kategori
        //2. player2 visa väntskärm
        //3. player1 får slumpad fråga.
        try {
            setCategory();
            chooseCategory();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    p
*/
    }
