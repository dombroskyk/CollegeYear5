//
//  Pipeline.java
//
//  Created by Warren R. Carithers 2016/10/23.
//  Based on code created by Joe Geigel on 1/21/10.
//  Copyright 2016 Rochester Institute of Technology. All rights reserved.
//
//  Contributor:  Kevin Dombrosky
//

///
// Simple wrapper class for midterm assignment
//
// Only methods in the implementation file whose bodies
// contain the comment
//
//    // YOUR IMPLEMENTATION HERE
//
// are to be modified by students.
///

import Jama.*;

import java.util.*;

public class Pipeline extends Canvas {
	
	///
	// Simple class to represent an edge for polygon fill
	///
	private class Edge {
		
		public int minY;
		public int maxY;
		public int xVal;
		public int slopeSign;
		public int dX;
		public int dY;
		public int sum;
		public Edge nextEdge;
		
		Edge( int x0, int y0, int x1, int y1 ){
			if( y0 > y1 ){
				this.minY = y1;
				this.maxY = y0;
				this.xVal = x1;
				
			} else {
				this.minY = y0;
				this.maxY = y1;
				this.xVal = x0;
			}
			
			this.dX = x0 - x1;
			this.dY = y0 - y1;
			this.sum = 0;
			
			if( ( this.dX < 0 && this.dY < 0 ) || ( this.dX > 0 && this.dY > 0 ) ){
				this.slopeSign = 1;
			} else {
				this.slopeSign = -1;
			}
			
			this.dX = Math.abs(this.dX);
			this.dY = Math.abs(this.dY);
		}
		
		public String toString(){
			return Integer.toString(this.maxY) + "|" + 
				Integer.toString(this.xVal) + "|" + 
				Integer.toString(this.slopeSign) + "|" +
				Integer.toString(this.dX) + "|" +
				Integer.toString(this.dY) + "|" +
				Integer.toString(this.sum) + "|minY:" +
				Integer.toString(this.minY);
		}
		
		///
	    // Create a deepcopy of an Edge.
	    //
		// @return The deepcopied edge without nextEdge references
	    ///
		public Edge deepcopy(){
			Edge newEdge = new Edge(0, 0, 0, 0);
			newEdge.minY = this.minY;
			newEdge.maxY = this.maxY;
			newEdge.xVal = this.xVal;
			newEdge.slopeSign = this.slopeSign;
			newEdge.dX = this.dX;
			newEdge.dY = this.dY;
			newEdge.sum = this.sum;
			return newEdge;
		}
	}
    
	private ArrayList<Vertex[]> polygons;
	private Edge[] globalEdgeTable;
	
	private Matrix tMatrix;
	private float clipT;
	private float clipR;
	private float clipB;
	private float clipL;
	private Clipper clipper;
	
	private int viewX;
	private int viewY;
	private int viewW;
	private int viewH;
	
    ///
    // Constructor
    //
    // @param w width of canvas
    // @param h height of canvas
    ///
    public Pipeline( int w, int h )
    {
        super( w, h );

        this.polygons = new ArrayList<Vertex[]>();
        this.tMatrix = Matrix.identity( 3, 3 );
        this.clipper = new Clipper();
    }
    
    ///
    //
    // addPoly - Add a polygon to the canvas.  This method does not draw
    //           the polygon, but merely stores it for later drawing.
    //           Drawing is initiated by the drawPoly() method.
    //
    //           Returns a unique integer id for the polygon.
    //
    // @param p - Array containing the vertices of the polygon to be added.
    // @param n - Number of vertices in polygon
    //
    // @return a unique integer identifier for the polygon
    ///
    public int addPoly( Vertex p[], int n )
    {
        this.polygons.add( p );

        // REMEMBER TO RETURN A UNIQUE ID FOR THE POLYGON
        return this.polygons.size() - 1;
    }
    
