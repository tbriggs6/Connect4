package alphabeta;

import java.util.LinkedList;

public class AlphaBeta {

	
	int max_depth;
	//char maxPlayer;
	
	public AlphaBeta(int max_depth)
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
				
				if (alpha >= beta) 
					return beta;
			}
			return alpha;
		}
		else {
			//double v = Double.MAX_VALUE;
			for(State child : S.next()) {
				beta = Math.min(beta,  
						alpha_beta(child, depth-1, alpha, beta, maxPlayer));
				
				if (beta <= alpha)
					return alpha;
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
	
	public State getMaxMove(State current, Player maxPlayer, int max_depth)
	{
		LinkedList<State> children = current.next();
		
		double maxV = -Double.MAX_VALUE;
		State best = null;
		
		for (State child : children)
		{
			double value = alpha_beta(child, max_depth, -Double.MAX_VALUE, Double.MAX_VALUE, maxPlayer);
		
			System.out.println("Search value: " + value + " " + child);
			if (value > maxV) {
				maxV = value;
				best = child;
			}
		}
		
		return best;
	}
	
	public State getMinMove(State current, Player maxPlayer, int max_depth)
	{
		LinkedList<State> children = current.next();
		
		double maxV = Double.MAX_VALUE;
		State best = null;
		
		for (State child : children)
		{
			double value = alpha_beta(child, max_depth, -Double.MAX_VALUE, Double.MAX_VALUE, maxPlayer);
		
			System.out.println("Search value: " + value + " " + child);
			if (value < maxV) {
				maxV = value;
				best = child;
			}
		}
		
		return best;
	}
}
