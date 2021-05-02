package connect;

import static org.junit.Assert.*;

import org.junit.Test;

import alphabeta.Minimax;

public class TestEval {

	@Test
	public void test1() {
		
		ConnectN test = new ConnectN(4);
		test = test.playColumn(1).playColumn(6).playColumn(2);
		
		System.out.println("Test1");;
		System.out.println(test);
		assertEquals(60, ConnectEval.eval(test));
	}
	
	@Test
	public void test2() {
		
		ConnectN test = new ConnectN(4);
		test = test.playColumn(1).playColumn(6).playColumn(2).playColumn(6).playColumn(1);
		
		System.out.println("Test2");;
		System.out.println(test);
		assertEquals(75, ConnectEval.eval(test));
	}

	@Test
	public void test3() {
		
		ConnectN test = new ConnectN(4);
		test = test.playColumn(1).playColumn(6).playColumn(2).playColumn(6).playColumn(1).playColumn(6);
		
		System.out.println("Test3");;
		System.out.println(test);
		assertEquals(-60, ConnectEval.eval(test));
	}

	@Test
	public void test4() {
		
		ConnectN test = new ConnectN(4);
		test = test.playColumn(3).playColumn(2).
				playColumn(4).playColumn(1).
				playColumn(5);
		
		System.out.println("Test3");;
		System.out.println(test);
		
		int v = test.eval();
		
		assertEquals(-1030, v);
	}
	
	
//	@Test
//	public void test4( ) {
//		
//		
//		String board[] = {        
//				   "    R", 
//				   "    R",
//				   "    R",
//				   "   BR", 
//				   "  BBRB", 
//				   "  RBBBR" };
//		
//		ConnectN test = new ConnectN(4);
//		int num_black = 0;
//		int num_red = 0;
//		
//		for (int r = 0; r < board.length; r++) {
//			for (int c = 0; c < board[board.length - r - 1].length(); c++) {
//				if (board[board.length - r - 1].charAt(c) == ' ') continue;
//				else if (board[board.length - r - 1].charAt(c) == 'R') { 
//					test.board.setGrid(c,r,Piece.RED);
//					num_red++;
//				}
//				else if (board[board.length - r - 1].charAt(c) == 'B') { 
//					test.board.setGrid(c,r,Piece.BLACK);
//					num_black++;
//				}
//				else throw new RuntimeException("Invalid board string");
//			}
//		}
//
//		int v = test.eval();
//		System.out.println(v);
//		
//		test.MAX_DEPTH = 3;
//		Minimax mm = new Minimax();
//		ConnectN best = (ConnectN) mm.minimax_decision(test);
//		System.out.println(best);
//		
//	}
}

