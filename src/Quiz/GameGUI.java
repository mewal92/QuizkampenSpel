package Quiz;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class GameGUI extends JFrame {
    JFrame frame;
    JPanel basePanel;
    BufferedImage backgroundImage;
    JLabel title;
    JLabel title2;
    JTextField nameField;
    JLabel chooseCategory;
    JButton category1;
    JButton category2;
    JButton category3;
    JButton category4;
    JButton answer1;
    JButton answer2;
    JButton answer3;
    JButton answer4;
    JButton play;
    public GameGUI(){}

    public void setStartScreenGUI() throws IOException{
        frame = new JFrame("Quizkampen");
        backgroundImage = ImageIO.read(new File("background.jpg"));
        title = new JLabel("Välkommen till Quizkampen");
        title2 = new JLabel("Skriv ditt namn för att börja spela: ");
        nameField = new JTextField("", 10);
        basePanel = new JPanel();
        play = new JButton("Starta spel");

        frame.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
        frame.setLayout(new FlowLayout());
        frame.add(basePanel);
        frame.setSize(800, 530);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        basePanel.setLayout(new GridLayout(4, 1));
        basePanel.setBorder(new EmptyBorder(100, 80, 20, 20));
        basePanel.add(title);
        basePanel.add(title2);
        basePanel.add(nameField);
        basePanel.setBackground(new Color(0, 0, 0, 0));

        title.setFont(new Font("Tahoma", Font.PLAIN, 25 ));

    }

    public void setChooseCategoryGui(){
        title.setText("Välj kategori");
        basePanel.setLayout(new GridLayout(4,1));
      category1 = new JButton("Film");
      category1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.lightGray, Color.darkGray, Color.black, Color.white  ));
        category2 = new JButton("Musik");
        category2.setBorder(new BevelBorder(BevelBorder.RAISED, Color.lightGray, Color.darkGray, Color.black, Color.white  ));
        category3 = new JButton("Java-kunskap");
        category3.setBorder(new BevelBorder(BevelBorder.RAISED, Color.lightGray, Color.darkGray, Color.black, Color.white  ));
       category4 = new JButton("Övrigt");
        category4.setBorder(new BevelBorder(BevelBorder.RAISED, Color.lightGray, Color.darkGray, Color.black, Color.white  ));
        basePanel.add(category1);
        basePanel.add(category2);
        basePanel.add(category3);
        basePanel.add(category4);
        frame.repaint();
        frame.revalidate();
    }

    public void setQuestionScreenGUI() {
        basePanel.remove(category1);
        basePanel.remove(category2);
        basePanel.remove(category3);
        basePanel.remove(category4);

        basePanel.setLayout(new GridLayout(4,1));
        answer1 = new JButton("Svarsalternativ 1");
       answer2 = new JButton("Svarsalternativ 2");
        answer3 = new JButton("Svarsalternativ 3");
        answer4 = new JButton("Svarsalternativ 4");
        basePanel.add(answer1);
        basePanel.add(answer2);
        basePanel.add(answer3);
        basePanel.add(answer4);
        frame.repaint();
        frame.revalidate();
    }
    public void setWaitScreenGUI(){
        basePanel.remove(title2);
        basePanel.remove(nameField);
        basePanel.remove(play);
        title.setText("Väntar på den andra spelaren...");
        frame.revalidate();
        frame.repaint();
    }


    public void setSummaryGUI(){
        title.setText("Här kommer poängen för ronden stå:");
        basePanel.add(title);
        frame.revalidate();
        frame.repaint();
    }

   public void setEndScreenGUI() {
        title.setText("END SCREEN - här visas vinnare samt resultat");
        basePanel.add(title);
        revalidate();
        repaint();
   }
    public static void main(String[] args) throws IOException{
       GameGUI gg = new GameGUI();
    }
}
