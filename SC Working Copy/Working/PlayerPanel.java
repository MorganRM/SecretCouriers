import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class PlayerPanel extends JPanel
   implements ActionListener
{
	public JButton[] myButton= new JButton[3];
	public SecretCouriers Mom = null;
	public int ID = 0;

	public Color colour;
	public String name;
	public PlayerPanel(Image img[], SecretCouriers m , int PlayerID)
	{
		/* 130 * 60 */
		Mom = m;
		setLayout(null);
		ID = PlayerID;

		if( PlayerID == 0){
			colour = Color.blue;
			name = "First Player";
		}
		else{
			colour = Color.pink;
			name = "Second Player";
		}


		for( int i=0; i<3; i++){
			myButton[i] = new JButton();
			myButton[i].setIcon( new ImageIcon( img[i/2 + PlayerID*2] ) );
			myButton[i].setBounds(10+40*i, 28, 30, 30);
			add( myButton[i] );
			myButton[i].addActionListener( this );
		}
	}
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		int ButIndex=0;
		for( int i=0; i<3; i++)
			if( myButton[i] == source )
				ButIndex = i;
		Mom.Board.PlayerClick(ID, ButIndex );
	}
	public void paintComponent( Graphics g ){
		super.paintComponent( g);
		g.drawString( name, 10, 20 );
		g.setColor( colour );
		g.drawRect(0, 0, 129, 59);
	}
}

