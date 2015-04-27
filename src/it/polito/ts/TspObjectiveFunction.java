package it.polito.ts;

import org.coinor.opents.*;

/**
 * 
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 *
 */
public class TspObjectiveFunction implements ObjectiveFunction
{   
    private static final long serialVersionUID = 1L;

	public double[] evaluate( Solution solution, Move move )
    {
		if(!(solution instanceof TspSolution))
    		throw new IllegalArgumentException("TspObjectiveFunction works on TspSolution only");
		
		TspSolution tspSolution= ((TspSolution)solution);
		
		// if move is null, return ObjectiveValue
		if( move == null){
        	
			return tspSolution.getObjectiveValue();
        }
		
		if(!(move instanceof TspUntangleMove))
    		throw new IllegalArgumentException("TspObjectiveFunction works on TspUntangleMove only");
        	
		// calculate incremental
		TspUntangleMove tspUntangleMove = (TspUntangleMove)move;
        
        // Prior objective value
        double prior = solution.getObjectiveValue()[0];
        
        int i = tspUntangleMove.getI();
        int j = tspUntangleMove.getJ();
        		
		prior -= tspSolution.norm(tspSolution.getElement(i),tspSolution.getElement(i+1));	// - (i--i+1)  
		prior -= tspSolution.norm(tspSolution.getElement(j),tspSolution.getElement(j+1));	// - (j -- j+1)
		prior += tspSolution.norm(tspSolution.getElement(i),tspSolution.getElement(j));		// + (i -- j) 
		prior += tspSolution.norm(tspSolution.getElement(i+1),tspSolution.getElement(j+1));	// + (i+1 -- j+1)
		
		return new double[]{ prior };
        
    }// end evaluate
} // end class TspObjectiveFunction
