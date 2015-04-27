package it.polito.tspSolver;

import java.util.List;

/**
 * A bean class for parameters
 * @author Amedeo Sapio (amedeo.sapio@gmail.com) 
 */

public class Parameters {
	
	private String _dataFileDir;
	private int _maxPopulationSize;
	private double _crossoverRate;
	private double _mutationRate;
	private int _tournamentArity;
	private int _seed;
	private int _maxSeconds;
	private int _maxUnimprovedIterations;
	private int _repetitions;
	private int _threadNumber;
	private int _tsMaxIterations;
	private int _tabuTenure;	
	private List<String> _instances;
	private String _outputFile;
	
	public Parameters(String dataFileDir, String outputFile, int maxPopulationSize, double crossoverRate,
			double mutationRate, int tournamentArity, int seed, int maxSeconds,
			int maxUninmprovedIterations, int repetitions, int threadNumber, int tsMaxIterations, int tabuTenure,
			List<String> instances) {
		super();
		this._dataFileDir = dataFileDir;
		this._maxPopulationSize = maxPopulationSize;
		this._crossoverRate = crossoverRate;
		this._mutationRate = mutationRate;
		this._tournamentArity = tournamentArity;
		this._seed = seed;
		this._maxSeconds = maxSeconds;
		this._maxUnimprovedIterations = maxUninmprovedIterations;
		this._repetitions = repetitions;
		this._instances = instances;
		this._outputFile = outputFile;
		this._threadNumber = threadNumber;
		this._tsMaxIterations = tsMaxIterations;
		this._tabuTenure = tabuTenure;
	}

	public String getDataFileDir() {
		return _dataFileDir;
	}

	public void setDataFileDir(String dataFileDir) {
		this._dataFileDir = dataFileDir;
	}

	public int getMaxPopulationSize() {
		return _maxPopulationSize;
	}

	public void setMaxPopulationSize(int maxPopulationSize) {
		this._maxPopulationSize = maxPopulationSize;
	}

	public double getCrossoverRate() {
		return _crossoverRate;
	}

	public void setCrossoverRate(double crossoverRate) {
		this._crossoverRate = crossoverRate;
	}

	public double getMutationRate() {
		return _mutationRate;
	}

	public void setMutationRate(double mutationRate) {
		this._mutationRate = mutationRate;
	}

	public int getTournamentArity() {
		return _tournamentArity;
	}

	public void setTournamentArity(int tournamentArity) {
		this._tournamentArity = tournamentArity;
	}

	public int getSeed() {
		return _seed;
	}

	public void setSeed(int seed) {
		this._seed = seed;
	}

	public int getMaxSeconds() {
		return _maxSeconds;
	}

	public void setMaxSeconds(int maxSeconds) {
		this._maxSeconds = maxSeconds;
	}

	public int getMaxUninmprovedIterations() {
		return _maxUnimprovedIterations;
	}

	public void setMaxUninmprovedIterations(int maxUninmprovedIterations) {
		this._maxUnimprovedIterations = maxUninmprovedIterations;
	}

	public int getRepetitions() {
		return _repetitions;
	}

	public void setRepetitions(int repetitions) {
		this._repetitions = repetitions;
	}
	
	public int getThreadNumber() {
		return _threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this._threadNumber = threadNumber;
	}
	
	public int getTSMaxIterations() {
		return _tsMaxIterations;
	}

	public void setTSMaxIterations(int tsMaxIterations) {
		this._tsMaxIterations = tsMaxIterations;
	}

	public int getTabuTenure() {
		return _tabuTenure;
	}

	public void setTabuTenure(int tabuTenure) {
		this._tabuTenure = tabuTenure;
	}

	public List<String> getInstances() {
		return _instances;
	}

	public void setInstances(List<String> instances) {
		this._instances = instances;
	}

	public String getOutputFile() {
		return _outputFile;
	}

	public void setOutputFile(String outputFile) {
		this._outputFile = outputFile;
	}
}
