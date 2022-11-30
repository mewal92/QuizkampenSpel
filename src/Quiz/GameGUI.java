package Quiz;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class GameGUI extends JFrame {
    JFrame frame = new JFrame("Quizkampen");
    BufferedImage backgroundImage = ImageIO.read(new File("src/Images/background.jpg"));
    JPanel topHalf = new JPanel();
    JPanel bottomHalf = new JPanel();
    //JLabel title = new JLabel("Välkommen till Quizkampen");
    JTextArea title = new JTextArea("Välkommen till Quizkampen");
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
    JButton vidare = new JButton("Nästa rond ->");

    public GameGUI() throws IOException {
        frame.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
        frame.setLayout(new BorderLayout());
        frame.add(topHalf, BorderLayout.NORTH);
        frame.add(bottomHalf, BorderLayout.SOUTH);
        topHalf.setLayout(new GridLayout(3,1));
        bottomHalf.setLayout(new GridLayout(2,2, 10, 10));
        topHalf.add(title);
        topHalf.add(title2);
        topHalf.add(nameField);
        scorePlayer1.setFont(new Font("Tahoma", Font.PLAIN, 23));
        scorePlayer2.setFont(new Font("Tahoma", Font.PLAIN, 23));
        frame.setSize(800, 530);
        topHalf.setSize(800,265);
        bottomHalf.setSize(800,265);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        topHalf.setBackground(new Color(0, 0, 0, 0));
        bottomHalf.setBackground(new Color(0, 0, 0, 0));
        bottomHalf.setBorder(new EmptyBorder(0, 150, 100, 150));
        setStartScreenGUI();
    }

    public void setStartScreenGUI(){
        topHalf.add(title);
        topHalf.add(title2);
        topHalf.add(nameField);


        topHalf.setBorder(new EmptyBorder(100, 150, 0, 150));
        topHalf.setBackground(new Color(0, 0, 0, 0));
        title.setFont(new Font("Tahoma", Font.PLAIN, 30));
        title.setEditable(false);
        title.setLineWrap(true);
        title.setWrapStyleWord(true);
        title.setOpaque(false);
        title.setBackground(new Color(0, 0, 0, 0));
        frame.repaint();
        frame.revalidate();

    }
    public void setStartScreen2(){
        topHalf.add(title2);
        bottomHalf.add(play);
        frame.revalidate();
        frame.repaint();

    }

    public void setChooseCategoryGui(){

        topHalf.repaint();
        title.setText("Välj kategori");
        bottomHalf.remove(vidare);
        bottomHalf.remove(play);
        topHalf.remove(scorePlayer1);
        topHalf.remove(scorePlayer2);
        topHalf.remove(title2);
        topHalf.remove(nameField);
        bottomHalf.add(category1);
        Dimension d = new Dimension(80, 65);
        category1.setFont(new Font("Tahoma", Font.BOLD, 18));
        category1.setPreferredSize(new Dimension(d));
        bottomHalf.add(category2);
        category2.setFont(new Font("Tahoma", Font.BOLD, 18));
        category2.setPreferredSize(new Dimension(d));
        bottomHalf.add(category3);
        category3.setFont(new Font("Tahoma", Font.BOLD, 18));
        category3.setPreferredSize(new Dimension(d));
        bottomHalf.add(category4);
        category4.setFont(new Font("Tahoma", Font.BOLD, 18));
        category4.setPreferredSize(new Dimension(d));
        bottomHalf.remove(answer1);
        bottomHalf.remove(answer2);
        bottomHalf.remove(answer3);
        bottomHalf.remove(answer4);
        title.setOpaque(true);
        title.setBackground(new Color(0, 0, 0, 0));
        frame.repaint();
        frame.revalidate();
    }

    public void setQuestionScreenGUI() {
        topHalf.repaint();
        bottomHalf.add(answer1);
        bottomHalf.add(answer2);
        bottomHalf.add(answer3);
        bottomHalf.add(answer4);
        Dimension d = new Dimension(80, 65);
        answer1.setBackground(Color.white);
        answer1.setFont(new Font("Tahoma", Font.BOLD, 16));
        answer1.setPreferredSize(new Dimension(d));
        answer2.setBackground(Color.white);
        answer2.setFont(new Font("Tahoma", Font.BOLD, 16));
        answer2.setPreferredSize(new Dimension(d));
        answer3.setBackground(Color.white);
        answer3.setFont(new Font("Tahoma", Font.BOLD, 16));
        answer3.setPreferredSize(new Dimension(d));
        answer4.setBackground(Color.white);
        answer4.setFont(new Font("Tahoma", Font.BOLD, 16));
        answer4.setPreferredSize(new Dimension(d));

        bottomHalf.remove(category1);
        bottomHalf.remove(category2);
        bottomHalf.remove(category3);
        bottomHalf.remove(category4);
        bottomHalf.remove(vidare);
        frame.repaint();
        frame.revalidate();
    }
    public void setWaitScreenGUI(){
        topHalf.repaint();
        title.setText("Väntar på din tur..");
        bottomHalf.remove(vidare);
        topHalf.remove(title2);
        topHalf.remove(nameField);
        topHalf.remove(scorePlayer1);
        topHalf.remove(scorePlayer2);
        bottomHalf.remove(play);
        bottomHalf.remove(answer1);
        bottomHalf.remove(answer2);
        bottomHalf.remove(answer3);
        bottomHalf.remove(answer4);
        bottomHalf.remove(category1);
        bottomHalf.remove(category2);
        bottomHalf.remove(category3);
        bottomHalf.remove(category4);
        frame.repaint();
        frame.revalidate();
    }


    public void setSummaryGUI(){
        bottomHalf.remove(answer1);
        bottomHalf.remove(answer2);
        bottomHalf.remove(answer3);
        bottomHalf.remove(answer4);
        Dimension d = new Dimension(60, 45);
        title.setText("Ronden är slut!");
        bottomHalf.add(vidare);
        vidare.setFont(new Font("Tahoma", Font.PLAIN, 20));
        vidare.setPreferredSize(new Dimension(d));
        vidare.setBackground(Color.green.brighter());
        vidare.setBorder(new BevelBorder(1));
        topHalf.add(scorePlayer1);
        topHalf.add(scorePlayer2);
        frame.repaint();
        frame.revalidate();
    }

    public void setEndScreenGUI() {
        bottomHalf.removeAll();
        topHalf.removeAll();
        topHalf.setLayout(new GridLayout(5,1));
        title.setText("Spelet är slut! Resultat:");
        topHalf.add(title);
        topHalf.add(scorePlayer1);
        topHalf.add(scorePlayer2);
        topHalf.add(title2);
        title2.setFont(new Font("Tahoma", Font.BOLD, 30));
        frame.repaint();
        frame.revalidate();
    }
}
