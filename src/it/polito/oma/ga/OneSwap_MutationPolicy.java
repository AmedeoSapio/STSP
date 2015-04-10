package it.polito.oma.ga;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.DummyLocalizable;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.MutationPolicy;

/**
 * Mutation for {@link TSPTour_Chromosome}s. Randomly swaps two genes.
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 */
public class OneSwap_MutationPolicy implements MutationPolicy{
	/**
     * Mutate the given chromosome. Randomly swaps two genes.
     *
     * @param originalChromosome the original chromosome.
     * @return the mutated chromosome.
     * @throws MathIllegalArgumentException if <code>originalChromosome</code> is not an instance of {@link TSPTour_Chromosome}.
     */
	@Override
	public Chromosome mutate(Chromosome originalChromosome)
			throws MathIllegalArgumentException {
		
		if (!(originalChromosome instanceof TSPTour_Chromosome)) 
            throw new MathIllegalArgumentException(new DummyLocalizable("OneSwap_MutationPolicy works on TSPTour_Chromosome only"));
        
		TSPTour_Chromosome  original= (TSPTour_Chromosome) originalChromosome;
        
		//make a copy of the original unmodifiable representation
		List<Integer> newRepresentation = new ArrayList<Integer>(original.getRepresentation());

        // randomly select two genes
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

        Chromosome newChromosome = original.newFixedLengthChromosome(newRepresentation);
        
        return newChromosome;
	}
}