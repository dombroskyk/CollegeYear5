import java.util.ArrayList;

public class DTreeNode {

	private ArrayList<double[]> inEn;
	private ArrayList<double[]> inEs;
	private ArrayList<double[]> inPl;
	private double[] max;
	private double[] min;
	private boolean[] attrAvailable;
	
	public double total;
	public double entropy;
	public int attr;
	public String lang = null;
	public DTreeNode lowerNode;
	public DTreeNode middleNode;
	public DTreeNode upperNode;
	
	public DTreeNode(ArrayList<double[]> inEn, ArrayList<double[]> inEs, ArrayList<double[]> inPl, boolean[] attrAvailable, double[] max, double[] min){
		//System.out.println("Sizes: " + inEn.size() + ", " + inEs.size() + ", " + inPl.size());
		this.inEn = inEn;
		this.inEs = inEs;
		this.inPl = inPl;
		this.attrAvailable = attrAvailable;
		this.total = inEn.size() + inEs.size() + inPl.size();
		if( this.inEn.size() == this.total )
			this.lang = "English";
		if ( this.inEs.size() == this.total )
			this.lang = "Spanish";
		if( this.inPl.size() == this.total )
			this.lang = "Polish";
		
		// check if we can branch more
		boolean attrsLeft = false;
		for( boolean attrPresent : attrAvailable ){
			if( attrPresent )
				attrsLeft = true;
		}
		if( !attrsLeft ){
			// no attributes left, choose most dominant class
			if( this.inEn.size() >= this.inEs.size() && this.inEn.size() >= this.inPl.size() )
				this.lang = "English";
			else if( this.inEs.size() > this.inEn.size() && this.inEs.size() >= this.inPl.size() )
				this.lang = "Spanish";
			else
				this.lang = "Polish";
		}
			
		this.max = max;
		this.min = min;
		this.entropy = this.entropyCalc();
	}
	
	public double entropyCalc(){
		return this.entropyPart(((double)(this.inEn.size())) / this.total) +
				this.entropyPart(((double)(this.inEs.size())) / this.total) +
				this.entropyPart(((double)(this.inPl.size())) / this.total);
	}
	
	private double entropyPart(double frac){
		if( frac == 0 ){
			return 0.0d;
		}
		return -frac * this.log2Calc(frac);
	} 
	
	private double log2Calc(double frac){
		return Math.log10(frac) / Math.log10(2.0d); 
	}
	
	public void bestAttrCalc(){
		int bestAttr = -1;
		if( this.lang != null )
			return;
		DTreeNode[] bestNodes = new DTreeNode[3];
		double bestEntropy = Double.MAX_VALUE;
		for(int i=0; i < 13; i++){
			if( !this.attrAvailable[i] )
				continue;
			
			double lowerThreshold;
			double upperThreshold;
			switch(i){
				case 0:
					lowerThreshold = -193.3001238;
					upperThreshold = -113.4861254;
					break;
				case 1:
					lowerThreshold = 2.05752346;
					upperThreshold = 9.915203513;
					break;
				case 2:
					lowerThreshold = -4.946240214;
					upperThreshold = 2.118639479;
					break;
				case 3:
					lowerThreshold = -3.021002992;
					upperThreshold = 2.585291024;
					break;
				case 4:
					lowerThreshold = -4.328821353;
					upperThreshold = 0.281594811;	
					break;
				case 5:
					lowerThreshold = -4.68471071;
					upperThreshold = -0.66962613;
					break;
				case 6:
					lowerThreshold = -3.710711378;
					upperThreshold = 0.188923877;
					break;
				case 7:
					lowerThreshold = -2.01568247;
					upperThreshold = 1.014188976;
					break;
				case 8:
					lowerThreshold = -1.879052036;
					upperThreshold = 1.253228207;
					break;
				case 9:
					lowerThreshold = -0.340944311;
					upperThreshold = 2.529262491;
					break;
				case 10:
					lowerThreshold = -1.9707425;
					upperThreshold = 1.227510361;
					break;
				case 11:
					lowerThreshold = -1.368993247;
					upperThreshold = 1.115249463;
					break;
				case 12:
					lowerThreshold = -1.729584009;
					upperThreshold = 0.858172704;
					break;
				default:
					// dummy values
					lowerThreshold = 0;
					upperThreshold = 1;
					break;
			}
			
			ArrayList<double[]> lowerEn = new ArrayList<double[]>();
			ArrayList<double[]> middleEn = new ArrayList<double[]>();
			ArrayList<double[]> upperEn = new ArrayList<double[]>();
			ArrayList<double[]> lowerEs = new ArrayList<double[]>();
			ArrayList<double[]> middleEs = new ArrayList<double[]>();
			ArrayList<double[]> upperEs = new ArrayList<double[]>();
			ArrayList<double[]> lowerPl = new ArrayList<double[]>();
			ArrayList<double[]> middlePl = new ArrayList<double[]>();
			ArrayList<double[]> upperPl = new ArrayList<double[]>();
			for(int j=0; j < inEn.size(); j++){
				double[] currEn = inEn.get(j);
				if( currEn[i] < lowerThreshold ){
					lowerEn.add(currEn);
				} else if( currEn[i] < upperThreshold ){
					middleEn.add(currEn);
				} else {
					upperEn.add(currEn);
				}
			}
			
			for(int j=0; j < inEs.size(); j++){
				double[] currEs = inEs.get(j);
				if( currEs[i] < lowerThreshold ){
					lowerEs.add(currEs);
				} else if( currEs[i] < upperThreshold ){
					middleEs.add(currEs);
				} else {
					upperEs.add(currEs);
				}
			}
			
			for(int j=0; j < inPl.size(); j++){
				double[] currPl = inPl.get(j);
				if( currPl[i] < lowerThreshold ){
					lowerPl.add(currPl);
				} else if( currPl[i] < upperThreshold ){
					middlePl.add(currPl);
				} else {
					upperPl.add(currPl);
				}
			}
			
			boolean[] newAttrList = this.attrAvailable.clone();
			newAttrList[i] = false;
			DTreeNode lowerNode = new DTreeNode(lowerEn, lowerEs, lowerPl, newAttrList, this.max, this.min);
			DTreeNode middleNode = new DTreeNode(middleEn, middleEs, middlePl, newAttrList, this.max, this.min);
			DTreeNode upperNode = new DTreeNode(upperEn, upperEs, upperPl, newAttrList, this.max, this.min);
			double currChildEntropy = childEntropy(lowerNode, middleNode, upperNode);	
			if( currChildEntropy < bestEntropy ){
				bestAttr = i;
				bestNodes = new DTreeNode[]{lowerNode, middleNode, upperNode};
				bestEntropy = currChildEntropy;
			}
			
		}
		this.lowerNode = bestNodes[0];
		this.middleNode = bestNodes[1];
		this.upperNode = bestNodes[2];
		this.attr = bestAttr;
	}
	
	private double childEntropy(DTreeNode node1, DTreeNode node2, DTreeNode node3){
		double lowerScaledEntropy = (node1.total / this.total) * node1.entropy;
		double middleScaledEntropy = (node2.total / this.total) * node2.entropy;
		double upperScaledEntropy = (node3.total / this.total) * node3.entropy;
		return lowerScaledEntropy + middleScaledEntropy + upperScaledEntropy;
	}
	
}
