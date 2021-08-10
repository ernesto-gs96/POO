import java.awt.BorderLayout;
/* w w  w  .j  a va  2  s  .c o  m*/
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main {
  public static void main(String[] args) {
    JFrame frame = new JFrame("JFrame");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel buttonPanel = new JPanel(new BorderLayout()); 
    JButton northButton = new JButton("North");
    JButton southButton = new JButton("South"); 
    JButton eastButton  = new JButton("East"); 
    JButton  westButton = new JButton("west");

    buttonPanel.add(northButton, BorderLayout.NORTH); 
    buttonPanel.add(southButton, BorderLayout.SOUTH); 
    buttonPanel.add(eastButton,  BorderLayout.EAST); 
    buttonPanel.add(westButton,  BorderLayout.WEST);

    frame.add(buttonPanel,  BorderLayout.CENTER);
     

    frame.pack();
    frame.setVisible(true);
  }
}