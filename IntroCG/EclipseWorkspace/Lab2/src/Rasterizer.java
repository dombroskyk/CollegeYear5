//
//  Rasterizer.java
//
//  Created by Warren R. Carithers on 2016/09/30.
//  Based on code created by Joe Geigel on 1/21/10.
//  Copyright 2016 Rochester Institute of Technology. All rights reserved.
//
//  Contributor:  Kevin Dombrosky
//

///
// 
// A simple class for performing rasterization algorithms.
//
///

import java.util.*;

public class Rasterizer {
	
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
    
    ///
    // number of scanlines
    ///
    private int n_scanlines;

    ///
    // Drawing canvas
    ///
    public Canvas C;
    
    ///
    // Constructor
    //
    // @param n number of scanlines
    // @param C drawing canvas to use
    ///
    Rasterizer( int n, Canvas canvas )
    {
        n_scanlines = n;
	C = canvas;
    }
    
    ///
    // Draw a filled polygon in the Canvas C.
    //
    // The polygon has n distinct vertices. The coordinates of the
    // vertices making up the polygon are stored in the x and y arrays.
    // The ith vertex will have coordinate  (x[i], y[i])
    //
    // You are to add the implementation here using only calls to C.setPixel()
    //
    // @param n number of vertices
    // @param x x coordinates
    // @param y y coordinates
    ///
    public void drawPolygon( int n, int x[], int y[] )
    {
    	//initialize edges and add to globalEdgeTable
    	Edge[] globalEdgeTable = new Edge[600];
    	int absMinY = 601;
    	int absMaxY = -1;
        for( int i = 0; i < n-1; i++ ){
        	Edge newEdge = new Edge( x[i], y[i], x[i+1], y[i+1] );
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
        Edge newEdge = new Edge( x[n-1], y[n-1], x[0], y[0] );
        int currMinY = newEdge.minY;
        int currMaxY = newEdge.maxY;
        addToEdgeTable( newEdge, globalEdgeTable );
    	if( currMinY < absMinY ){
    		absMinY = currMinY;
    	}
    	if( currMaxY > absMaxY ){
    		absMaxY = currMaxY;
    	}
        
        for( int i = absMinY; i <= absMaxY; i++ ){
        	System.out.print(i + ": " + globalEdgeTable[i]);
        	if( globalEdgeTable[i] != null ){
        		Edge nextEdge = globalEdgeTable[i].nextEdge;
	        	while( nextEdge != null ){
	        		
	        		System.out.print("|||" + nextEdge.toString());
	        		nextEdge = nextEdge.nextEdge;
	        	}
        	}
        	System.out.println();
        }
        
        int edgeIndex = absMinY;
        Edge activeListHead = null;
        while( edgeIndex <= absMaxY ){
        	System.out.println("iter: " + edgeIndex);
        	// remove all entries from active list whose y is greater than y index
        	if( activeListHead != null ){
	        	while( activeListHead != null && activeListHead.maxY == edgeIndex ){
	        		activeListHead = activeListHead.nextEdge;
	        	}
	        	
	        	if( activeListHead != null ){
		        	Edge currActiveEdge = activeListHead;
		        	while( currActiveEdge.nextEdge != null ){
		        		if( currActiveEdge.nextEdge.maxY <= edgeIndex ){
		        			currActiveEdge.nextEdge = currActiveEdge.nextEdge.nextEdge;
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
	        		while( !placed && currActiveEdge != null && currActiveEdge.nextEdge != null ){
	        			
	        			if( newEdgeHead.xVal < currActiveEdge.nextEdge.xVal ){
		        			Edge insertEdge = newEdgeHead.deepcopy();
		        			newEdgeHead = newEdgeHead.nextEdge;
		        			
		        			insertEdge.nextEdge = currActiveEdge.nextEdge;
		        			currActiveEdge = insertEdge;
		        			placed = true;
		        		} else if( newEdgeHead.xVal == currActiveEdge.nextEdge.xVal && 
		        				newEdgeHead.dX * newEdgeHead.dY * 
		        				newEdgeHead.slopeSign * currActiveEdge.nextEdge.dY <
		        				currActiveEdge.nextEdge.dX * currActiveEdge.nextEdge.dY * 
		        				currActiveEdge.nextEdge.slopeSign * newEdgeHead.dY ){
		        			Edge insertEdge = newEdgeHead.deepcopy();
		        			newEdgeHead = newEdgeHead.nextEdge;
		        			
		        			insertEdge.nextEdge = currActiveEdge.nextEdge;
		        			currActiveEdge = insertEdge;
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
        		System.out.println("in on iter");
        		Edge nextActiveEdge = currActiveEdge.nextEdge;
        		System.out.println(currActiveEdge);
        		System.out.println(nextActiveEdge);
        		if( draw && nextActiveEdge != null ){
        			if( currActiveEdge.xVal > nextActiveEdge.xVal ){
        				for( int currX = nextActiveEdge.xVal; currX < currActiveEdge.xVal; currX++ ){
            				System.out.println("coloring: " + currX + ", " + edgeIndex);
            				C.setPixel(currX, edgeIndex);
            			}
        			}else{
        			for( int currX = currActiveEdge.xVal; currX < nextActiveEdge.xVal; currX++ ){
        				System.out.println("coloring: " + currX + ", " + edgeIndex);
        				C.setPixel(currX, edgeIndex);
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
    public void addToEdgeTable( Edge newEdge, Edge[] globalEdgeTable ){
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
}
