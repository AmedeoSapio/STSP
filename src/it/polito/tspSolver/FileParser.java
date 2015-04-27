package it.polito.tspSolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A class for parse parameters and instance files
 * @author Amedeo Sapio (amedeo.sapio@gmail.com) 
 */

public class FileParser {

	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}

	public static Parameters readParamsFile(String paramsFilepath) {
		
		String dataFileDir=null;
		String outputFile=null;
		int repetitions=-1;
		int maxPopulationSize=-1;
		double crossoverRate=-1;
		double mutationRate=-1;
		int tournamentArity=-1;
		int seed=12345;
		int maxSeconds=-1;
		int maxUnimprovedIterations=-1;
		int threadNumber = Runtime.getRuntime().availableProcessors();
		int tsMaxiterations=-1;
		int tabuTenure=-1;
		
		List<String> instances=new LinkedList<String>();
		FileReader paramsFileReader=null;
		BufferedReader br=null;
		
		try{
			if (paramsFilepath==null || paramsFilepath.equals(""))
				throw new Exception("Invalid parameters file path!");;
			
			Parameters params=null;
			
			try {
				paramsFileReader=new FileReader(paramsFilepath);
			} catch (FileNotFoundException e) {
				System.err.println("Parameters file not found\n" +
						"Usage: tspSolver <parametersFilePath>");
				System.exit(1);
			}		
		
			br=new BufferedReader(paramsFileReader);
		
			ParamFile fileStatus = ParamFile.NONE;
						
			String line=null;
			while ((line = br.readLine()) != null)
			{
				if (!line.trim().startsWith("#") && !line.trim().isEmpty()){ //# is a comment line
					
					String token=null;
					StringTokenizer st=null;
					
					switch (fileStatus)
					{
					case NONE: 
							st = new StringTokenizer(line);	
							token = st.nextToken();    							
							switch (token.toUpperCase()) 
							{
								case "PARAMS":
									fileStatus = ParamFile.PARAMS;								
									break;
								case "INSTANCES":
									fileStatus = ParamFile.INSTANCES;								
									break;
								case "EOF":								
									br.close();
									paramsFileReader.close();
									if (dataFileDir==null)
										throw new Exception("Data file directory not present!");
									if (outputFile==null)
										throw new Exception("Output file name not present!");									
									if (repetitions<0)
										throw new Exception("Repetitions number invalid or not present!");
									if (maxPopulationSize<0)
										throw new Exception("Max Population Size invalid or not present!");
									if (crossoverRate<0)
										throw new Exception("Crossover Rate invalid or not present!");
									if (mutationRate<0)
										throw new Exception("Mutation rate invalid or not present!");
									if (tournamentArity<0)
										throw new Exception("Tournament arity invalid or not present!");
									if (maxSeconds<0)
										throw new Exception("Max Seconds invalid or not present!");
									if (maxUnimprovedIterations<0)
										throw new Exception("Max Unimproved Iterations invalid or not present!");
									if (tsMaxiterations<0)
										throw new Exception("Max Tabu Search Iterations invalid or not present!");
									if (tabuTenure<0)
										throw new Exception("Tabu Tenure invalid or not present!");
									
									params=new Parameters(dataFileDir, outputFile, maxPopulationSize,
											crossoverRate, mutationRate, tournamentArity, seed, maxSeconds,
											maxUnimprovedIterations, repetitions, threadNumber, tsMaxiterations,
											tabuTenure, instances);
									return params;
								default:
									throw new Exception("Invalid parameters file format!");
							}
						break;
					case PARAMS:
						if (line.toUpperCase().contains("ENDPARAMS"))
							fileStatus = ParamFile.NONE;
						else
						{
							//Read the parameters
							st = new StringTokenizer(line);	
							token = st.nextToken();
							String nextToken=null;
							if (st.hasMoreTokens())
								nextToken=st.nextToken();
							else 
								throw new Exception("Empty field: "+token);
							
							if (token.equalsIgnoreCase("DataFileDir"))
								dataFileDir=nextToken;
							else if (token.equalsIgnoreCase("OutputCsv"))
								outputFile=nextToken;
							else if(token.equalsIgnoreCase("MaxPopulationSize"))
								maxPopulationSize=Integer.parseInt(nextToken);
							else if(token.equalsIgnoreCase("CrossoverRate"))
								crossoverRate=Double.parseDouble(nextToken);
							else if(token.equalsIgnoreCase("MutationRate"))
								mutationRate=Double.parseDouble(nextToken);
							else if(token.equalsIgnoreCase("TournamentArity"))
								tournamentArity=Integer.parseInt(nextToken);
							else if(token.equalsIgnoreCase("Seed"))
								seed=Integer.parseInt(nextToken);
							else if(token.equalsIgnoreCase("MaxSeconds"))
								maxSeconds=Integer.parseInt(nextToken);
							else if(token.equalsIgnoreCase("MaxUnimprovedIterations"))
								maxUnimprovedIterations=Integer.parseInt(nextToken);							
							else if(token.equalsIgnoreCase("Repetitions"))
								repetitions=Integer.parseInt(nextToken);
							else if(token.equalsIgnoreCase("ThreadNumber"))
								threadNumber=Integer.parseInt(nextToken);
							else if(token.equalsIgnoreCase("TSMaxIterations"))
								tsMaxiterations=Integer.parseInt(nextToken);
							else if(token.equalsIgnoreCase("TabuTenure"))
								tabuTenure=Integer.parseInt(nextToken);
							//else skip line							
						}
						break;
					case INSTANCES:
						if (line.toUpperCase().contains("ENDINSTANCES"))						
							fileStatus = ParamFile.NONE;
						else
							instances.add(line);				
						break;
					}//end switch
				}//end if
			}//end while	
			
			throw new Exception("Invalid parameters file format! EOF missing");
		}
		catch (Throwable t){
			
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {}
			if(paramsFileReader!=null)
				try {
					paramsFileReader.close();
				} catch (IOException e) {}
			
			System.err.println("The program ended with the error:\n" + t.getMessage());
			System.exit(1);
		}
		return null;	
	}//end readParamsFile
		
	@SuppressWarnings("resource")
	public static Instance readInstanceFile(String fileInstancePath) {
		double [][] customers=null;
		int index=0;
		int numCustomers=0;
		BufferedReader br=null;
		FileReader instanceFileReader=null;
		Instance instance=new Instance(fileInstancePath);
		
		try{
			if (fileInstancePath==null || fileInstancePath.equals(""))
				throw new Exception("Invalid file instance name!");			
			
			try {
				instanceFileReader=new FileReader(fileInstancePath);
			} catch (FileNotFoundException e) {
				throw new Exception(fileInstancePath+" not found!");				
			}
			
			br=new BufferedReader(instanceFileReader);
			String line=null;
			boolean coordinatesSection=false;
			
			while ((line = br.readLine()) != null)
			{
				StringTokenizer st=new StringTokenizer(line.trim());
				String token=st.nextToken();
				
				if(coordinatesSection){
					//read coordinates
					if(customers==null){
						throw new Exception("Invalid "+fileInstancePath+" format! Dimension missing");
					}
					token = st.nextToken();
					customers[index][0] = Double.parseDouble(token); 
					token = st.nextToken();
					customers[index][1] = Double.parseDouble(token);
					index++;
					if (index==numCustomers)
						coordinatesSection=false;
				}				
				else if(token.equalsIgnoreCase("NAME:"))
					instance.setName(st.nextToken());
				else if(token.equalsIgnoreCase("NAME")){
					st.nextToken(); //remove ':'
					instance.setName(st.nextToken());
				}
				else if(token.equalsIgnoreCase("TYPE:")){
					token=st.nextToken();
					if (!token.equalsIgnoreCase("TSP"))
						throw new Exception("Unsupported instance type: "+token);
					else
						instance.setType(token);
				}
				else if(token.equalsIgnoreCase("TYPE")){
					st.nextToken(); //remove ':'
					token=st.nextToken();
					
					if (!token.equalsIgnoreCase("TSP"))
						throw new Exception("Unsupported instance type: "+token);
					else
						instance.setType(token);
				}
				else if(token.equalsIgnoreCase("COMMENT:")){
					StringBuffer sb =new StringBuffer();
					while (st.hasMoreTokens())
						sb.append(st.nextToken()).append(" ");
					instance.setComment(sb.toString());
				}
				else if(token.equalsIgnoreCase("COMMENT")){
					st.nextToken(); //remove ':'
					StringBuffer sb =new StringBuffer();
					while (st.hasMoreTokens())
						sb.append(st.nextToken()).append(" ");
					instance.setComment(sb.toString());
				}
				else if(token.equalsIgnoreCase("DIMENSION:")){
					numCustomers=Integer.parseInt(st.nextToken());
					customers=new double[numCustomers][2];	
					instance.setDimension(numCustomers);
				}
				else if(token.equalsIgnoreCase("DIMENSION")){
					st.nextToken(); //remove ':'
					numCustomers=Integer.parseInt(st.nextToken());
					customers=new double[numCustomers][2];
					instance.setDimension(numCustomers);
				}
				else if(token.equalsIgnoreCase("EDGE_WEIGHT_TYPE:")){
					token=st.nextToken();
					if (!token.equalsIgnoreCase("EUC_2D"))
						throw new Exception("Unsupported edge weight type: "+token);
					else
						instance.setEdgeWeightTipe(token);					
				}
				else if(token.equalsIgnoreCase("EDGE_WEIGHT_TYPE")){
					st.nextToken(); //remove ':'
					token=st.nextToken();
					
					if (!token.equalsIgnoreCase("EUC_2D"))
						throw new Exception("Unsupported edge weight type: "+token);
					else
						instance.setEdgeWeightTipe(token);
				}
				else if (token.equalsIgnoreCase("NODE_COORD_SECTION"))
					coordinatesSection=true;
				else if (token.equalsIgnoreCase("EOF")){
					if(index!=numCustomers)
						throw new Exception("Invalid "+fileInstancePath+" format! Customers are less than declared");
					br.close();
					instanceFileReader.close();
					
					instance.setCustomers(customers);
					return instance;
				}
				//else skip line						
			}//end while
			throw new Exception("Invalid "+fileInstancePath+" format! EOF missing");
		}
		catch (Throwable t){
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {}
			if(instanceFileReader!=null)
				try {
					instanceFileReader.close();
				} catch (IOException e) {}
			
			System.err.println("The program ended with the error:\n" + t.getMessage());			
			System.exit(1);
		}	
		return null;
	}//end readInstanceFile
	
	/**
	 * Reads a tour file
	 * @return The tour representation with indexes starting from 0	
	 */
	@SuppressWarnings("resource")
	public static List<Integer> readTourFile(File tourFile) {
		List <Integer> tour=null;
		int index=0;
		int numCustomers=0;
		BufferedReader br=null;
		FileReader tourFileReader=null;
				
		try{
			if (tourFile==null)
				return null;			
			
			try {				
				tourFileReader=new FileReader(tourFile);
			} catch (FileNotFoundException e) {							
				return null;				
			}
			
			br=new BufferedReader(tourFileReader);
			String line=null;
			boolean tourSection=false;
			
			while ((line = br.readLine()) != null)
			{
				StringTokenizer st=new StringTokenizer(line.trim());
				String token=st.nextToken();
				
				if(tourSection){
					//read tour
					if(numCustomers==0)
						throw new Exception("Invalid "+tourFile.getName()+" format! Dimension missing or zero");		
										
					tour.add(Integer.parseInt(token)-1);
					index++;
					if (index==numCustomers)
						tourSection=false;
					
					while (st.hasMoreTokens() && tourSection){
						token = st.nextToken();
						tour.add(Integer.parseInt(token)-1);
						index++;
						if (index==numCustomers)
							tourSection=false;
					}//end while					
				}				
				else if(token.equalsIgnoreCase("NAME:")){
					token=st.nextToken();
					if(!token.equalsIgnoreCase(tourFile.getName()))
						throw new Exception("Tour file name "+tourFile.getName()+" does not match with the NAME field: "+token);
				}
				else if(token.equalsIgnoreCase("NAME")){
					st.nextToken(); //remove ':'
					token=st.nextToken();
					if(!token.equalsIgnoreCase(tourFile.getName()))
						throw new Exception("Tour file name "+tourFile.getName()+" does not match with the NAME field: "+token);
				}
				else if(token.equalsIgnoreCase("TYPE:")){
					token=st.nextToken();
					if (!token.equalsIgnoreCase("TOUR"))
						throw new Exception("Tour file "+tourFile.getName()+" has a type: "+token+" different from TOUR");					
				}
				else if(token.equalsIgnoreCase("TYPE")){
					st.nextToken(); //remove ':'
					token=st.nextToken();
					if (!token.equalsIgnoreCase("TOUR"))
						throw new Exception("Tour file "+tourFile.getName()+" has a type: "+token+" different from TOUR");
				}
				else if(token.equalsIgnoreCase("COMMENT:")){					
					//do nothing
				}
				else if(token.equalsIgnoreCase("COMMENT")){
					//do nothing
				}
				else if(token.equalsIgnoreCase("DIMENSION:")){
					numCustomers=Integer.parseInt(st.nextToken());
					tour=new ArrayList<Integer>(numCustomers);					
				}
				else if(token.equalsIgnoreCase("DIMENSION")){
					st.nextToken(); //remove ':'
					numCustomers=Integer.parseInt(st.nextToken());
					tour=new ArrayList<Integer>(numCustomers);
				}				
				else if (token.equalsIgnoreCase("TOUR_SECTION"))
					tourSection=true;
				else if (token.equalsIgnoreCase("EOF")){
					if(index!=numCustomers)
						throw new Exception("Invalid "+tourFile.getName()+" format! Customers are less than declared");
					br.close();
					tourFileReader.close();
										
					return tour;
				}
				//else skip line						
			}//end while
			throw new Exception("Invalid "+tourFile.getName()+" format! EOF missing");
		}
		catch (Throwable t){
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {}
			if(tourFileReader!=null)
				try {
					tourFileReader.close();
				} catch (IOException e) {}
			
			System.err.println("WARNING:\n"
					+ t.getMessage()+"\n"
					+"The best known value can be wrong!");			
			return null;
		}	
	}//end readTourFile
}
