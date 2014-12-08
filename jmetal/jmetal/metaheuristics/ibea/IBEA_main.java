//  IBEA_main.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.metaheuristics.ibea;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.splot.ProductSolution;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.problems.SPL;
import jmetal.problems.mTSP;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

/**
 * Class for configuring and running the DENSEA algorithm
 */
public class IBEA_main {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	/**
	 * @param args
	 *            Command line arguments.
	 * @throws JMException
	 * @throws IOException
	 * @throws SecurityException
	 *             Usage: three choices -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main problemName -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main problemName
	 *             paretoFrontFile
	 */
	/**
	 * @param args
	 *            Command line arguments.
	 * @throws JMException
	 * @throws IOException
	 * @throws SecurityException
	 *             Usage: three choices -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main problemName -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main problemName
	 *             paretoFrontFile
	 */
	public static void main(String cost_file, int amount, int numberOfFeatures, int productsInProductLine) throws JMException, IOException,
			ClassNotFoundException {
		Problem problem; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator

		QualityIndicator indicators; // Object to get quality indicators

		HashMap parameters; // Operator parameters

		// Logger object and file to store log messages
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("IBEA.log");
		logger_.addHandler(fileHandler_);

		indicators = null;

		problem = new SPL("productsCreated", cost_file, amount, numberOfFeatures, productsInProductLine);
		// indicators = new QualityIndicator(problem, "paretoFront.txt");

		algorithm = new IBEA(problem);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("archiveSize", 100);
		algorithm.setInputParameter("maxEvaluations", 1000);

		// Mutation and Crossover for Real codification
		parameters = new HashMap();
		parameters.put("probability", 0.9);
		parameters.put("distributionIndex", 20.0);
		crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);

		/* Mutation operator */
		parameters = new HashMap();
		parameters.put("probability", 0.2);
		mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);

		/* Selection Operator */
		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament",
				parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		// Add the indicator object to the algorithm
		algorithm.setInputParameter("indicators", indicators);

		// Execute the Algorithm
		long initTime = System.currentTimeMillis();
		SolutionSet population = algorithm.execute();
		long estimatedTime = System.currentTimeMillis() - initTime;

		// Print the results
		// logger_.info("Total execution time: "+estimatedTime + "ms");
		// logger_.info("Variables values have been writen to file VAR");
		// population.printVariablesToFile("VAR");
		// logger_.info("Objectives values have been writen to file FUN");
		// population.printObjectivesToFile("FUN");
		System.out.println("IBEA: ");
		population.printObjectives();
		ProductSolution.print(population.solutionsList_, "IBEA", amount, numberOfFeatures);
		// if (indicators != null) {
		// logger_.info("Quality indicators") ;
		// logger_.info("Hypervolume: " + indicators.getHypervolume(population))
		// ;
		// logger_.info("GD         : " + indicators.getGD(population)) ;
		// logger_.info("IGD        : " + indicators.getIGD(population)) ;
		// logger_.info("Spread     : " + indicators.getSpread(population)) ;
		// logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;
		// } // if
	}
} // IBEA_main.java
