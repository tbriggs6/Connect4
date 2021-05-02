package connect;

import java.util.LinkedList;

public class ConnectEval {

	private static int BASE = 8;
	
	
	// always eval from Black's perspective
	public static int eval(ConnectN state)
	{
		int sum = 0;
		BASE = 10; // (state.N+1);
		
		// these all evaluate for Black as positive
		sum += evalRows(state);
		sum += evalCols(state);
		sum += evalDiags(state);
		
		return sum;
	}
	
	
	private static int evalDiags(ConnectN state)
	{
		int sum = 0;
		for (int r = 0; r < state.board.rows; r++)
			for (int c = 0; c < state.board.cols; c++)
				sum += evalRange(state, r, c, 1, 1);
			
			
		
		for (int r = 0; r < state.board.rows; r++)
			for (int c = 0; c < state.board.cols; c++)
				sum += evalRange(state, r, c, 1, -1);
		
		return sum;
	}
	
	private static int evalRows(ConnectN state)
	{
		int sum = 0;
		for (int r = 0; r < state.board.rows; r++)
			sum += evalRange(state, r, 0, 0, 1);
		
		return sum;
	}
	
	private static int evalCols(ConnectN state)
	{
		int sum = 0;
		for (int c = 0; c < state.board.cols; c++)
			sum += evalRange(state, 0, c, 1, 0);
		
		return sum;
	}
	
	private static int evalRange(ConnectN S, int start_row, int start_col, int row_delta, int col_delta)
	{
		
		int col = start_col;
		int row = start_row;
		
		int sum = 0;
		
		LinkedList<Piece> sequence = new LinkedList<Piece>( );

		while ((col >= 0) && (col < S.board.cols) && 
			   (row >= 0) && (row < S.board.rows)) 
		{
			if (sequence.size() >= S.N) 
				sequence.removeFirst();
			
			sequence.add( S.board.getGrid(col, row));
			col+= col_delta;
			row += row_delta;
			
			if (sequence.size() == S.N) {
				sum += scoreSequence( sequence );
			}
		}
		
		return sum;
	}

	static int scoreSequence(LinkedList<Piece> sequence)
	{
		int num_black = 0;
		int num_red = 0;
		
		for (Piece P : sequence) {
			switch(P) {
			case BLANK: break;
			case BLACK: num_black++; break;
			case RED: num_red++; break;
			}
		}
		
		if ((num_black > 0) && (num_red > 0)) return 0;
		
		else if (num_black > 0) {
			
			return (int) Math.pow(BASE, num_black);
		}
		else if (num_red > 0) {
			return -(int) Math.pow(BASE,  num_red);
		}
		
		return 0;
	}
}
