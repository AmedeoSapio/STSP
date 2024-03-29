package it.polito.ts;

import java.util.Arrays;
import java.util.List;

import it.polito.ga.TspChromosome;

import org.coinor.opents.*;

/**
 * This class is a wrapper of the TspChromosome.
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 *
 */
public class TspSolution extends SolutionAdapter 
{    
	
	private static final long serialVersionUID = 1L;
	
	private TspChromosome chromosome;
  
	public TspSolution( TspChromosome chromosome)
    {    	
    	this.chromosome = (TspChromosome)chromosome; 
        setObjectiveValue(new double[]{chromosome.getFitness()});
    }   // end constructor
    
    public List<Integer> getTour(){
    	return chromosome.getTour();
    }
    
    public Integer[] getTourAsArray(){
    	return chromosome.getTourAsArray();
    }
    
    public Integer getElement(int index){
    	return chromosome.getElement(index%chromosome.getLength());
    }
    
    public long norm(int first, int second){
    	return chromosome.norm(first, second);    	
    }
    
    /**
     * Update the solution.
     * @param arrayRepresentation The new solution representation.
     */
    public void updateSolution(Integer[] arrayRepresentation){
    	chromosome = (TspChromosome)chromosome.newFixedLengthChromosome(Arrays.asList(arrayRepresentation));
    	setObjectiveValue(new double[]{chromosome.getFitness()});
    }
    
    public TspChromosome getChromosome() {
  		return chromosome;
  	}
    
    public Object clone()
    {   
        TspSolution copy = (TspSolution)super.clone();
        copy.chromosome = this.chromosome;
        return copy;
    }   // end clone
        
    public String toString()
    {
        return chromosome.toString();
    }   // end toString
    
    public int getLength(){
    	return chromosome.getLength();
    }
    
    @Override
	public int hashCode() {
    	return ((chromosome == null) ? 0 : chromosome.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TspSolution other = (TspSolution) obj;
		if (chromosome == null) {
			if (other.chromosome != null)
				return false;
		} else if (!chromosome.equals(other.chromosome))
			return false;
		return true;
	}

}   // end class MySolution
