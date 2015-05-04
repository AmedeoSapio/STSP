package it.polito.tspSolver;

import it.polito.ga.MultiThreadedGeneticAlgorithm;
import it.polito.ga.SaveOnlyTheBest_Population;
import it.polito.ga.TimedNIterationsUnchanged;
import it.polito.ga.TournamentDiversity_SelectionPolicy;
import it.polito.ga.TspChromosome;
import it.polito.ga.TabuSearch_MutationPolicy;
import it.polito.ga.TwoOpt_MutationPolicy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.genetics.*;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * The starting point of the TSP solver.
 * @author Amedeo Sapio (amedeo.sapio@gmail.com) 
 */

public class TspSolverMain {
    
	public static void main(String[] args){
		if (args.length!=1){
			System.out.println("Usage: tspSolver <parametersFilePath>\n");		
			return;
		}

		//read parameters file
		Parameters p=FileParser.readParamsFile(args[0]);		
		File csvFile=new File(p.getDataFileDir()+File.separator+p.getOutputFile());;
		FileWriter csvFileWriter=null;
		BufferedWriter csvBw=null;		
		
		System.out.println("Data file directory: "+p.getDataFileDir()+"\n"
				+ (p.getMaxPopulationSize()>0 ? ("MaxPopulationSize: "+p.getMaxPopulationSize() +"\n"):"") 
				+"CrossoverRate: "+p.getCrossoverRate() +"\n"
				+"MutationRate: "+p.getMutationRate() +"\n"
				+"TournamentArity: "+p.getTournamentArity() +"\n"
				+"Seed: "+p.getSeed() +"\n"
				+ (p.getMaxSeconds()>0 ? ("MaxSeconds: "+p.getMaxSeconds() +"\n"):"")
				+"MaxUnimprovedIterations: "+p.getMaxUninmprovedIterations() +"\n"			  
				+"Repetitions: "+p.getRepetitions() +"\n"
				+"ThreadNumber: "+p.getThreadNumber()+"\n"
				+"TSMaxIterations: "+p.getTSMaxIterations()+"\n"
				+"DecreaseThreshold: "+p.getDecreaseThreshold()+"\n"
				+ (p.getMaxTenure()>0 ? ("MaxTenure: "+p.getMaxTenure() +"\n"):""));
		
		try {			
			csvFileWriter=new FileWriter(csvFile); 	
			csvBw=new BufferedWriter(csvFileWriter);
			csvBw.write("Name;BestSolution;MeanSolution;MinSolution;MaxSolution;TimeOfTheBest;MeanTime;BestKnownSolution");
			csvBw.newLine();
			
		} catch (IOException e) {
			System.err.println("Impossible to write the csv file: \n" + csvFile.getName());
			System.exit(1);
		}
		
		//set random generator
		RandomGenerator rg=new JDKRandomGenerator();
		rg.setSeed(p.getSeed());
		GeneticAlgorithm.setRandomGenerator(rg);
		
		for (String instanceFileName: p.getInstances()){
			
			//for each instance
			Instance instance=FileParser.readInstanceFile(p.getDataFileDir()+File.separator+instanceFileName);
			
			System.out.println("********************************\n"
					+ "Instance: "+instance.getName()+"\n"
					+"Comment: "+instance.getComment());
					
			if(!(p.getMaxSeconds()>0)){
				p.setMaxSeconds(instance.getDimension());
				System.out.println("MaxSeconds: "+p.getMaxSeconds());
			}			
			
			if(!(p.getMaxPopulationSize()>0 )){
				p.setMaxPopulationSize(instance.getDimension());
				System.out.println("MaxPopulationSize: "+p.getMaxPopulationSize());
			}
			
			if(!(p.getMaxTenure()>0 )){
				p.setMaxTenure(instance.getDimension()/2);
				System.out.println("MaxTenure: "+p.getMaxTenure());
			}
			
			System.out.println("");
				
			final int populationLimit=p.getMaxPopulationSize();
			
			double meanSolution=0;
			long meanTime=0;
						
			for (int repetitions=0; repetitions<p.getRepetitions(); repetitions++){
			
				//for each repetition
				
				long startTime=System.nanoTime();
				
				//Genetic Algorithm 
				
				//initialize a new genetic algorithm	
				GeneticAlgorithm ga = new MultiThreadedGeneticAlgorithm(
				    new OrderedCrossover<Integer>(),	//Crossover policy
				    p.getCrossoverRate(),	//Crossover rate
				    new TwoOpt_MutationPolicy(), //Mutation policy
				    //new TabuSearch_MutationPolicy(p.getMaxTenure(), p.getTSMaxIterations(), p.getDecreaseThreshold()) 
				    p.getMutationRate(),	//Mutation rate
				    new TournamentDiversity_SelectionPolicy(p.getTournamentArity()), //Selection policy
				    p.getThreadNumber() //number of threads
				);

				//initial population
				Population initial = nearestNeighborPopulation(instance.getCustomers(),populationLimit);
				        
				//stopping condition
				StoppingCondition stopCond = new TimedNIterationsUnchanged(p.getMaxUninmprovedIterations(), p.getMaxSeconds());
				    
				//best initial chromosome
		        Chromosome bestInitial = initial.getFittestChromosome();
				
				// run the algorithm
				Population finalPopulation = ga.evolve(initial, stopCond);
				         
				//best chromosome from the final population
				Chromosome bestFinal = finalPopulation.getFittestChromosome();
				
				long endTime=System.nanoTime();
				double elapsedTime=Math.floor((double)(endTime-startTime)/10000000)/100;
				
				System.out.println("Repetition: "+(repetitions+1)+"\n"
						+"\tInitial solution: "+(Math.floor(bestInitial.getFitness()*100)/100)+"\n"
						+ "\tFinal solution: "+(Math.floor(bestFinal.getFitness()*100)/100)+"\n"			
						+ "\tElapsed time: "+elapsedTime+(elapsedTime==1?" second":" seconds")+"\n");
								
				if(instance.getMinSolution()==0 || bestFinal.getFitness()<instance.getMinSolution()){
					instance.setMinSolution(bestFinal.getFitness());
					instance.setTimeBest(endTime-startTime);
					instance.setOptTour(((TspChromosome)bestFinal).getTour());
				}
				
				if(instance.getMaxSolution()==0 || bestFinal.getFitness()>instance.getMaxSolution())
					instance.setMaxSolution(bestFinal.getFitness());
					
				meanSolution+= bestFinal.getFitness();
				meanTime+=endTime-startTime;
			}//end of repetitions

			instance.setMeanSolution(meanSolution/p.getRepetitions());
			instance.setTimeMean(meanTime/p.getRepetitions());
			
			//look for optTourFile
			File optTourFile = new File(p.getDataFileDir()+File.separator+instance.getName()+".opt.tour");
			if(optTourFile.exists()){
				List <Integer> optTour=FileParser.readTourFile(optTourFile);
				if(optTour!=null){
					TspChromosome optChromosome=new TspChromosome(optTour, instance.getCustomers());
					instance.setBestKnownSolution(optChromosome.getFitness());					
				}
			}
			
			
			System.out.println("Best solution: "+ (long)instance.getMinSolution()+"\n"
					+ "Mean solution: "+instance.getMeanSolution()+"\n"
					+ "Minimum solution: "+(long)instance.getMinSolution()+"\n"
					+ "Maximum solution: "+(long)instance.getMaxSolution()+"\n"
					+ "Time of the best: "+instance.getTimeBest()+"\n"
					+ "Mean time: "+instance.getTimeMean()+"\n"
					+ "Best known: "+(long)instance.getBestKnownSolution());
						
			//write line in csv file
			try {
				
				csvBw.write(instance.getName()+";"
						+(long)instance.getMinSolution()+";"
						+instance.getMeanSolution()+";"
						+(long)instance.getMinSolution()+";"
						+(long)instance.getMaxSolution()+";"
						+instance.getTimeBest()+";"
						+instance.getTimeMean()+";"
						+(long)instance.getBestKnownSolution());
				csvBw.newLine();
				
			} catch (IOException e) {
				System.err.println("Impossible to write the csv file: \n" + csvFile.getName());
				System.exit(1);
			}
			
			//write TOUR file
			File tourFile=new File(p.getDataFileDir()+File.separator+instance.getName()+".tour");
			FileWriter tourFileWriter=null;
			BufferedWriter tourBw=null;
			
			try {			
				tourFileWriter=new FileWriter(tourFile); 	
				tourBw=new BufferedWriter(tourFileWriter);
				tourBw.write("NAME: "+tourFile.getName());
				tourBw.newLine();
				tourBw.write("COMMENT: Best tour for "+instanceFileName+" ("+instance.getMinSolution()+")");
				tourBw.newLine();
				tourBw.write("TYPE: TOUR");
				tourBw.newLine();
				tourBw.write("DIMENSION: "+instance.getDimension());
				tourBw.newLine();				
				tourBw.write("TOUR_SECTION");
				tourBw.newLine();
				for (Integer gene: instance.getOptTour()){
					tourBw.write(gene.toString());
					tourBw.newLine();
				}
				tourBw.write("-1");
				tourBw.newLine();
				tourBw.write("EOF");				
			} catch (IOException e) {
				System.err.println("Impossible to write the tour file: \n" + tourFile.getName());
				System.exit(1);
			}
			
			try {
				tourBw.close();
				tourFileWriter.close();
				
			} catch (IOException e) {
				System.err.println("Error writing the tour file: \n" + tourFile.getName());
				System.exit(1);
			}
			System.out.println("TOUR file: "+tourFile.getAbsolutePath());
			System.out.println("********************************");
		}//end for each instance
		
		//close csv file
		try {
			csvBw.close();
			csvFileWriter.close();
			
		} catch (IOException e) {
			System.err.println("Error writing the csv file: \n" + p.getOutputFile());
			System.exit(1);
		}
		System.out.println("Results file: "+csvFile.getAbsolutePath());
		
		
	}//end main
	
