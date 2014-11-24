import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

public class MakeProducts {

	static FileOutputStream fos;
	static OutputStreamWriter osw;
	static BufferedWriter bw;

	int high = 0;
	
	public static void main(String[] args) throws IOException {
//		BufferedReader in = new BufferedReader(new FileReader("solutions"));
		fos   = new FileOutputStream("FMfile.txt");
		osw = new OutputStreamWriter(fos);
		bw      = new BufferedWriter(osw);
        
        int[] features = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
        String line;
        
    	for (int i = 0; i < 10000; i++) {
    		features = makeNeg(features);
    		write(features);
    	}
    	System.out.println("finished!");
	}
	
	private static int[] makeNeg(int[] features) {
		Random r = new Random();
		int changeCount = r.nextInt(10);
		
		for (int i = 0; i < changeCount; i++) {
			features[r.nextInt(24)] *= -1;
		}
			
		return features;
	}
	
	public static void write(int[] input) throws IOException {

		try {
			for (int i = 0; i < input.length; i++) {
				if (i != input.length - 1) {
					bw.write(Integer.toString(input[i]) + ", ");
				} else {
					bw.write(Integer.toString(input[i]));
					bw.newLine();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
}
