package connect;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Scanner;

import alphabeta.AlphaBeta;
import alphabeta.AlphaBetaThreads;
import alphabeta.Minimax;
import alphabeta.State;

public class AIGame {

	
	public static int suggestMove(ConnectN state, Piece maxPlayer) {
		
		System.out.println("... Searching ..." + maxPlayer);
		long start = System.currentTimeMillis();
		
		
		AlphaBeta ab = new AlphaBeta(5);
		//AlphaBetaThreads ab = new AlphaBetaThreads(5);
		
		ConnectN best = null;
		ConnectN.winForPlayer = maxPlayer;
				
		int max_depth = 0;
		
		double MAX_EVALS = 5e6;
		max_depth = (int) (Math.log(MAX_EVALS) / Math.log( (state.N * 2 - 1)));
		
		
		best = (ConnectN) ab.getMove(state, maxPlayer, max_depth);
		
//		Minimax mm = new Minimax();
//		ConnectN best = (ConnectN) mm.minimax_decision(state);
		
		long end = System.currentTimeMillis();
		
		System.out.println("... Done ... " + (end-start) + " ms ");
		System.out.println(best.evalDebug());
		return best.last_move;
		
	}
	
	static ConnectN fromString(int N)
	{
//		String board[] = {        
//				   "   RR  ", 
//				   "B  RRR ", 
//				   "BR BBB ", 
//				   "BRBBBR ", 
//				   "RRBBBR " };
//		
//		
		String board[] = { 
				"  B",
				"  R",    
				"  RBB",
				"RRRBBB"
		};
		
		ConnectN test = new ConnectN(N);
		int num_black = 0;
		int num_red = 0;
		
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[board.length - r - 1].length(); c++) {
				if (board[board.length - r - 1].charAt(c) == ' ') continue;
				else if (board[board.length - r - 1].charAt(c) == 'R') { 
					test.board.setGrid(c,r,Piece.RED);
					num_red++;
				}
				else if (board[board.length - r - 1].charAt(c) == 'B') { 
					test.board.setGrid(c,r,Piece.BLACK);
					num_black++;
				}
				else throw new RuntimeException("Invalid board string");
			}
		}

		test.depth = num_red + num_black;
		if (num_red >= num_black) test.last_turn = Piece.RED;
		else test.last_turn = Piece.BLACK;
		
		return test;
	}
	
	static int getValidMove(ConnectN curr) {
	
		int col;
		Scanner sc = new Scanner(System.in);
		
		do {
			if (curr.last_turn == Piece.BLACK) {
				System.out.println("Red moves: ");
			}
			else {
				System.out.println("Black moves: ");
			}
		
			try {
			col = sc.nextInt();
			}
			catch(Throwable E)
			{
				col = -1;
			}

		} while ((col < 0) || (col > curr.board.cols)
					|| (curr.board.getGrid(col, curr.board.rows - 1) != Piece.BLANK));

		return col;
	}
	
	public static LinkedList<Integer> playGame() {
		
		boolean fromString = false;
		boolean computerIsRed = true;
		boolean computerIsBlack = true;
		
		int num_moves = 0;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter N: ");
		int N = sc.nextInt();
		
		System.out.println("Black is 0=human 1=computer: ");
		int b = sc.nextInt();
		if (b == 0) computerIsBlack = false;
		
		System.out.println("Red is 0=human 1=computer: ");
		int r = sc.nextInt();
		if (r == 0) computerIsRed = false;
		
		ConnectN curr;
		if (fromString) {
			curr = fromString(N);
			num_moves = curr.depth;
		}
		else {
			curr = new ConnectN(N);
		}
		
		LinkedList<Integer> moves = new LinkedList<Integer>();

		while (true) {

			num_moves++;
			
			// its human's turn
			if (((curr.last_turn == Piece.BLACK) && (!computerIsRed)) ||
				((curr.last_turn == Piece.RED && (!computerIsBlack)))) 
			{
				System.out.println("Your board: ");
				System.out.println(curr);
				
				int move = getValidMove(curr);
				moves.add(move);
				curr = curr.playColumn(move);
			}
			// else its computer's turn
			else {
				int move = 3;
				
				if (num_moves <= 1) move = 3;
				if (num_moves <= 2) {
					if (curr.board.getGrid(N/2, 0) == Piece.BLANK) move = N/2;
					else if (curr.board.getGrid(N/2+1, 0) == Piece.BLANK) move = N/2+1;
					else if (curr.board.getGrid(N/2-1, 0) == Piece.BLANK) move = N/2-1;
					else if (curr.board.getGrid(N/2+2, 0) == Piece.BLANK) move = N/2+2;
					else if (curr.board.getGrid(N/2-2, 0) == Piece.BLANK) move = N/2-2;
					
				}
				else {
					curr.MAX_DEPTH = (num_moves+3);
					
					move = suggestMove(curr, curr.getNextTurn());
				}
				
				moves.add(move);
				curr = curr.playColumn(move);
				
			}
			
			System.out.println(curr);
			
			if (curr.isWin(Piece.BLACK)) {
				System.out.println("Black Wins!!");
				return moves;
			}

			if (curr.isDraw()) {
				System.out.println("Draw!!");
				return moves;
			}

			if (curr.isWin(Piece.RED)) {
				System.out.println("Red Wins!!");
				return moves;
			}


		}
	}

	public static void main(String args[])
	{
		
//		do {

			LinkedList<Integer> moves = playGame( );
			
			System.out.println(moves);
//		} while(true);
		
	}

}
