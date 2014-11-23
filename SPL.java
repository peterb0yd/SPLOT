package jmetal.problems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.PermutationSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

public class SPL extends Problem {
	
	HashMap<Integer, Integer> costs;

	public SPL(String solutionType,
				String file_FM,
				String file_cost) {
		numberOfVariables_ = 2;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;
		problemName_ = "SPL";
		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];
		
		try {
			costs = loadCost(file_cost);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
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
		    f[2] = variable[2].getValue();
		       
		    solution.setObjective(0,f[0]);
		    solution.setObjective(1,f[1]);
		    solution.setObjective(2,f[2]);
		  } // evaluate
	  

	    public HashMap<Integer, Integer> loadCost(String filename) throws Exception {
	        BufferedReader in = new BufferedReader(new FileReader(filename));
	        HashMap<Integer, Integer> costs = new HashMap<Integer, Integer>();
	        String line;
	        while ((line = in.readLine()) != null) {
	            StringTokenizer st = new StringTokenizer(line, ":");
	            costs.put(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
	        }
	        in.close();
	        return costs;

	    }
}
