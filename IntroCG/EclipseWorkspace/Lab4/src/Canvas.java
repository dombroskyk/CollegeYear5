//
//  Canvas.java
//
//  Created by Warren R. Carithers on 2016/11/03.
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
        elements = new Vector<Integer>();
    }


    ///
    // adds a triangle to the current shape
    //
    // @param p0 first triangle vertex
    // @param p1 second triangle vertex
    // @param p2 final triangle vertex
    ///
    public void addTriangle( Vertex p0, Vertex p1, Vertex p2 )
    {
        points.add( new Float(p0.x) );
        points.add( new Float(p0.y) );
        points.add( new Float(p0.z) );
        points.add( new Float(1.0f) );

        points.add( new Float(p1.x) );
        points.add( new Float(p1.y) );
        points.add( new Float(p1.z) );
        points.add( new Float(1.0f) );

        points.add( new Float(p2.x) );
        points.add( new Float(p2.y) );
        points.add( new Float(p2.z) );
        points.add( new Float(1.0f) );

        numElements += 3;  // three vertices per triangle
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
    // get the number of vertices in the current shape
    ///
    public int nVertices()
    {
        return numElements;
    }

}
