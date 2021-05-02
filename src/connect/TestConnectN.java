package connect;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestConnectN {

	@Test
	public void testBoardSize() {

		ConnectN four = new ConnectN(4);
		assertEquals(6, four.board.rows);
		assertEquals(7, four.board.cols);

		ConnectN six = new ConnectN(6);
		assertEquals(10, six.board.rows);
		assertEquals(11, six.board.cols);
	}

	@Test
	public void testEmptyBoard() {

		ConnectN four = new ConnectN(4);

		for (int r = 0; r < four.board.rows; r++)
			for (int c = 0; c < four.board.cols; c++)
				assertEquals(Piece.BLANK, four.board.getGrid(c, r));
	}

	@Test
	public void testMakeMove() {

		ConnectN empty = new ConnectN(4);

		ConnectN first = empty.playColumn(3);
		assertEquals(Piece.BLACK, first.board.getGrid(3, 0));

		ConnectN second = first.playColumn(3);
		assertEquals(Piece.BLACK, first.board.getGrid(3, 0));
		assertEquals(Piece.RED, second.board.getGrid(3, 1));

	}

	@Test
	public void testWin() {

		ConnectN empty = new ConnectN(4);
		ConnectN win = empty.playColumn(3).playColumn(3).playColumn(2).playColumn(2).playColumn(1).playColumn(1)
				.playColumn(0);

		assertTrue(win.isWin(Piece.BLACK));
		assertTrue(win.isLoss(Piece.RED));
		
	}

	@Test
	public void testDiagDL( ) {
		ConnectN empty = new ConnectN(4);
		int moves[] = { 
				0,1, 1,2, 3,2, 2,3, 4, 3, 3
		};
		
		ConnectN curr = empty;
		for (int i = 0; i < moves.length; i++) {
			curr = curr.playColumn(moves[i]);
		}
		
		assertTrue(curr.isWin(Piece.BLACK));
		assertTrue(curr.isLoss(Piece.RED));
	}
	
	@Test
	public void testDiagUL( ) {
		ConnectN empty = new ConnectN(4);
		int moves[] = { 
				3,2,  2,1, 0,1,  1,0,  0,2,  0
		};
		
		ConnectN curr = empty;
		for (int i = 0; i < moves.length; i++) {
			curr = curr.playColumn(moves[i]);
		}
		
		assertTrue(curr.isWin(Piece.BLACK));
		assertTrue(curr.isLoss(Piece.RED));
	}

	@Test
	public void testDraw( ) {
		ConnectN empty = new ConnectN(4);
		int moves[] = { 
			4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 5, 5, 5, 5, 5, 5, 0, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0
		};
		
		ConnectN curr = empty;
		for (int i = 0; i < moves.length; i++) {
			curr = curr.playColumn(moves[i]);
		}
		
		
		assertFalse(curr.isWin(Piece.BLACK));
		assertFalse(curr.isLoss(Piece.RED));
		assertTrue(curr.isDraw());
	}

	
	
}
