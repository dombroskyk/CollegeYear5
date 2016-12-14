//
// textingMain.java
//
//  Created by Warren R. Carithers 2016/11/12.
//  Based on code created by Joe Geigel and updated by
//    Vasudev Prasad Bethamcherla.
//  Copyright 2016 Rochester Institute of Technology.  All rights reserved.
//
// Main class for lighting/shading/texturing assignment.
//
// This file should not be modified by students.
//

import java.awt.*;
import java.nio.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.*;
import com.jogamp.opengl.util.Animator;

public class textingMain implements GLEventListener, KeyListener
{

    ///
    // dimensions of the drawing window
    ///
    private static int w_width  = 600;
    private static int w_height = 600;

    ///
    // buffer info
    ///

    ///
    // We need two vertex buffers and four element buffers:
    // one set for the quad (texture mapped), and one set
    // for the teapot (Phong shaded)
    private BufferSet quadBuffers;
    private BufferSet teapotBuffers;

    ///
    // Animation control
    ///
    Animator anime;
    boolean animating;

    ///
    // Initial animation rotation angles
    ///
    float angles[];

    ///
    // Program IDs...for shader programs
    ///
    public int pshader;
    public int tshader;

    ///
    // Shape info
    ///

    ///
    // Lighting information
    ///
    Lighting myPhong;

    ///
    // Viewing information
    ///
    Viewing myView;

    ///
    // Texturing information
    ///
    Textures myTexture;

    ///
    // canvas and shape info
    ///
    GLCanvas myGLCanvas;
    Canvas canvas;
    Shapes myShape;
    ShaderSetup myShaders;
    private static Frame frame;
    private boolean updateNeeded;

    ///
    // Constructor
    ///
    public textingMain( GLCanvas G )
    {
        myGLCanvas = G;

	// initialize our various parameters

        angles = new float[2];

        animating = false;

        angles[0] = 0.0f;
        angles[1] = 0.0f;

        // Initialize lighting, view, etc.
	canvas = new Canvas( w_width, w_height );
	myShaders = new ShaderSetup();
        myPhong = new Lighting();
        myView = new Viewing();
	myTexture = new Textures();
	myShape = new Shapes( canvas );
	quadBuffers = new BufferSet();
	teapotBuffers = new BufferSet();

        // Set up event listeners
        G.addGLEventListener (this);
        G.addKeyListener (this);
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
    // Create a set of vertex and element buffers
    ///
    private void createBuffers( GL3 gl3, BufferSet B, Canvas C ) {

        // get the vertices
        B.numElements = C.nVertices();
        Buffer points = C.getVertices();
        // #bytes = number of elements * 4 floats/element * bytes/float
        B.vSize = B.numElements * 4l * 4l;
        long vbufferSize = B.vSize;

        // get the normals
        Buffer normals = C.getNormals();
        B.nSize = B.numElements * 3l * 4l;
        vbufferSize += B.nSize;

        // get the UV data (if any)
        Buffer uv = C.getUV();
	B.tSize = 0;
	if( uv != null ) {
            B.tSize = B.numElements * 2l * 4l;
	}
	vbufferSize += B.tSize;

        // get the element data
        Buffer elements = C.getElements();
        B.eSize = B.numElements * 4l;

        // set up the vertex buffer
        if( B.bufferInit ) {
            // must delete the existing buffers first
            int buf[] = new int[2];
            buf[0] = B.vbuffer;
            buf[1] = B.ebuffer;
            gl3.glDeleteBuffers( 2, buf, 0 );
            B.bufferInit = false;
        }

        // first, create the connectivity data
        B.ebuffer = makeBuffer( gl3, GL.GL_ELEMENT_ARRAY_BUFFER,
                                elements, B.eSize );

        // next, the vertex buffer, containing vertices and "extra" data
        B.vbuffer = makeBuffer( gl3, GL.GL_ARRAY_BUFFER, null, vbufferSize );
	gl3.glBufferSubData( GL.GL_ARRAY_BUFFER, 0, B.vSize, points );
	gl3.glBufferSubData( GL.GL_ARRAY_BUFFER, B.vSize, B.nSize, normals );
	if( uv != null ) {
	    gl3.glBufferSubData( GL.GL_ARRAY_BUFFER, B.vSize+B.nSize,
	        B.tSize, uv );
	}

        // finally, mark it as set up
        B.bufferInit = true;

    }


    ///
    // creates a new shape
    ///
    public void createShape( GL3 gl3, int obj, BufferSet B )
    {
        // clear the old shape
        canvas.clear();

        // make the shape
        myShape.makeShape( obj );

	// create the necessary buffers
        createBuffers( gl3, B, canvas );

    }


    ///
    // Bind the correct vertex and element buffers
    //
    // Assumes the correct shader program has already been enabled
    ///
    private void selectBuffers( GL3 gl3, int program, int obj, BufferSet B )
    {

        gl3.glBindBuffer( GL.GL_ARRAY_BUFFER, B.vbuffer );
        gl3.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, B.ebuffer );

        // set up the vertex attribute variables
        int vPosition = gl3.glGetAttribLocation( program, "vPosition" );
        gl3.glEnableVertexAttribArray( vPosition );
        gl3.glVertexAttribPointer( vPosition, 4, GL.GL_FLOAT, false,
                                       0, 0l );

        int vNormal = gl3.glGetAttribLocation( program, "vNormal" );
        gl3.glEnableVertexAttribArray( vNormal );
        gl3.glVertexAttribPointer( vNormal, 3, GL.GL_FLOAT, false,
                                   0, B.vSize );

	if( obj == Shapes.OBJ_QUAD ) {
            int vTexCoord = gl3.glGetAttribLocation( program, "vTexCoord" );
            gl3.glEnableVertexAttribArray( vTexCoord );
            gl3.glVertexAttribPointer( vTexCoord, 2, GL.GL_FLOAT, false,
                                       0, B.vSize+B.nSize );
	}

    }


