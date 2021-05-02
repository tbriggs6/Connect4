package connect;

import java.util.BitSet;
import java.util.LinkedList;

import alphabeta.State;
import alphabeta.*;

public class ConnectN implements State {

	int MAX_DEPTH = 10;
	static Piece winForPlayer = Piece.BLACK;
	
	class Board {

		BitSet board;
		final int rows, cols;
		
		Board() {

			rows = N * 2 - 2;
			cols = N * 2 - 1;

			int nbits = 2 * rows * cols;
			board = new BitSet(nbits);
		}

		Board(Board B) {
			this.board = (BitSet) B.board.clone();
			this.rows = B.rows;
			this.cols = B.cols;
		}

		Piece getGrid(int col, int row) {
			assert (col < cols);
			assert (row < rows);

			int index = (row * cols + col) * 2;
			int v = 0;
			if (board.get(index))
				v = 2;
			if (board.get(index + 1))
				v += 1;

			switch (v) {
			case 0:
				return Piece.BLANK;
			case 1:
				return Piece.RED;
			case 2:
				return Piece.BLACK;
			default:
				throw new RuntimeException("error");
			}
		}

		void setGrid(int col, int row, Piece P) {
			assert (col < cols);
			assert (row < rows);

			int index = (row * cols + col) * 2;
			boolean hi = (P == Piece.BLACK);
			boolean lo = (P == Piece.RED);

			if (hi)
				board.set(index);
			else
				board.clear(index);

			if (lo)
				board.set(index + 1);
			else
				board.clear(index + 1);
		}

		public String toString() {
			StringBuffer buff = new StringBuffer();

			for (int r = rows - 1; r >= 0; r--) {
				for (int c = 0; c < cols; c++) {
					Piece p = getGrid(c, r);
					switch (p) {
					case BLANK:
						buff.append(" ");
						break;
					case RED:
						buff.append("R");
						break;
					case BLACK:
						buff.append("B");
						break;
					}
				}
				buff.append("\n");
			}

			return buff.toString();
		}
	}

	
	final int N;
	Board board;
	Piece last_turn;
	int depth;
	int last_move = -1;
	
	/** Default constructor */
	public ConnectN(int N) {
		this.N = N;
		board = new Board();
		this.last_turn = Piece.RED;
		this.depth = 0;
		
	}

	/** Copy Constructor */
	public ConnectN(ConnectN source) {
		this.N = source.N;
		this.board = new Board(source.board);
		this.last_turn = source.last_turn;
		this.depth = source.depth;
	}

	@Override
	public boolean isTerminal() {

		if (isWin(Piece.BLACK)) 
			return true;
		if (isLoss(Piece.BLACK)) 
			return true;
		if (isDraw( )) 
			return true;
		
		return false;
	}

	@Override
	public boolean isCutOff() {

		if (isWin(last_turn)) return true;
		if (isLoss(last_turn)) return true;
		if (isDraw( )) return true;
		if (this.depth >= MAX_DEPTH) return true;
		
		return false;
	}
	
	@Override
	public LinkedList<State> next() {

		LinkedList<State> states = new LinkedList<State>( );
		
		for (int i = 0; i < board.cols; i++) {
			if (board.getGrid(i, board.rows - 1) == Piece.BLANK)
			{
				states.add(playColumn(i));
			}
		}
		
		return states;
	}

	@Override
	public int utility() {

		int value = 0;
		if (isWin(Piece.BLACK)) value = 5000;
		else if (isLoss(Piece.BLACK)) value = -5000;
		else value = 0;
//		else value = ConnectEval.eval(this);
//		
		if (winForPlayer == Piece.BLACK) return value;
		else return -value;
	}

	public int eval( ) {
		
		int value = 0;
		if (isWin(Piece.BLACK)) value = 5000;
		else if (isLoss(Piece.BLACK)) value = -5000;
		
		else value = ConnectEval.eval(this);
		
		if (winForPlayer == Piece.BLACK) return value;
		else return -value;
	}

