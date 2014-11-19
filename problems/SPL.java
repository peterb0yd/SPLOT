package jmetal.problems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.minisat.core.Solver;
import org.sat4j.minisat.orders.RandomLiteralSelectionStrategy;
import org.sat4j.minisat.orders.RandomWalkDecorator;
import org.sat4j.minisat.orders.VarOrderHeap;
import org.sat4j.reader.DimacsReader;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVecInt;
import org.sat4j.tools.ModelIterator;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.PermutationSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.metaheuristics.splot.Individual;
import jmetal.metaheuristics.splot.TSet;
import jmetal.util.JMException;

public class SPL extends Problem {
	
	HashMap<Integer, Integer> costs;
	private int maxProductCost = 0;
	private int minProds = 1;
	private int maxIndSize = 100;
	private double maxPairs;
	private String file_FM;
	private String file_cost;
	double o1 = 0;
	double o2 = 0;
	double o3 = 0;

	public SPL(String solutionType,
				String file_FM,
				String file_cost) {
		this.file_FM = file_FM;
		this.file_cost = file_cost;
		numberOfVariables_ = 2;
		numberOfObjectives_ = 3;
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
		
		maxPairs = 50;
        maxProductCost = 0;
        for (Integer i : costs.keySet()) {
            maxProductCost += costs.get(i);
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
		    try {
		    
		        List<Individual> indivs = new ArrayList<Individual>();
		        double totalFitness = 0;

		        /** Get Objectives **/
		        Random r = new Random();
		        for (int i = 1; i <= 100; i++) {
		            int size = r.nextInt(maxIndSize - 1) + minProds;
		            List<IVecInt> prods = getDissimilarConfigs(size, file_cost);
		            Individual indiv = new Individual(prods);
		            evaluateFitness(indiv, solution);
		        }
		        
		    } catch (Exception e) {
		    	System.out.println(e.getMessage());
		    }
		       
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
	    
	    public List<IVecInt> getDissimilarConfigs(int count, String dimacsFM) throws Exception {

	        ISolver dimacsSolver = SolverFactory.instance().createSolverByName("MiniSAT");
	        DimacsReader dr = new DimacsReader(dimacsSolver);
	        dr.parseInstance(new FileReader(dimacsFM));
	        Solver solver = (Solver) dimacsSolver;
	        solver.setTimeout(1000);
	        solver.setOrder(new RandomWalkDecorator(new VarOrderHeap(new RandomLiteralSelectionStrategy()), 1));
	        ISolver solverIterator = new ModelIterator(solver);
	        solverIterator.setTimeoutMs(150000);

	        List<IVecInt> products = new ArrayList<IVecInt>(count);


	        while (products.size() < count) {

	            try {
	                if (solverIterator.isSatisfiable()) {

	                    int[] vec = solverIterator.model();

	                    IVecInt vect = toVec(vec);

	                    if (!products.contains(vect)) {
	                        products.add(vect);
	                    }


	                } else {
	                    dimacsSolver = SolverFactory.instance().createSolverByName("MiniSAT");
	                    dr = new DimacsReader(dimacsSolver);
	                    dr.parseInstance(new FileReader(dimacsFM));
	                    solver = (Solver) dimacsSolver;
	                    solver.setTimeout(1000);
	                    solver.setOrder(new RandomWalkDecorator(new VarOrderHeap(new RandomLiteralSelectionStrategy()), 1));

	                    solverIterator = new ModelIterator(solver);
	                    solverIterator.setTimeoutMs(150000);
	                }

	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        return products;
	    }
	    
	    public IVecInt toVec(int[] vec) {
	        IVecInt vect = new VecInt(vec.length);
	        for (int i = 0; i < vec.length; i++) {
	            vect.push(vec[i]);

	        }
	        return vect;
	    }
	    
	    public void evaluateFitness(Individual indiv, Solution solution) {
	        List<IVecInt> products = indiv.getProds();
	        double n = products.size();
	        Set<TSet> pairs = new HashSet<TSet>();
	        List<IVecInt> prods = indiv.getProds();
	        for (IVecInt v : prods) {

	            for (int i = 0; i < v.size(); i++) {
	                for (int j = 0; j < v.size(); j++) {
	                    if (j > i) {
	                        pairs.add(new TSet(new int[]{v.get(i), v.get(j)}));
	                    }

	                }

	            }
	        }

	        int nf = products.get(0).size();

	        o1 = (-pairs.size() + maxPairs) / (-(nf * (nf - 1) / 2) + maxPairs);

	        o2 = (n - minProds) / (maxIndSize - minProds);
	        double cost = getCost(products);


	        double maxCost = maxProductCost * n;

	        o3 = (double) cost / maxCost;
	        
		    /** Set Objectives **/
		    solution.setObjective(0,o1);
		    solution.setObjective(1,o2);
		    solution.setObjective(2,o3);

	    }
	    
	    public int getCost(List<IVecInt> products) {
	        int cost = 0;
	        for (IVecInt p : products) {
	            for (int i = 0; i < p.size(); i++) {
	                if (p.get(i) > 0) {

	                    cost += costs.get(p.get(i)) == null ? 0 : costs.get(p.get(i));
	                }

	            }
	        }
	        return cost;
	    }
}
