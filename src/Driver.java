import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;


public class Driver {
	
	static List<int[]> products = new ArrayList<int[]>();
	
	public static void main(String[] args) throws FileNotFoundException, ParseFormatException, IOException, ContradictionException, TimeoutException {
		MakeProducts.createAll();
	}

}
