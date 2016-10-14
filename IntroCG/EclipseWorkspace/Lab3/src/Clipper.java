//
//  Clipper.java
//
//  Created by Warren R. Carithers on 2016/10/10.
//  Based on a version created by Joe Geigel on 1/21/10.
//  Copyright 2010 Rochester Institute of Technology. All rights reserved.
//
//  Contributor:  Kevin Dombrosky
//

///
// Object for performing clipping
//
///

public class Clipper {

	///
	// Object used to represent clipping edges
	//
	///
	private class ClipEdge{
		
		public float e1;
		public float e2;
		public float o;
		public char side;
		
		ClipEdge( float endpoint1, float endpoint2, float offset, char side ){
			this.e1 = endpoint1;
			this.e2 = endpoint2;
			this.o = offset;
			this.side = side;
		}
		
		public String toString(){
			return String.format("%.2f", this.e1) + "|" +
					String.format("%.2f", this.e2) + "|" +
					String.format("%.2f", this.o) + "|" +
					this.side;
		}
	}
	
    ///
    // clipPolygon
    //
    // Clip the polygon with vertex count in and vertices inV against the
    // rectangular clipping region specified by lower-left corner ll and
    // and upper-right corner ur. The resulting vertices are placed in outV.
    //
    // The routine should return the vertex count of polygon resulting
    // from the clipping.
    //
    // @param in   the number of vertices in the polygon to be clipped
    // @param inV  the incoming vertex list
    // @param outV the outgoing vertex list
    // @param ll   the lower-left corner of the clipping rectangle
    // @param ur   the upper-right corner of the clipping rectangle
    //
    // @return number of vertices in the polygon resulting after clipping
    //
    ///
    public int clipPolygon( int in, Vertex inV[], Vertex outV[],
		            Vertex ll, Vertex ur )
    {
        int outLength = 0;
        
        //create ClipEdges for future use and iteration
        ClipEdge[] clipEdges = new ClipEdge[4];
        clipEdges[0] = new ClipEdge(ll.x, ur.x, ur.y, 't');
        clipEdges[1] = new ClipEdge(ll.y, ur.y, ur.x, 'r');
        clipEdges[2] = new ClipEdge(ll.x, ur.x, ll.y, 'b');
        clipEdges[3] = new ClipEdge(ll.y, ur.y, ll.x, 'l');
        
        //deep copy inV
        Vertex[] inVertices = new Vertex[inV.length];
        for( int i = 0; i < inV.length; i++ ){
        	inVertices[i] = inV[i];
        }
        	
        
        for( ClipEdge clipEdge : clipEdges ){
        	//clip against each edge
        	
        	if( inVertices.length == 0 ){
        		// if there are no more vertices to iterate against, stop
        		return 0;
        	}
	        Vertex p = inVertices[inVertices.length - 1];
	        outLength = 0;
	        
	        for( int i = 0; i < inVertices.length; i++ ){
	        	Vertex s = inVertices[i];
	        	
	        	if( inside( s, clipEdge ) ){ // Cases 1 & 4
	        		 if ( inside( p, clipEdge )) { // Case 1
	        			 // add s to outVertices, increment outLength
	        			 outV[outLength++] = s;
	        		 } else { // Case 4
	        			 Vertex intersection = intersect( p, s, clipEdge );
	        			 //add i to outVertices, increment outLength
	        			 outV[outLength++] = intersection;
	        			 //add s to outVertices, increment outLength
	        			 outV[outLength++] = s;
	        		 }
	        	} else { // Cases 2 & 3
	        		if( inside ( p, clipEdge ) ) { // Case 2
	        			Vertex intersection = intersect( p, s, clipEdge );
	        			//add intersection to outVertices, increment outLength
	        			outV[outLength++] = intersection;
	        		} // Case 3 adds nothing
	        	}
	        	p = s;
	        }
	        
	        // deep copy outV into inVertices 
        	inVertices = new Vertex[outLength];
        	for( int i = 0; i < outLength; i++ ){
	        	inVertices[i] = outV[i];
	        }
        }

        return outLength;  // remember to return the outgoing vertex count!
    }
    
    
    ///
    // inside
    //
    // Determine if the vertex is inside the clipping window, relative to the
    // current clipping edge.
    //
    // The method should return true if the vertex is "inside", false
    // otherwise
    //
    // @param v        the vertex to check if inside the polygon
    // @param clipEdge The edge used to determine the vertex being inside
    //
    // @return true if v is inside of clipEdge, false otherwise
    //
    ///
    private boolean inside( Vertex v, ClipEdge clipEdge ){
    	switch( clipEdge.side ){
    		case 't':
    			if( v.y <= clipEdge.o )
    				return true;
    			break;
    		case 'b':
    			if( v.y >= clipEdge.o )
    				return true;
    			break;
    		case 'r':
    			if( v.x <= clipEdge.o )
    				return true;
    			break;
    		case 'l':
    			if( v.x >= clipEdge.o )
    				return true;
    			break;
    	}
    	return false;
    }
    
    ///
    // intersect
    //
    // Find the intersection point of the polygon edge created by vertices
    // s and p and the clipping edge, clipEdge.
    //
    // The routine should return the vertex at the intersection
    // of the polygon edge and clipping edge.
    //
    // @param s        the first vertex of the polygon edge
    // @param p        the second vertex of the polygon edge
    // @param clipEdge the clipping edge
    //
    // @return intersection vertex of the polygon and clipping edges
    //
    ///
    private Vertex intersect( Vertex s, Vertex p, ClipEdge clipEdge ){
    	Vertex intersection;
    	if( s.x != p.x ){
			float m = ( s.y - p.y ) / ( s.x - p.x );
			float b = -m * s.x + s.y;
    		if( clipEdge.side == 't' || clipEdge.side == 'b' ){
    			// intersection with horizontal edges
    			float intX = ( clipEdge.o - b ) * ( 1.0f / m );
    			intersection = new Vertex( intX, clipEdge.o );
    		} else {
    			// intersection with vertical edges
    			float intY = m * clipEdge.o + b;
    			intersection = new Vertex( clipEdge.o, intY );
    		}
    	} else {
    		intersection = new Vertex( s.x, clipEdge.o );
    	}
    	
    	return intersection;
    }
}
