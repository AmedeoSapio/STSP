package it.polito.ts;

import java.util.ArrayList;
import java.util.Random;

import org.coinor.opents.*;

/**
 * 
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 *
 */
public class TspMoveManager implements MoveManager
{    
	private static final long serialVersionUID = 1L;
	private TabuSearch tabuSearch=null; 

	
	public void setTabuSearch(TabuSearch tabuSearch) {
		this.tabuSearch = tabuSearch;
	}

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
				if(
						tspSolution.norm(arrayRepresentation[i],arrayRepresentation[(i + 1) % length]) + 
						tspSolution.norm(arrayRepresentation[j],arrayRepresentation[(j + 1) % length])  
						> 
						tspSolution.norm(arrayRepresentation[i],arrayRepresentation[j]) + 
						tspSolution.norm(arrayRepresentation[(i + 1) % length],arrayRepresentation[(j + 1) % length])
						){
					moves.add(new TspUntangleMove(i, j));
				}
			}
		}
		
		if(moves.size()!=0)
			moves.trimToSize();
		else{
			// moves.size()==0 means no further improvement can be made.
			// This is the last iteration of tabu search and runs with ...
			for (int k=0; k<movesNumber; k++){
				//TODO
				int i=new Random().nextInt(length);
				int j=new Random().nextInt(length);
				if (i==j)
					j=(j+2)%length;
				moves.add(new TspUntangleMove(i, j));
				moves.trimToSize();
			}
			
			// set iterationsToGo to 0
			if (tabuSearch!=null){
				tabuSearch.setIterationsToGo(0);				
			}
		}
		
        return moves.toArray(new Move[moves.size()]);       
    }// end getAllMoves
}// end class MyMoveManager
