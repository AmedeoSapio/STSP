package it.polito.oma.ga;

import it.polito.oma.tspSolver.TspSolverMain;

import java.util.Arrays;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.DummyLocalizable;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.MutationPolicy;

/**
 * Mutation for {@link TspChromosome}s. 2-opt algorithm as described in http://en.wikipedia.org/wiki/2-opt
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 */

public class TwoOpt_TS_MutationPolicy implements MutationPolicy{
	/**
     * Mutate the given chromosome. 2-opt algorithm as described in http://en.wikipedia.org/wiki/2-opt
     *
     * @param originalChromosome the original chromosome.
     * @return the mutated chromosome.
     * @throws MathIllegalArgumentException if <code>originalChromosome</code> is not an instance of {@link TspChromosome}.
     */
	@Override
	public Chromosome mutate(Chromosome originalChromosome)
			throws MathIllegalArgumentException {
		
		if (!(originalChromosome instanceof TspChromosome)) 
            throw new MathIllegalArgumentException(new DummyLocalizable("TwoOpt_MutationPolicy works on TSPTour_Chromosome only"));
        
		TspChromosome  original= (TspChromosome) originalChromosome;
        
		//make a copy of the original unmodifiable representation
		//use an array instead of a list, for better performances
		Integer[] arrayRepresentation=original.getRepresentation().toArray(new Integer[original.getRepresentation().size()]);	
        
        /* randomly swaps two genes (does not improve the results)
        int firstIndex = GeneticAlgorithm.getRandomGenerator().nextInt(original.getLength());
        int secondIndex = GeneticAlgorithm.getRandomGenerator().nextInt(original.getLength());
        
        if(firstIndex==secondIndex){ 
            if(secondIndex>0) 
               secondIndex--;
            else 
               secondIndex++;
        }
        
        Integer swap=newRepresentation.get(firstIndex);
        newRepresentation.set(firstIndex, newRepresentation.get(secondIndex));
        newRepresentation.set(secondIndex, swap);
        */
		
        boolean done = false;
        final int count = arrayRepresentation.length;
        for(int k = 0; k < count && !done; k++)
        {
            done = true;
            for(int i = 0; i < count; i++)
            {
                for(int j = i + 2; j < count; j++)
                    if(
                       original.norm(arrayRepresentation[i],arrayRepresentation[(i + 1) % count])  + original.norm(arrayRepresentation[j],arrayRepresentation[(j + 1) % count])  
                                    > 
                       original.norm(arrayRepresentation[i],arrayRepresentation[j])  + original.norm(arrayRepresentation[(i + 1) % count],arrayRepresentation[(j + 1) % count])
                    )
                    {
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
                        
                        done = false;
                    }
            }
        }
                
        // Run Tabu Search
        Chromosome newChromosome = TspSolverMain.runTS(original.newFixedLengthChromosome(Arrays.asList(arrayRepresentation)));
      
        return newChromosome;
	}
}