//
//  fillMain.java
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

public class fillMain implements GLEventListener, KeyListener {
    
    ///
    // dimensions of drawing window
    ///
    private static int w_width  = 900;
    private static int w_height = 600;

    ///
    // our Canvas and Rasterizer
    ///
    private Canvas C;
    private Rasterizer R;

    ///
    // buffer information
    ///
    private Boolean bufferInit;
    private int vbuffer;
    private int ebuffer;
    private int numElements;
    private long vdataSize, edataSize, cdataSize;

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
    public fillMain( GLCanvas G )
    {
        
	myCanvas = G;

        C = new Canvas( w_width, w_height );
        R = new Rasterizer( w_height, C );

        C.setColor( 0.0f, 0.0f, 0.0f );
        C.clear();

	bufferInit = false;
	numElements = 0;

	G.addGLEventListener( this );
        G.addKeyListener( this );

    }

    private void makePolygons( Rasterizer R )
    {

	// start with a clean canvas
	R.C.clear();

	// make these large enough for any of our polygons
        int x[] = new int[10];
        int y[] = new int[10];

        // ########### TEAPOT START ###########
        // BASE
        x[0] = 760;  y[0] =  40;
        x[1] = 600;  y[1] =  40;
        x[2] = 620;  y[2] =  60;
        x[3] = 740;  y[3] =  60;
	R.C.setColor( 1.0f, 0.0f, 0.0f );
        R.drawPolygon( 4, x, y );
    
        // RIGHT BOTTOM TRIANGLE
        /*x[0] = 800;  y[0] = 120;
        x[1] = 740;  y[1] =  60;
        x[2] = 620;  y[2] =  60;
        R.C.setColor( 0.90f, 0.0f, 0.0f );
        R.drawPolygon( 3, x, y );
    
        // SPOUT
        x[0] = 620;  y[0] =  60;
        x[1] = 560;  y[1] = 100;
        x[2] = 500;  y[2] = 180;
        R.C.setColor( 0.80f, 0.0f, 0.0f );
        R.drawPolygon( 3, x, y );
    
        x[0] = 620;  y[0] =  60;
        x[1] = 500;  y[1] = 180;
        x[2] = 460;  y[2] = 200;
        x[3] = 520;  y[3] = 200;
        x[4] = 580;  y[4] = 160;
        R.C.setColor( 0.7f, 0.0f, 0.0f );
        R.drawPolygon( 5, x, y );
    
        x[0] = 620;  y[0] =  60;
        x[1] = 580;  y[1] = 160;
        x[2] = 620;  y[2] = 240;
        x[3] = 740;  y[3] = 240;
        x[4] = 800;  y[4] = 120;
        R.C.setColor( 0.6f, 0.0f, 0.0f );
        R.drawPolygon( 5, x, y );
    
        x[0] = 800;  y[0] = 120;
        x[1] = 840;  y[1] = 160;
        x[2] = 855;  y[2] = 200;
        x[3] = 720;  y[3] = 220;
        x[4] = 720;  y[4] = 200;
        x[5] = 830;  y[5] = 190;
        x[6] = 825;  y[6] = 165;
        x[7] = 780;  y[7] = 120;
        R.C.setColor( 0.5f, 0.0f, 0.0f );
        R.drawPolygon( 8, x, y );
    
        x[0] = 690;  y[0] = 240;
        x[1] = 710;  y[1] = 260;
        x[2] = 650;  y[2] = 260;
        x[3] = 670;  y[3] = 240;
        R.C.setColor( 0.4f, 0.0f, 0.0f );
        R.drawPolygon( 4, x, y );
    
        // ######## TRIANGLE #######
        x[0] = 460;  y[0] = 220;
        x[1] = 490;  y[1] = 280;
        x[2] = 420;  y[2] = 280;
        R.C.setColor( 0.0f, 1.0f, 0.0f );
        R.drawPolygon( 3, x, y );
    
        // ########## QUAD ##########
        x[0] = 380;  y[0] = 280;
        x[1] = 320;  y[1] = 320;
        x[2] = 360;  y[2] = 380;
        x[3] = 420;  y[3] = 340;
        R.C.setColor( 0.0f, 0.8f, 0.8f );
        R.drawPolygon( 4, x, y );
    
        // ############ STAR #############
        // RIGHT SIDE
        x[0] = 230;  y[0] = 389;
        x[1] = 260;  y[1] = 369;
        x[2] = 254;  y[2] = 402;
        x[3] = 278;  y[3] = 425;
        x[4] = 245;  y[4] = 430;
        x[5] = 230;  y[5] = 460;
        x[6] = 230;  y[6] = 410;
        R.C.setColor( 0.8f, 0.8f, 0.0f );
        R.drawPolygon( 7, x, y );
    
        // LEFT SIDE
        x[0] = 230;  y[0] = 460;
        x[1] = 216;  y[1] = 430;
        x[2] = 183;  y[2] = 425;
        x[3] = 207;  y[3] = 402;
        x[4] = 201;  y[4] = 369;
        x[5] = 230;  y[5] = 389;
        x[6] = 230;  y[6] = 410;
        R.C.setColor( 0.7f, 0.7f, 0.0f );
        R.drawPolygon( 7, x, y );
    
        // ########## BORDERS ###############
        // SQUARE BOTTOM LEFT
        x[0] =   0;  y[0] =   0;
        x[1] =   0;  y[1] =  20;
        x[2] =  20;  y[2] =  20;
        x[3] =  20;  y[3] =   0;
        R.C.setColor( 0.0f, 0.0f, 1.0f );
        R.drawPolygon( 4, x, y );
    
        x[0] =   0;  y[0] =  10;
        x[1] =  10;  y[1] =  10;
        x[2] =  10;  y[2] = 580;
        x[3] =   0;  y[3] = 580;
        R.C.setColor( 0.0f, 0.1f, 0.9f );
        R.drawPolygon( 4, x, y );
    
        x[0] =   0;  y[0] = 580;
        x[1] =   0;  y[1] = 599;
        x[2] =  20;  y[2] = 599;
        x[3] =  20;  y[3] = 580;
        R.C.setColor( 0.0f, 0.2f, 0.8f );
        R.drawPolygon( 4, x, y );
    
        //  TRIANGLE TOP:TOP
        x[0] =  10;  y[0] = 590;
        x[1] =  10;  y[1] = 599;
        x[2] = 880;  y[2] = 599;
        R.C.setColor( 0.0f, 0.3f, 0.7f );
        R.drawPolygon( 3, x, y );
    
        //  TRIANGLE TOP:BOTTOM
        x[0] =  10;  y[0] = 590;
        x[1] = 880;  y[1] = 590;
        x[2] = 880;  y[2] = 599;
        R.C.setColor( 0.0f, 0.4f, 0.6f );
        R.drawPolygon( 3, x, y );
    
        // SQUARE TOP RIGHT
        x[0] = 899;  y[0] = 599;
        x[1] = 899;  y[1] = 580;
        x[2] = 880;  y[2] = 580;
        x[3] = 880;  y[3] = 599;
        R.C.setColor( 0.1f, 0.4f, 0.5f );
        R.drawPolygon( 4, x, y );
    
        //  TRIANGLE RIGHT:RIGHT
        x[0] = 890;  y[0] = 580;
        x[1] = 899;  y[1] = 580;
        x[2] = 890;  y[2] =  20;
        R.C.setColor( 0.2f, 0.4f, 0.4f );
        R.drawPolygon( 3, x, y );
    
        //  TRIANGLE RIGHT:LEFT
        x[0] = 899;  y[0] = 580;
        x[1] = 899;  y[1] =  20;
        x[2] = 890;  y[2] =  20;
        R.C.setColor( 0.3f, 0.4f, 0.3f );
        R.drawPolygon( 3, x, y );
    
        // SQUARE BOTTOM RIGHT
        x[0] = 899;  y[0] =   0;
        x[1] = 899;  y[1] =  20;
        x[2] = 880;  y[2] =  20;
        x[3] = 880;  y[3] =   0;
        R.C.setColor( 0.4f, 0.4f, 0.2f );
        R.drawPolygon( 4, x, y );
    
        // QUAD BOTTOM
        x[0] =  20;  y[0] =   0;
        x[1] =  20;  y[1] =  10;
        x[2] = 880;  y[2] =  10;
        x[3] = 880;  y[3] =   0;
        R.C.setColor( 0.4f, 0.5f, 0.1f );
        R.drawPolygon( 4, x, y );*/

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
    // create the shapes and fill all the buffers
    ///
    private void createImage( GL3 gl3, Rasterizer R )
    {
        // draw all our polygons
        makePolygons( R );

        // get the vertices
        numElements = R.C.nVertices();
        Buffer points = R.C.getVertices();
	// #bytes = number of elements * 4 floats/element * 4 bytes/float
        vdataSize = numElements * 4l * 4l;

        // get the element data
        Buffer elements = R.C.getElements();
	// #bytes = number of elements * 4 bytes/element
        edataSize = numElements * 4l;

        // get the color data
        Buffer colors = R.C.getColors();
	// #bytes = number of elements * 4 floats/element * 4 bytes/float
        cdataSize = numElements * 4l * 4l;

        // set up the vertex buffer
        if( bufferInit ) {
            int buf[] = new int[2];
            buf[0] = vbuffer;
            buf[1] = ebuffer;
            gl3.glDeleteBuffers( 2, buf, 0 );
            bufferInit = false;
        }

        // first, create the connectivity data
        ebuffer = makeBuffer( gl3, GL.GL_ELEMENT_ARRAY_BUFFER,
                              elements, edataSize
        );

        // next, the vertex buffer, containing vertices and color data
        vbuffer = makeBuffer( gl3, GL.GL_ARRAY_BUFFER,
                              null, vdataSize + cdataSize
        );
        gl3.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, vdataSize, points );
        gl3.glBufferSubData(GL.GL_ARRAY_BUFFER, vdataSize, cdataSize, colors);

        // note that we've already created buffers once
        bufferInit = true;
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
        gl3.glEnable( GL.GL_CULL_FACE );
        gl3.glCullFace( GL.GL_BACK );
        gl3.glFrontFace( GL.GL_CCW );
        gl3.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );
        gl3.glPolygonMode( GL.GL_FRONT_AND_BACK, GL3.GL_POINT );

