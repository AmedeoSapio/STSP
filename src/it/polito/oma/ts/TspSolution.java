package it.polito.oma.ts;
import org.coinor.opents.*;


public class TspSolution extends SolutionAdapter 
{
    public int[] tour;
    
    public TspSolution(){} // Appease clone()
    
    public TspSolution( double[][] customers )
    {
        // Crudely initialize solution
        tour = new int[ customers.length ];
        for( int i = 0; i < customers.length; i++ )
            tour[i] = i;
    }   // end constructor
    
    
    public Object clone()
    {   
        TspSolution copy = (TspSolution)super.clone();
        copy.tour = (int[])this.tour.clone();
        return copy;
    }   // end clone
    
    
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        
        s.append( "Solution value: " + getObjectiveValue()[0] );
        s.append( "Sequence: [ " );
        
        for( int i = 0; i < tour.length - 1; i++ )
            s.append( tour[i] ).append( ", " );
        
        s.append( tour[ tour.length - 1 ] );
        s.append( " ]" );
        
        return s.toString();
    }   // end toString
    
}   // end class MySolution
