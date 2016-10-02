//
//  lineMain.java
//
//  Created by Warren R. Carithers on 2016/09/23.
//  Based on code reated by Joe Geigel on 2010/01/21.
//  Copyright 2016 Rochester Institute of Technology. All rights reserved.
//
//  Main program for line drawing assignment.
//
//  Students should not be modifying this file.
//

import java.awt.*;
import java.nio.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.*;
import com.jogamp.opengl.util.Animator;

public class lineMain implements GLEventListener, KeyListener {

    ///
    // dimensions of drawing window
    ///
    private static int w_height = 600;
    private static int w_width = 600;

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
    ///
    public lineMain( GLCanvas G )
    {

	myCanvas = G;

        C = new Canvas( w_width, w_height );
        R = new Rasterizer( w_height, C );

        C.setColor( 0.0f, 0.0f, 0.0f );
        C.clear();
        C.setColor( 1.0f, 1.0f, 1.0f );

	bufferInit = false;

	G.addGLEventListener( this );
	G.addKeyListener( this );

    }

    private void makeLines()
    {

// Idea for lettering style from:
// http://patorjk.com/software/taag/#p=display&f=Star%20Wars&t=Type%20Something
//          _______   ______   
//         /  _____| /  __  \
//        |  |  __  |  |  |  | 
//        |  | |_ | |  |  |  | 
//        |  |__| | |  `--'  | 
//         \______|  \______/

        // ######## The letter 'G' in green ########
        C.setColor( 0.0f, 1.0f, 0.0f );
        R.drawLine(  80, 340, 220, 340 );   // Horizontal left to right 
        R.drawLine(  40, 380,  80, 340 );    // 315 degree slope        
        R.drawLine( 220, 340, 260, 380 );  // 45 degree slope          
        R.drawLine( 260, 380, 260, 440 );  // Vertical bottom to top
        R.drawLine( 260, 440, 180, 440 );  // Horizontal right to left
        R.drawLine( 180, 440, 180, 400 );
        R.drawLine( 180, 400, 220, 400 );
        R.drawLine( 220, 400, 200, 380 );
        R.drawLine( 200, 380, 100, 380 );
        R.drawLine( 100, 380,  80, 400 );
        R.drawLine(  80, 400,  80, 500 );
        R.drawLine(  80, 500, 100, 520 );
        R.drawLine( 100, 520, 200, 520 );
        R.drawLine( 200, 520, 220, 500 );
        R.drawLine( 220, 500, 220, 480 );
        R.drawLine( 220, 480, 260, 480 );
        R.drawLine( 260, 480, 260, 520 );
        R.drawLine( 260, 520, 220, 560 );  // 135 degree slope
        R.drawLine( 220, 560,  80, 560 );
        R.drawLine(  80, 560,  40, 520 );    // 225 degree slope
        R.drawLine(  40, 520,  40, 380 );    // Vertical top to bottom

        // ######## The letter 'O' in red ########
        C.setColor( 1.0f, 0.0f, 0.0f );
        R.drawLine( 450, 320, 520, 340 );  // 16.6 degree slope
        R.drawLine( 520, 340, 540, 360 );  // 45 degree slope
        R.drawLine( 540, 360, 560, 450 );  // 77.47 degree slope
        R.drawLine( 560, 450, 540, 540 );  // 102.83 degree slope
        R.drawLine( 540, 540, 520, 560 );  // 135 degree slope
        R.drawLine( 520, 560, 450, 580 );  // 163.3 degree slope
        R.drawLine( 450, 580, 380, 560 );  // 196.71 degree slope
        R.drawLine( 380, 560, 360, 540 );  // 225 degree slope
        R.drawLine( 360, 540, 340, 450 );  
        R.drawLine( 340, 450, 360, 360 );
        R.drawLine( 360, 360, 380, 340 );
        R.drawLine( 380, 340, 450, 320 );
        R.drawLine( 420, 380, 480, 380 );
        R.drawLine( 480, 380, 520, 420 );
        R.drawLine( 520, 420, 520, 480 );
        R.drawLine( 520, 480, 480, 520 );
        R.drawLine( 480, 520, 420, 520 );
        R.drawLine( 420, 520, 380, 480 );
        R.drawLine( 380, 480, 380, 420 );
        R.drawLine( 380, 420, 420, 380 );

        // now, draw the student's initials

        R.myInitials();

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
	// draw all our lines
        makeLines();

	// get the vertices
	numElements = R.C.nVertices();
	Buffer points = R.C.getVertices();
	vdataSize = numElements * 4l * 4l;

	// get the element data
	Buffer elements = R.C.getElements();
	edataSize = numElements * 2l;

	// get the color data
	Buffer colors = R.C.getColors();
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
	    System.err.println( "Error setting up shaders - " +
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
	checkShaderError( myShaders, program, "shader" );

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
        gl3.glDrawElements( GL.GL_POINTS, numElements,
            GL.GL_UNSIGNED_SHORT, 0l
        );

    }

    ///
    // Notifies the listener to perform the release of all OpenGL
    // resources per GLContext, such as memory buffers and GLSL
    // programs.
    ///
    public void dispose( GLAutoDrawable drawable )
    {
    }

    /**
     * Because I am a Key Listener... we'll respond to key presses
     */
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}

    /**
     * Invoked when a key has been pressed.
     */
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


    /**
     * main program
     */
    public static void main(String [] args)
    {
        // GL setup
        GLProfile glp = GLProfile.get( GLProfile.GL3 );
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        // create your tessMain
        lineMain myMain = new lineMain(canvas);


        frame = new Frame("Lab 1 - Line Drawing");
        frame.setSize( w_width, w_height );
        frame.add(canvas);
        frame.setVisible(true);

        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
		frame.dispose();
                System.exit(0);
            }
        });
    }
}
