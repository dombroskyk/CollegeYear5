//
//  Rasterizer.java
//
//  Created by Warren R. Carithers on 2016/09/23.
//  Based on code created by Joe Geigel on 2011/11/30.
//  Copyright 2016 Rochester Institute of Technology. All rights reserved.
//
//  Contributor:  Kevin Dombrosky <kfd6490@rit.edu>
//

///
// 
// A simple class for performing rasterization algorithms.
//
///

import java.util.*;

public class Rasterizer {
    
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
    //
    ///

    Rasterizer( int n, Canvas canvas )
    {
        n_scanlines = n;
	C = canvas;
    }

    ///
    // Draw my initials
    //
    // Draw my own initials using calls to drawLine(), in the same
    // manner that makeLines() in the driver program draws 'CG'.
    //
    ///
    void myInitials()
    {

        // ######## Use blue color (0,0.5,1) to write your initials ########

        C.setColor( 0.0f, 0.5f, 1.0f );
        
        // draw block K
        drawLine(50, 50, 50, 250);
        drawLine(50, 250, 100, 250);
        drawLine(100, 250, 100, 175);
        drawLine(100, 175, 175, 250);
        drawLine(175, 250, 225, 250);
        drawLine(225, 250, 125, 150);
        drawLine(125, 150, 225, 50);
        drawLine(225, 50, 175, 50);
        drawLine(175, 50, 100, 125);
        drawLine(100, 125, 100, 50);
        drawLine(100, 50, 50, 50);
        
        C.setColor( 1.0f, 0.654f,  0.0f );
        // draw outer block D
        drawLine(350, 50, 350, 250);
        drawLine(350, 250, 500, 250);
        drawLine(500, 250, 550, 200);
        drawLine(550, 200, 550, 100);
        drawLine(550, 100, 500, 50);
        drawLine(500, 50, 350, 50);
        
        // draw inner block D
        drawLine(400, 100, 400, 200);
        drawLine(400, 200, 475, 200);
        drawLine(475, 200, 500, 175);
        drawLine(500, 175, 500, 125);
        drawLine(500, 125, 475, 100);
        drawLine(475, 100, 400, 100);

    }

    ///
    // Draw a line from (x0,y0) to (x1, y1) on the simpleCanvas C.
    //
    // Implementation should be using the Midpoint Method
    //
    // You are to add the implementation here using only calls
    // to C.setPixel()
    //
    // @param x0 x coord of first endpoint
    // @param y0 y coord of first endpoint
    // @param x1 x coord of second endpoint
    // @param y1 y coord of second endpoint
    ///

    public void drawLine( int x0, int y0, int x1, int y1 )
    {
    	// swap vertices if the second one is to the left of the first
    	// or if the y values are swapped for a vertical line
    	if( x1 < x0 || ( x1 == x0 && y0 > y1 ) ){
    		int tempX = x1;
    		int tempY = y1;
    		x1 = x0;
    		y1 = y0;
    		x0 = tempX;
    		y0 = tempY;
    	}
    	
    	
    	int dE, dNE, dN, dSE, x, y, d;
    	int dy = y1 - y0;
    	int dx = x1 - x0;
    	if( dx == 0 ) {
    		// vertical lines
    		for( y = y0; y <= y1; ++y ){
    			C.setPixel( x0, y );
    		}
    	} else if( dy >= 0 && dx >= dy ){
    		// shallow positive slope
    		
	    	// delta if we choose E
	    	dE = 2 * dy;
	    	// delta if we choose NE
	    	dNE = 2 * ( dy - dx );
	    	// initial d, 2*dy - dx
	    	d = dE - dx;
	    	for( x = x0, y = y0; x <= x1; ++x ) {
	    		C.setPixel( x, y );
	    		if( d <= 0 ) {
	    			// choose E
	    			d += dE;
	    		} else {
	    			// choose NE
	    			++y;
	    			d += dNE;
	    		}
	    	}
    	} else if( dy > dx && dx > 0 ){
    		// steep positive slope
    		
    		//delta if we choose N
    		dN = 2 * dx;
    		//delta if we choose NE
    		dNE = 2 * ( dx - dy );
    		//initial d, dy - 2dx
    		d = dNE - dy;
    		for( x = x0, y = y0; y <= y1; ++y ) {
    			C.setPixel( x,  y );
    			if( d <= 0 ) {
    				// choose N
    				d += dN;
    			} else {
    				// choose NE
    				++x;
    				d += dNE;
    			}
    		}	
    	} else if( dy < 0 && dx >= dy*-1 ) {
    		// shallow negative slope
    		
	    	// delta if we choose E
	    	dE = 2 * -dy;
	    	// delta if we choose SE
	    	dSE = 2 * ( -dy - dx );
	    	// initial d, 2*dy - dx
	    	d = dE - dx;
	    	for( x = x0, y = y0; x <= x1; ++x ) {
	    		C.setPixel( x, y );
	    		if( d <= 0 ) {
	    			// choose E
	    			d += dE;
	    		} else {
	    			// choose NE
	    			--y;
	    			d += dSE;
	    		}
	    	}
    	} else {
    		// steep negative slope
    		
    		//delta if we choose N
    		dN = 2 * dx;
    		//delta if we choose SE
    		dSE = 2 * ( dx + dy );
    		//initial d, dy - 2dx
    		d = dSE + dy;
    		for( x = x0, y = y0; y >= y1; --y ) {
    			C.setPixel( x,  y );
    			if( d <= 0 ) {
    				// choose N
    				d += dN;
    			} else {
    				// choose SE
    				++x;
    				d += dSE;
    			}
    		}
    	}

    }

}
