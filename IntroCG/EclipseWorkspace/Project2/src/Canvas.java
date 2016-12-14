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
    private Vector<Float> normals;
    private Vector<Float> uv;
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
        normals = new Vector<Float>();
        uv = new Vector<Float>();
        elements = new Vector<Integer>();
	numElements = 0;
    }


    ///
    // adds a triangle to the current shape, along with (u,v) data
    //
    // @param p0 first triangle vertex
    // @param uv0 first vertex (u,v) data
    // @param p1 second triangle vertex
    // @param uv1 second vertex (u,v) data
    // @param p2 final triangle vertex
    // @param uv2 final vertex (u,v) data
    ///
    public void addTriangleWithUV( Vertex p0, UVcoord uv0,
           Vertex p1, UVcoord uv1, Vertex p2, UVcoord uv2 )
    {
        // calculate the normal
        float ux = p1.x - p0.x;
        float uy = p1.y - p0.y;
        float uz = p1.z - p0.z;

        float vx = p2.x - p0.x;
        float vy = p2.y - p0.y;
        float vz = p2.z - p0.z;

        Normal nn = new Normal( (uy * vz) - (uz * vy),
                                (uz * vx) - (ux * vz),
                                (ux * vy) - (uy * vx) );

       // Attach the normal to all 3 vertices
       addTriangleWithNorms( p0, nn, p1, nn, p2, nn );

       // Attach the texture coordinates
       uv.add( new Float(uv0.u) );
       uv.add( new Float(uv0.v) );
       uv.add( new Float(uv1.u) );
       uv.add( new Float(uv1.v) );
       uv.add( new Float(uv2.u) );
       uv.add( new Float(uv2.v) );
    }


    ///
    // adds a triangle to the current shape, along with normal data
    //
    // @param p0 first triangle vertex
    // @param n0 first triangle normal data
    // @param p1 second triangle vertex
    // @param n1 second triangle normal data
    // @param p2 final triangle vertex
    // @param n2 final triangle normal data
    ///
    public void addTriangleWithNorms( Vertex p0, Normal n0,
                       Vertex p1, Normal n1, Vertex p2, Normal n2 )
    {
        points.add( new Float(p0.x) );
        points.add( new Float(p0.y) );
        points.add( new Float(p0.z) );
        points.add( new Float(1.0f) );
	elements.add( new Integer(numElements) );
	numElements++;

        normals.add( new Float(n0.x) );
        normals.add( new Float(n0.y) );
        normals.add( new Float(n0.z) );

        points.add( new Float(p1.x) );
        points.add( new Float(p1.y) );
        points.add( new Float(p1.z) );
        points.add( new Float(1.0f) );
	elements.add( new Integer(numElements) );
	numElements++;

        normals.add( new Float(n1.x) );
        normals.add( new Float(n1.y) );
        normals.add( new Float(n1.z) );

        points.add( new Float(p2.x) );
        points.add( new Float(p2.y) );
        points.add( new Float(p2.z) );
        points.add( new Float(1.0f) );
	elements.add( new Integer(numElements) );
	numElements++;

        normals.add( new Float(n2.x) );
        normals.add( new Float(n2.y) );
        normals.add( new Float(n2.z) );
    }

    ///
    // get the array of vertices for the current shape
    ///
    public Buffer getVertices()
    {
        float v[] = new float[points.size()];
        for( int i = 0; i < points.size(); i++ ) {
            v[i] = (points.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap( v );
    }

    ///
    // get the array of normals for the current shape
    ///
    public Buffer getNormals()
    {
        float n[] = new float[normals.size()];
        for( int i = 0; i < normals.size(); i++ ) {
            n[i] = (normals.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap( n );
    }

    ///
    // get the array of texture coords for the current shape
    ///
    public Buffer getUV()
    {
        float t[] = new float[uv.size()];
        for( int i = 0; i < uv.size(); i++ ) {
            t[i] = (uv.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap( t );
    }

    ///
    // get the array of elements for the current shape
    ///
    public Buffer getElements()
    {
        int e[] = new int[elements.size()];
        for( int i = 0; i < elements.size(); i++ ) {
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
