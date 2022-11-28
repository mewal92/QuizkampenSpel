import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.System.out;


public class GameGUI extends JFrame implements ActionListener {
    JFrame frame;
    JPanel basePanel;
    BufferedImage backgroundImage;
    JLabel title;
    JLabel title2;
    JLabel score = new JLabel("Poäng: ");
    JTextField nameField;
    JButton play;
    JButton category1;
    JButton category2;
    JButton category3;
    JButton category4;
    JButton answer1;
    JButton answer2;
    JButton answer3;
    JButton answer4;
    JButton nextQuestion;

    public GameGUI() throws IOException {
    }

    public void setStartScreen() throws IOException {
        frame = new JFrame("Quizkampen");
        title = new JLabel("Välkommen till Quizkampen!");
        title2 = new JLabel("Skriv ditt namn för att börja spela: ");
        basePanel = new JPanel();
        play = new JButton("Starta spel");
        nameField = new JTextField("", 10);
        backgroundImage = ImageIO.read(new File("background.jpg"));

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

        nameField.addActionListener(this::actionPerformed);
        title.setFont(new Font("Tahoma", Font.PLAIN, 23));
        score.setFont(new Font("Tahoma", Font.PLAIN, 23));
    }


    public void setChooseCategory() {
        category1 = new JButton("Film");
        category2 = new JButton("Musik");
        category3 = new JButton("Java-kunskap");
        category4 = new JButton("Övrigt");
        basePanel.setLayout(new GridLayout(1, 4));
        title.setText("Välj kategori");
        basePanel.remove(title2);
        basePanel.remove(nameField);
        basePanel.remove(play);
        basePanel.add(score);
        basePanel.add(category1);
        basePanel.add(category2);
        basePanel.add(category3);
        basePanel.add(category4);
        category1.addActionListener(e -> out.println("Film"));
        category2.addActionListener(e -> out.println("Musik"));
        category3.addActionListener(e -> out.println("Java-kunskap"));
        category4.addActionListener(e -> out.println("Övrigt"));
        frame.repaint();
        frame.revalidate();
        revalidate();
        repaint();
    }

    public void setWaitScreen() {
        title.setText("Väntar på din tur..");
        basePanel.remove(title2);
        basePanel.remove(nameField);
        basePanel.remove(play);
        basePanel.add(score);
        frame.repaint();
        frame.revalidate();
    }

    public void setQuestion() {
        JButton answer1 = new JButton("Svarsalternativ 1");
        JButton answer2 = new JButton("Svarsalternativ 2");
        JButton answer3 = new JButton("Svarsalternativ 3");
        JButton answer4 = new JButton("Svarsalternativ 4");
        nextQuestion = new JButton("Nästa fråga");

        basePanel.remove(category1);
        basePanel.remove(category2);
        basePanel.remove(category3);
        basePanel.remove(category4);

        basePanel.add(answer1);
        basePanel.add(answer2);
        basePanel.add(answer3);
        basePanel.add(answer4);


        frame.repaint();
        frame.revalidate();
    }


    public static void main(String[] args) throws IOException {
        GameGUI gg = new GameGUI();
    }

    @Override
    public void actionPerformed(ActionEvent a) {
        if (a.getSource() == nameField) {
            out.println(nameField.getText());
            title2.setText("Väntar på en motståndare..");
            frame.repaint();
            frame.revalidate();
        }
    }
}
