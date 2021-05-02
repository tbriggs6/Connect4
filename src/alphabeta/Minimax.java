package alphabeta;

import java.util.LinkedList;

public class Minimax {

	final static boolean DEBUG = false;
	final static boolean LIMITED_DEPTH = true;
	
	public State minimax_decision(State initial)
	{
		LinkedList<State> children = initial.next();
		
		double best = -Double.MAX_VALUE;
		State bestState = null;
		
		for (State child : children)
		{
			double value = min_value(child);
			if ((value == best) && (Math.random() > 0.7)) {
				best = value;
				bestState = child;
			}
			else if (value > best) {
				best = value;
				bestState = child;
			}
		}
		
		return bestState;
	}
	
	private double min_value(State state)
	{
		if (DEBUG)
			System.out.println("MIN " + state.getDepth() + " " + state.isTerminal() + state);
		
		if (LIMITED_DEPTH) {
			if (state.isCutOff())
				return state.eval();
		}
		else {
			if (state.isTerminal())
				return state.utility();
		}
		
		double best = Double.MAX_VALUE;
		LinkedList<State> children = state.next();
		
		for (State child : children)
		{
			best = Math.min(best, max_value(child));
		}
		
		return best;
	}
	
	private double max_value(State state)
	{
		if (DEBUG)
			System.out.println("MAX " + state.getDepth() + " " + state.isTerminal() + state);
		
		if (LIMITED_DEPTH) {
			if (state.isCutOff())
				return state.eval();
		}
		else {
			if (state.isTerminal())
				return state.utility();
		}

		double best = -Double.MAX_VALUE;
		LinkedList<State> children = state.next();
		
		for (State child : children)
		{
			best = Math.max(best, min_value(child));
		}
		
		return best;
	}
}
