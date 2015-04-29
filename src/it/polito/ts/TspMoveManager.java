package it.polito.ts;

import java.util.ArrayList;

import org.coinor.opents.*;

/**
 * 
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 *
 */
public class TspMoveManager implements MoveManager
{    
	private static final long serialVersionUID = 1L;
	
	public Move[] getAllMoves( Solution solution )
    {   
    	if(!(solution instanceof TspSolution))
    		throw new IllegalArgumentException("TspMoveManager works on TspSolution only");
    	
    	TspSolution tspSolution = (TspSolution)solution;
    	
    	Integer[] arrayRepresentation = tspSolution.getTourAsArray();
    	
    	final int length = arrayRepresentation.length;
    	
    	// maximum number of moves
    	final int movesNumber= (length-1)*(length-2)/2;
    	
    	ArrayList<Move> moves= new ArrayList<Move>(movesNumber);
    	    	
		for(int i = 0; i < length-2; i++){
			for(int j = i + 2; j < length; j++){
				
				moves.add(new TspUntangleMove(i, j));
			}
		}
				
        return moves.toArray(new Move[moves.size()]);       
    }// end getAllMoves
}// end class MyMoveManager
