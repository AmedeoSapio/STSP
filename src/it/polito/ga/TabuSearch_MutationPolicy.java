package it.polito.ga;

import it.polito.ts.TspMoveManager;
import it.polito.ts.TspObjectiveFunction;
import it.polito.ts.TspSolution;
import it.polito.ts.TspTSListener;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.DummyLocalizable;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.MutationPolicy;
import org.coinor.opents.BestEverAspirationCriteria;
import org.coinor.opents.SimpleTabuList;
import org.coinor.opents.SingleThreadedTabuSearch;

/**
 * Mutation for {@link TspChromosome}s using Tabu Search with 
 * 2-opt algorithm as described in http://en.wikipedia.org/wiki/2-opt. 
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 */

public class TabuSearch_MutationPolicy implements MutationPolicy{
	
	private int _maxTenure;
	private int _maxIterations;
	private int _decreaseThreshold;

	/**
	 * Constructor for TabuSearch_MutationPolicy.
	 * @param maxTenure Tabu List's maximum tenure.
	 * @param maxIterations Maximum number of iterations.
	 * @param decreaseThreshold Number of non-repeating iterations after which the tenure is decreased
	 */
	public TabuSearch_MutationPolicy(int maxTenure, int maxIterations, int decreaseThreshold) {
		this._maxTenure = maxTenure;
		this._maxIterations = maxIterations;
		this._decreaseThreshold = decreaseThreshold;
	}
	
	/**
	 * Private constructor without parameters, in order to force to set parameters.
	 */
	@SuppressWarnings("unused")
	private TabuSearch_MutationPolicy() {
	}
	
	/**
     * Mutate the given chromosome using Tabu Search with 
     * 2-opt algorithm as described in http://en.wikipedia.org/wiki/2-opt.
     *
     * @param initial the original chromosome.
     * @return the mutated chromosome.
     * @throws MathIllegalArgumentException if <code>originalChromosome</code> is not an instance of {@link TspChromosome}.
     */
	@Override
	public Chromosome mutate(Chromosome initial)
			throws MathIllegalArgumentException {
		
		if (!(initial instanceof TspChromosome)) 
            throw new MathIllegalArgumentException(new DummyLocalizable("TabuSearch_MutationPolicy works on TSPTour_Chromosome only"));
		
        // Run Tabu Search
					
		// Create Tabu Search object
		SingleThreadedTabuSearch tabuSearch = new SingleThreadedTabuSearch(
				new TspSolution((TspChromosome)initial),
				new TspMoveManager(),
				new TspObjectiveFunction(),
				new SimpleTabuList(_maxTenure), // In OpenTS package
				new BestEverAspirationCriteria(), // In OpenTS package
				false ); // maximizing = yes/no; false means minimizing

		tabuSearch.addTabuSearchListener(new TspTSListener(_maxTenure, _decreaseThreshold));
		
		tabuSearch.setIterationsToGo(_maxIterations);
		
		// Start solving
		tabuSearch.startSolving();

		// return new solution
		return ((TspSolution)tabuSearch.getBestSolution()).getChromosome();
	}
}