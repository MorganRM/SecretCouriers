/*
General Error Value is -1 and false;

*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class BoardPanel extends JPanel
   implements MouseMotionListener
{

//variables

	public SecretCouriers Mom = null;
	private Image[] GamePics = new Image[4];
	private Image HiLight;

	public int playerTurn = 1;
	public int imgWidth = 20;

	public int[] Pieces= new int[32];
	public int[] HiLights = new int[256];
	public int[] Squares = new int[256];

	public int SelectedPieces =-1;
	public int[] Dices =new int[2];

//Constants
	//for selectedPieces
	public final int noSelect= -1;

	//for Players' pieces
	private final int DECOY = 0;
	private final int CARRIER =1;
	private final int GUARD = 2;
	//for getallmoves
	public final int UP = -16;
	public final int DOWN = 16;
	//for addHiLights
	public final int HILIGHT = 879;



	/* current player   firstplayer:0  , secondplayer: 1*/
	public int PlayerID = -1;

	/* see if all 32 pieces are in places*/
	public boolean inPlace = false;

	/* if inPlace is false, then temp store the value of  0:decoy, 1:Carrier, 2:Guard*/
	public int PieceID = 0;



public BoardPanel(Image img[], SecretCouriers m)
{	addMouseListener(new MouseAdapter()
	/* MOUSE CLICK */
		{  public void mouseClicked(MouseEvent evt)
			{
				int x = evt.getX()/ imgWidth;
				int y = evt.getY()/ imgWidth;
				BoardClick( y * 16 + x );
			}
		}
	);
	addMouseMotionListener(this);
	/*END MOUSE CLICK */



	Mom = m;
	setLayout( null );
	GamePics[0]= img[0];
	GamePics[1]= img[1];
	GamePics[2]= img[2];
	GamePics[3]= img[3];
	HiLight    = img[4];

	int i =0;
	for( i=0; i<256; i++){
		Squares[i] = -1;
		HiLights[i] = -1;
	}
	for(i=0; i<16; i++){
 /*allow user to select the positions of pieces
		Pieces[i] = -1;
		Pieces[i+16]= -1;
*/
//select the position for both players.
		Pieces[i]= i;
		Squares[i]=i;
		Pieces[i+16] = i+ 16*15;
		Squares[i+240] = i +16;
	}
}
	public void mouseDragged(MouseEvent evt){}
	public void mouseMoved(MouseEvent evt){}

public int[] getAllPieces(){
	return Pieces;
}

public boolean isPiecesInPlace(){
for(int i=0; i<32; i++){
	if( Pieces[i] == -1 )
		return false;
}
return true;
}

public boolean inRange( int num ){
	return (num >=0 && num < 256 );
}

//move the Selected Piece from currentPos to target position
public void movePiece(int curPos, int target ){
	if( SelectedPieces != noSelect && inRange( curPos) && inRange(target) ){
		Squares[ curPos ] = -1;
		Pieces[ SelectedPieces] = target;
		Squares[ target ] = SelectedPieces ;
		SelectedPieces = noSelect;
	}
}

//setup:  add a piece to a given position
public boolean AddPiece(int position, int player, int pieceID){
if( pieceID== 0 && DecoyCount(player) == 8 )
	return false;
if( pieceID== 1 && CarrierCount(player) == 4)
	return false;
if( pieceID== 2 && GuardCount(player) == 4 )
	return false;

Squares[position] = firstSpace(player, pieceID );
Pieces[Squares[position]] = position;
return true;
}

public void PlayerClick(int id, int index ){
/* each playerPanel has three Button
first Button ==> Decoy
second Button ==> Carrier
third Button ==> Guard
*/
if( inPlace == false){
	if( ( index == 0 && DecoyCount( id )!= 8) ||
		( index == 1 && CarrierCount( id ) != 4 ) ||
		( index == 2 && GuardCount( id ) != 4 ) ){

		//clear high light, then high light the top or bottom row
		clearHiLight();
		addHiLight(0 + 240*id, 16+240*id );
		PieceID = index;
		PlayerID = id;
	}
	inPlace = isPiecesInPlace();
}
else{
	unSelected();
}
repaint();

}
//remove the piece
public boolean RemovePiece(int position ){
if( !inRange(position ) || Squares[position] == -1 ){
	//no pieces in here
	return false;
}

Pieces[Squares[position]] = -1;
Squares[position] = -1;
return true;

}



//setup:  return the first empty space for addPiece
private int firstSpace(int player, int pieceID ){
int begin=0;
int end =0;
switch( pieceID){
	case 0: begin = 0; end = 8; break;
	case 1: begin = 8; end =12; break;
	case 2: begin =12; end =16; break;
}
for(int i=begin; i<end; i++){
	if( Pieces[player*16+i ] == -1 )
		return (player*16+ i);
}
//this should never happen
return -1;

}

public int PieceCount(int player, int pieceID ){
int sum =0;
int begin=0;
int end =0;
switch( pieceID){
	case 0: begin = 0; end = 8; break;
	case 1: begin = 8; end =12; break;
	case 2: begin =12; end =16; break;
}
for(int i=begin; i<end ; i++){
	if( Pieces[i+ player*16] != -1)
		sum++;
}
return sum;
}

//how many Decoy in the Board
public int DecoyCount(int id ){
int sum =0;
for( int i=0; i<8; i++){
	if( Pieces[i+ id*16] != -1)
		sum++;
}
return sum;
}
public int CarrierCount(int id ){
int sum =0;
for( int i=8; i<12; i++){
	if( Pieces[i+ id*16] != -1)
		sum++;
}
return sum;
}
public int GuardCount(int id ){
int sum =0;
for( int i=12; i<16; i++){
	if( Pieces[i+ id*16] != -1)
		sum++;
}
return sum;
}