        // create the geometry for our shapes.
        createImage( gl3, R );
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

        // bind our vertex buffer
        gl3.glBindBuffer( GL.GL_ARRAY_BUFFER, vbuffer );

        // bind our element array buffer
        gl3.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer );

        // set up our attribute variables
        int vPosition = gl3.glGetAttribLocation( program, "vPosition" );
        gl3.glEnableVertexAttribArray( vPosition );
        gl3.glVertexAttribPointer( vPosition, 4, GL.GL_FLOAT, false,
                                   0, 0l );
        int vColor = gl3.glGetAttribLocation( program, "vColor" );
        gl3.glEnableVertexAttribArray( vColor );
        gl3.glVertexAttribPointer( vColor, 4, GL.GL_FLOAT, false,
                                   0, vdataSize );

        // set up our uniforms
        int wh = gl3.glGetUniformLocation( program, "wh" );
        gl3.glUniform2f( wh, (float) w_width, (float) w_height );

        // draw our shape
        gl3.glDrawElements(GL.GL_POINTS, numElements, GL.GL_UNSIGNED_INT, 0l);

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
    // main program
    ///
    public static void main(String [] args)
    {
        // GL setup
        GLProfile glp = GLProfile.get( GLProfile.GL3 );
        GLCapabilities caps = new GLCapabilities( glp );
        GLCanvas canvas = new GLCanvas( caps );

        // create the fill object
        fillMain myMain = new fillMain( canvas );

        frame = new Frame( "Lab 2 - Polygon Fill" );
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
