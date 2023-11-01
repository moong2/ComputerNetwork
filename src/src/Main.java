import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{
    private CardLayout card=new CardLayout();
    Mail_GUI mail=new Mail_GUI();
    public Main(){
        setLayout(card);
        add("LOGIN",mail);
        setBounds(300,100,544,560);
        setVisible(true);
    }
    public static void main(String[] args) {
        new Main();
    }
}