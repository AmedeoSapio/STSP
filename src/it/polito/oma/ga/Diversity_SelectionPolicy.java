package it.polito.oma.ga;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.DummyLocalizable;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.SelectionPolicy;


/**
 * Selects 2 chromosomes with different fitness
 * @author Amedeo Sapio (amedeo.sapio@gmail.com) 
 */
 
public class Diversity_SelectionPolicy implements SelectionPolicy{
	
	private List<Chromosome> sortedpop=null;
	private Population currentpopulation=null;
	private int currentIndex;
		
	@Override
	public ChromosomePair select(Population population)
			throws MathIllegalArgumentException {
					
		if(population.getPopulationSize()<2)
			throw new MathIllegalArgumentException(new DummyLocalizable("Selection error: too few chromosomes in the population"));
		
		if (sortedpop==null || currentpopulation==null || currentpopulation!=population){
			//new population
			sortedpop=new LinkedList<Chromosome>(((SaveOnlyTheBest_Population)population).getChromosomes());
			Collections.sort(sortedpop);
			currentpopulation=population;			
			currentIndex=sortedpop.size()-1;
		}

		Chromosome first=sortedpop.get(currentIndex);
		currentIndex--;		
		if (currentIndex<0)
			currentIndex+=population.getPopulationSize();
		Chromosome second=sortedpop.get(currentIndex);
		currentIndex--;
		if (currentIndex<0)
			currentIndex+=population.getPopulationSize();
		
		
		while (first.getFitness()==second.getFitness()){
			//too close
			second=sortedpop.get(currentIndex);
			if(first==second)
				throw new RuntimeException("");
			currentIndex--;
			if (currentIndex<0)
				currentIndex+=population.getPopulationSize();

		}
		
		return new ChromosomePair(first,second);
	
	}

}
