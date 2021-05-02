package alphabeta;

import java.util.LinkedList;

public interface State {

	
	public boolean isTerminal( );
	public boolean isCutOff( );
	
	LinkedList<State> next( );
	
	public int utility( );
	public int eval( );
	
	public Player getTurn( );
	
	
	int getDepth( ); 
	boolean isMaxState( );
	
}
