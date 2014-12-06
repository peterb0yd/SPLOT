package jmetal.metaheuristics.splot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.Configuration;
import jmetal.util.JMException;

public class ProductSolution {

	private static int[][] products = new int[1000][24];

	public static void print(List<Solution> solutionsList) {

		readSolutionsFromFile();

		printSolutionsToFile(solutionsList);

	}

	private static void readSolutionsFromFile() {
		try {
			// Read "solutions" file to obtain products
			BufferedReader br = new BufferedReader(new FileReader("solutions"));
			String line;
			String[] line_string = new String[24];
			int line_count = 0;

			// Set each line of file to int array - represents each product
			while ((line = br.readLine()) != null) {
				int[] product = new int[24];
				line_string = line.toString().replace(",", "").split(" ");
				for (int i = 0; i < 24; i++) {
					product[i] = Integer.parseInt(line_string[i]);
				}
				products[line_count] = product; // add product to product array
				line_count++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printSolutionsToFile(List<Solution> solutionsList) {
		try {
			FileOutputStream fos = new FileOutputStream("ProductSolutionList");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			int productIndex = 0;
			int numberOfVariables = solutionsList.get(0).getDecisionVariables().length;
			for (Solution solution : solutionsList) {
				bw.write("Product Line Solution " + (solutionsList.indexOf(solution)+1));
				bw.newLine();
				for (int i = 0; i < numberOfVariables; i++) {
					productIndex = (int) solution.getDecisionVariables()[i].getValue();
					if (productIndex < 1000){
						bw.write("Product " + productIndex + ": " + Arrays.toString(products[productIndex]));
						bw.newLine();
					}
				}
				bw.newLine();
			}
			bw.close();
		} catch (IOException | JMException e) {
			e.printStackTrace();
		}
	}
}
