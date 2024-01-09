import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class Application extends JFrame{
    private JButton Creation, Supp, Boutton[];
    private JPanel zone;

    public Application(){
        setSize(500,500);
        setTitle("Premiere applis");
        Creation =new JButton("CREATION");
        getContentPane().add(Creation,"North");
        Creation.addActionListener(this);
        Supp =new JButton("SUPPRESSION");
        getContentPane().add(Supp,"South");
        zone=new JPanel();
        getContentPane().add(zone);
        Boutton=new JButton[100];
    }
    public void actionPerformed(ActionEvent e){
        Object o = e.getSource();
        if(o==Creation){
            Boutton[nb]=new JButton()
        }
    }
}

class ProgA {
    public static void main(String args[]){
        Application f=new Application();
        f.setVisible(true);
    }
}