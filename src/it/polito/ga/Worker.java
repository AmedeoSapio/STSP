package it.polito.ga;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.CrossoverPolicy;
import org.apache.commons.math3.genetics.MutationPolicy;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * Worker thread to evolve the given population into the next generation
 * @author Amedeo Sapio (amedeo.sapio@gmail.com) 
 *
 */
public class Worker extends Thread{

	ChromosomePair pair;  
	RandomGenerator randGen;
	CrossoverPolicy crossoverPolicy;
	double crossoverRate;
	MutationPolicy mutationPolicy;
	double mutationRate;
	Population nextGeneration;
	CountDownLatch latch;
	
	public Worker (
			ChromosomePair pair,
			CrossoverPolicy crossoverPolicy,
			double crossoverRate,
			MutationPolicy mutationPolicy,
			double mutationRate,
			Population nextGeneration,
			RandomGenerator randGen,
			CountDownLatch latch){
		
		this.pair = pair;
		this.randGen = randGen;
		this.crossoverPolicy = crossoverPolicy;
		this.crossoverRate = crossoverRate;
		this.mutationPolicy = mutationPolicy;
		this.mutationRate = mutationRate;
		this.nextGeneration = nextGeneration;
		this.latch = latch;
	}
	
	@Override
	public void run() {		
		
        // crossover?
        if (randGen.nextDouble() < crossoverRate) {
        	
            // apply crossover policy to create two offspring
            pair = crossoverPolicy.crossover(pair.getFirst(), pair.getSecond());
        }

        // mutation?
        if (randGen.nextDouble() < mutationRate) {
        	
            // apply mutation policy to the chromosomes
            pair = new ChromosomePair(
                mutationPolicy.mutate(pair.getFirst()),
                mutationPolicy.mutate(pair.getSecond()));
        }
        
        synchronized (nextGeneration) {
        	
        	// add the first chromosome to the population
        	nextGeneration.addChromosome(pair.getFirst());
        	
            // is there still a place for the second chromosome?
            if (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit()) {
        	
            	// add the second chromosome to the population
            	nextGeneration.addChromosome(pair.getSecond());
            }
        		             
		}//end synchronized
        
        latch.countDown();
	}
}
