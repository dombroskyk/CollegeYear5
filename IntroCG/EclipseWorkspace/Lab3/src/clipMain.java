//
//  clipMain.java
//
//  Created by Warren R. Carithers on 2016/09/23.
//  Based on code created by Joe Geigel on 1/21/10.
//  Copyright 2016 Rochester Institute of Technology. All rights reserved.
//
//  Main program for polygon fill assignment.
//
//  This file should not be modified by students.
//

import java.awt.*;
import java.nio.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.*;
import com.jogamp.opengl.util.Animator;

public class clipMain implements GLEventListener, KeyListener {

    ///
    // Object information
    ///
    
    // our clipping regions (ll and ur corners)
    
    private static Vertex clip1[] = {
        new Vertex(  10.0f, 110.0f ), new Vertex(  50.0f, 150.0f )
    };
    private static Vertex clip2[] = {
        new Vertex(  30.0f,  10.0f ), new Vertex(  70.0f,  80.0f )
    };
    private static Vertex clip3[] = {
        new Vertex(  90.0f,  34.0f ), new Vertex( 120.0f,  60.0f )
    };
    private static Vertex clip4[] = {
        new Vertex(  90.0f,  80.0f ), new Vertex( 130.0f, 110.0f )
    };
    
    // our polygons (list of vertices)
    
    private static Vertex quad1[] = {
        new Vertex( 20.0f, 120.0f ),
	new Vertex( 20.0f, 140.0f ),
	new Vertex( 40.0f, 140.0f ),
	new Vertex( 40.0f, 120.0f )
    };
    private int quad1_nv;
    
    private static Vertex quad2[] = {
        new Vertex( 80.0f, 160.0f ),
	new Vertex( 80.0f, 200.0f ),
	new Vertex( 60.0f, 200.0f ),
	new Vertex( 60.0f, 160.0f )
    };
    private int quad2_nv;
    
    private static Vertex quad3[] = {
        new Vertex( 20.0f, 60.0f ),
	new Vertex( 50.0f, 60.0f ),
	new Vertex( 50.0f, 50.0f ),
	new Vertex( 20.0f, 50.0f )
    };
    private int quad3_nv;
    
    private static Vertex quad4[] = {
        new Vertex( 44.0f, 122.0f ),
	new Vertex( 60.0f, 122.0f ),
	new Vertex( 60.0f, 146.0f ),
	new Vertex( 44.0f, 146.0f )
    };
    private int quad4_nv;
    
    private static Vertex pent1[] = {
        new Vertex(  80.0f, 20.0f ),
	new Vertex( 90.0f, 10.0f ),
	new Vertex( 110.0f, 20.0f ),
	new Vertex( 100.0f, 50.0f ),
	new Vertex( 80.0f, 40.0f )
    };
    private int pent1_nv;
    
    private static Vertex hept1[] = {
        new Vertex( 120.0f,  70.0f ),
	new Vertex( 140.0f,  70.0f ),
	new Vertex( 160.0f,  80.0f ),
	new Vertex( 160.0f, 100.0f ),
	new Vertex( 140.0f, 110.0f ),
	new Vertex( 120.0f, 100.0f ),
	new Vertex( 110.0f,  90.0f )
    };
    private int hept1_nv;
    
    // count of vertices in each clipped polygon
    private int nv[];
    
    ///
    // dimensions of drawing window
    ///
    private static int w_width  = 300;
    private static int w_height = 320;  // Java height includes titlebar!

    ///
    // our Canvas and Clipper
    ///
    private Canvas canvas;
    private Clipper clipper;

    ///
    // buffer information
    ///
    private BufferSet clipBuffers;
    private BufferSet polyOutlineBuffers;
    private BufferSet clippedPolyBuffers;

    ///
    // shader program ID
    ///
    private int program;

    ///
    // Canvas and window frame
    ///
    GLCanvas myCanvas;
    private static Frame frame;

