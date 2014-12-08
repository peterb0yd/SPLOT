import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jmetal.metaheuristics.ibea.IBEA_main;
import jmetal.metaheuristics.nsgaII.NSGAII_main;
import jmetal.metaheuristics.randomSearch.RandomSearch_main;
import jmetal.util.JMException;

import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;


public class Driver {
	
	public static void main(String[] args) throws FileNotFoundException, ParseFormatException, IOException, ContradictionException, TimeoutException {

		// Set number of products
		int amount = 1000;
		
		// Set number of features
		int numberOfFeatures = 94; 
		
		// Set size of Product Line
		int productsInProductLine = 50;
		
		// Files for cost and fm
		String FM_file = "Models/CocheEcologico.dimacs";
		String cost_file = "Costs/CocheEcologico.dimacs.cost";
		
		try {
			
			// Make products
			MakeProducts.createAll(amount, FM_file);
			
			// Run tests
			NSGAII_main.main(cost_file, amount, numberOfFeatures, productsInProductLine);
			IBEA_main.main(cost_file, amount, numberOfFeatures, productsInProductLine);
			RandomSearch_main.main(cost_file, amount, numberOfFeatures, productsInProductLine);
			
		} catch (SecurityException | ClassNotFoundException | JMException e) {
			e.printStackTrace();
		}
		
	}

}
