//
//  Canvas.java
//
//  Created by Warren R. Carithers on 2016/09/30.
//  Copyright 2016 Rochester Institute of Technology.  All rights reserved.
//
//  This file should not be modified by students.
//

import java.awt.*;
import java.nio.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import java.util.*;

public class Canvas {

    ///
    // canvas size information
    ///
    private int width;
    private int height;

    ///
    // point-related data
    ///
    private Vector<Float> points;
    private int numElements;
    private Vector<Integer> elements;

    ///
    // color-related data
    ///
    private Vector<Float> colors;
    private Color currentColor;

    ///
    // outline vs. polygon drawing flag
    ///
    public boolean drawingOutlines;
    public int countNum;
    public static int countNumLimit = 100;
    public int outlineCounts[];

    ///
    // Constructor
    //
    // @param w width of canvas
    // @param h height of canvas
    ///
    public Canvas( int w, int h )
    {
        width = w;
        height = h;
	clear();    // do this the easy way
    }

    ///
    // clear the canvas
    ///
    public void clear()
    {
        // easiest method:  just allocate new ones!
	outlineCounts = new int[countNumLimit];
        points = new Vector<Float>();
        colors = new Vector<Float>();
        elements = new Vector<Integer>();
	numElements = 0;
        currentColor = new Color( 0.0f, 0.0f, 0.0f );
	drawingOutlines = false;
	countNum = 0;
    }

    ///
    // change the current drawing color
    //
    // @param r The red component of the new color (between 0-1)
    // @param g The green component of the new color (between 0-1)
    // @param b The blue component of the new color (between 0-1);
    ///
    public void setColor( float r, float g, float b )
    {
        currentColor = new Color( r,g, b );
    }

    ///
    // set a pixel to the current drawing color
    //
    // @param x The x coord of the pixel to be set
    // @param y The y coord of the pixel to be set
    ///
    public void setPixel( float x, float y )
    {
	points.add( new Float(x) );
	points.add( new Float(y) );
	points.add( new Float(-1.0f) );  // fixed Z depth
	points.add( new Float(1.0f) );

	float v[] = currentColor.getRGBColorComponents( null );
	colors.add( new Float(v[0]) );
	colors.add( new Float(v[1]) );
	colors.add( new Float(v[2]) );
	colors.add( new Float(1.0f) );  // alpha channel

        elements.add( new Integer(numElements) );
	numElements += 1;
    }

    ///
    // Draw the outline of a polygon.
    //
    // Use this drawOutline() method only if you were NOT able to
    // create a working drawPolygon() Rasterizer routine of your own.
    // This method will only draw the outline of the polygon.
    //
    // @param n The number of vertices in the polygon
    // @param x The x coordinates of the vertices
    // @param y the y coordinates of the vertices
    /// 
    void drawOutline( int n, int x[], int y[] )
    {
        if( countNum >= countNumLimit ) {
            System.err.println( "too many outlines to draw!" );
            return;
        }

        drawingOutlines = true;
        outlineCounts[countNum++] = n;

        for( int i = 0; i < n; i++ )
            setPixel( x[i], y[i] );
    }

    ///
    // get the array of vertices for the current shape
    ///
    public Buffer getVertices()
    {
        float v[] = new float[points.size()];
        for( int i=0; i < points.size(); i++ ) {
            v[i] = (points.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap( v );
    }

    ///
    // get the array of elements for the current shape
    ///
    public Buffer getElements()
    {
        int e[] = new int[elements.size()];
        for( int i=0; i < elements.size(); i++ ) {
            e[i] = (elements.elementAt(i)).intValue();
        }
        return IntBuffer.wrap( e );
    }

    ///
    // get the colors for the current shape
    ///
    public Buffer getColors()
    {
        float v[] = new float[colors.size()];
        for( int i=0; i < colors.size(); i++ ) {
            v[i] = (colors.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap( v );
    }

    ///
    // get the number of vertices in the current shape
    ///
    public int nVertices()
    {
        return numElements;
    }

    ///
    // return the outline status of this object
    ///
    public boolean isOutline()
    {
        return drawingOutlines;
    }

}
