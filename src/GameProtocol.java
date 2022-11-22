import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;


public class GameProtocol extends JFrame implements ActionListener {
    BufferedImage backgroundImage;
    JPanel basePanel = new JPanel();
    JPanel scoreboardPanel;
    JLabel question;
    JLabel chooseCategory = new JLabel("Välj en kategori");
    JButton answerButtons;



    public GameProtocol() {
        basePanel.add(chooseCategory);
        getFrame();
    }


    private void getFrame() {
        try {
            JFrame frame = new JFrame("Quizkampen");
            backgroundImage = ImageIO.read(new File("background.jpg"));
            frame.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
            frame.add(basePanel);
            frame.setLayout(new FlowLayout());
            frame.setSize(800, 530);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        } catch (Exception e) {
        }
    }


    public static void main(String[] args) {
        GameProtocol gp = new GameProtocol();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
