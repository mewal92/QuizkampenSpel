package Quiz;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class GameProtocol extends JFrame {
    JFrame frame = new JFrame("Quizkampen");
    BufferedImage backgroundImage = ImageIO.read(new File("background.jpg"));
    JPanel basePanel = new JPanel();
    JLabel chooseCategory = new JLabel("Välj en kategori");
    JButton category1 = new JButton("Film");
    JButton category2 = new JButton("Musik");
    JButton category3 = new JButton("Java-kunskap");
    JButton category4 = new JButton("Övrigt");

    public GameProtocol() throws IOException {
        frame.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
        frame.add(basePanel);
        frame.setSize(800, 530);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void setChooseCategoryGui(){
        basePanel.setLayout(new GridLayout(1,4));
        basePanel.add(category1);
        basePanel.add(category2);
        basePanel.add(category3);
        basePanel.add(category4);
        basePanel.add(chooseCategory);
        revalidate();
        repaint();
    }

    public static void main(String[] args) throws IOException {
        new GameProtocol().setChooseCategoryGui();
    }
}
