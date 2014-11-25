import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.minisat.core.Solver;
import org.sat4j.minisat.orders.RandomLiteralSelectionStrategy;
import org.sat4j.minisat.orders.RandomWalkDecorator;
import org.sat4j.minisat.orders.VarOrderHeap;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;


public class MakeProducts {
	static Writer writer;
	
	/** This method creates 1000 products
	 * 		- DIMACS solver guarantees feasibility and non-duplicate for each product
	 * 		- Function below checks for duplicates
	 * @throws FileNotFoundException
	 * @throws ParseFormatException
	 * @throws IOException
	 * @throws ContradictionException
	 * @throws TimeoutException
	 */
	public static void createAll() throws FileNotFoundException, ParseFormatException, IOException, ContradictionException, TimeoutException {
		// Writes our output to solutions file
		writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("solutions"), "utf-8"));
	    
	    // 3SAT DIMACS solver
        ISolver dimacsSolver = SolverFactory.instance().createSolverByName("MiniSAT");
        DimacsReader dr = new DimacsReader(dimacsSolver);
        dr.parseInstance("CounterStrikeSimpleFeatureModel.dimacs");
        Solver solver = (Solver) dimacsSolver;
        solver.setTimeout(1000);
        solver.setOrder(new RandomWalkDecorator(new VarOrderHeap(new RandomLiteralSelectionStrategy()), 1));
        ISolver solverIterator = new ModelIterator(solver);

        // Our list of products
        List<int[]> products = new ArrayList<int[]>();
        
        // Make 1000 feasible products
        while (products.size() < 1000) {

            if (solverIterator.isSatisfiable()) {
                int[] product = solverIterator.model();
                products.add(product);
            } 
        }
        
        // Check solutions file for duplicates
        checkDuplicates(products);
        
        // Add each product to solutions file
        for (int i = 0; i < products.size(); i++) {
        	write(products.get(i));
        }
        
        // Close writer
        writer.close();
        
        // All products made
        System.out.println("Finished! Products written to 'solutions' file.");
	}
	
	/** Write function **/
	public static void write(int[] input) throws IOException {

		// Writes each product to file lines
		for (int i = 0; i < input.length; i++) {
			if (i != input.length-1) {
				writer.write(Integer.toString(input[i])+ ", ");	// Include comma
			} else {
				writer.write(Integer.toString(input[i])+"\n");	// Last feature, new line
			}
		}
	}
	
	/** This function checks a list of products for duplicates
	 * 		- It should never find duplicates considering products
	 * 			are created using 3SAT DIMACS solver
	 * 
	 * @param products - list of all products
	 * @throws IOException
	 */
	public static void checkDuplicates(List<int[]> products) throws IOException {
		Set<String> product_set = new HashSet<String>();	// Sets don't allow duplicates
		String product_string;		// Convert list of features to string
		int lineCount = 0;
		int duplicates = 0;
        
		// Read each line of file
        for (int i = 0; i < products.size(); i++) {
        	product_string = Arrays.toString(products.get(i));
        	lineCount++;
        	
        	// Check if the string is NOT added due to being a duplicate
        	if (!product_set.add(product_string)) {	
        		duplicates++;		// duplicate found, increment counter
        	}
        }
        
        // Finished checking all products
        System.out.println(lineCount + " products checked with " + duplicates + " duplicates found");
	}
}