    ///
    // Constructor
    public clipMain( GLCanvas G )
    {
        
	myCanvas = G;

	// set up our Canvas and Clipper instances
        canvas = new Canvas( w_width, w_height );
        clipper = new Clipper();

        canvas.setColor( 0.0f, 0.0f, 0.0f );
        canvas.clear();

	// next, set up our object buffers
	clipBuffers = new BufferSet();
	polyOutlineBuffers = new BufferSet();
	clippedPolyBuffers = new BufferSet();

	nv = new int[6];
	for( int i = 0; i < 6; ++i )
	    nv[i] = 0;

	quad1_nv = quad1.length;
	quad2_nv = quad2.length;
	quad3_nv = quad3.length;
	quad4_nv = quad4.length;
	pent1_nv = pent1.length;
	hept1_nv = hept1.length;

	// event handlers
	G.addGLEventListener( this );
        G.addKeyListener( this );

    }

    ///
    // create a buffer
    ///
    private int makeBuffer( GL3 gl3, int target, Buffer data, long size )
    {
        int buffer[] = new int[1];

        gl3.glGenBuffers( 1, buffer, 0 );
        gl3.glBindBuffer( target, buffer[0] );
        gl3.glBufferData( target, size, data, GL.GL_STATIC_DRAW );

        return( buffer[0] );
    }

    ///
    // Creat a set of vertex and element buffers
    ///
    private void createBuffers( GL3 gl3, BufferSet B, Canvas C ) {
    
        // get the vertices
        B.numElements = C.nVertices();
        Buffer points = C.getVertices();
        // #bytes = number of elements * 4 floats/element * bytes/float
        B.vSize = B.numElements * 4l * 4l;
    
        // get the element data
        Buffer elements = C.getElements();
        // #bytes = number of elements * bytes/element
        B.eSize = B.numElements * 4l;
    
        // get the color data
        Buffer colors = C.getColors();
        // #bytes = number of elements * 4 floats/element * bytes/float
        B.cSize = B.numElements * 4l * 4l;
    
        // set up the vertex buffer
        if( B.bufferInit ) {
            // must delete the existing buffers first
	    int buf[] = new int[2];
	    buf[0] =  B.vbuffer;
	    buf[1] = B.ebuffer;
	    gl3.glDeleteBuffers( 2, buf, 0 );
            B.bufferInit = false;
        }
    
        // first, create the connectivity data
        B.ebuffer = makeBuffer( gl3, GL.GL_ELEMENT_ARRAY_BUFFER,
	                        elements, B.eSize
	);
    
        // next, the vertex buffer, containing vertices and color data
        B.vbuffer = makeBuffer( gl3, GL.GL_ARRAY_BUFFER,
	                        null, B.vSize + B.cSize );
        gl3.glBufferSubData( GL.GL_ARRAY_BUFFER, 0, B.vSize, points );
        gl3.glBufferSubData( GL.GL_ARRAY_BUFFER, B.vSize, B.cSize, colors );
    
        // finally, mark it as set up
        B.bufferInit = true;
    
    }
    
    ///
    // Support function that draws clip regions as line loops
    ///
    private void drawClipRegion( Vertex ll, Vertex ur, Canvas C ) {
        C.setPixel( ll.x, ll.y );  // LL
        C.setPixel( ur.x, ll.y );  // LR
        C.setPixel( ur.x, ur.y );  // UR
        C.setPixel( ll.x, ur.y );  // UL
    }
    
    ///
    // Draw all the clipping rectangles
    ///
    private void makeClipOutlines( Canvas C ) {
    
        // we draw the clipping regions as white rectangles.
        // all vertices are put into one vertex buffer, and
        // we use glDrawArrays() instead of glDrawElements()
        // to draw them as line loops
    
        drawClipRegion( clip1[0], clip1[1], C );
        drawClipRegion( clip2[0], clip2[1], C );
        drawClipRegion( clip3[0], clip3[1], C );
        drawClipRegion( clip4[0], clip4[1], C );
    }
    
    ///
    // Draw a polygon
    ///
    private void drawPolygon( Vertex v[], int nv, Canvas C ) {
    
        // just put the vertices into the vertex buffer, in order
    
        for( int i = 0; i < nv; ++i ) {
            C.setPixel( v[i].x, v[i].y );
        }
    }
    