    ///
    // drawPoly - Draw the polygon with the given id.  The polygons hold
    //            be drawn after applying the current transformation to
    //            the vertices of the polygon.
    //
    // @param polyID - the ID of the polygon to be drawn.
    ///
    public void drawPoly( int polyID )
    {
    	Vertex[] currPoly = this.polygons.get( polyID );
    	Vertex[] transPoly = new Vertex[currPoly.length];
    	Vertex[] outClip = new Vertex[currPoly.length * 2];
    	
    	// apply transformations
    	for( int i = 0; i < currPoly.length; i++ ){
    		double[][] pointElems = {{currPoly[i].x}, {currPoly[i].y}, {1.}};
    		Matrix pointMat = new Matrix( pointElems );
    		pointMat = tMatrix.times( pointMat );
    		transPoly[i] = new Vertex( (float)pointMat.get( 0, 0 ), (float)pointMat.get( 1, 0 ) );
    	}
    	
    	// clip poly
    	Vertex ll = new Vertex(this.clipL, this.clipB);
    	Vertex ur = new Vertex(this.clipR, this.clipT);
    	int clipOutLength = clipper.clipPolygon(transPoly.length, transPoly,
    											outClip, ll, ur);
    	
    	// viewport computations
    	double sx = this.viewW / ( this.clipR - this.clipL );
		double sy = this.viewH / ( this.clipT - this.clipB );
		double tx = ( ( this.clipR * this.viewX ) - 
						( this.clipL * ( this.viewX + this.viewW ) ) ) / 
						( this.clipR - this.clipL );
		double ty = ( ( this.clipT * this.viewY ) -
						( this.clipB * ( this.viewY + this.viewH ) ) ) / 
						( this.clipT - this.clipB );
		double[][] viewportElems = {{ sx, 0, tx},
									 {0, sy, ty},
									 {0, 0, 1}};
		Matrix viewportMat = new Matrix( viewportElems );
    	for( int i = 0; i < clipOutLength; i++ ){
    		double[][] pointElems = {{outClip[i].x}, {outClip[i].y}, {1.}};
    		Matrix pointMat = new Matrix( pointElems );
    		pointMat = viewportMat.times( pointMat );
    		outClip[i] = new Vertex( (float)pointMat.get( 0, 0 ),
    								 (float)pointMat.get( 1, 0 ) );
    	}
    	
    	
    	//initialize edges and add to globalEdgeTable
    	Edge[] globalEdgeTable = new Edge[600];
    	int absMinY = 601;
    	int absMaxY = -1;
        for( int i = 0; i < clipOutLength - 1; i++ ){
        	Edge newEdge = new Edge( Math.round( outClip[i].x ),
        							 Math.round( outClip[i].y ),
        							 Math.round( outClip[i+1].x ),
        							 Math.round( outClip[i+1].y ) );
        	if( newEdge.dY != 0 ){
        		// ignore horizontal edges
	        	int currMinY = newEdge.minY;
	        	int currMaxY = newEdge.maxY;
	        	addToEdgeTable( newEdge, globalEdgeTable );
	        	if( currMinY < absMinY ){
	        		absMinY = currMinY;
	        	}
	        	if( currMaxY > absMaxY ){
	        		absMaxY = currMaxY;
	        	}
        	}
        }
        Edge newEdge = new Edge( Math.round(outClip[clipOutLength - 1].x),
        						 Math.round(outClip[clipOutLength - 1].y),
        						 Math.round(outClip[0].x),
        						 Math.round(outClip[0].y) );
        if( newEdge.dY != 0 ){
        	// ignore horizontal edges
	        int currMinY = newEdge.minY;
	        int currMaxY = newEdge.maxY;
	        addToEdgeTable( newEdge, globalEdgeTable );
	    	if( currMinY < absMinY ){
	    		absMinY = currMinY;
	    	}
	    	if( currMaxY > absMaxY ){
	    		absMaxY = currMaxY;
	    	}
        }
        for( int i = absMinY; i <= absMaxY; i++ ){
        	if( globalEdgeTable[i] != null ){
        		Edge nextEdge = globalEdgeTable[i].nextEdge;
	        	while( nextEdge != null ){
	        		nextEdge = nextEdge.nextEdge;
	        	}
        	}
        }
        
        int edgeIndex = absMinY;
        Edge activeListHead = null;
        while( edgeIndex <= absMaxY ){
        	// remove all entries from active list whose y is greater than y index
        	if( activeListHead != null ){
	        	while( activeListHead != null &&
	        			activeListHead.maxY <= edgeIndex ){
	        		activeListHead = activeListHead.nextEdge;
	        	}
	        	
	        	if( activeListHead != null ){
		        	Edge currActiveEdge = activeListHead;
		        	while( currActiveEdge.nextEdge != null ){
		        		if( currActiveEdge.nextEdge.maxY <= edgeIndex ){
		        			currActiveEdge.nextEdge =
		        					currActiveEdge.nextEdge.nextEdge;
		        		} else {
		        			currActiveEdge = currActiveEdge.nextEdge;
		        		}
		        	}
	        	}
        	}
        	
        	// add current y index to activeList and sort
        	Edge newEdgeHead = globalEdgeTable[edgeIndex];
	        while( newEdgeHead != null ){
	        	
	        	if( activeListHead == null ){
	        		// if table happens to be empty, just add current new edge
	        		activeListHead = newEdgeHead.deepcopy();
	        		activeListHead.nextEdge = null;
	        		
	        		newEdgeHead = newEdgeHead.nextEdge;
	        	} else {
	        		// else, insertion sort into active list

	        		boolean placed = false;
	        		Edge currActiveEdge = activeListHead;
	        		// attempt insert to head
	        		if( newEdgeHead.xVal < currActiveEdge.xVal ){
	        			Edge insertEdge = newEdgeHead.deepcopy();
	        			newEdgeHead = newEdgeHead.nextEdge;
	        			
	        			insertEdge.nextEdge = activeListHead;
	        			activeListHead = insertEdge;
	        			placed = true;
	        		} else if( newEdgeHead.xVal == currActiveEdge.xVal && 
	        				newEdgeHead.dX * newEdgeHead.dY * 
	        				newEdgeHead.slopeSign * currActiveEdge.dY <
	        				currActiveEdge.dX * currActiveEdge.dY * 
	        				currActiveEdge.slopeSign * newEdgeHead.dY ){
	        			Edge insertEdge = newEdgeHead.deepcopy();
	        			newEdgeHead = newEdgeHead.nextEdge;
	        			
	        			insertEdge.nextEdge = activeListHead;
	        			activeListHead = insertEdge;
	        			placed = true;
	        		}
	        		
	        		//insert into middle of list
	        		while( !placed && currActiveEdge != null &&
	        				currActiveEdge.nextEdge != null ){
	        			
	        			if( newEdgeHead.xVal < currActiveEdge.nextEdge.xVal ){
		        			Edge insertEdge = newEdgeHead.deepcopy();
		        			newEdgeHead = newEdgeHead.nextEdge;
		        			
		        			insertEdge.nextEdge = currActiveEdge.nextEdge;
		        			currActiveEdge.nextEdge = insertEdge;
		        			placed = true;
		        		} else if( newEdgeHead.xVal == currActiveEdge.nextEdge.xVal && 
		        				newEdgeHead.dX * newEdgeHead.dY * 
		        				newEdgeHead.slopeSign * currActiveEdge.nextEdge.dY <
		        				currActiveEdge.nextEdge.dX * currActiveEdge.nextEdge.dY * 
		        				currActiveEdge.nextEdge.slopeSign * newEdgeHead.dY ){
		        			Edge insertEdge = newEdgeHead.deepcopy();
		        			newEdgeHead = newEdgeHead.nextEdge;
		        			
		        			insertEdge.nextEdge = currActiveEdge.nextEdge;
		        			currActiveEdge.nextEdge = insertEdge;
		        			placed = true;
		        		}
	        			
	        			currActiveEdge = currActiveEdge.nextEdge;
	        		}
	        		
	        		//place at end
	        		if( !placed ){
	        			Edge insertEdge = newEdgeHead.deepcopy();
	        			newEdgeHead = newEdgeHead.nextEdge;
	        			currActiveEdge.nextEdge = insertEdge;
	        		}
	        	}
	        }
        	
        	// fill pixels
        	Edge currActiveEdge = activeListHead;
        	boolean draw = true;
        	while( currActiveEdge != null ){
        		Edge nextActiveEdge = currActiveEdge.nextEdge;
        		if( draw && nextActiveEdge != null ){
        			if( currActiveEdge.xVal > nextActiveEdge.xVal ){
        				for( int currX = nextActiveEdge.xVal; currX < currActiveEdge.xVal; currX++ ){
            				this.setPixel(currX, edgeIndex);
            			}
        			}else{
        			for( int currX = currActiveEdge.xVal; currX < nextActiveEdge.xVal; currX++ ){
        				this.setPixel(currX, edgeIndex);
        			}}
        		}
        		currActiveEdge = currActiveEdge.nextEdge;
        		draw = !draw;
        	}
        	
        	// increment y
        	edgeIndex++;
        	
        	// for each non vertical edge in activeList
        	currActiveEdge = activeListHead;
        	while( currActiveEdge != null ){
            	// add slope to x
        		if( currActiveEdge.dX != 0 && currActiveEdge.dY != 0 ){
        			currActiveEdge.sum += currActiveEdge.dX;
        			while( currActiveEdge.sum >= currActiveEdge.dY ){
        				currActiveEdge.xVal += currActiveEdge.slopeSign;
        				currActiveEdge.sum -= currActiveEdge.dY;
        			}
        		}
        		currActiveEdge = currActiveEdge.nextEdge;
        	}
        }
    }
    
