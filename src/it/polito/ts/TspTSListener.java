package it.polito.ts;

import java.util.HashSet;

import org.coinor.opents.SimpleTabuList;
import org.coinor.opents.TabuSearch;
import org.coinor.opents.TabuSearchAdapter;
import org.coinor.opents.TabuSearchEvent;

/**
 * 
 * @author Amedeo Sapio (amedeo.sapio@gmail.com)
 *
 */
public class TspTSListener extends TabuSearchAdapter{

	private static final long serialVersionUID = 1L;
	
	private int _maxTenure;
	
	private int _decreaseThreshold;
	
	private int _noRepetitionsCounter;
	
	
	private HashSet<Integer> _solutionsTable;
	
	/**
	 * Private constructor without parameters, in order to force to set parameters.
	 */
	@SuppressWarnings("unused")
	private TspTSListener(){
		
	}
	
	public TspTSListener(int maxTenure, int decreaseThreshold){
		
		this._maxTenure = maxTenure;
		this._decreaseThreshold=decreaseThreshold;
		
		this._solutionsTable = new HashSet<Integer>();
		_noRepetitionsCounter=0;
	}
	    
    public void newCurrentSolutionFound( TabuSearchEvent evt ){
    	
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	
    	if(!(theTS.getCurrentSolution() instanceof TspSolution))
    		throw new IllegalArgumentException("TspTSListener works on TspSolution only");
    	
    	if(!(theTS.getTabuList() instanceof SimpleTabuList))
    		throw new IllegalArgumentException("TspTSListener works on SimpleTabuList only");
    	
    	TspSolution currentSolution = (TspSolution)theTS.getCurrentSolution();
    	
    	SimpleTabuList mytl = (SimpleTabuList)theTS.getTabuList();
    	
    	Integer currentHash = new Integer(currentSolution.hashCode());
    	    	
    	if(_solutionsTable.contains(currentHash)){
    		
    		// repetition, increase the tabu tenure
    		
    		int T=mytl.getTenure();
    		    		
    		mytl.setTenure( Math.min(Math.max((int)(T * 1.1),T+1),_maxTenure));
    		
    	}else{
    		
    		// no repetition
    		_solutionsTable.add(currentHash);
    		    		
    		_noRepetitionsCounter++;
    		    		
    		if (_noRepetitionsCounter==_decreaseThreshold){
    			
    			_noRepetitionsCounter=0;
    			
    			//decrese tabu tenure    			
    			mytl.setTenure(mytl.getTenure()-1);
    			
    		}    		
    	}    	
    }
    
    public void tabuSearchStarted( TabuSearchEvent evt ){}
    public void tabuSearchStopped( TabuSearchEvent evt ){}
    public void newBestSolutionFound( TabuSearchEvent evt ) {}
    public void unimprovingMoveMade( TabuSearchEvent evt ) {}

}
