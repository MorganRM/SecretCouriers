

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class DicePanel extends JPanel
   implements MouseMotionListener
{
 public SecretCouriers Mom = null;
 public Image[] DicePic = new Image[6];
 public int Dices[] = new int[2];

public DicePanel(Image img[] , SecretCouriers m)
{ addMouseListener(new MouseAdapter()
 /* MOUSE CLICK */
  {  public void mouseClicked(MouseEvent evt)
   {
   // Do some checking before rolling the dices
    Mom.RollTheDices();
   }
  }
 );
 addMouseMotionListener(this);
 /*END MOUSE CLICK */

 Mom = m;
 setLayout( null );
 for( int i=0; i<6; i++)
  DicePic[i] = img[ i +5];
 Dices[0] = 0;
 Dices[1] = 0;
}
   public void mouseDragged(MouseEvent evt){}
   public void mouseMoved(MouseEvent evt){}

public void rollDices(){
 Dices[0] = (int)Math.floor(Math.random() *6) +1;
 Dices[1] = (int)Math.floor(Math.random() *6) +1;
}


public void paintComponent( Graphics g ){
 super.paintComponent( g );
 g.drawRect(0, 0, 129, 49);
 if( Dices[0] > 0 && Dices[1] > 0){
  g.drawImage( DicePic[Dices[0]-1], 20, 10, this );
  g.drawImage( DicePic[Dices[1]-1], 80, 10, this );
 }
 else
  g.drawString("Roll the Dice!", 0, 30 );
}



}
