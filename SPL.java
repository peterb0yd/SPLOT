package jmetal.problems;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.PermutationSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

public class SPL extends Problem {

	public SPL(String solutionType) {
		numberOfVariables_ = 2;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;
		problemName_ = "SPL";
		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];
	      
		if(solutionType.compareTo("Permutation")==0)
			solutionType_ = new PermutationSolutionType(this);
		else if (solutionType.compareTo("BinaryReal") == 0)
	    	solutionType_ = new BinaryRealSolutionType(this) ;
	    else if (solutionType.compareTo("Real") == 0)
	    	solutionType_ = new RealSolutionType(this) ;
	    else {
	    	System.out.println("Error: solution type " + solutionType + " invalid") ;
	    	System.exit(-1) ;
	    }  
	}

	  public void evaluate(Solution solution) throws JMException {
		   Variable[] variable = solution.getDecisionVariables();
		    
		    double [] f = new double[numberOfObjectives_];
		    f[0] = variable[0].getValue();
		    f[1] = variable[1].getValue();
		       
		    solution.setObjective(0,f[0]);
		    solution.setObjective(1,f[1]);
		  } // evaluate
	  
	  public void evaluateConstraints(Solution solution) throws JMException {
	  }
}
