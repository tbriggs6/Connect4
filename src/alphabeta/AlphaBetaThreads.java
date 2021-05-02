package alphabeta;

import java.util.LinkedList;

public class AlphaBetaThreads {

	final boolean DEBUG = false;
	
	int max_depth;
	
	Thread threads[];
	int values[];
	
	public AlphaBetaThreads(int max_depth)
	{
		this.max_depth = max_depth;
	}
	
	public double alpha_beta(State S, int depth, double alpha, double beta, Player maxPlayer)
	{
		if ((depth == 0) || S.isTerminal())
			return S.eval();
		
		
		if (S.getTurn( ) == maxPlayer) {
			
			for (State child : S.next()) {
				alpha = Math.max(alpha, 
						alpha_beta(child, depth-1, alpha, beta, maxPlayer));
				
				if (alpha >= beta) {
					if (DEBUG) System.out.printf("%f >= [%f] %s",  alpha, beta, S.toString());
					return beta;
				}
			}
			return alpha;
		}
		else {
			//double v = Double.MAX_VALUE;
			for(State child : S.next()) {
				beta = Math.min(beta,  
						alpha_beta(child, depth-1, alpha, beta, maxPlayer));
				
				if (beta <= alpha) {
					if (DEBUG) System.out.printf("[%f] <= %f %s",  alpha, beta, S.toString());
					return alpha;
				}
			}
			return beta;
		}
	}

	
	public State getMove(State current, Player maxPlayer, int max_depth)
	{
		if (current.getTurn( ) == maxPlayer) 
			return getMaxMove(current,maxPlayer, max_depth);
		else
			return getMinMove(current,maxPlayer, max_depth);
	}
	
	

	void evaluateThread(int threadid, State child, Player maxPlayer, int max_depth)
	{
		Thread T = new Thread( new Runnable( ) { 
			public void run( ) {
				double value = alpha_beta(child, max_depth, -Double.MAX_VALUE, Double.MAX_VALUE, maxPlayer);
				values[threadid] = (int) value;
			}
		});
		threads[threadid]  = T;
		threads[threadid].start();
	}
	
	
	private void threadedEval(Player maxPlayer, int max_depth, LinkedList<State> children) {
		int threadid = 0;
		threads = new Thread[children.size()];
		values = new int[children.size()];
		
		for (State child : children)
		{
			evaluateThread(threadid++, child, maxPlayer, max_depth);
		}
		
		for (int i = 0; i < threadid; i++) 
		{
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public State getMaxMove(State current, Player maxPlayer, int max_depth)
	{
		LinkedList<State> children = current.next();
		
		threadedEval(maxPlayer, max_depth, children);
		
		int maxV = values[0];
		State best = children.get(0);
		
		for (int i = 1; i < children.size(); i++) {
			
			System.out.println("Max Search " + maxPlayer + " value: " + values[i] + " " + children.get(i));
			
			if ((maxV == values[i]) && (Math.random() > 0.7)) {
				maxV = values[i];
				best = children.get(i);
			}
			else if (maxV < values[i]) {
				maxV = values[i];
				best = children.get(i);
			}
		}

		return best;
	}

	
	
	public State getMinMove(State current, Player maxPlayer, int max_depth)
	{
		LinkedList<State> children = current.next();
		
		threadedEval(maxPlayer, max_depth, children);
		
		int minV = values[0];
		State best = children.get(0);
		
		for (int i = 1; i < children.size(); i++) {
			
			System.out.println("Min Search " + maxPlayer + " value: " + values[i] + " " + children.get(i));
			
			if ((minV == values[i]) && (Math.random() > 0.7)) {
				minV = values[i];
				best = children.get(i);
			}
			else if (minV > values[i]) {
				minV = values[i];
				best = children.get(i);
			}
			
		}

		return best;
	}
}
