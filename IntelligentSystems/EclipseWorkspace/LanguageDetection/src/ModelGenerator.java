import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class ModelGenerator {

	public static void main(String[] args) throws FileNotFoundException {
		
		Map<String, ArrayList<double[]>> langMFCC = new HashMap<String, ArrayList<double[]>>();
		int totalRecords = 0;
		
		// init stat vectors
		double[] mean = new double[13];
		double[] max = new double[13];
		double[] min = new double[13];
		
		// read in all mfcc vectors
		for(String inFileName : args){
			String lang = inFileName.substring(0, 2);
			File inFile = new File(inFileName);
			Scanner scanner = new Scanner(inFile);
	        scanner.useDelimiter(",");

	        langMFCC.put(lang, new ArrayList<double[]>());
	        
			int sampleIndex = 0;
	        double[] vector = new double[13];
	        int vectorIndex = 0;
	        
	        while(scanner.hasNext()){
	        	String dblStr = scanner.next().trim();
	        	if( dblStr.length() == 0 || dblStr == null ){
	        		break;
	        	}
	        	double currValue = Double.parseDouble(dblStr);
	        	mean[vectorIndex] += currValue;
	        	if( currValue > max[vectorIndex] )
	        		max[vectorIndex] = currValue;
	        	if( currValue < min[vectorIndex] )
	        		min[vectorIndex] = currValue;
	            vector[vectorIndex++] = currValue;
	        	
	            if( vectorIndex == 13 ){
	    	        langMFCC.get(lang).add(vector);
	    	        vector = new double[13];
	    	        vectorIndex = 0;
	            }
	        }
	        scanner.close();
		}
		
		for(int i=0; i < mean.length; i++){
			mean[i] /= totalRecords;
		}
		
		boolean[] availableAttrs = new boolean[13];
		Arrays.fill(availableAttrs, true);
		Queue<DTreeNode> nodeDiscovery = new LinkedList<DTreeNode>();
		DTreeNode rootNode = new DTreeNode(langMFCC.get("en"), langMFCC.get("es"), langMFCC.get("pl"), availableAttrs, max, min); 
		nodeDiscovery.add(rootNode);
		while( nodeDiscovery.size() > 0){
			DTreeNode currNode = nodeDiscovery.remove();
			currNode.bestAttrCalc();
			nodeDiscovery.add(currNode.lowerNode);
			nodeDiscovery.add(currNode.middleNode);
			nodeDiscovery.add(currNode.upperNode);
			int currAttr = currNode.attr;
			System.out.println(currAttr);
		}
		
	}
	
}
