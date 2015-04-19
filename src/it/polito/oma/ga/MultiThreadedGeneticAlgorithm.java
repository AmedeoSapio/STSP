package it.polito.oma.ga;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.CrossoverPolicy;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.MutationPolicy;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.SelectionPolicy;
import org.apache.commons.math3.genetics.StoppingCondition;
import org.apache.commons.math3.random.RandomGenerator;

public class MultiThreadedGeneticAlgorithm extends GeneticAlgorithm{
	
	private ExecutorService executor;
	
	public MultiThreadedGeneticAlgorithm(CrossoverPolicy crossoverPolicy,
			double crossoverRate, MutationPolicy mutationPolicy,
			double mutationRate, SelectionPolicy selectionPolicy)
			throws OutOfRangeException {
		super(crossoverPolicy, crossoverRate, mutationPolicy, mutationRate,
				selectionPolicy);		
	}
	
	@Override
	public Population evolve(final Population initial, final StoppingCondition condition) {
        
		executor = Executors.newFixedThreadPool(4);
		
		Population result = super.evolve(initial, condition);
		
		executor.shutdown();
		
		return result;
    }
	
	@Override
    public Population nextGeneration(final Population current) {
		
        Population nextGeneration = current.nextGeneration();

        RandomGenerator randGen = getRandomGenerator();
                       
        // every worker produces up to 2 chromosomes
        int workersNumber = (nextGeneration.getPopulationLimit() - nextGeneration.getPopulationSize() +1)/2;
       
        CountDownLatch latch = new CountDownLatch(workersNumber);
        
        for (int i=0; i<workersNumber; i++){       
        	
        	// select parent chromosomes
            ChromosomePair pair = getSelectionPolicy().select(current);

            Runnable worker = new Worker(
            		pair,
            		getCrossoverPolicy(),
            		getCrossoverRate(),
            		getMutationPolicy(),
            		getMutationRate(),
            		nextGeneration,
            		randGen,
            		latch);
            
            executor.execute(worker);
        }        
        
        try {
			latch.await();
		} catch (InterruptedException e) {
			//TODO log error
			System.err.println("Main Thread InterruptedException");
		}

        return nextGeneration;
    }

}
