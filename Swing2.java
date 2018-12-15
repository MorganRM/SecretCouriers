import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Swing2Frame extends JFrame
{  public Swing2Frame()
   {  setTitle("Swing2");
      setSize(560, 320+30 );
      addWindowListener(new WindowAdapter()
         {  public void windowClosing(WindowEvent e)
            {  System.exit(0);
            }
         } );


	Container contentPane = getContentPane();
	contentPane.add( MomPanel );
   }
public MotherPanel MomPanel= new MotherPanel();
}

public class Swing2
{  public static void main(String[] args)
   {  JFrame frame = new Swing2Frame();
      frame.show();  
   }
}
