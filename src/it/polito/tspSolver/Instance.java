package it.polito.tspSolver;

import java.util.ArrayList;
import java.util.List;

/**
 * A bean class for instances
 * @author Amedeo Sapio (amedeo.sapio@gmail.com) 
 */
public class Instance {

	private String _name;
	private String _fileName;
	private String _comment;
	private String _type;
	private int _dimension;
	private String _edgeWeightTipe;
	private double _bestKnownSolution;
	private double _meanSolution;
	private double _minSolution;
	private double _maxSolution;
	private long _timeBest;
	private long _timeMean;
	private double[][] _customers;
	private List<Integer> _optTour;
	
	public Instance(String filename){
		_fileName=filename;
		_name="unknown";
		_comment="unknown";
		_type="unknown";
		_dimension=0;
		_edgeWeightTipe="unknown";
		_bestKnownSolution=0;
		_meanSolution=0;
		_minSolution=0;
		_maxSolution=0;
		_timeBest=0;
		_timeMean=0;
		_customers=new double[][]{{0,0}};
		_optTour=new ArrayList<Integer>();
	}
	public String getName() {
		return _name;
	}
	public String getFileName() {
		return _fileName;
	}
	public void setName(String name) {
		this._name = name;
	}
	public String getComment() {
		return _comment;
	}
	public void setComment(String comment) {
		this._comment = comment;
	}
	public String getType() {
		return _type;
	}
	public void setType(String type) {
		this._type = type;
	}
	public int getDimension() {
		return _dimension;
	}
	public void setDimension(int dimension) {
		this._dimension = dimension;
	}
	public String getEdgeWeightTipe() {
		return _edgeWeightTipe;
	}
	public void setEdgeWeightTipe(String edgeWeightTipe) {
		this._edgeWeightTipe = edgeWeightTipe;
	}
	public double getBestKnownSolution() {
		if(_bestKnownSolution==0 && !_name.equalsIgnoreCase("unknown")){
			
			//values for TSPLib
			//from http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/STSP.html
			if(_name.equalsIgnoreCase("a280"))
				_bestKnownSolution=2579;
			else if(_name.equalsIgnoreCase("ali535")) 
				_bestKnownSolution=202339;
			else if(_name.equalsIgnoreCase("att48"))
				_bestKnownSolution=10628;
			else if(_name.equalsIgnoreCase("att532")) 
				_bestKnownSolution=27686;
			else if(_name.equalsIgnoreCase("bayg29"))
				_bestKnownSolution=1610;
			else if(_name.equalsIgnoreCase("bays29"))
				_bestKnownSolution=2020;
			else if(_name.equalsIgnoreCase("berlin52"))
				_bestKnownSolution=7542;
			else if(_name.equalsIgnoreCase("bier127"))
				_bestKnownSolution=118282;
			else if(_name.equalsIgnoreCase("brazil58")) 
				_bestKnownSolution=25395;
			else if(_name.equalsIgnoreCase("brd14051"))
				_bestKnownSolution=469385;
			else if(_name.equalsIgnoreCase("brg180"))
				_bestKnownSolution=1950;
			else if(_name.equalsIgnoreCase("burma14")) 
				_bestKnownSolution=3323;
			else if(_name.equalsIgnoreCase("ch130")) 
				_bestKnownSolution=6110;
			else if(_name.equalsIgnoreCase("ch150")) 
				_bestKnownSolution=6528;
			else if(_name.equalsIgnoreCase("d198")) 
				_bestKnownSolution=15780;
			else if(_name.equalsIgnoreCase("d493"))
				_bestKnownSolution=35002;
			else if(_name.equalsIgnoreCase("d657"))
				_bestKnownSolution=48912;
			else if(_name.equalsIgnoreCase("d1291")) 
				_bestKnownSolution=50801;
			else if(_name.equalsIgnoreCase("d1655"))
				_bestKnownSolution=62128;
			else if(_name.equalsIgnoreCase("d2103"))
				_bestKnownSolution=80450;
			else if(_name.equalsIgnoreCase("d15112")) 
				_bestKnownSolution=1573084;
			else if(_name.equalsIgnoreCase("d18512"))
				_bestKnownSolution=645238;
			else if(_name.equalsIgnoreCase("dantzig42")) 
				_bestKnownSolution=699;
			else if(_name.equalsIgnoreCase("dsj1000") && _edgeWeightTipe.equalsIgnoreCase("EUC_2D")) 
				_bestKnownSolution=18659688; 
			else if(_name.equalsIgnoreCase("dsj1000") && _edgeWeightTipe.equalsIgnoreCase("CEIL_2D")) 
				_bestKnownSolution=18660188; 
			else if(_name.equalsIgnoreCase("eil51")) 
				_bestKnownSolution=426;
			else if(_name.equalsIgnoreCase("eil76"))
				_bestKnownSolution=538;
			else if(_name.equalsIgnoreCase("eil101")) 
				_bestKnownSolution=629;
			else if(_name.equalsIgnoreCase("fl417"))
				_bestKnownSolution=11861;
			else if(_name.equalsIgnoreCase("fl1400")) 
				_bestKnownSolution=20127;
			else if(_name.equalsIgnoreCase("fl1577")) 
				_bestKnownSolution=22249;
			else if(_name.equalsIgnoreCase("fl3795")) 
				_bestKnownSolution=28772;
			else if(_name.equalsIgnoreCase("fnl4461")) 
				_bestKnownSolution=182566;
			else if(_name.equalsIgnoreCase("fri26"))
				_bestKnownSolution=937;
			else if(_name.equalsIgnoreCase("gil262")) 
				_bestKnownSolution=2378;
			else if(_name.equalsIgnoreCase("gr17"))
				_bestKnownSolution=2085;
			else if(_name.equalsIgnoreCase("gr21")) 
				_bestKnownSolution=2707;
			else if(_name.equalsIgnoreCase("gr24")) 
				_bestKnownSolution=1272;
			else if(_name.equalsIgnoreCase("gr48")) 
				_bestKnownSolution=5046;
			else if(_name.equalsIgnoreCase("gr96"))
				_bestKnownSolution=55209;
			else if(_name.equalsIgnoreCase("gr120")) 
				_bestKnownSolution=6942;
			else if(_name.equalsIgnoreCase("gr137")) 
				_bestKnownSolution=69853;
			else if(_name.equalsIgnoreCase("gr202")) 
				_bestKnownSolution=40160;
			else if(_name.equalsIgnoreCase("gr229")) 
				_bestKnownSolution=134602;
			else if(_name.equalsIgnoreCase("gr431")) 
				_bestKnownSolution=171414;
			else if(_name.equalsIgnoreCase("gr666")) 
				_bestKnownSolution=294358;
			else if(_name.equalsIgnoreCase("hk48"))
				_bestKnownSolution=11461;
			else if(_name.equalsIgnoreCase("kroA100")) 
				_bestKnownSolution=21282;
			else if(_name.equalsIgnoreCase("kroB100")) 
				_bestKnownSolution=22141;
			else if(_name.equalsIgnoreCase("kroC100")) 
				_bestKnownSolution=20749;
			else if(_name.equalsIgnoreCase("kroD100")) 
				_bestKnownSolution=21294;
			else if(_name.equalsIgnoreCase("kroE100")) 
				_bestKnownSolution=22068;
			else if(_name.equalsIgnoreCase("kroA150")) 
				_bestKnownSolution=26524;
			else if(_name.equalsIgnoreCase("kroB150")) 
				_bestKnownSolution=26130;
			else if(_name.equalsIgnoreCase("kroA200")) 
				_bestKnownSolution=29368;
			else if(_name.equalsIgnoreCase("kroB200")) 
				_bestKnownSolution=29437;
			else if(_name.equalsIgnoreCase("lin105")) 
				_bestKnownSolution=14379;
			else if(_name.equalsIgnoreCase("lin318"))
				_bestKnownSolution=42029;
			else if(_name.equalsIgnoreCase("linhp318")) 
				_bestKnownSolution=41345;
			else if(_name.equalsIgnoreCase("nrw1379")) 
				_bestKnownSolution=56638;
			else if(_name.equalsIgnoreCase("p654"))
				_bestKnownSolution=34643;
			else if(_name.equalsIgnoreCase("pa561"))
				_bestKnownSolution=2763;
			else if(_name.equalsIgnoreCase("pcb442"))
				_bestKnownSolution=50778;
			else if(_name.equalsIgnoreCase("pcb1173")) 
				_bestKnownSolution=56892;
			else if(_name.equalsIgnoreCase("pcb3038")) 
				_bestKnownSolution=137694;
			else if(_name.equalsIgnoreCase("pla7397"))
				_bestKnownSolution=23260728;
			else if(_name.equalsIgnoreCase("pla33810"))
				_bestKnownSolution=66048945;
			else if(_name.equalsIgnoreCase("pla85900"))
				_bestKnownSolution=142382641;
			else if(_name.equalsIgnoreCase("pr76"))
				_bestKnownSolution=108159;
			else if(_name.equalsIgnoreCase("pr107")) 
				_bestKnownSolution=44303;
			else if(_name.equalsIgnoreCase("pr124"))
				_bestKnownSolution=59030;
			else if(_name.equalsIgnoreCase("pr136")) 
				_bestKnownSolution=96772;
			else if(_name.equalsIgnoreCase("pr144"))
				_bestKnownSolution=58537;
			else if(_name.equalsIgnoreCase("pr152")) 
				_bestKnownSolution=73682;
			else if(_name.equalsIgnoreCase("pr226")) 
				_bestKnownSolution=80369;
			else if(_name.equalsIgnoreCase("pr264")) 
				_bestKnownSolution=49135;
			else if(_name.equalsIgnoreCase("pr299")) 
				_bestKnownSolution=48191;
			else if(_name.equalsIgnoreCase("pr439"))
				_bestKnownSolution=107217;
			else if(_name.equalsIgnoreCase("pr1002")) 
				_bestKnownSolution=259045;
			else if(_name.equalsIgnoreCase("pr2392")) 
				_bestKnownSolution=378032;
			else if(_name.equalsIgnoreCase("rat99"))
				_bestKnownSolution=1211;
			else if(_name.equalsIgnoreCase("rat195")) 
				_bestKnownSolution=2323;
			else if(_name.equalsIgnoreCase("rat575"))
				_bestKnownSolution=6773;
			else if(_name.equalsIgnoreCase("rat783")) 
				_bestKnownSolution=8806;
			else if(_name.equalsIgnoreCase("rd100")) 
				_bestKnownSolution=7910;
			else if(_name.equalsIgnoreCase("rd400"))
				_bestKnownSolution=15281;
			else if(_name.equalsIgnoreCase("rl1304")) 
				_bestKnownSolution=252948;
			else if(_name.equalsIgnoreCase("rl1323"))
				_bestKnownSolution=270199;
			else if(_name.equalsIgnoreCase("rl1889")) 
				_bestKnownSolution=316536;
			else if(_name.equalsIgnoreCase("rl5915")) 
				_bestKnownSolution=565530;
			else if(_name.equalsIgnoreCase("rl5934"))
				_bestKnownSolution=556045;
			else if(_name.equalsIgnoreCase("rl11849")) 
				_bestKnownSolution=923288;
			else if(_name.equalsIgnoreCase("si175")) 
				_bestKnownSolution=21407;
			else if(_name.equalsIgnoreCase("si535"))
				_bestKnownSolution=48450;
			else if(_name.equalsIgnoreCase("si1032")) 
				_bestKnownSolution=92650;
			else if(_name.equalsIgnoreCase("st70"))
				_bestKnownSolution=675;
			else if(_name.equalsIgnoreCase("swiss42")) 
				_bestKnownSolution=1273;
			else if(_name.equalsIgnoreCase("ts225"))
				_bestKnownSolution=126643;
			else if(_name.equalsIgnoreCase("tsp225")) 
				_bestKnownSolution=3916;
			else if(_name.equalsIgnoreCase("u159")) 
				_bestKnownSolution=42080;
			else if(_name.equalsIgnoreCase("u574")) 
				_bestKnownSolution=36905;
			else if(_name.equalsIgnoreCase("u724"))
				_bestKnownSolution=41910;
			else if(_name.equalsIgnoreCase("u1060")) 
				_bestKnownSolution=224094;
			else if(_name.equalsIgnoreCase("u1432"))
				_bestKnownSolution=152970;
			else if(_name.equalsIgnoreCase("u1817")) 
				_bestKnownSolution=57201;
			else if(_name.equalsIgnoreCase("u2152")) 
				_bestKnownSolution=64253;
			else if(_name.equalsIgnoreCase("u2319"))
				_bestKnownSolution=234256;
			else if(_name.equalsIgnoreCase("ulysses16")) 
				_bestKnownSolution=6859;
			else if(_name.equalsIgnoreCase("ulysses22")) 
				_bestKnownSolution=7013;
			else if(_name.equalsIgnoreCase("usa13509")) 
				_bestKnownSolution=19982859;
			else if(_name.equalsIgnoreCase("vm1084")) 
				_bestKnownSolution=239297;
			else if(_name.equalsIgnoreCase("vm1748")) 
				_bestKnownSolution=336556;
		}
		
		return _bestKnownSolution;
	}
	public void setBestKnownSolution(double bestKnownSolution) {
		this._bestKnownSolution = bestKnownSolution;
	}
	public double getMeanSolution() {
		return _meanSolution;
	}
	public void setMeanSolution(double meanSolution) {
		this._meanSolution = meanSolution;
	}
	public double getMinSolution() {
		return _minSolution;
	}
	public void setMinSolution(double minSolution) {
		this._minSolution = minSolution;
	}
	public double getMaxSolution() {
		return _maxSolution;
	}
	public void setMaxSolution(double maxSolution) {
		this._maxSolution = maxSolution;
	}
	
