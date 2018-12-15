import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class SwingApplet extends JApplet
{  public void init()
   {  Container contentPane = getContentPane();
      contentPane.add(new SecretCouriers());
   }
}