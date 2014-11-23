import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class afroMan {

	public afroMan() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Checks for duplicates
	 * @param input
	 * @return
	 */
	public static Set<String> removeDuplicates(int[] input) {

		Set<String> set = new HashSet<String>();
		// Collections.addAll(set, input);
		return set;
	}
	/**
	 * writes onto a text file
	 * @param input
	 */
	public static void write(int[] input){

        PrintStream output = null;
		try {
			output = new PrintStream(new File("FMfile.txt"));
		    for(int i =0;i<input.length;i++){
		        output.println(input[i]);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    output.close();
	}

	public static void main(String[] args) {
		Random rand = new Random();
		int gamble = rand.nextInt(5) + 1;
		int index = rand.nextInt(5) + 1;
		int count = 1;
		int[] drake = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
				17, 18, 19, 20, 21, 22, 23, 24 };
		// int [] drake;
		// drake = new int [5];
		// for loop gamble, find the numbe of indexes, randomly
		switch (gamble) {
		case 1:
			// for (count = 1; count < 24; count++) {
			drake[index] *= -1;
			System.out.println(Arrays.toString(drake));

			// }
		case 2:
			for (count = 1; count < 4; count++) {
			drake[index] *= -1;
			index = rand.nextInt(24) + 1;
			drake[index] *= -1;
			System.out.println(Arrays.toString(drake));

			 }
		case 3:
			// for (count = 1; count < 24; count++) {
			drake[index] *= -1;
			index = rand.nextInt(24) + 1;
			drake[index] *= -1;
			index = rand.nextInt(24) + 1;
			drake[index] *= -1;
			System.out.println(Arrays.toString(drake));
			// }
		case 4:
			// for (count = 1; count < 24; count++) {
			drake[index] *= -1;
			index = rand.nextInt(24) + 1;
			drake[index] *= -1;
			index = rand.nextInt(24)+1;
			drake[index] *= -1;
			index = rand.nextInt(24) + 1;
			drake[index] *= -1;
			System.out.println(Arrays.toString(drake));
			// }
		case 5:
			// for (count = 1; count < 24; count++) {
			drake[index] *= -1;
			index = rand.nextInt(24) + 1;
			drake[index] *= -1;
			index = rand.nextInt(24) + 1;
			drake[index] *= -1;
			index = rand.nextInt(24) + 1;
			drake[index] *= -1;
			index = rand.nextInt(24) + 1;
			drake[index] *= -1;
			System.out.println(Arrays.toString(drake));
			// }
		default:
			System.out.println("DONE");
		}
		removeDuplicates(drake);
		write(drake);

	}

}
