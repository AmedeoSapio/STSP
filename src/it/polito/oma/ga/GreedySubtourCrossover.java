package it.polito.oma.ga;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.DummyLocalizable;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.CrossoverPolicy;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * Greedy Subtour Crossover  [GSX] builds offspring from <b>ordered</b> chromosomes by choosing a random element to 
 * add in the child, and then adds:
 * -to the left the elements on the left of that element in the other parent, until a gene already present is found
 * -to the right the elements on the right of that element in one parent, until a gene already present is found,
 * then adds randomly the remaining genes.
 * 
 * <p>
 * Example:
 * <pre>
 * p1 = (D H B A C F G E)   
 *                          
 * p2 = (B C D G H F E A)   
 * 
 * if C is chosen as first node:
 * 
 * c = ( H B A C D G )
 * 
 * then E F are added randomly:
 * 
 * c = (H B A C D G E F)
 * </pre>
 * <p>
 * This policy works only on {@link TspChromosome}, and therefore it
 * is parameterized by T. Moreover, the chromosomes must have same lengths.
 *
 *
 * @param <T> generic type of the {@link AbstractListChromosome}s for crossover
 * @author Amedeo Sapio (amedeo.sapio@gmail.com) 
 */
public class GreedySubtourCrossover implements CrossoverPolicy {

	/**
     * {@inheritDoc}
     *
     * @throws MathIllegalArgumentException if one of the chromosomes is
     *   not an instance of {@link TspChromosome}
     * @throws DimensionMismatchException if the length of the two chromosomes is different
     */
	
	@Override
	public ChromosomePair crossover(Chromosome first, Chromosome second)
			throws MathIllegalArgumentException {
		 
		if (!(first instanceof TspChromosome && second instanceof TspChromosome)) 
			throw new MathIllegalArgumentException(new DummyLocalizable("GreedySubtourCrossover works only with TSPTour_Chromosome"));
	        
		
		TspChromosome firstChromosome=(TspChromosome)first;
		TspChromosome secondChromosome=(TspChromosome)second;
		List<Integer> firstRepresentation=firstChromosome.getRepresentation();
		List<Integer> secondRepresentation=secondChromosome.getRepresentation();
		int chromosomeSize=firstRepresentation.size();
		
		if (chromosomeSize!=secondRepresentation.size())
			throw new DimensionMismatchException(chromosomeSize, secondRepresentation.size());
		
		RandomGenerator rg= GeneticAlgorithm.getRandomGenerator();

		List<Integer> vertices = new ArrayList<Integer>(chromosomeSize);
    	    	 
        for (int i = 0; i < chromosomeSize; i++)
                vertices.add(i);
        
		LinkedList<Integer> firstChild=new LinkedList<Integer>();
		LinkedList<Integer> secondChild=new LinkedList<Integer>();
				
		//first child
		boolean fa=true, fb=true;
		int x=rg.nextInt(chromosomeSize);
		Integer t=firstRepresentation.get(x);
		int y=secondRepresentation.indexOf(t);
		firstChild.add(t);
		Integer current=null;
		
		while (fa || fb){
			x=((x-1)%chromosomeSize);
			if(x<0)
				x=chromosomeSize+x;
					
			y=((y+1)%chromosomeSize);
			
			
			if(fa){
				current=firstRepresentation.get(x);
				if(firstChild.contains(current))
					fa=false;
				else
					firstChild.addFirst(current);
			}
			if(fb){
				current=secondRepresentation.get(y);
				if(firstChild.contains(current))
					fb=false;
				else
					firstChild.addLast(current);
			}
		}//end while
		
		List<Integer> remainingElements=new ArrayList<Integer>(vertices);
		remainingElements.removeAll(firstChild);
		firstChild.addAll(remainingElements);
		
		//second child
		fa=true;
		fb=true;
		x=rg.nextInt(chromosomeSize);
		t=firstRepresentation.get(x);
		y=secondRepresentation.indexOf(t);
		secondChild.add(t);
		current=null;
		
		while (fa || fb){
			x=((x-1)%chromosomeSize);
			if(x<0)
				x=chromosomeSize+x;
			
			y=((y+1)%chromosomeSize);
			if(fa){
				current=firstRepresentation.get(x);
				if(secondChild.contains(current))
					fa=false;
				else
					secondChild.addFirst(current);
			}
			if(fb){
				current=secondRepresentation.get(y);
				if(secondChild.contains(current))
					fb=false;
				else
					secondChild.addLast(current);
			}
		}//end while
		
		remainingElements=new ArrayList<Integer>(vertices);
		remainingElements.removeAll(secondChild);
		secondChild.addAll(remainingElements);
				
		return new ChromosomePair(firstChromosome.newFixedLengthChromosome(firstChild), firstChromosome.newFixedLengthChromosome(secondChild));
	}

}
