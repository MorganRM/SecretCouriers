/*

General Error value is -1 and false;

*/


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class Board{
	public Board( int x, int y ){
		Xmargin = x;
		Ymargin = y;
		int i =0;
		for( i=0; i<256; i++){
			Squares[i] = -1;
			HiLights[i] = -1;
		}
		for(i=0; i<16; i++){
			Pieces[i]=i;
			Squares[i]=i;
			Pieces[i+16] = i+ 16*15;
			Squares[i+240] = i +16;
		}
	}

public void nextPlayer(){
	playerTurn= (playerTurn +1)%2;
}

public void setPlayer(int p){
	if( Squares[p] > 16 )
		playerTurn =1;
	else
		playerTurn =0;
}
public boolean isSafetyArea( int p ){
	if ( p>= 7*16+3 && p <= 7*16+6 )
		return true;
	if ( p>= 7*16+9 && p <= 7*16+12 )
		return true;
	if ( p>= 8*16+3 && p <= 8*16+6 )
		return true;
	if ( p>= 8*16+9 && p <= 8*16+12 )
		return true;
return false;
}

private boolean addHiLight(int p ){
	/* cannot high light the space if it is already highlighted */
	if( HiLights[p]  == HILIGHT )
		return true;
	/* cannot high light your own pieces */
	if( myPieces(p) || Pieces[SelectedPieces] == p )
		return false;

	/* check if the target position is next to two or more pieces. */

	int x = p % 16;
	if( x <= 13 && !noPiecesIn(p+1) && !noPiecesIn(p+2) &&
		Pieces[SelectedPieces] != p+1 && Pieces[SelectedPieces] != p+2 )
		return false;
	if( x >=2 && !noPiecesIn(p-1) && !noPiecesIn(p-2) &&
		Pieces[SelectedPieces] != p-1 && Pieces[SelectedPieces] != p-2 )
		return false;
	if( x >=1 && x <=14 && !noPiecesIn(p+1) && !noPiecesIn(p-1) && 
		Pieces[SelectedPieces] != p+1 && Pieces[SelectedPieces] != p-1 )
		return false;
	if( x >=1 && !noPiecesIn(p -1) && !noPiecesIn(p ) &&
		Pieces[SelectedPieces] != p-1 && Pieces[SelectedPieces] != p)
		return false;
	if( x <=14 && !noPiecesIn(p+1) && !noPiecesIn(p) &&
		Pieces[SelectedPieces] != p+1 && Pieces[SelectedPieces] != p)
		return false;

	/* check if the target position is taken and in safety area */
	if( !noPiecesIn(p) && isSafetyArea(p) )
		return false;
	if( !noPiecesIn(p) ){
		if( playerTurn ==0 && p/16 == 15 )
			return false;
		if( playerTurn ==1 && p/16 == 0 )
			return false;
	}

	HiLights[p] = HILIGHT;
	return true;
}

private void clearHiLight(){
	for(int i=0; i<256; i++)
		HiLights[i] = -1;
}

private boolean isHiLighted( int p ){
	return ( HiLights[p] == HILIGHT );
}

public void setDices(int d1, int d2){
	if( d1 > d2 ){
		Dices[0] = d1;
		Dices[1] = d2;
	}
	else{
		Dices[0] = d2;
 		Dices[1] = d1;
	}
}

public boolean myPieces( int p ){
//if( p>= 0 && p < 256 )
	return ( Squares[p] >= playerTurn*16 && Squares[p] < 16 + playerTurn*16 );
//return false;
}

public boolean noPiecesIn( int p ){
//	return ( p>=0 && p<256 && Squares[p] == -1 );
	return ( Squares[p] == -1 );
}

public int getPieceID(int p){
	if( p<0 || p>=256|| Squares[p] == -1 )
		return -1;

	if( Squares[p] %16 < 8 )
		return DECOY;
	else if(Squares[p] %16 <12 )
		return CARRIER;
	return GUARD;
}

public void getAllMoves(int pos, int speed, int UpDown, int ID ){
/*
pos: position;
speed:  1-6
UpDown:  UP= -16; DOWN = 16;
ID:   Decoy, Carrier, Guard;
*/
if( pos >=0 && pos <256 ){
	int x = pos % 16;
	int y = pos / 16;
	boolean firstLevel = ((UpDown == UP && y >= 1 ) || (UpDown == DOWN && y <= 14 ) );
	boolean secondLevel = ((UpDown == UP && y >= 2 ) || (UpDown == DOWN && y <= 13 ) );

	//base case and take other pieces.
	if( speed == 1){
		if( x != 0 ){//left and left diagonal
			if( ID == GUARD && !myPieces( pos -1 ) )
				addHiLight( pos -1 );
			if( firstLevel && !myPieces( pos -1 + UpDown )  )
				addHiLight( pos -1 + UpDown );
		}
		if( x >=2 ){//left and left diagonal JUMP
			if( !noPiecesIn( pos -1 ) && !myPieces(pos -2 ) )
				addHiLight( pos - 2);
			if( secondLevel && !noPiecesIn( pos -1 + UpDown ) && !myPieces( pos -2 +UpDown*2) )
				addHiLight( pos -2 + UpDown*2 );
		}
		if( x != 15 ){//right and right diagonal
			if( ID == GUARD && !myPieces( pos +1 ) )
				addHiLight( pos +1 );
			if( firstLevel && !myPieces( pos +1 + UpDown )  )
				addHiLight( pos +1 + UpDown );
		}
		if( x <= 13 ){// right and right diagonal JUMP
			if( !noPiecesIn( pos +1 ) && !myPieces(pos +2 ) )
				addHiLight( pos + 2);
			if( secondLevel && !noPiecesIn( pos +1 + UpDown ) && !myPieces( pos +2 + UpDown*2) )
				addHiLight( pos +2 + UpDown*2 );
		}
		if ( firstLevel ){//UP and Down
			if( ID == GUARD && !myPieces(pos + UpDown) )
				addHiLight(pos + UpDown );
			if( secondLevel && !noPiecesIn(pos + UpDown) && !myPieces(pos + UpDown*2) )
				addHiLight(pos +UpDown*2 );
		}
	}//end base case
	else if (speed > 1 ){//recursive case 
		if( x != 0){//left and left diagonal move
			if( ID == GUARD && noPiecesIn( pos -1 ) )
				getAllMoves(pos -1, speed-1, UpDown, ID );
			if( firstLevel && noPiecesIn( pos -1 + UpDown ) )
				getAllMoves(pos-1+UpDown, speed-1, UpDown, ID );
		}
		if( x >=2 ){//left and left diagonal JUMP
			if( !noPiecesIn(pos -1) && (noPiecesIn( pos -2 ) || pos -2 == Pieces[SelectedPieces]) )
				getAllMoves(pos -2, speed-1, UpDown, ID);
			if( secondLevel && !noPiecesIn(pos -1 + UpDown) && noPiecesIn(pos -2 + UpDown*2 ) )
				getAllMoves(pos-2+UpDown*2, speed-1, UpDown, ID);
		}
		if( x != 15){//right and right diagonal move
			if( ID == GUARD && noPiecesIn( pos +1 ) )
				getAllMoves(pos +1, speed-1, UpDown, ID );
			if( firstLevel && noPiecesIn( pos +1 + UpDown ) )
				getAllMoves(pos+1+UpDown, speed-1, UpDown, ID );
		}
		if( x <=13 ){//left and left diagonal JUMP
			if( !noPiecesIn(pos +1) && ( noPiecesIn( pos +2 ) || pos+2 == Pieces[SelectedPieces]) )
				getAllMoves(pos +2, speed-1, UpDown, ID);
			if( secondLevel && !noPiecesIn(pos +1 + UpDown) && noPiecesIn(pos +2 + UpDown*2 ) )
				getAllMoves(pos+2+UpDown*2, speed-1, UpDown, ID);
		}
		if( firstLevel ){
			if( ID ==GUARD && noPiecesIn( pos + UpDown) )
				getAllMoves(pos+UpDown, speed-1, UpDown, ID );
			if( secondLevel && !noPiecesIn(pos+ UpDown) && noPiecesIn(pos +UpDown*2 ) )
				getAllMoves(pos+UpDown*2, speed-1, UpDown, ID);
		}
	}
}
}

public void Click(int x, int y){
int i =0; 
x = (x - Xmargin) /imgWidth;
y = (y - Ymargin) /imgWidth;
int position = x+ y*16;

if(  SelectedPieces != -1 && Pieces[SelectedPieces] == position ){
	SelectedPieces = -1;
	clearHiLight();
}
else {
	while( SelectedPieces == -1 && i<32){
		if(Pieces[i]== position ){
			SelectedPieces = i;
			setPlayer( position );
			if( playerTurn == 1){
				getAllMoves( position, Dices[0], UP, getPieceID(position) );
				getAllMoves( position, Dices[1], DOWN, getPieceID(position) );
			}
			else {
				getAllMoves( position, Dices[1], UP, getPieceID(position) );
				getAllMoves( position, Dices[0], DOWN, getPieceID(position) );
			}
		}
		i++;
	}
}
//move the selectedPieces to the position
if( isHiLighted(position) == true && SelectedPieces != -1 ){
	clearHiLight();
	if( !noPiecesIn(position) && !myPieces(position) )
		Pieces[Squares[position]] = -1;
	Squares[Pieces[SelectedPieces]] = -1;
	Pieces[SelectedPieces] = position;
	Squares[position] = SelectedPieces;
	SelectedPieces = -1;
}
}

public void paintRY_row(int begin, int end, int colorIndex, Graphics g ){ 
	int i=0;
	int colorTurn= colorIndex;
	for( i= begin; i<end; i++){
		if ( colorTurn %2== 0)
			g.setColor(Color.red);
		else
			g.setColor(Color.yellow);
		g.fillRect( (i%16)*imgWidth+Xmargin, (i/16) * imgWidth+Ymargin, imgWidth, imgWidth);
		colorTurn++;					
	}
}

//Draw the 16*16 board;
public void drawBoard(Graphics g){
	g.setColor( Color.white);
	g.fillRect( Xmargin, Ymargin, 16 * imgWidth, 16 * imgWidth );
	g.setColor( Color.black );
	for(int i=0; i< 17 * 16; i+=2)
		if( i % 17 != 16 )
			g.fillRect( (i%17)*imgWidth + Xmargin, (i/(17)) * imgWidth+ Ymargin, imgWidth, imgWidth);
	paintRY_row( 7*16+3, 7*16+7, 0, g);
	paintRY_row( 7*16+9, 7*16+13, 0, g);
	paintRY_row( 8*16+3, 8*16+7, 1, g);
	paintRY_row( 8*16+9, 8*16+13, 1, g);
}

//variables
	public int playerTurn = 1;
	public int imgWidth = 20;
	public int Xmargin = 0;
	public int Ymargin =0;

	public int[] Pieces= new int[32];
	public int[] HiLights = new int[256];
	public int[] Squares = new int[256];

	public int SelectedPieces =-1;
	public int[] Dices =new int[2];

//Constants
	//for Players' pieces
	private final int DECOY = 0;
	private final int CARRIER =1;
	private final int GUARD = 2;
	//for getallmoves
	public final int UP = -16;
	public final int DOWN = 16;
	//for addHiLights
	public final int HILIGHT = 879;
}

