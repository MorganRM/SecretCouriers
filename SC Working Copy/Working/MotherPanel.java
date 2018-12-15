

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

	/* current player   firstplayer:0  , secondplayer: 1*/
	public int PlayerID = -1;

	/* see if all 32 pieces are in places*/
	public boolean inPlace = false;

	/* if inPlace is false, then temp store the value of  0:decoy, 1:Carrier, 2:Guard*/
	public int PieceID = 0;


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


public void PlayerClick(int id, int index ){
/* each playerPanel has three Button
first Button ==> Decoy
second Button ==> Carrier
third Button ==> Guard
*/
if( inPlace == false){
	if( ( index == 0 && Board.DecoyCount( id )!= 8) ||
		( index == 1 && Board.CarrierCount( id ) != 4 ) ||
		( index == 2 && Board.GuardCount( id ) != 4 ) ){

		//clear high light, then high light the top or bottom row
		Board.clearHiLight();
		Board.addHiLight(0 + 240*id, 16+240*id );
		PieceID = index;
		PlayerID = id;
	}
	inPlace = Board.isPiecesInPlace();
}
else{
	unSelected();
}
repaint();

}

public void BoardClick( int p ){
if( inPlace == false){
	if( Board.isHiLighted( p ) ){
		if( Board.noPiecesIn( p ) == false ){
			//the space is already taken
			//replace it with current selection
			Board.RemovePiece( p );
		}
		Board.AddPiece(p, PlayerID, PieceID );
		Board.clearHiLight();
		inPlace = Board.isPiecesInPlace();
	}
	repaint();
	return ;
}
if( Board.Dices[0] ==0 || Board.Dices[1] == 0)
	return ;
if( p == Board.getSelectedPosition() )
	Board.unSelected();
else if( Board.getSelectedPosition() == Board.noSelect && Board.myPieces( p ) ){
	//select this position and high light all possible move
	Board.selectPosition( p );
	Board.HighLightAllMoves( p );
}
if( Board.isHiLighted( p ) && Board.getSelectedPosition() != Board.noSelect ){
	//if the target position is taken and it is not my pieces, remove it
	if( Board.PiecesIn(p) && !Board.myPieces( p )  )
		Board.RemovePiece( p );
	//move the select piece to here.
	Board.movePiece( Board.getSelectedPosition(), p );
	Board.unSelected();
	//nextplayer should be Mom's function, but now....
	Board.nextPlayer();
	//force the next player to roll the dice
	Board.setDices(0, 0);
}

repaint();
}



public void RollTheDices(){
if( Board.Dices[0] ==0 && Board.Dices[1] ==0){
	DiceArea.rollDices();
	Board.setDices( DiceArea.Dices[0], DiceArea.Dices[1] );
	repaint();
}
}
public void unSelected(){
	Board.unSelected();
	repaint();
}

public void paintComponent( Graphics g ){
	super.paintComponent( g );
	g.drawString("HERE", xx, yy );

	g.drawString("" + Board.Dices[0] + " "+ Board.Dices[1], secondline, 40);
	int[] PPP = Board.getAllPieces();
	for(int i =0; i<16; i++)
		g.drawString("" + PPP[i] + " " + PPP[i+16] , secondline, 60+ 10*i );
//	for(int i =0; i<16; i++)
//		g.drawString( "" + Board.Pieces[i] + " "+ Board.Pieces[i+16], secondline, 60+10*i );
	g.drawString(" sp= "+ Board.SelectedPieces, secondline, 260) ;
	g.drawString("HERE", xx, yy );
}

public int RandomInt( int n){
	return (int)Math.floor(Math.random() *n);
}

}