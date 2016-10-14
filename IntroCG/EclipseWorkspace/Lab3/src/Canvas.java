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
    // Constructor
    //
    // @param w width of canvas
    // @param h height of canvas
    ///
    Canvas( int w, int h )
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
	numElements = 0;
        points = new Vector<Float>();
        colors = new Vector<Float>();
        elements = new Vector<Integer>();
        currentColor = new Color( 0.0f, 0.0f, 0.0f );
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

}
