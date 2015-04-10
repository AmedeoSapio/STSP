package it.polito.oma.ga;

import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;

/**
 * Stops after a fixed number of generations during which the population is not improved,
 * or after a specific maximum amount of seconds. 
 * Each time {@link #isSatisfied(Population)} is invoked, a generation counter is 
 * incremented if the population is not improved. Once the counter reaches the configured 
 * <code>maxGenerations</code> value, or the elapsed time reaches <code>maxSeconds</code>, 
 * {@link #isSatisfied(Population)} returns true.
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 */
public class TimedNIterationsUnchanged implements StoppingCondition {

	/** Number of unchanged generations that have passed */
    private int numGenerations;

    /** Maximum number of unimproved generations */
    private final int maxGenerations;

    /** Maximum allowed time period (in nanoseconds)*/
    private final long maxTimePeriod;

    /** The predetermined termination time*/
    private long endTime;
    
    /** The fittest chromosome of the last population*/
    private Chromosome pastFittest;
	
    /**
     * Create a new TimedNIterationsUnchanged instance.
     *
     * @param maxGenerations maximum number of unimproved generations 
     * @param maxTime maximum number of seconds generations are allowed to evolve
     * @throws NumberIsTooSmallException if the number of generations is &lt; 1 or if the provided time is &lt; 0
     */
    public TimedNIterationsUnchanged(final int maxGenerations, final long maxTime) throws NumberIsTooSmallException {
        if (maxGenerations <= 0) {
            throw new NumberIsTooSmallException(maxGenerations, 1, true);
        }
        if (maxTime < 0) {
            throw new NumberIsTooSmallException(maxTime, 0, true);
        }
        
        numGenerations = 0;
        endTime = -1;
        pastFittest = null;
        this.maxGenerations = maxGenerations;
        maxTimePeriod = TimeUnit.SECONDS.toNanos(maxTime);
        
    }
     

    /**
     * Determine whether or not the given number of unimproved generations, or the maximum allowed time, have passed. 
     * Increments the number of generations counter if the maximum has not been reached and the population is unimproved.
     *
     * @param population actual population
     * @return <code>true</code> if the maximum number of unimproved generations has been exceeded or the maximum 
     * allowed time period has elapsed
     */
    
    @Override
    public boolean isSatisfied(final Population population) {
    	  
    	if (endTime < 0) {
              endTime = System.nanoTime() + maxTimePeriod;
        }

        if (System.nanoTime() >= endTime)
        	return true;
        
        Chromosome actualFittest=population.getFittestChromosome();
                
        if(pastFittest==null){
        	numGenerations++;
        	pastFittest=actualFittest;
        	return false;
        }else if(actualFittest.compareTo(pastFittest)<=0){
        		//not improved
        		if (this.numGenerations < this.maxGenerations) {
                    numGenerations++;
                    pastFittest=actualFittest;
                    return false;
                }else
                	return true;
    	}
    	else{
    		//improved
    		pastFittest=actualFittest;
    		numGenerations=0;
    		return false;
    	}            	
    }
}
