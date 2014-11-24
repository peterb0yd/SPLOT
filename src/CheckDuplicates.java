import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CheckDuplicates {

	static Writer writer;
	
	int high = 0;
	
	/** Main class **/
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("FMfile.txt"));

	    writer = new BufferedWriter(new OutputStreamWriter(
	          new FileOutputStream("solutions"), "utf-8"));
		
		List <int[]> all_products = new ArrayList <int[]> ();
		Set<String> lines = new HashSet<String>();				// Sets don't allow duplicates
		String line;
		int duplicates = 0;
        
		// Read each line of FMfile
        while ((line = in.readLine()) != null) {
        	if (lines.add(line)) {			// if line is duplicate, this returns false
	        	String[] features = line.replaceAll(",","").split(" ");
	        	int[] product = new int[features.length];
	        	for (int i = 0; i < product.length; i++) {
	        		product[i] = Integer.parseInt(features[i]);
	        	}
        		all_products.add(product);	// add non-duplicates to list
        	} else {
        		duplicates++;				// duplicate found, increment counter
        	}
        }
        
        System.out.println(duplicates + " duplicates found");
        System.out.println(all_products.size() + " products discovered");
        
        // Add each product to solutions file
        for (int i = 0; i < all_products.size(); i++) {
        	write(all_products.get(i));
        }
        
        writer.close();
        
        System.out.println("Finished!");
	}
	
	/** Write function **/
	public static void write(int[] input) throws IOException {

		for (int i = 0; i < input.length; i++) {
			if (i != input.length-1) {
				writer.write(Integer.toString(input[i])+ ", ");
			} else {
				writer.write(Integer.toString(input[i])+"\n");
			}
		}
	}
		
	
}