    ///
    // Add an edge to the global edge table.
    //
    // @param newEdge Edge to be inserted into globalEdgeTable
    // @param globalEdgeTable The array for the edge to be inserted into
    ///
    private void addToEdgeTable( Edge newEdge, Edge[] globalEdgeTable ){
    	int newMinY = newEdge.minY;
    	if( globalEdgeTable[newMinY] == null ){
    		globalEdgeTable[newEdge.minY] = newEdge;
    	} else {
    		Edge currEdge = globalEdgeTable[newMinY];
    		while( currEdge.nextEdge != null ){
    			currEdge = currEdge.nextEdge;
    		}
    		currEdge.nextEdge = newEdge;
    	}
    }
    
    ///
    // clearTransform - sets the current transformation to be the identity 
    ///
    public void clearTransform()
    {
    	this.tMatrix = Matrix.identity( 3, 3 );
    }
    
    ///
    //
    // translate - Add a translation to the current transformation by
    //             premultiplying the appropriate translation matrix to
    //             the current transformation matrix.
    //
    // @param tx - Amount of translation in x.
    // @param ty - Amount of translation in y.
    ///
    public void translate( float tx, float ty )
    {
    	double[][] translateElems = {{1., 0., tx},
    								 {0., 1., ty},
    								 {0., 0., 1.}};
    	Matrix translateMatrix = new Matrix(translateElems);
    	
    	this.tMatrix = translateMatrix.times( this.tMatrix );
    }
    
