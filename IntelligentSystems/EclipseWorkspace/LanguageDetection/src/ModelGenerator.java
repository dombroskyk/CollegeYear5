import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class ModelGenerator {

	public static void main(String[] args) throws FileNotFoundException {
		
		HashMap<String, double[][]> langMFCC = new HashMap<String, double[][]>();
		
		try{
			// get number of lines in each file, initialize vector arrays
			for(String inFileName : args){
				String lang = inFileName.substring(0, 2);
				BufferedReader reader = new BufferedReader(new FileReader(inFileName));
				int lines = 0;
				while (reader.readLine() != null) lines++;
				reader.close();
				System.out.println(lang + ": " + lines);
				langMFCC.put(lang, new double[lines][]);
			}
		} catch(Exception e){
			e.printStackTrace();
			System.exit(2);
		}
		
		// read in all mfcc vectors
		for(String inFileName : args){
			String lang = inFileName.substring(0, 2);
			File inFile = new File(inFileName);
			int sampleIndex = 0;
			Scanner scanner = new Scanner(inFile);
	        scanner.useDelimiter(",");
	        double[] vector = new double[13];
	        int vectorIndex = 0;
	        while(scanner.hasNext()){
	        	String dblStr = scanner.next().trim();
	        	if( dblStr.length() == 0 || dblStr == null ){
	        		break;
	        	}
	        	System.out.println(dblStr);
	            vector[vectorIndex++] = Double.parseDouble(dblStr);
	            if( vectorIndex == 13 ){
	    	        double[][] currLang = langMFCC.get(lang);
	    	        currLang[sampleIndex++] = vector;
	    	        vector = new double[13];
	    	        vectorIndex = 0;
	            }
	        }
	        scanner.close();
		}
		
	}
	
}
