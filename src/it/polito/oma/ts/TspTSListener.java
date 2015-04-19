package it.polito.oma.ts;

import org.coinor.opents.*;

public class TspTSListener extends TabuSearchAdapter{

	public int MAX_TENURE = GlobalData.numCustomers/2;

    public void newBestSolutionFound( TabuSearchEvent evt )
    {   
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	Solution   best  = theTS.getBestSolution();
    	SimpleTabuList mytl;

    	mytl = (SimpleTabuList)theTS.getTabuList();
    	mytl.setTenure( Math.max( 7, (int)( 0.75 * mytl.getTenure() ) ) );
//        System.out.println("Decrease tenure to " + mytl.getTenure());

        final String msg = "New Best solution found at iteration " + theTS.getIterationsCompleted() + "\n Done. Best solution: " + best;
    	System.out.println(msg);
    }

    public void unimprovingMoveMade( TabuSearchEvent evt )
    {   // Increase tenure
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	SimpleTabuList mytl;

    	mytl = (SimpleTabuList)theTS.getTabuList();

    	mytl.setTenure( Math.min( MAX_TENURE, mytl.getTenure() + 2 ));
 //       System.out.println("Increase tenure to " + mytl.getTenure());
  }

    // We're not using these events
    public void newCurrentSolutionFound( TabuSearchEvent evt ){}
    public void tabuSearchStarted( TabuSearchEvent evt ){}
    public void tabuSearchStopped( TabuSearchEvent evt ){}
}