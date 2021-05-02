package connect;

import java.util.LinkedList;
import java.util.Scanner;

public class TerminalGame {

	public static LinkedList<Integer> playGame() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter N: ");
		int N = sc.nextInt();
		ConnectN curr = new ConnectN(N);
		LinkedList<Integer> moves = new LinkedList<Integer>();

		while (true) {

			int col;
			do {
				System.out.println("Black moves: ");
				col = sc.nextInt();

			} while ((col < 0) || (col > curr.board.cols)
					|| (curr.board.getGrid(col, curr.board.rows - 1) != Piece.BLANK));

			curr = curr.playColumn(col);
			moves.add(col);

			if (curr.isWin(Piece.BLACK)) {
				System.out.println("Black Wins!!");
				return moves;
			}
			System.out.println(curr);

			if (curr.isDraw()) {
				System.out.println("Draw!!");
				return moves;
			}

			do {
				System.out.println("Red moves: ");
				col = sc.nextInt();

			} while ((col < 0) || (col > curr.board.cols)
					|| (curr.board.getGrid(col, curr.board.rows - 1) != Piece.BLANK));

			curr = curr.playColumn(col);
			moves.add(col);

			if (curr.isWin(Piece.RED)) {
				System.out.println("Red Wins!!");
				return moves;
			}

			if (curr.isDraw()) {
				System.out.println("Draw!!");
				return moves;
			}

			System.out.println(curr);
		}
	}

	public static void main(String args[])
	{
		
		do {

			LinkedList<Integer> moves = playGame( );
			
			System.out.println(moves);
		} while(true);
		
	}

}
