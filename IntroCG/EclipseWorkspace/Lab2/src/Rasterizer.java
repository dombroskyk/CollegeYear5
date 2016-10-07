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
		
		private int minY;
		private int maxY;
		private int xVal;
		private double m;
		
		Edge( int x0, int y0, int x1, int y1 ){
			if( y0 > y1 ){
				this.minY = y1;
				this.xVal = x1;
				this.maxY = y0;
			} else {
				this.minY = y0;
				this.xVal = x0;
				this.maxY = y1;
			}
			if( x0 - x1 == 0 ){
				this.m = Double.POSITIVE_INFINITY;
			} else {
				this.m = (y0 - y1) / (x0 - x1);
			}
		}
		
		public String toString(){
			return "Edge - minY: " + Integer.toString(this.minY) + ", maxY: " +
					Integer.toString(this.maxY) + ", xVal: " + 
					Integer.toString(this.xVal) + ", m: " + 
					Double.toString(this.m);
		}
	}
	
	private class EdgeComparator implements Comparator{
		
		
		public int compare( Edge e1, Edge e2 ){
			if( e1.minY < e2.minY ){
				return -1;
			} else if ( e2.minY < e1.minY ){
				return 1;
			} else {
				return 0;
			}
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
    	System.out.println("One invocation");
    	Edge[] allEdges = new Edge[n];
        for( int i = 0; i < n-1; i++ ){
        	allEdges[i] = new Edge( x[i], y[i], x[i+1], y[i+1] );
        }
        allEdges[n-1] = new Edge( x[n-1], y[n-1], x[0], y[0] );
        
        for( Edge edge : allEdges ){
        	System.out.println(edge);
        }
        
        Edge[] globalEdgeTable = new Edge[n];

    }
}
