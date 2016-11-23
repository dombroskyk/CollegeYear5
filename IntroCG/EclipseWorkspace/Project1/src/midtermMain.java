//
//  midtermMain.java
//
//  Main program for CG 2D Pipeline midterm
//
//  Created by Warren R. Carithers on 2016/10/23.
//  Based on code created by Joe Geigel on 1/21/10.
//  Copyright 2016 Rochester Institute of Technology. All rights reserved.
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

public class midtermMain implements GLEventListener, KeyListener, MouseListener
{

    ///
    // Object information
    ///

    // Which image are we showing?
    private int displayNumber = 1;

    // Polygon IDs for our objects
    private int triangle;
    private int square;
    private int octagon;
    private int star;
    private int R;
    private int I;
    private int T;
    private int H;
    
    // Object vertex lists
    private static Vertex triangleVerts[] = {
        new Vertex( 25.0f, 125.0f ), new Vertex( 75.0f, 125.0f ),
	new Vertex( 50.0f, 175.0f )
    };
    private static Vertex squareVerts[] = {
        new Vertex( 125.0f, 125.0f ), new Vertex( 175.0f, 125.0f ),
        new Vertex( 175.0f, 175.0f ), new Vertex( 125.0f, 175.0f )
    };
    private static Vertex octagonVerts[] = {
        new Vertex( 25.0f, 25.0f ), new Vertex( 35.0f, 15.0f ),
	new Vertex( 55.0f, 15.0f ), new Vertex( 75.0f, 25.0f ),
        new Vertex( 75.0f, 55.0f ), new Vertex( 55.0f, 75.0f ),
	new Vertex( 35.0f, 75.0f ), new Vertex( 25.0f, 55.0f )
    };
    private static Vertex starVerts[] = {
        new Vertex( 150.0f, 90.0f ), 
    		new Vertex( 140.0f, 65.0f ),
	new Vertex( 110.0f, 65.0f ),
    		new Vertex( 140.0f, 40.0f ),
        new Vertex( 110.0f, 10.0f ),
        new Vertex( 150.0f, 25.0f ),
	new Vertex( 190.0f, 10.0f ), new Vertex( 160.0f, 40.0f ),
        new Vertex( 190.0f, 65.0f ), 
        new Vertex( 160.0f, 65.0f )
    };
    private static Vertex rVerts[] = {
        new Vertex(  10.0f, 480.0f ), new Vertex( 140.0f, 480.0f ),
	new Vertex( 175.0f, 450.0f ), new Vertex( 175.0f, 430.0f ),
	new Vertex( 140.0f, 370.0f ), new Vertex(  90.0f, 370.0f ),
        new Vertex( 175.0f, 140.0f ), new Vertex( 145.0f, 140.0f ),
	new Vertex(  65.0f, 370.0f ), new Vertex(  35.0f, 370.0f ),
	new Vertex(  35.0f, 140.0f ), new Vertex(  10.0f, 140.0f )
    };
    private static Vertex iVerts[] = {
        new Vertex( 190.0f, 480.0f ), new Vertex( 340.0f, 480.0f ),
	new Vertex( 340.0f, 440.0f ), new Vertex( 280.0f, 440.0f ),
	new Vertex( 280.0f, 180.0f ), new Vertex( 340.0f, 180.0f ),
        new Vertex( 340.0f, 140.0f ), new Vertex( 190.0f, 140.0f ),
	new Vertex( 190.0f, 180.0f ), new Vertex( 250.0f, 180.0f ),
	new Vertex( 250.0f, 440.0f ), new Vertex( 190.0f, 440.0f )
    };
    private static Vertex tVerts[] = {
        new Vertex( 360.0f, 480.0f ), new Vertex( 480.0f, 480.0f ),
	new Vertex( 480.0f, 440.0f ), new Vertex( 430.0f, 440.0f ),
	new Vertex( 430.0f, 140.0f ), new Vertex( 400.0f, 140.0f ),
        new Vertex( 400.0f, 440.0f ), new Vertex( 360.0f, 440.0f )
    };
    private static Vertex hVerts[] = {
        new Vertex(  35.0f, 450.0f ), new Vertex( 110.0f, 450.0f ),
	new Vertex( 130.0f, 430.0f ), new Vertex( 110.0f, 410.0f ),
	new Vertex(  35.0f, 410.0f )
    };
    
    ///
    // dimensions of drawing window
    ///
    private static int w_width  = 500;
    private static int w_height = 520;  // Java height includes titlebar!

    ///
    // our Pipeline
    ///
    private Pipeline pipeline;

    ///
    // variables related to drawing the resulting polygons
    ///
    private BufferSet polyBuffers;

    ///
    // shader program handle
    ///
    private int program;