//return the selected piece
public int getSelectedPosition(){
	if( SelectedPieces != noSelect && Pieces[SelectedPieces] != -1 )
		return Pieces[ SelectedPieces ] ;
	return noSelect;
}

//set the selected piece
public void selectPosition( int p ){
	SelectedPieces = Squares[p];
}

//unselected
public void unSelected(){
	SelectedPieces = -1;
	clearHiLight();
}


public void nextPlayer(){
	playerTurn= (playerTurn +1)%2;
}

public void setSide(int p){
	if( Squares[p] >= 16 )
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

//setup:  high light given positions
public boolean addHiLight(int begin, int end ){
if( begin < end && begin >= 0 && end <= 256 ){
	for(int i=begin; i<end; i++)
		HiLights[i] = HILIGHT;
	return true;
}
return false;
}


private boolean addHiLight(int p ){
	/* cannot high light the space if it is already highlighted  OR not such space*/
	if( HiLights[p]  == HILIGHT || !inRange(p)  )
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

public void clearHiLight(){
	for(int i=0; i<256; i++)
		HiLights[i] = -1;
}

public boolean isHiLighted( int p ){
	return ( inRange(p) && HiLights[p] == HILIGHT );
}

public int[] getHiLights(){
	return HiLights;
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
	return ( inRange(p) && Squares[p] >= playerTurn*16 && Squares[p] < 16 + playerTurn*16 );
}

public boolean noPiecesIn( int p ){
	return ( inRange( p ) && Squares[p] == -1 );
}
public boolean PiecesIn( int p){
	return ( inRange( p ) && Squares[p] != -1 );
}


public int getPieceID(int p){
	if( !inRange( p ) || Squares[p] == -1 )
		return -1;

	if( Squares[p] %16 < 8 )
		return DECOY;
	else if(Squares[p] %16 <12 )
		return CARRIER;
	return GUARD;
}

//high light all possible moves.
public void HighLightAllMoves(int p){
	setSide( p );
	if( playerTurn == 1){
		getAllMoves( p, Dices[0], UP, getPieceID( p ) );
		getAllMoves( p, Dices[1], DOWN, getPieceID( p ) );
	}
	else {
		getAllMoves( p, Dices[1], UP, getPieceID( p ) );
		getAllMoves( p, Dices[0], DOWN, getPieceID( p) );
	}
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

public void paintRY_row(int begin, int end, int colorIndex, Graphics g ){
	int i=0;
	int colorTurn= colorIndex;
	for( i= begin; i<end; i++){
		if ( colorTurn %2== 0)
			g.setColor(Color.red);
		else
			g.setColor(Color.yellow);
		g.fillRect( (i%16)*imgWidth, (i/16) * imgWidth, imgWidth, imgWidth);
		colorTurn++;
	}
}

//Draw the 16*16 board;
public void drawBoard(Graphics g){
	g.setColor( Color.white);
	g.fillRect( 0, 0, 16 * imgWidth, 16 * imgWidth );
	g.setColor( Color.black );
	for(int i=0; i< 17 * 16; i+=2)
		if( i % 17 != 16 )
			g.fillRect( (i%17)*imgWidth, (i/(17)) * imgWidth , imgWidth, imgWidth);
	paintRY_row( 7*16+3, 7*16+7, 0, g);
	paintRY_row( 7*16+9, 7*16+13, 0, g);
	paintRY_row( 8*16+3, 8*16+7, 1, g);
	paintRY_row( 8*16+9, 8*16+13, 1, g);
}
public void paintComponent( Graphics g ){
	super.paintComponent( g );
	drawBoard( g );
	int i =0;
	/* Draw the Pieces */
	for( i=0; i<32; i++){
		if( Pieces[i] != -1 ){
			int pic=0;
			if ( Squares[Pieces[i]] %16 >= 12 )
				pic++;
			if( Squares[Pieces[i]] >= 16 )
				pic+=2;
			g.drawImage( GamePics[pic], (Pieces[i] %16)*imgWidth, ( Pieces[i]/16)*imgWidth , this);
		}
	}
	/* Hight Light the Possible Moves */
	for( i=0; i<256; i++)
		if( HiLights[i] == HILIGHT )
			g.drawImage(HiLight, (i %16) *imgWidth , (i/16)* imgWidth, this );
}


public void BoardClick( int p ){
if( inPlace == false){
	if( isHiLighted( p ) ){
		if( noPiecesIn( p ) == false ){
			//the space is already taken
			//replace it with current selection
			RemovePiece( p );
		}
		AddPiece(p, PlayerID, PieceID );
		clearHiLight();
		inPlace = isPiecesInPlace();
	}
	repaint();
	return ;
}
if( Dices[0] ==0 || Dices[1] == 0)
	return ;
if( p == getSelectedPosition() )
	unSelected();
else if( getSelectedPosition() == noSelect && myPieces( p ) ){
	//select this position and high light all possible move
	selectPosition( p );
	HighLightAllMoves( p );
}
if( isHiLighted( p ) && getSelectedPosition() != noSelect ){
	//if the target position is taken and it is not my pieces, remove it
	if( PiecesIn(p) && !myPieces( p )  )
		RemovePiece( p );
	//move the select piece to here.
	movePiece( getSelectedPosition(), p );
	unSelected();
	//nextplayer should be Mom's function, but now....
	nextPlayer();
	//force the next player to roll the dice
	setDices(0, 0);
}

repaint();
}



}