    ///
    // verify shader creation
    ///
    private void checkShaderError( ShaderSetup myShaders, int program,
        String which )
    {
        if( program == 0 ) {
            System.err.println( "Error setting " + which +
                " shader - " +
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
        // get the gl object
        GL3 gl3 = drawable.getGL().getGL3();

        // create the Animator now that we have the drawable
        anime = new Animator( drawable );

	// Load texture image(s)
	myTexture.loadTexture( gl3 );

        tshader = myShaders.readAndCompile(gl3,"texture.vert","texture.frag");
        checkShaderError( myShaders, tshader, "texture" );

        pshader = myShaders.readAndCompile( gl3, "phong.vert", "phong.frag" );
        checkShaderError( myShaders, pshader, "phong" );

        // Create all our objects
        createShape( gl3, Shapes.OBJ_QUAD, quadBuffers );
        createShape( gl3, Shapes.OBJ_TEAPOT, teapotBuffers );

        // Other GL initialization
        gl3.glEnable( GL.GL_DEPTH_TEST );
        gl3.glFrontFace( GL.GL_CCW );
	gl3.glPolygonMode( GL.GL_FRONT_AND_BACK, GL3.GL_FILL );
        gl3.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
        gl3.glDepthFunc( GL.GL_LEQUAL );
        gl3.glClearDepth( 1.0f );

    }

    ///
    // Called by the drawable to initiate OpenGL rendering by the client.
    ///
    public void display( GLAutoDrawable drawable )
    {
        // get GL
        GL3 gl3 = (drawable.getGL()).getGL3();

        // clear and draw params..
        gl3.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

        // first, the quad
        gl3.glUseProgram( tshader );

        // set up viewing and projection parameters
        myView.setUpFrustum( tshader, gl3 );
	
	// set up the texture information
	myTexture.setUpTexture( tshader, gl3 );

        // set up the camera
        myView.setUpCamera( tshader, gl3,
            0.2f, 3.0f, 6.5f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f
        );

        // set up transformations for the quad
        myView.setUpTransforms( tshader, gl3,
            1.5f, 1.5f, 1.5f,
            angles[Shapes.OBJ_QUAD],
            angles[Shapes.OBJ_QUAD],
            angles[Shapes.OBJ_QUAD],
            -1.5f, 0.5f, -1.5f
        );

        // draw it
        selectBuffers( gl3, tshader, Shapes.OBJ_QUAD, quadBuffers );
        gl3.glDrawElements( GL.GL_TRIANGLES, (int) quadBuffers.vSize,
            GL.GL_UNSIGNED_INT, 0l
        );

	// now, draw the teapot
        gl3.glUseProgram( pshader );

        // set up viewing and projection parameters
        myView.setUpFrustum( pshader, gl3 );

	// set up the Phong shading information
	myPhong.setUpPhong( pshader, gl3 );

        // set up the camera
        myView.setUpCamera( pshader, gl3,
            0.2f, 3.0f, 6.5f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f
        );

        myView.setUpTransforms( pshader, gl3,
            2.0f, 2.0f, 2.0f,
            angles[Shapes.OBJ_TEAPOT],
            angles[Shapes.OBJ_TEAPOT],
            angles[Shapes.OBJ_TEAPOT],
            1.5f, 0.5f, -1.5f
        );

        // draw it
        selectBuffers( gl3, pshader, Shapes.OBJ_TEAPOT, teapotBuffers );
        gl3.glDrawElements( GL.GL_TRIANGLES, (int) teapotBuffers.vSize,
            GL.GL_UNSIGNED_INT, 0l
        );

        // perform any required animation for the next time
        if( animating ) {
            animate();
        }
    }

    ///
    // Notifies the listener to perform the release of all OpenGL
    // resources per GLContext, such as memory buffers and GLSL
    // programs.
    ///
    public void dispose(GLAutoDrawable drawable)
    {
    }


    ///
    // Create a vertex or element array buffer
    //
    // @param target - which type of buffer to create
    // @param data   - source of data for buffer (or null)
    // @param size   - desired length of buffer
    ///
    int makeBuffer( int target, Buffer data, int size, GL3 gl3 )
    {
        int buffer[] = new int[1];

        gl3.glGenBuffers( 1, buffer, 0 );
        gl3.glBindBuffer( target, buffer[0] );
        gl3.glBufferData( target, size, null, GL.GL_STATIC_DRAW );

	return( buffer[0] );
    }

    ///
    // Called by teh drawable during the first repaint after the component
    // has been resized.
    ///
    public void reshape( GLAutoDrawable drawable, int x, int y, int width,
                         int height )
    {
    }


    ///
    // Because I am a Key Listener...we'll only respond to key presses
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

            case 'a':    // animate
                animating = true;
                anime.start();
                break;

            case 'r':    // reset rotations
                angles[0] = 0.0f;
                angles[1] = 0.0f;
                break;

            case 's':    // stop animating
                animating = false;
                anime.stop();
                break;

            case 'q': case 'Q':
		frame.dispose();
                System.exit( 0 );
                break;
        }

        // do a redraw
        myGLCanvas.display();
    }

    ///
    // Simple animate function
    ///
    public void animate() {
        angles[Shapes.OBJ_QUAD]   += 1;
        angles[Shapes.OBJ_TEAPOT] += 1;
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

        // create your tessMain
        textingMain myMain = new textingMain( canvas );

        frame = new Frame( "Lab 6 - Shading and Texturing");
        frame.setSize( w_width, w_height );
        frame.add( canvas );
        frame.setVisible( true );

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