	/**	   
	 * @return the time in seconds spent by the iteration that gave the best solution 
	 */
	public double getTimeBest() {
		return Math.floor((double)(_timeBest)/10000000)/100;
	}
	/**
	 * @param timeBest the time in nanoseconds spent by the iteration that gave the best solution
	 */
	public void setTimeBest(long timeBest) {
		this._timeBest = timeBest;
	}
	
	/**	   
	 * @return the mean time spent in seconds 
	 */
	public double getTimeMean() {
		return Math.floor((double)(_timeMean)/10000000)/100;
	}
	
	/** 
	 * @param timeMean the mean time in nanoseconds
	 */
	public void setTimeMean(long timeMean) {
		this._timeMean = timeMean;
	}
	public double[][] getCustomers() {
		return _customers;
	}
	public void setCustomers(double[][] customers) {
		this._customers = customers;
	}
	
	/**
	 * @return the optimal tour with indexes starting from 1
	 */
	public List<Integer> getOptTour() {
		return _optTour;
	}
	
	/**
	 * Sets the optimal tour translating the indexes (starting from 1 instead of 0)
	 * @param optTour the optimal tour
	 */
	public void setOptTour(List<Integer> optTour) {
		//translate indexes starting from 1 instead of 0
		this._optTour=new ArrayList<Integer>(optTour.size());
		for (Integer elem: optTour)
			this._optTour.add(new Integer(elem.intValue()+1));
	}
}