    ///
    // Create the polygon outlines
    ///
    private void makePolygonOutlines( Canvas C ) {
    
        // here, we draw the original polygons; these
        // will be rendered using line loops
    
        C.setColor( 1.0f, 0.0f, 0.0f );	// red
        drawPolygon( quad1, quad1_nv, C );
        C.setColor( 0.0f, 1.0f, 0.0f );     // green
        drawPolygon( quad2, quad2_nv, C );
        C.setColor( 0.0f, 0.0f, 1.0f );	// blue
        drawPolygon( quad3, quad3_nv, C );
        C.setColor( 1.0f, 0.0f, 1.0f );	// magenta
        drawPolygon( quad4, quad4_nv, C );
        C.setColor( 1.0f, 0.5f, 1.0f );     // red-greenish-blue
        drawPolygon( pent1, pent1_nv, C );
        C.setColor( 0.7f, 0.7f, 0.7f );	// gray
        drawPolygon( hept1, hept1_nv, C );
    }
    
    ///
    // Create the filled polygons
    ///
    private void makePolygons( Canvas C ) {
        // temporary vertex array
        Vertex tmp[] = new Vertex[50];
        int wl;
    
        ///
        // first polygon:  entirely within region
        ///
    
        C.setColor( 1.0f, 0.0f, 0.0f );	// red
        wl = 0;
        wl = clipper.clipPolygon( 4, quad1, tmp, clip1[0], clip1[1] );
        if( wl > 0 ) {
            drawPolygon( tmp, wl, C );
    	    nv[0] = wl;
        }
    
        ///
        // second polygon:  entirely outside region
        ///
    
        C.setColor( 0.0f, 1.0f, 0.0f ); // green
        wl = 0;
        wl = clipper.clipPolygon( 4, quad2, tmp, clip1[0], clip1[1] );
        // shouldn't draw anything!
        if( wl > 0 ) {
            drawPolygon( tmp, wl, C );
    	    nv[1] = wl;
        }
    
        ///
        // third polygon:  halfway outside on left
        ///
    
        C.setColor( 0.0f, 0.0f, 1.0f );	// blue
        wl = 0;
        wl = clipper.clipPolygon( 4, quad3, tmp, clip2[0], clip2[1] );
        if( wl > 0 ) {
            drawPolygon( tmp, wl, C );
    	    nv[2] = wl;
        }
    
        ///
        // fourth polygon:  part outside on right
        ///
    
        C.setColor( 1.0f, 0.0f, 1.0f );	// magenta
        wl = 0;
        wl = clipper.clipPolygon( 4, quad4, tmp, clip1[0], clip1[1] );
        if( wl > 0 ) {
            drawPolygon( tmp, wl, C );
    	    nv[3] = wl;
        }
    
        ///
        // fifth polygon:  outside on left and bottom
        ///
    
        C.setColor( 1.0f, 0.5f, 1.0f ); // red-greenish-blue
        wl = 0;
        wl = clipper.clipPolygon( 5, pent1, tmp, clip3[0], clip3[1] );
        if( wl > 0 ) {
            drawPolygon( tmp, wl, C );
    	    nv[4] = wl;
        }
    
        ///
        // sixth polygon:  outside on top, right, and bottom
        ///
    
        C.setColor( 0.7f, 0.7f, 0.7f );	// gray
        wl = 0;
        wl = clipper.clipPolygon( 7, hept1, tmp, clip4[0], clip4[1] );
        if( wl > 0 ) {
            drawPolygon( tmp, wl, C );
    	    nv[5] = wl;
        }
    
    }
    
    ///
    // Create all our objects
    ///
    private void createImage( GL3 gl3, Canvas C )
    {
        // start with a clean canvas
        C.clear();
    
        // first, create the clipping region buffers
        //
        // start by putting all the vertices for all
        // the regions into a single set of buffers
    
        // draw all of them in white
        C.setColor( 1.0f, 1.0f, 1.0f );
        makeClipOutlines( C );
    
        // collect the vertex, element, and color data for these
        createBuffers( gl3, clipBuffers, C );
    
        // next, do the polygon outlines
        C.clear();
        makePolygonOutlines( C );
        createBuffers( gl3, polyOutlineBuffers, C );
    
        // finally, do the polygons
        C.clear();
        makePolygons( C );
        createBuffers( gl3, clippedPolyBuffers, C );
    
    }

