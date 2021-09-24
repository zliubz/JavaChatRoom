import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;



import java.awt.event.*;
import java.awt.*;

public class clientUI {
    PrintWriter writer;
    JTextField textField;
    JTextArea textArea;
    Socket s;
    void go(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,50);
        frame.setVisible(true);

        textField = new JTextField(20);
        textArea = new JTextArea(15,20);

        JButton button = new JButton("Send");
        buttonlistener l = new buttonlistener();
        button.addActionListener(l);

        JPanel panel= new JPanel();
        panel.add(textField);
        panel.add(button);
        frame.getContentPane().add(BorderLayout.SOUTH,panel);
        frame.getContentPane().add(BorderLayout.CENTER,textArea);

        try {
            s = new Socket("127.0.0.1",5000);
            writer = new PrintWriter(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread chats = new Thread(new seeOtherChats());
        chats.start();

    }

    class seeOtherChats implements Runnable{
        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String message = null;
                while((message = reader.readLine())!=null){
                    textArea.append(message+"\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class buttonlistener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            writer.println(textField.getText());
            writer.flush();
        }
    }
    public static void main(String[] args) {
        new clientUI().go();
    }
}