    ///
    //
    // rotate - Add a rotation to the current transformation by premultiplying
    //          the appropriate rotation matrix to the current transformation
    //          matrix.
    //
    // @param degrees - Amount of rotation in degrees.
    ///
    public void rotate( float degrees )
    {
    	double radians = degrees * Math.PI / 180.0f;
    	double[][] rotateElems = {{Math.cos( radians ), -Math.sin( radians ), 0.},
    							  {Math.sin( radians ), Math.cos( radians ), 0.},
    							  {0., 0., 1.}};
    	Matrix rotateMatrix = new Matrix(rotateElems);
    	
    	this.tMatrix = rotateMatrix.times( this.tMatrix );
    }
    
    ///
    // scale - Add a scale to the current transformation by premultiplying
    //         the appropriate scaling matrix to the current transformation
    //         matrix.
    //
    // @param sx - Amount of scaling in x.
    // @param sy - Amount of scaling in y.
    ///
    public void scale( float sx, float sy )
    {
    	double[][] scaleElems = {{sx, 0., 0.},
    							 {0., sy, 0.},
    							 {0., 0., 1.}};
    	Matrix scaleMatrix = new Matrix(scaleElems);
    	
    	this.tMatrix = scaleMatrix.times( this.tMatrix );

    }
    
    ///
    // setClipWindow - Define the clip window.
    //
    // @param bottom - y coord of bottom edge of clip window (in world coords)
    // @param top - y coord of top edge of clip window (in world coords)
    // @param left - x coord of left edge of clip window (in world coords)
    // @param right - x coord of right edge of clip window (in world coords)
    ///
    public void setClipWindow( float bottom, float top,
                               float left, float right )
    {
    	this.clipB = bottom;
    	this.clipT = top;
    	this.clipL = left;
    	this.clipR = right;
    }
    
    ///
    // setViewport - Define the viewport.
    //
    // @param xmin - x coord of lower left of view window (in screen coords)
    // @param ymin - y coord of lower left of view window (in screen coords)
    // @param width - width of view window (in world coords)
    // @param height - width of view window (in world coords)
    ///
    public void setViewport( int x, int y, int width, int height )
    {
    	this.viewX = x;
    	this.viewY = y;
    	this.viewW = width;
    	this.viewH = height;
    }
}
