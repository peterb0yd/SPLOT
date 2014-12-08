package jmetal.problems;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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

	private String file_FM;
	private String file_cost;

	private int[][] products;
	private int numberOfFeatures = 0;
	private List<int[]> productLine = new LinkedList<int[]>();
	private HashMap<Integer, Integer> costs;
	
	private ListIterator productListIterator;
	
	// Weights
	float fitness = 0;
	float w1 = 0.5f;
	float w2 = 0.25f;
	float w3 = 0.25f;

	public SPL(String file_FM, String file_cost, int amount, int numberOfFeatures, int productsInProductLine) {
		this.file_FM = file_FM;
		this.file_cost = file_cost;
		this.numberOfFeatures = numberOfFeatures;
		products = new int[amount][numberOfFeatures];
		numberOfObjectives_ = 3;
		numberOfVariables_ = productsInProductLine; // Change this for testing
		lowerLimit_ = new double[numberOfVariables_];
		upperLimit_ = new double[numberOfVariables_];
		for (int var = 0; var < numberOfVariables_; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 2000.0;
		} 

		solutionType_ = new RealSolutionType(this);
		
	}

	public void evaluate(Solution solution) throws JMException {
		try {

			// Read "productsCreated" file to obtain products
			BufferedReader br = new BufferedReader(new FileReader(file_FM));
			String line;
			String[] line_string = new String[numberOfFeatures];
			int line_count = 0;

			// Set each line of file to int array - represents each product
			while ((line = br.readLine()) != null) {
				line_string = line.toString().replace(",", "").split(" ");
				int[] product = new int[numberOfFeatures];
				for (int i = 0; i < numberOfFeatures; i++) {
					product[i] = Integer.parseInt(line_string[i]);
				}
				products[line_count] = product; // add product to product array
				line_count++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Add chosen products to product line (list of products)
		Variable[] variables = solution.getDecisionVariables();
		int variable_value = 0;
		for (int i = 0; i < variables.length; i++) {
			if ((int) variables[i].getValue() < 1000) {
				variable_value = (int) variables[i].getValue();
				productLine.add(products[variable_value]);
			}
		}
		
		try {
			
			// Remove duplicate products in product line
			productLine = removeDuplicates(productLine);
			
			// Get costs for each feature
			costs = loadCost(file_cost);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// Find all features tested in product line and assign costs
		int productTotalCost = 0;
		boolean[] featuresUsed = new boolean[numberOfFeatures];
		for (int i = 0; i < productLine.size(); i++) {
			for (int j = 0; j < productLine.get(i).length; j++) {
				if (productLine.get(i)[j] > 0) {
					featuresUsed[j] = true;			// assign used feature to true
					if (costs.containsKey(j)) 		
						productTotalCost += costs.get(j);	// sum of costs
				}
			}
		}

		// Count number of features tested
		int features_used = 0; // objective 1
		for (int i = 0; i < numberOfFeatures; i++) {
			if (featuresUsed[i]) {
				features_used++;
			}
		}

		/*
		 * OBJECTIVE 1: We want to maximize features used. We need to convert
		 * this to a minimization problem for jMetal. Therefore, we can subtract
		 * the features used by the total number of features. This gives us the
		 * number of features that aren't tested in the product line. We set
		 * this value as our first objective.
		 */
		int obj1 = numberOfFeatures - features_used;

		/*
		 * OBJECTIVE 2: We want to minimize the number of products tested. This
		 * will be equivalent to the size of our product line list. We set this
		 * value as our second objective.
		 */
		int obj2 = productLine.size();
		
		/*
		 * OBJECTIVE 3: We want to minimize the total cost of each product. 
		 * This was calculated by finding all of the features tested and
		 * summing the cost for each of them. We se this value as our 
		 * third objective.
		 */
		int obj3 = productTotalCost;
		
		System.out.println("features not used: " + obj1
				+ "  |  products tested: " + obj2
				+ "  |  product cost: " + obj3 + "\n");
		
		fitness = obj1 * w1 + obj2 * w2 + obj3 * w3;

		solution.setObjective(0, obj1);
		solution.setObjective(1, obj2);
		solution.setObjective(2, obj3);
		solution.setFitness(fitness);

		productLine.clear();
	} 

	
	/** This function checks a list of products for duplicates
	 * @param products - list of all products
	 * @throws IOException
	 */
	public List<int[]> removeDuplicates(List<int[]> products) throws IOException {
		Set<String> product_set = new HashSet<String>();	// Sets don't allow duplicates
		String product_string;		// Convert list of features to string
		int duplicates = 0;
		int lineCount = 0;
		int[] product;
		
		// Initiate iterator for product list
		productListIterator = products.listIterator();
        
		// Add product to set which won't add duplicate items
        while (productListIterator.hasNext()) {
        	product = (int[]) productListIterator.next();
        	product_string = Arrays.toString(product);
        	lineCount++;
        	
        	// Check if the string is NOT added due to being a duplicate
        	if (!product_set.add(product_string)) {	
        		duplicates++;		// duplicate found, increment counter
        		productListIterator.remove();
        	}
        }
        
        // Finished checking all products
        String word_duplicate = duplicates == 1 ? " duplicate" : " duplicates";
        System.out.print(lineCount + " products checked with ");
        System.out.println(duplicates + word_duplicate + " found and removed");
        return products;
	}
	
	
	/** This function sets the costs to each product using the cost file
	 * @param filename
	 * @return
	 * @throws Exception
	 */
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
