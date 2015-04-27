package it.polito.ts;

import org.coinor.opents.*;

/**
 * Moves that untangles a knot:
 * ... i - i+1 ... j - j+1 ... => ... i - j ... i+1 - j+1 ...
 * @see <a href="http://en.wikipedia.org/wiki/2-opt">2-opt algorithm</a>.  
 * 
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 *
 */
public class TspUntangleMove implements Move 
{   
	private static final long serialVersionUID = 1L;
	
	private int i;
   	private int j;
    
    
    public TspUntangleMove( int i, int j)
    {   
        this.i = i;
        this.j = j;
    }   // end constructor
    
    
    public void operateOn( Solution solution )
    {
    	if(!(solution instanceof TspSolution))
    		throw new IllegalArgumentException("TspUntangleMove works on TspSolution only");
    	
    	TspSolution tspSolution = (TspSolution)solution;
    	
    	Integer[] arrayRepresentation = tspSolution.getTourAsArray();
    	
    	final int count = arrayRepresentation.length;
    		
		Integer swap=arrayRepresentation[(i + 1) % count];
        arrayRepresentation[(i + 1) % count]= arrayRepresentation[j];
        arrayRepresentation[j]= swap;
          
        int startIndex=i + 2, stopIndex=j - 1;
          if(!(startIndex >= stopIndex || startIndex >= count || stopIndex < 0))
              
              for(; startIndex < stopIndex; stopIndex--)
              {
                  swap = arrayRepresentation[startIndex];
                  arrayRepresentation[startIndex]=arrayRepresentation[stopIndex];
                  arrayRepresentation[stopIndex]=swap;
                  startIndex++;
              }                                    

    	 tspSolution.updateSolution(arrayRepresentation);
    }   // end operateOn
    
    public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}
    
    /* Identify a move for SimpleTabuList*/
    @Override
   	public int hashCode() {
   		final int prime = 31;
   		int result = 1;
   		result = prime * result + i;
   		result = prime * result + j;
   		return result;
   	}

   	@Override
   	public boolean equals(Object obj) {
   		if (this == obj)
   			return true;
   		if (obj == null)
   			return false;
   		if (getClass() != obj.getClass())
   			return false;
   		
   		TspUntangleMove other = (TspUntangleMove) obj;
   		
   		if (i != other.i)
   			return false;
   		if (j != other.j)
   			return false;
   		return true;
   	}
    
}   // end class MySwapMove
