/**
 * @version 1.10 25 Mar 1997
 * @author Cay Horstmann
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ButtonPanel extends JPanel 
   implements ActionListener
{  public ButtonPanel()
   {	String str[] = {"Yellow", "Blue", "Red" };
	for(int i=0; i<3; i++){
		kButton[i]= new JButton(str[i] );
		add( kButton[i] );
		kButton[i].addActionListener(this);
	}
/*
      yellowButton = new JButton("Yellow");
      blueButton = new JButton("Blue");
      redButton = new JButton("Red");

      add(yellowButton);
      add(blueButton);
      add(redButton);

      yellowButton.addActionListener(this); 
      blueButton.addActionListener(this); 
      redButton.addActionListener(this); 
*/
   }

   public void actionPerformed(ActionEvent evt)
   {  Object source = evt.getSource();
      Color color = getBackground();
	if( source == kButton[0] ) color = Color.yellow;
	if( source == kButton[1] ) color = Color.blue;
	if( source == kButton[2] ) color = Color.red;
/*
      if (source == yellowButton) color = Color.yellow;
      else if (source == blueButton) color = Color.blue;
      else if (source == redButton) color = Color.red;
*/
      setBackground(color);
      repaint();
   }

	private JButton[] kButton = new JButton[3];
//   private JButton yellowButton;
//   private JButton blueButton;
//  private JButton redButton; 
}


class ButtonFrame extends JFrame
{  public ButtonFrame()
   {  setTitle("ButtonTest");
      setSize(300, 200);
      addWindowListener(new WindowAdapter()
         {  public void windowClosing(WindowEvent e)
            {  System.exit(0);
            }
         } );

      Container contentPane = getContentPane();
      contentPane.add(new ButtonPanel());
   }
}

public class ButtonTest
{  public static void main(String[] args)
   {  JFrame frame = new ButtonFrame();
      frame.show();  
   }
}
