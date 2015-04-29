package it.polito.ga;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;
/**
 * A chromosome representing a TSP tour.
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 */
public class TspChromosome extends AbstractListChromosome<Integer>{
	
	//a chromosome is IMMUTABLE
		
	private double[][] customers;
	private long [][] distanceMatrix;
	
	/**
	 * Constructor for Traveling Salesman Problem Chromosome
	 * @param representation Representation of a tour
	 * @param customers Customers coordinates
	 * @throws InvalidRepresentationException iff the representation can not represent a valid chromosome
	 */	
	public TspChromosome(List<Integer> representation, double[][] customers)
			throws InvalidRepresentationException {
		super(representation);
		
		this.customers=customers;
		
		//build the symmetric distance matrix
        distanceMatrix= new long[customers.length][customers.length];
        
        for (int i=0; i<customers.length; i++){
			distanceMatrix[i][i]=0;
        	for (int j=i+1; j<customers.length; j++){
        		distanceMatrix[i][j]=TspChromosome.norm(customers, i, j);
        		distanceMatrix[j][i]=distanceMatrix[i][j];
        	}        	
		}        
	}
	
	/**
	 * Constructor for Traveling Salesman Problem Chromosome
	 * @param representation Representation of a tour
	 * @param customers Customers coordinates
	 * @param distanceMatrix The precalculated distance matrix
	 * @throws InvalidRepresentationException iff the representation can not represent a valid chromosome
	 */	
	public TspChromosome(List<Integer> representation, double[][] customers, long [][] distanceMatrix)
			throws InvalidRepresentationException {
		super(representation);
		
		this.customers=customers;	
        this.distanceMatrix=distanceMatrix;
	}
	
	/**	  
	 * @param first
	 * @param second
	 * @return The euclidean distance between first and second
	 */
	public long norm( int first, int second )
    {        
        return distanceMatrix[first][second];
    }
	
	/**
	 * 
	 * @param customers
	 * @param first
	 * @param second
	 * @return The euclidean distance between first and second, according to the coordinates in customers
	 */
	public static long norm(double[][] customers, int first, int second )
    {
        double xDiff = customers[second][0] - customers[first][0];
        double yDiff = customers[second][1] - customers[first][1];
        return Math.round(Math.sqrt( Math.pow(xDiff,2) + Math.pow(yDiff,2)));
    }
	
	/**
	 * Compute the fitness. This is usually very time-consuming, so the value should be cached.
	 * Don't use this function, use getFitness() instead
	 */
	@Override
	public double fitness() {
		//the fitness is calculated only once, than is cached
		double fitness=0;
		List<Integer> representation=getRepresentation();
		int first=0,second=0;
		
		for (int i=0; i<representation.size()-1;i++){
			first=representation.get(i).intValue();
			second=representation.get(i+1).intValue();
			fitness+=norm(first,second);
		}			
		
		first=representation.get(representation.size()-1).intValue();
		second=representation.get(0).intValue();
		fitness+=norm(first,second);
		
		return fitness;
	}
	
	 /**
     * Access the fitness of this chromosome. The SMALLER the fitness, the better the chromosome.
     * <p>
     * Computation of fitness is usually very time-consuming task, therefore the fitness is cached.
     *
     * @return the fitness
     */
	@Override
    public double getFitness() {        
        return super.getFitness();
    }
	
	/**
	 * Check the validity of the representation.
	 * Not implemented because not needed.
	 */
	@Override
	protected void checkValidity(List<Integer> chromosomeRepresentation)
			throws InvalidRepresentationException {
			//not needed
		return;
		/*
		//bit array setted to true
		BitSet bits=new BitSet(chromosomeRepresentation.size());
		bits.set(0, chromosomeRepresentation.size()-1, true);
		BitSet testSet=new BitSet(chromosomeRepresentation.size());
		testSet.set(0, chromosomeRepresentation.size()-1, true);
		
		for (Integer chromosome:chromosomeRepresentation){
			try{
				//set to false the bit corresponding to the chromosome
				bits.flip(chromosome.intValue());
			}catch(IndexOutOfBoundsException ioobe){
				throw new InvalidRepresentationException(new DummyLocalizable("Validity test failed! Index out of bound."));
			}
		}
		
		if (bits.intersects(testSet))
			throw new InvalidRepresentationException(new DummyLocalizable("Validity test failed! Missing or duplicated element."));
			*/
	}

	@Override
	public AbstractListChromosome<Integer> newFixedLengthChromosome(
			List<Integer> chromosomeRepresentation) {
		
		return new TspChromosome(chromosomeRepresentation, customers, distanceMatrix);
	}
	 
	/**
     * Compares two chromosomes based on their fitness. The SMALLER the fitness, the better the chromosome.
     *
     * @param another another chromosome to compare
     * @return
     * <ul>
     *   <li>-1 if <code>another</code> is better than <code>this</code></li>
     *   <li>1 if <code>another</code> is worse than <code>this</code></li>
     *   <li>0 if the two chromosomes have the same fitness</li>
     * </ul>
     */
	@Override
	public int compareTo(final Chromosome another) {
        return ((Double)another.getFitness()).compareTo(this.getFitness());
    }
    
	@Override
    public String toString() {
		//translate indexes starting from 1 instead of 0
		List<Integer> vector=new ArrayList<Integer>(getRepresentation().size());
		for (Integer elem: getRepresentation())
			vector.add(new Integer(elem.intValue()+1));
			
        return String.format("O.F.= %s\n%s", Math.round(getFitness()), vector);
    }
	
    /**
     * Returns the (immutable) inner representation of the chromosome.
     * @return the representation of the chromosome
     */
	@Override
    protected List<Integer> getRepresentation() {
        return super.getRepresentation();
    }
	
	/**
     * Returns a copy of the tour.
     * @return a copy of the tour
     */
    public List<Integer> getTour() {
        return new ArrayList<Integer>(super.getRepresentation());
    }
    
    /**
     * Returns a copy of the tour.
     * @return a copy of the tour in an array
     */
    public Integer[] getTourAsArray() {
        return super.getRepresentation().toArray(new Integer[super.getRepresentation().size()]);
    }
    
    /**
     * Returns a copy of element at the position <var>index</var>.
     * @return a copy of element at the position <var>index</var>.
     */
    public Integer getElement(int index){
    	return new Integer(super.getRepresentation().get(index).intValue());
    }
    
    @Override
	public int hashCode() {    	
		return getRepresentation().hashCode(); 
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TspChromosome other = (TspChromosome) obj;
		if (!getRepresentation().equals(other.getRepresentation()))
			return false;
		return true;
	}

}