	 /**
     * Initializes a random population
     */
    public static SaveOnlyTheBest_Population randomPopulation(double[][] customers, int populationSize) {
           	
    	List<Chromosome> population = new ArrayList<Chromosome>(populationSize);

    	List<Integer> vertices = new ArrayList<Integer>(customers.length);
    	
    	RandomGenerator rg=GeneticAlgorithm.getRandomGenerator();
    	 
        for (int i = 0; i < customers.length; i++)
                vertices.add(i);
        
        for (int i=0; i<populationSize; i++) {
        	
        	//create one chromosome
        	       	
        	List <Integer> tempVertices=new ArrayList<Integer>(vertices); 
            // get random element from 1..n and add it to the random solution
            List<Integer> representation = new ArrayList<Integer>(customers.length);
            for(int j = 0; j < customers.length; j++) {
                    representation.add(j, tempVertices.remove(rg.nextInt(tempVertices.size())));
            }
        	
            TspChromosome randomChromosome = new TspChromosome(representation, customers);
            population.add(randomChromosome);
        }

        return new SaveOnlyTheBest_Population(population, population.size());
    }
    
    /**
     * Initializes a population with Nearest Neighbor
     */
    public static SaveOnlyTheBest_Population nearestNeighborPopulation(double[][] customers, int populationLimit){
        
    	final int populationSize= (customers.length<populationLimit?customers.length:populationLimit);
    	
    	List<Chromosome> population = new ArrayList<Chromosome>(populationSize);

    	List<Integer> vertices = new ArrayList<Integer>(customers.length);
    	
    	RandomGenerator rg=GeneticAlgorithm.getRandomGenerator();
    	 
        for (int i = 0; i < customers.length; i++)
                vertices.add(i);
        
        //list from which choose the initial node
        List <Integer> initialChoiceVertices=new ArrayList<Integer>(vertices); 
        
        //build the symmetric distance matrix
        long [][] distanceMatrix= new long[customers.length][customers.length];
                
        for (int i=0; i<customers.length; i++){
			distanceMatrix[i][i]=0;
        	for (int j=i+1; j<customers.length; j++){
        		distanceMatrix[i][j]=TspChromosome.norm(customers, i, j);
        		distanceMatrix[j][i]=distanceMatrix[i][j];
        	}        	
		}        
        
        for (int i=0; i<populationSize; i++) {
        	
        	//create one chromosome
        	
            // get random element that is the start of the NN (any chromosome has a new starting point)
        	Integer first=initialChoiceVertices.remove(rg.nextInt(initialChoiceVertices.size()));
        	        	
        	List<Integer> representation = new ArrayList<Integer>(customers.length);
            representation.add(0,first);
            
            //list in which is searched the nearest neighbor (all remaining nodes)
            List <Integer> availableVertices=new ArrayList<Integer>(vertices);
            availableVertices.remove(first);
            
        	for(int j = 1; j < customers.length; j++) {
        			
        			//the last node inserted is the one currently used for finding the NN 
        			Integer current=representation.get(j-1);
        			
        			//search for nearest neighbor in available vertices
        			double distance=Double.MAX_VALUE;
        			Integer choosen=null;
        			
        			for(Integer vertex:availableVertices){
        				if(distanceMatrix[vertex.intValue()][current.intValue()]<distance){
        					choosen=vertex;
        					distance=distanceMatrix[vertex.intValue()][current.intValue()];        					
        				}
        			}
        			
        			if(choosen==null){
        				//this should never happen
        				System.err.println("Nearest Neighbor fatal error!\n");
        				System.exit(1);
        			}
        			
            		representation.add(j,choosen);
            		availableVertices.remove(choosen);
            }
        	
            TspChromosome newChromosome = new TspChromosome(representation, customers,distanceMatrix);
            population.add(newChromosome);
        }//end for
        
        return new SaveOnlyTheBest_Population(population, populationLimit);
    }
}
