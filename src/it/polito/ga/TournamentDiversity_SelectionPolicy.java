package it.polito.ga;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.SelectionPolicy;

/**
 * Tournament selection scheme. Each of the two selected chromosomes is selected
 * based on n-ary tournament -- this is done by drawing {@link #arity} random
 * chromosomes without replacement from the population, and then selecting the
 * fittest chromosome among them. To keep diversity, if the two selected chromosomes 
 * have the same fitness, the second is chosen with a new tournament. 
 * @author Amedeo Sapio (amedeo.sapio@gmail.com) 
 */

public class TournamentDiversity_SelectionPolicy implements SelectionPolicy {

    /** number of chromosomes included in the tournament selections */
    private int arity;

    /**
     * Creates a new TournamentSelection instance.
     *
     * @param arity how many chromosomes will be drawn to the tournament
     */
    public TournamentDiversity_SelectionPolicy(final int arity) {
        this.arity = arity;
    }

    /**
     * Select two chromosomes from the population. Each of the two selected
     * chromosomes is selected based on n-ary tournament -- this is done by
     * drawing {@link #arity} random chromosomes without replacement from the
     * population, and then selecting the fittest chromosome among them. 
     * To keep diversity, if the two selected chromosomes 
     * have the same fitness, the second is chosen with a new tournament.
     *
     * @param population the population from which the chromosomes are chosen.
     * @return the selected chromosomes.
     * @throws MathIllegalArgumentException if the tournament arity is bigger than the population size
     */
    public ChromosomePair select(final Population population) throws MathIllegalArgumentException {
        Chromosome first=tournament((ListPopulation) population);
        Chromosome second=tournament((ListPopulation) population);
        
        int i=0;
        while (first.getFitness()==second.getFitness() && i<population.getPopulationSize()){
        	second=tournament((ListPopulation) population);
        	i++;
        }//end while
                
    	return new ChromosomePair(first, second);
    }

    /**
     * Helper for {@link #select(Population)}. Draw {@link #arity} random chromosomes without replacement from the
     * population, and then select the fittest chromosome among them.
     *
     * @param population the population from which the chromosomes are choosen.
     * @return the selected chromosome.
     * @throws MathIllegalArgumentException if the tournament arity is bigger than the population size
     */
    private Chromosome tournament(final ListPopulation population) throws MathIllegalArgumentException {
        if (population.getPopulationSize() < this.arity) {
            throw new MathIllegalArgumentException(LocalizedFormats.TOO_LARGE_TOURNAMENT_ARITY,
                                                   arity, population.getPopulationSize());
        }
        // auxiliary population
        ListPopulation tournamentPopulation = new ListPopulation(this.arity) {
            public Population nextGeneration() {
                // not useful here
                return null;
            }
        };

        // create a copy of the chromosome list
        List<Chromosome> chromosomes = new ArrayList<Chromosome> (population.getChromosomes());
        for (int i=0; i<this.arity; i++) {
            // select a random individual and add it to the tournament
            int rind = GeneticAlgorithm.getRandomGenerator().nextInt(chromosomes.size());
            tournamentPopulation.addChromosome(chromosomes.get(rind));
            // do not select it again
            chromosomes.remove(rind);
        }
        // the winner takes it all
        return tournamentPopulation.getFittestChromosome();
    }

    /**
     * Gets the arity (number of chromosomes drawn to the tournament).
     *
     * @return arity of the tournament
     */
    public int getArity() {
        return arity;
    }

    /**
     * Sets the arity (number of chromosomes drawn to the tournament).
     *
     * @param arity arity of the tournament
     */
    public void setArity(final int arity) {
        this.arity = arity;
    }

}