    ///
    // verify shader creation
    ///
    private void checkShaderError( shaderSetup myShaders, int program,
        String which )
    {
        if( program == 0 ) {
            System.err.println( "Error setting up " + which + " shader - " +
                myShaders.errorString(myShaders.shaderErrorCode)
            );
            System.exit( 1 );
        }
    }

    ///
    // OpenGL initialization
    ///
    public void init( GLAutoDrawable drawable )
    {
        // get the GL object
        GL3 gl3 = drawable.getGL().getGL3();

        // Load shaders and use the resulting shader program
        shaderSetup myShaders = new shaderSetup();

        program = myShaders.readAndCompile(gl3, "shader.vert", "shader.frag");
        checkShaderError( myShaders, program, "basic" );

        // OpenGL state initialization
        gl3.glEnable( GL.GL_DEPTH_TEST );
        // gl3.glEnable( GL.GL_CULL_FACE );
        // gl3.glCullFace( GL.GL_BACK );
        gl3.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );
        gl3.glDepthFunc( GL.GL_LEQUAL );
        gl3.glClearDepth( 1.0f );

        // create the geometry for our shapes.
        createImage( gl3, canvas );
    }
    
    ///
    // Called by the drawable during the first repaint
    // after the component has been resized.
    ///
    public void reshape( GLAutoDrawable drawable, int x, int y,
        int width, int height )
    {
    }

    ///
    // Called by the drawable to initiate OpenGL rendering by the client.
    ///
    public void display( GLAutoDrawable drawable )
    {
        // get GL
        GL3 gl3 = drawable.getGL().getGL3();

        // clear the frame buffer
        gl3.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

        // ensure we have selected the correct shader program
        gl3.glUseProgram( program );

        // set up our uniforms
        int wh = gl3.glGetUniformLocation( program, "wh" );
        gl3.glUniform2f( wh, (float) w_width, (float) w_height );

        ///
        // first, draw the clip region outlines
        ///
        int vPosition, vColor;
    
        // bind our buffers
        gl3.glBindBuffer( GL.GL_ARRAY_BUFFER, clipBuffers.vbuffer );
        gl3.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, clipBuffers.ebuffer );

        // set up our attribute variables
        vPosition = gl3.glGetAttribLocation( program, "vPosition" );
        gl3.glEnableVertexAttribArray( vPosition );
        gl3.glVertexAttribPointer( vPosition, 4, GL.GL_FLOAT, false, 0, 0l );
        vColor = gl3.glGetAttribLocation( program, "vColor" );
        gl3.glEnableVertexAttribArray( vColor );
        gl3.glVertexAttribPointer( vColor, 4, GL.GL_FLOAT, false, 0,
                                   clipBuffers.vSize );

        // draw our shapes
        gl3.glDrawArrays( GL.GL_LINE_LOOP,  0, 4 );
        gl3.glDrawArrays( GL.GL_LINE_LOOP,  4, 4 );
        gl3.glDrawArrays( GL.GL_LINE_LOOP,  8, 4 );
        gl3.glDrawArrays( GL.GL_LINE_LOOP, 12, 4 );
    
        ///
        // next, draw the polygon outlines
        ///
    
        // bind our buffers
        gl3.glBindBuffer( GL.GL_ARRAY_BUFFER,
	                  polyOutlineBuffers.vbuffer );
        gl3.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER,
	                  polyOutlineBuffers.ebuffer );
    
        // set up our attribute variables
        vPosition = gl3.glGetAttribLocation( program, "vPosition" );
        gl3.glEnableVertexAttribArray( vPosition );
        gl3.glVertexAttribPointer( vPosition, 4, GL.GL_FLOAT, false, 0, 0l );
        vColor = gl3.glGetAttribLocation( program, "vColor" );
        gl3.glEnableVertexAttribArray( vColor );
        gl3.glVertexAttribPointer( vColor, 4, GL.GL_FLOAT, false, 0,
                               polyOutlineBuffers.vSize );
    
        // draw our shapes
        int skip = 0;
        gl3.glDrawArrays( GL.GL_LINE_LOOP, skip, quad1_nv ); skip += quad1_nv;
        gl3.glDrawArrays( GL.GL_LINE_LOOP, skip, quad2_nv ); skip += quad2_nv;
        gl3.glDrawArrays( GL.GL_LINE_LOOP, skip, quad3_nv ); skip += quad3_nv;
        gl3.glDrawArrays( GL.GL_LINE_LOOP, skip, quad4_nv ); skip += quad4_nv;
        gl3.glDrawArrays( GL.GL_LINE_LOOP, skip, pent1_nv ); skip += pent1_nv;
        gl3.glDrawArrays( GL.GL_LINE_LOOP, skip, hept1_nv ); skip += hept1_nv;
    
        ///
        // finally, draw the clipped polygons
        ///
    
        // bind our buffers
        gl3.glBindBuffer( GL.GL_ARRAY_BUFFER,
	                  clippedPolyBuffers.vbuffer );
        gl3.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER,
	                  clippedPolyBuffers.ebuffer );
    
        // set up our attribute variables
        vPosition = gl3.glGetAttribLocation( program, "vPosition" );
        gl3.glEnableVertexAttribArray( vPosition );
        gl3.glVertexAttribPointer( vPosition, 4, GL.GL_FLOAT, false, 0,
                               0l );
        vColor = gl3.glGetAttribLocation( program, "vColor" );
        gl3.glEnableVertexAttribArray( vColor );
        gl3.glVertexAttribPointer( vColor, 4, GL.GL_FLOAT, false, 0,
                               clippedPolyBuffers.vSize );
    
        // draw our shapes
        //
        // be sure to only draw what's there
        skip = 0;
        if( nv[0] != 0 ) {
            gl3.glDrawArrays( GL.GL_TRIANGLE_FAN, skip, nv[0] ); skip += nv[0];
        }
        if( nv[1] != 0 ) {
            gl3.glDrawArrays( GL.GL_TRIANGLE_FAN, skip, nv[1] ); skip += nv[1];
        }
        if( nv[2] != 0 ) {
            gl3.glDrawArrays( GL.GL_TRIANGLE_FAN, skip, nv[2] ); skip += nv[2];
        }
        if( nv[3] != 0 ) {
            gl3.glDrawArrays( GL.GL_TRIANGLE_FAN, skip, nv[3] ); skip += nv[3];
        }
        if( nv[4] != 0 ) {
            gl3.glDrawArrays( GL.GL_TRIANGLE_FAN, skip, nv[4] ); skip += nv[4];
        }
        if( nv[5] != 0 ) {
            gl3.glDrawArrays( GL.GL_TRIANGLE_FAN, skip, nv[5] ); skip += nv[5];
        }
    }

    ///
    // Notifies the listener to perform the release of all OpenGL
    // resources per GLContext, such as memory buffers and GLSL
    // programs.
    ///
    public void dispose( GLAutoDrawable drawable )
    {
    }

    ///
    // Because I am a Key Listener... we'll respond to key presses
    ///
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}

    ///
    // Invoked when a key has been pressed.
    ///
    public void keyPressed(KeyEvent e)
    {
        // Get the key that was pressed
        char key = e.getKeyChar();

        // Respond appropriately
        switch( key ) {

            case 'q': case 'Q':
                frame.dispose();
                System.exit( 0 );
                break;
        }

        // do a redraw
        myCanvas.display();
    }

    ///
    // main program for the polygon clip assignment
    ///
    public static void main(String [] args)
    {
        // GL setup
        GLProfile glp = GLProfile.get( GLProfile.GL3 );
        GLCapabilities caps = new GLCapabilities( glp );
        GLCanvas canvas = new GLCanvas( caps );

        // create the fill object
        clipMain myMain = new clipMain( canvas );

        frame = new Frame( "Lab 3 - Polygon Clipping" );
        frame.setSize( w_width, w_height );
        frame.add( canvas );
        frame.setVisible( true );

        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
        frame.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent e ) {
                frame.dispose();
                System.exit( 0 );
            }
        });
    }

}
