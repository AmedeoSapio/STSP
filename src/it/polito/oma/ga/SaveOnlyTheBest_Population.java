package it.polito.oma.ga;

import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;

/**
 * Population of chromosomes which saves only the best chromosome.
 *
 *@author Amedeo Sapio (amedeo.sapio@gmail.com)
 */
public class SaveOnlyTheBest_Population extends ListPopulation {
	
	Chromosome fittest;
	
    /**
     * Creates a new {@link SaveOnlyTheBest_Population} instance.
     *
     * @param chromosomes list of chromosomes in the population
     * @param populationLimit maximal size of the population 
     * @throws NullArgumentException if the list of chromosomes is {@code null}
     * @throws NotPositiveException if the population limit is not a positive number (&lt; 1)
     * @throws NumberIsTooLargeException if the list of chromosomes exceeds the population limit     * 
     */
    public SaveOnlyTheBest_Population(final List<Chromosome> chromosomes, final int populationLimit)
        throws NullArgumentException, NotPositiveException, NumberIsTooLargeException{

        super(chromosomes, populationLimit);
        if(chromosomes.size()!=0){
	        // find the best so far
	        fittest = chromosomes.get(0);
	        
	        for (Chromosome chromosome : chromosomes) {
	            if (chromosome.compareTo(fittest) > 0) {
	                
	            	// better chromosome found
	            	fittest = chromosome;
	            }	            
	        }//end for
        }
        else        	   
               fittest=null;                   
    }

    /**
     * Creates a new {@link SaveOnlyTheBest_Population} instance and initializes its inner chromosome list.
     *
     * @param populationLimit maximal size of the population
     * @throws NotPositiveException if the population limit is not a positive number (&lt; 1)
     */
    public SaveOnlyTheBest_Population(final int populationLimit)
        throws NotPositiveException{

        super(populationLimit);
        fittest=null;       
    }

    /**
     * Start the population for the next generation. The best
     * chromosome is directly copied to the next generation.
     *
     * @return the beginnings of the next generation.
     */
    public Population nextGeneration() {
    	
        // initialize a new generation with the same parameters
        SaveOnlyTheBest_Population nextGeneration =
                new SaveOnlyTheBest_Population(getPopulationLimit());
        
        //add fittest to the new generation
        nextGeneration.addChromosome(getFittestChromosome());
                
        return nextGeneration;
    }     
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void setChromosomes(final List<Chromosome> chromosomes)
        throws NullArgumentException, NumberIsTooLargeException{
    	throw new RuntimeException("setChromosome is deprecated. Use addChromosomes(Collection) instead");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addChromosomes(final Collection<Chromosome> chromosomeColl) throws NumberIsTooLargeException {
    	super.addChromosomes(chromosomeColl);
    	
    	for (Chromosome chromosome : chromosomeColl) {
            if (fittest==null ||chromosome.compareTo(fittest) > 0) {
                // better chromosome found
            	fittest = chromosome;
            }            
        }//end for
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addChromosome(final Chromosome chromosome) throws NumberIsTooLargeException {
    	super.addChromosome(chromosome);
		
    	if (fittest==null || chromosome.compareTo(fittest) > 0) {
	        // better chromosome found
	    	fittest = chromosome;
	    }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Chromosome getFittestChromosome(){
    	if(fittest!=null)
    		return fittest;
    	else 
    		return super.getFittestChromosome();    	
    }    
}