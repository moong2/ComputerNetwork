import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Mail_GUI extends JPanel {
    //스킨
    JLabel la1, la2,la3,path;
    JLabel line1,line2,line3;
    JTextField tf1,tf2;
    JTextArea content;

    JButton b1, b2;
    public Mail_GUI()
    {
        Color backgroundColor1=new Color(200,200,200,10);
        Color backgroundColor2=new Color(100,100,100,40);


        setLayout(null); //직접배치
        la1=new JLabel("받는 사람",JLabel.LEFT);
        la1.setBounds(20, 2, 50, 40);
        tf1=new JTextField();
        tf1.setBounds(90, 2, 430, 40);
        tf1.setBorder(BorderFactory.createEmptyBorder());
        tf1.setBackground(backgroundColor1);
        add(la1);
        add(tf1);

        line1=new JLabel();
        line1.setBounds(20, 40, 500, 1);
        line1.setOpaque(true);
        line1.setBackground(backgroundColor2);
        add(line1);

        la2=new JLabel("제목",JLabel.LEFT);
        la2.setBounds(20, 40, 50, 40);
        tf2=new JTextField();
        tf2.setBounds(90, 40, 430, 40);
        tf2.setBorder(BorderFactory.createEmptyBorder());
        tf2.setBackground(backgroundColor1);
        add(la2);
        add(tf2);

        line2=new JLabel();
        line2.setBounds(20, 78, 500, 1);
        line2.setOpaque(true);
        line2.setBackground(backgroundColor2);
        add(line2);

        la3=new JLabel("파일 첨부",JLabel.LEFT);
        la3.setBounds(20, 80, 50, 40);
        path=new JLabel();
        path.setBounds(90, 80, 350, 40);
        path.setBorder(BorderFactory.createEmptyBorder());
        path.setBackground(backgroundColor1);
        add(la3);
        add(path);

        JButton browseButton= new JButton("찾아보기");
        browseButton.setBounds(450,85,80,35);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser=new JFileChooser();
                int returnValue=fileChooser.showOpenDialog(null);
                if (returnValue==JFileChooser.APPROVE_OPTION){
                    File selectedFile=fileChooser.getSelectedFile();
                    path.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        add(browseButton);


        line3=new JLabel();
        line3.setBounds(20, 118, 420, 1);
        line3.setOpaque(true);
        line3.setBackground(backgroundColor2);
        add(line3);

        content=new JTextArea(10,40);
        content.setBounds(20, 140, 500, 330);
        add(content);

        b1=new JButton("보내기");
        b1.setBounds(20,480,80,35);
        add(b1);

        b2=new JButton("취소");
        b2.setBounds(110,480,80,35);
        add(b2);

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }
}
