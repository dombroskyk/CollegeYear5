//
//  Canvas.java
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
    private short numElements;
    private Vector<Short> elements;

    ///
    // color-related data
    ///
    private Vector<Float> colors;
    private Color currentColor;

    Canvas( int w, int h )
    {
        width = w;
        height = h;
	numElements = 0;
        points = new Vector<Float>();
        colors = new Vector<Float>();
        elements = new Vector<Short>();
        currentColor = new Color( 0.0f, 0.0f, 0.0f );
    }

    public void clear()
    {
        // easiest method:  just allocate new ones!
	numElements = 0;
        points = new Vector<Float>();
        colors = new Vector<Float>();
        elements = new Vector<Short>();
        currentColor = new Color( 0.0f, 0.0f, 0.0f );
    }

    public void setColor( float r, float g, float b )
    {
        currentColor = new Color( r,g, b );
    }

    public void setPixel( int x, int y )
    {
	points.add( new Float(x) );
	points.add( new Float(y) );
	points.add( new Float(-1.0) );  // fixed Z depth
	points.add( new Float(1.0) );

	float v[] = currentColor.getRGBColorComponents( null );
	colors.add( new Float(v[0]) );
	colors.add( new Float(v[1]) );
	colors.add( new Float(v[2]) );
	colors.add( new Float(1.0 ) );  // alpha channel

        elements.add( new Short(numElements) );
	numElements += 1;
    }

    /**
     * get the vertex points for the current shape
     */
    public Buffer getVertices()
    {
        float v[] = new float[points.size()];
        for( int i=0; i < points.size(); i++ ) {
            v[i] = (points.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap( v );
    }

    /**
     * get the array of elements for the current shape
     */
    public Buffer getElements()
    {
        short e[] = new short[elements.size()];
        for( int i=0; i < elements.size(); i++ ) {
            e[i] = (elements.elementAt(i)).shortValue();
        }
        return ShortBuffer.wrap( e );
    }

    /**
     * get the colors for the current shape
     */
    public Buffer getColors()
    {
        float v[] = new float[colors.size()];
        for( int i=0; i < colors.size(); i++ ) {
            v[i] = (colors.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap( v );
    }

    public int nVertices()
    {
        return numElements;
    }

}