    ///
    // Canvas and window frame
    ///
    GLCanvas myCanvas;
    private static Frame frame;

    ///
    // Constructor
    ///
    public midtermMain( GLCanvas G )
    {
	myCanvas = G;

	// set up our Pipeline instance
        pipeline = new Pipeline( w_width, w_height );

        pipeline.setColor( 0.0f, 0.0f, 0.0f );
        pipeline.clear();

	// next, set up our object buffers
	polyBuffers = new BufferSet();

	// event handlers
	G.addGLEventListener( this );
        G.addKeyListener( this );
	G.addMouseListener( this );
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
    // Create all polygon objects
    ///
    private void createPolygons( GL3 gl3, Pipeline P ) {
        triangle = P.addPoly( triangleVerts, 3 );
        square   = P.addPoly( squareVerts, 4 );
        octagon  = P.addPoly( octagonVerts, 8 );
        star     = P.addPoly( starVerts, 10 );
        R        = P.addPoly( rVerts, 12 );
        I        = P.addPoly( iVerts, 12 );
        T        = P.addPoly( tVerts, 8 );
        H        = P.addPoly( hVerts, 5 );
    }

    ///
    // Draw plain old polygons.
    ///

    private void drawPolysNorm( Pipeline P  ) {

        ///
        // Draw a dark blue/purple triangle
        ///
        P.clearTransform();
        P.setColor( 0.25f, 0.0f, 0.74f );
        P.drawPoly( triangle );

        ///
        // Draw a green square
        ///
        P.setColor( 0.0f, 0.91f, 0.08f );
        P.drawPoly( square );

        ///
        // Draw a pink otcagon
        ///
        P.setColor( 0.98f, 0.0f, 0.48f );
        P.drawPoly( octagon );

        ///
        // Draw a green star
        ///
        P.setColor( 0.68f, 0.0f, 0.75f );
        P.drawPoly( star );

    }

    ///
    // Draw the world transformed...used for transformation tests.
    ///
    private void drawPolysXform( Pipeline P ) {

        ///
        // Draw a dark blue/purple triangle rotated
        ///
        P.clearTransform();
        P.rotate( -25.0f );
        P.setColor( 0.25f, 0.0f, 0.74f );
        P.drawPoly( triangle );
    
        ///
        // Draw a green square translated
        ///
        P.clearTransform();
        P.translate( 80.0f, 75.0f );
        P.setColor( 0.0f, 0.91f, 0.08f );
        P.drawPoly( square );
    
        ///
        // Draw a pink octagon scaled
        ///
        P.clearTransform();
        P.scale( 0.75f, 0.5f );
        P.setColor( 0.98f, 0.0f, 0.48f );
        P.drawPoly( octagon );
    
        ///
        // Draw a green star translated, scaled, rotated, then scaled back
        ///
        P.clearTransform();
        P.rotate( -10.0f );
        P.translate( 50.0f, 50.0f );
        P.scale( 2.0f, 2.0f );
        P.translate( -50.0f, 50.0f );
        P.setColor( 0.68f, 0.0f, 0.75f );
        P.drawPoly( star );
    }

    ///
    // Draw the RIT letters.
    ///
    void drawLetters( Pipeline P )
    {
        ///
        // Draw with staggered translation
        ///
    
        P.clearTransform();
        P.translate(15.0f, 0.0f);
        P.drawPoly( R );
    
        P.clearTransform();
        P.translate(10.0f, 0.0f);
        P.drawPoly( I );
    
        P.clearTransform();
        P.translate(5.0f, 0.0f);
        P.drawPoly( T );
    
        P.clearTransform();
        P.translate(15.0f, 0.0f);
        P.setColor(0.0f, 0.0f, 0.0f);
        P.drawPoly( H );
    }

    ///
    // Draw the objects
    ///
    public void drawObjects( Pipeline P )
    {
        ///
        // Set clear color to black
        ///
        P.setColor ( 0.0f, 0.0f, 0.0f );
        P.clear();
	P.clearTransform();
	switch( displayNumber % 4 ) {

            case 1: // plain old polygon test

                // default clip window
                P.setClipWindow( 0.0f, (float)(w_height-1),
		                 0.0f, (float)(w_width-1)   );

                // default viewport
                P.setViewport( 0, 0, w_width, w_height );

                // draw the polys
                drawPolysNorm( P );
		break;
        
	    case 2: // clipping test

                // clipping test
                P.setClipWindow( 35.0f, 175.0f, 35.0f, 165.0f );

                // default viewport
                P.setViewport( 0, 0, w_width, w_height );

                // draw the polys
                drawPolysNorm( P );
		break;

            case 3: // varying transformations

                // default clip window
                P.setClipWindow( 0.0f, (float)(w_height-1),
		                 0.0f, (float)(w_width-1)   );

                // default viewport
                P.setViewport( 0, 0, w_width, w_height );

                // draw the tranformed polys
                drawPolysXform( P );
		break;

            case 0: // multiple varying viewports

                // default clip window
                P.setClipWindow( 0.0f, (float)(w_height-1),
		                 0.0f, (float)(w_width-1)   );

                // have some fun with the view port
                int wdiff = w_width / 3;
                int hdiff = w_height / 3;

                // will use xaspect and yaspect to
                // draw each row with a different ratio
                int xaspect = w_width / 3;
                int yaspect = w_height / 3;
                int x = 0;
                int y = 0;
                int i, j;

                for( i = 0; i < 3; i++ ) {
                    // adjust yaspect
                    yaspect = hdiff/(i+1);

                    for( j = 0; j < 3; j++ ) {
                        // let's play around with colors
                        if( i == j ) // 1::1 ratios
                            P.setColor( 0.98f, 0.31f, 0.08f );
                        else if( i < j ) // yaspect is smaller
                            P.setColor( 0.0f, 0.91f, 0.08f );
                        else // i > j, xaspect larger
                            P.setColor( 0.98f, 0.0f, 0.48f );

                        // adjust xaspect and shift to next column
                        xaspect = wdiff/(j+1);
                        P.setViewport( x, y, xaspect, yaspect );
                        drawLetters( P );
                        x += wdiff + 35;
                    }

                    // shift to next row, also add a little extra space
                    // due to aspect ratio stuff making lots of blank canvas
                    y += hdiff + 35;

                    // change y aspect ratio
                    xaspect = wdiff;

                    // reset to left side of canvas
                    x = 0;
                }
		break;

            }

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
        createPolygons( gl3, pipeline );
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

	// draw the objects and create the buffers
	drawObjects( pipeline );
	createBuffers( gl3, polyBuffers, pipeline );
    
        // bind our buffers
        gl3.glBindBuffer( GL.GL_ARRAY_BUFFER, polyBuffers.vbuffer );
        gl3.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, polyBuffers.ebuffer );

        // set up the attribute variables and draw the objects
        int vPosition, vColor;

        vPosition = gl3.glGetAttribLocation( program, "vPosition" );
        gl3.glEnableVertexAttribArray( vPosition );
        gl3.glVertexAttribPointer( vPosition, 4, GL.GL_FLOAT, false, 0, 0l );

        vColor = gl3.glGetAttribLocation( program, "vColor" );
        gl3.glEnableVertexAttribArray( vColor );
        gl3.glVertexAttribPointer( vColor, 4, GL.GL_FLOAT, false, 0,
                                   polyBuffers.vSize );

	if( pipeline.drawingOutlines ) {

            // draw the objects as separate line loops

	    int skip = 0;
	    for( int i = 0; i < pipeline.countNum; ++i ) {
                gl3.glDrawArrays( GL.GL_LINE_LOOP, skip,
		                  pipeline.outlineCounts[i] );
		skip += pipeline.outlineCounts[i];
	    }

	} else {
    
            // draw all the individual points

	    gl3.glDrawElements( GL.GL_POINTS, polyBuffers.numElements,
	                        GL.GL_UNSIGNED_INT, 0l );

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

	    case 'P':  // basic polygons
	    case '1':
	        displayNumber = 1;
		break;

	    case 'C':  // altered clipping
	    case '2':
	        displayNumber = 2;
		break;

	    case 'T':  // object transformations
	    case '3':
	        displayNumber = 3;
		break;

	    case 'V':  // basic polygons
	    case '4':
	        displayNumber = 4;
		break;

	    default:   // everything else
	        return;

        }

        // do a redraw
        myCanvas.display();
    }

    @Override
    public void mouseClicked( MouseEvent arg0 ) {
        displayNumber++;
        myCanvas.display();
    }

    @Override
    public void mouseEntered( MouseEvent arg0 ) {
        // not used
    }

    @Override
    public void mouseExited( MouseEvent arg0 ) {
        // not used
    }

    @Override
    public void mousePressed( MouseEvent arg0 ) {
        // not used
    }

    @Override
    public void mouseReleased( MouseEvent arg0 ) {
        // not used
    }

    ///
    // main program for the 2D pipeline assignment
    ///
    public static void main(String [] args)
    {
        // GL setup
        GLProfile glp = GLProfile.get( GLProfile.GL3 );
        GLCapabilities caps = new GLCapabilities( glp );
        GLCanvas canvas = new GLCanvas( caps );

        // create the fill object
        midtermMain myMain = new midtermMain( canvas );

        frame = new Frame( "Project 1 - Midterm" );
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
