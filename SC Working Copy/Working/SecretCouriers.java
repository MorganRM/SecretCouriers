

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class SecretCouriers extends JPanel
{
	public Image[] AllPics = new Image[11];
	public BoardPanel Board = null;
	public PlayerPanel firstPlayer = null;
	public PlayerPanel secondPlayer = null;
	public DicePanel DiceArea = null;

	public final int firstline = 130;
	public final int secondline = 450; //firstline + 320;
	public int xx =0;
	public int yy =0;

public SecretCouriers()
{
	/* loads all the pictures */
			String str[]= { "pic1.gif", "pic2.gif", "pic3.gif", "pic4.gif", "hilight.gif",
					"dice1.gif", "dice2.gif", "dice3.gif", "dice4.gif", "dice5.gif", "dice6.gif" };
			MediaTracker Mt = new MediaTracker( this );
			for(int i =0; i<11; i++){
				AllPics[i]= Toolkit.getDefaultToolkit().getImage(str[i]);
				Mt.addImage( AllPics[i], i);
			}
			try { Mt.waitForAll(); }
			catch( InterruptedException e ){}
	/* Finish loaded the pictures */

	firstPlayer = new PlayerPanel(AllPics , this, 0);
	firstPlayer.setBounds(0, 0, firstline, 60);

	secondPlayer = new PlayerPanel(AllPics , this, 1);
	secondPlayer.setBounds(0, 60, firstline, 60);

	Board = new BoardPanel( AllPics, this );
	Board.setBounds( firstline, 0, 320, 320 );

	DiceArea = new DicePanel( AllPics, this );
	DiceArea.setBounds( 0, 120, firstline, 50);


	this.setLayout(null );
	this.add(Board );
	this.add( firstPlayer );
	this.add( secondPlayer );
	this.add( DiceArea );
}
//public void mouseDragged(MouseEvent evt){}
//public void mouseMoved(MouseEvent evt){}



public void paintComponent( Graphics g ){
	super.paintComponent( g );
	//g.drawString("HERE", xx, yy );

	g.drawString("" + Board.Dices[0] + " "+ Board.Dices[1], secondline, 40);
	int[] PPP = Board.getAllPieces();
	for(int i =0; i<16; i++)
		g.drawString("" + PPP[i] + " " + PPP[i+16] , secondline, 60+ 10*i );
	g.drawString(" sp= "+ Board.SelectedPieces, secondline, 260) ;
	//g.drawString("HERE", xx, yy );
}

public void RollTheDices(){
if( Board.Dices[0] ==0 && Board.Dices[1] ==0){
	DiceArea.rollDices();
	Board.setDices( DiceArea.Dices[0], DiceArea.Dices[1] );
	repaint();
}
}

}