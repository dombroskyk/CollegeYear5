//
//  BufferSet.java
//
//  Simple representation of a "Vertex".
//
//  Author:  Warren R. Carithers
//  Date:    2016/10/11 12:45:54
//
//  This class is an example of an anomaly in Java, as it
//  has data members but no member functions (other than a
//  constructor for ease of use).  It is, in essence, a
//  record/struct.
//
//  Java purists who insist that this is a heinous crime
//  should refer to the Wikipedia description of "PODS",
//  <https://en.wikipedia.org/wiki/Plain_old_data_structure>.
//
//  Not everything needs to be "smart".
//

public class BufferSet {

    ///
    // canvas size information
    ///
    public int vbuffer;         // buffer handles
    public int ebuffer;
    public int numElements;     // total number of vertices
    public long vSize;          // component sizes (bytes)
    public long nSize;
    public long tSize;
    public long eSize;
    public boolean bufferInit;  // have these already been set up?

    ///
    // Constructor
    ///
    public BufferSet()
    {
        vbuffer = 0;
        ebuffer = 0;
        numElements = 0;
	vSize = 0;
	nSize = 0;
	tSize = 0;
	eSize = 0;
	bufferInit = false;
    }

    public void dump()
    {
    	System.out.print( " vb " + vbuffer );
    	System.out.print( " eb " + ebuffer );
    	System.out.print( " #E " + numElements );
	System.out.print( " vS " + vSize );
	System.out.print( " nS " + nSize );
	System.out.print( " tS " + tSize );
	System.out.print( " eS " + eSize );
	System.out.print( " i? " + bufferInit );
    }
}