class Player{
	public Player( Board b_) {
		b = b_;
	}
	public Board b;
}


class Swing1Panel extends JPanel
   implements MouseMotionListener
{  public Swing1Panel()
   {  addMouseListener(new MouseAdapter() 
	/* MOUSE CLICK */
	{  public void mouseClicked(MouseEvent evt) 
		{
		int X = evt.getX();
		int Y = evt.getY();
		//the user has click the board;
		if( X >b.Xmargin && X <b.Xmargin+b.imgWidth*16 &&
			Y >b.Ymargin && Y < b.Ymargin+b.imgWidth *16){
			b.Click( X, Y);
			repaint();
		}
		else if( X >0 && X<100 && Y> 0 && Y<100 ){
			b.setDices( RandomInt(6) + 1, RandomInt(6) + 1 );
			repaint();
		}

		}
	});
	addMouseMotionListener(this);
	/*END MOUSE CLICK */

//loads all the pictures
	MediaTracker Mt = new MediaTracker( this );
	for(int i =0; i<4; i++){
		GamePics[i]= Toolkit.getDefaultToolkit().getImage("pic"+(i+1)+".gif");
		Mt.addImage( GamePics[i], i);
	}
	HiLight= Toolkit.getDefaultToolkit().getImage("hilight.gif");
	Mt.addImage(HiLight, 4 );
	try { Mt.waitForAll(); }
	catch( InterruptedException e ){}

   }

   public void mouseDragged(MouseEvent evt){}
   public void mouseMoved(MouseEvent evt){}

public void paintComponent( Graphics g ){
	super.paintComponent( g );
	b.drawBoard( g );
	int i =0;
	/* Draw the Pieces */
	for( i=0; i<32; i++){
		if( b.Pieces[i] != -1 ){
			int pic=0;
			if ( b.Squares[b.Pieces[i]] %16 >= 12 )
				pic++;
			if( b.Squares[b.Pieces[i]] >= 16 )
				pic+=2;
			g.drawImage( GamePics[pic], (b.Pieces[i] %16)*b.imgWidth + b.Xmargin, ( b.Pieces[i]/16)*b.imgWidth + b.Ymargin, this);
		}
	}
	/* Hight Light the Possible Moves */
	for( i=0; i<256; i++)
		if( b.HiLights[i] == b.HILIGHT )
			g.drawImage(HiLight, (i %16) *b.imgWidth + b.Xmargin, (i/16)* b.imgWidth+b.Ymargin, this );
	g.drawString("" + b.Dices[0] + " "+ b.Dices[1], 50, 50);
	g.drawRect(0, 0, 100, 100);
	for(i =0; i<16; i++)
		g.drawString( "" + player.b.Pieces[i] + " "+ player.b.Pieces[i+16], 30, 100+10*i );
	g.drawString(" sp= "+ b.SelectedPieces, 30, 260) ;
}


public int RandomInt( int n){
	return (int)Math.floor(Math.random() *n);
}

private Board b = new Board(100, 10);
private Image[] GamePics = new Image[4];
private Image HiLight;

private Player player= new Player(b);

}

class Swing1Frame extends JFrame
{  public Swing1Frame()
   {  setTitle("Swing1");
      setSize(16*20+20+90, 16*20+20 + 30);
      addWindowListener(new WindowAdapter()
         {  public void windowClosing(WindowEvent e)
            {  System.exit(0);
            }
         } );

      Container contentPane = getContentPane();
      contentPane.add(new Swing1Panel());
   }
}

public class Swing1
{  public static void main(String[] args)
   {  JFrame frame = new Swing1Frame();
      frame.show();  
   }
}