	public int evalDebug( ) {
		
		int value = 0;
		if (isWin(Piece.BLACK)) value = 5000;
		else if (isLoss(Piece.BLACK)) value = -5000;
		
		else value = ConnectEval.eval(this);
		
		System.out.println("Connect Eval: " + value + " but " + getTurn() + " ==? " + winForPlayer );
		
		if (winForPlayer == Piece.BLACK) return value;
		else return -value;
	}
	
	@Override
	public int getDepth() {
		return this.depth;
	}

	
	public Piece getNextTurn( )
	{
		return this.last_turn == Piece.BLACK ? Piece.RED : Piece.BLACK;
	}
	
	public Piece getTurn( )
	{
		return getNextTurn( );
	}
	public ConnectN playColumn(int column)
	{
		int row = 0;
		while ((row < board.rows) && (board.getGrid(column, row) != Piece.BLANK)) 
			row++;
		
		if (row >= board.rows)
			throw new RuntimeException("Error - column " + column + " is already full.");
		
		ConnectN result = new ConnectN(this);
		result.board.setGrid(column, row, getNextTurn());
		result.last_turn = getNextTurn();
		result.depth = this.depth + 1;
		result.last_move = column;
		
		return result;
	}
	
	private boolean isWinColumn(Piece piece, int column)
	{
		int count = 0;
		for (int r = 0; r < board.rows; r++)
		{
			if (board.getGrid(column, r) == piece) count++;
			else count = 0;
			
			if (count == N) return true;
		}
		return false;
	}
	
	private boolean isWinRow(Piece piece, int row)
	{
		int count = 0;
		for (int c = 0; c < board.cols; c++)
		{
			if (board.getGrid(c, row) == piece) count++;
			else count = 0;
			
			if (count == N) return true;
		}
		return false;
	}
	
	/**
	 * isWinDiag - determine if there are N tiles in a diagonal
	 * starting from the given row, col position.  This  
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isWinDiag(Piece piece, int row, int col)
	{
		int count = 0;
		
		if ((row <= board.rows - N) && (col >= board.cols - N)) {
			// check up and left
			for (int i = 0; i < N; i++) {
				if (board.getGrid(row+i, col-i) != piece) 
					break;
				count++;
			}
			if (count == N) return true;
		}
		
		count = 0;
		
		if ((row > board.rows - N) && (col >= board.cols - N)) {
			// check down and left
			for (int i = 0; i < N; i++) {
				if (board.getGrid(row-i, col-i) != piece) 
					break;
				count++;
			}
			if (count == N) return true;
		}
		return false;
	}
	
	public boolean isWin(Piece P)
	{
		for (int r = 0; r < board.rows; r++)
			if (isWinRow(P, r)) return true;
		
		for (int c = 0; c < board.cols; c++)
			if (isWinColumn(P, c)) return true;
		
		for (int r = 0; r < board.rows; r++)
			for (int c = (N-1); c < board.cols; c++)
				if (isWinDiag(P, r, c)) return true;
		
		
		return false;
	}
	
	public boolean isLoss(Piece P)
	{
		return isWin(P == Piece.BLACK ? Piece.RED : Piece.BLACK);
	}
	
	public boolean isDraw( )
	{
		for (int c = 0; c < board.cols; c++) {
			if (board.getGrid(c, board.rows-1) == Piece.BLANK) return false; 
		}
		return true;
	}
	
	public String toString( )
	{
		StringBuffer buff = new StringBuffer();
		buff.append(String.format("%s %d\n", this.last_turn.toString(), this.depth));
		for (int i = 0; i < board.cols; i++) buff.append("-");
		buff.append("\n");
		
		buff.append( board.toString());
		for (int i = 0; i < board.cols; i++) buff.append("-");
		buff.append("\n");
		
		if (board.cols >= 10) {
			for (int i = 0; i < board.cols; i++) 
				if ((i % 10) == 0) buff.append(i / 10);
				else buff.append(' ');
			buff.append("\n");
		}
		for (int i = 0; i < board.cols; i++) buff.append(i % 10);
		buff.append("\n");
		
		return buff.toString();
	}

	@Override
	public boolean isMaxState() {

		return (depth % 2) == 0;
	}
}
