//
//  tessMain.java
//
//  Created by Warren R. Carithers 2016/11/04.
//  Based on code created by Joe Geigel and updated by
//    Vasudev Prasad Bethamcherla.
//  Copyright 2016 Rochester Institute of Technology.  All rights reserved.
//
//  Main program for tessellation assignment.
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

public class tessMain implements GLEventListener, KeyListener {

    ///
    // static values
    ///
    public final static int CUBE = 0;
    public final static int CYLINDER = 1;
    public final static int CONE = 2;
    public final static int SPHERE = 3;

    ///
    // dimensions of the drawing window
    ///
    private static int w_width = 512;
    private static int w_height = 512;

    ///
    // values for levels of subdivisions
    ///
    private int division1 = 1;
    private int division2 = 1;
    private int currentShape = tessMain.CUBE;

    ///
    // buffer information
    ///
    private BufferSet shapeBuffers;

    ///
    // shader arguments
    ///
    private int theta;      // theta uniform location
    private int vPosition;  // vertex attribute location

    ///
    // rotation angles
    ///
    public float angles[];
    public float anglesReset[];
    private float angleInc = 5.0f;

    ///
    // animation control
    ///
    private boolean animating = false;
    private int level = 0;
    Animator anime;

    ///
    // shader info
    ///
    private ShaderSetup myShaders;
    private int program = 0;
    private boolean updateNeeded = true;

    ///
    // canvas and shape info
    ///
    GLCanvas myGLCanvas;
    Canvas myCanvas;
    Shapes myShape;
    private static Frame frame;

    ///
    // constructor
    ///
    public tessMain( GLCanvas G )
    {
        myGLCanvas = G;

	// set up our rotation angles

        angles = new float[3];
        angles[0] = 30.0f;
        angles[1] = 30.0f;
        angles[2] = 0.0f;

	anglesReset = new float[3];
        anglesReset[0] = 30.0f;
        anglesReset[1] = 30.0f;
        anglesReset[2] = 0.0f;

        // create the various objects we'll need

        myShaders = new ShaderSetup();
        myCanvas = new Canvas( w_width, w_height );
        myShape = new Shapes( myCanvas );
	shapeBuffers = new BufferSet();

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

        // next, the vertex buffer, containing vertices only
        B.vbuffer = makeBuffer( gl3, GL.GL_ARRAY_BUFFER,
	                        points, B.vSize );

        // finally, mark it as set up
        B.bufferInit = true;

    }

    ///
    // creates a new shape
    ///
    public void createNewShape()
    {
        // clear the old shape
        myCanvas.clear();

        // create the new shape...should be a switch here
        switch( currentShape )
        {
            case CUBE: myShape.makeCube( division1 );
                break;

            case CYLINDER: myShape.makeCylinder( 0.5f, division1, division2 );
                break;

            case CONE: myShape.makeCone( 0.5f, division1, division2 );
                break;

            case SPHERE: myShape.makeSphere( 0.5f, division1, division2 );
                break;
        }

	// this will be done in display()
        // createBuffers( shapeBuffers, myCanvas );

        updateNeeded = true;
	myGLCanvas.display();
    }

    ///
    // verify shader creation
    ///
    private void checkShaderError( ShaderSetup myShaders, int program,
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
        // get the gl object
        GL3 gl3 = drawable.getGL().getGL3();

	// create the Animator now that we have the drawable
	anime = new Animator( drawable );

        // Load shaders
        program = myShaders.readAndCompile( gl3, "shader.vert", "shader.frag" );
        checkShaderError( myShaders, program, "basic" );

	// select this shader program
	gl3.glUseProgram( program );

	// get location of shader global variables
        vPosition = gl3.glGetAttribLocation( program, "vPosition" );
        theta = gl3.glGetUniformLocation( program, "theta" );

        // Other GL initialization
        gl3.glEnable( GL.GL_DEPTH_TEST );
        gl3.glEnable( GL.GL_CULL_FACE );
        gl3.glCullFace( GL.GL_BACK );
        gl3.glPolygonMode( GL.GL_FRONT_AND_BACK, GL3.GL_LINE );
        gl3.glFrontFace( GL.GL_CCW );
        gl3.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );
        gl3.glDepthFunc( GL.GL_LEQUAL );
        gl3.glClearDepth( 1.0f );

        // initially create a new Shape
        createNewShape();

    }

    ///
    // Called by the drawable during the first repaint after the component
    // has been resized.
    ///
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                     int height)
    {

    }

    ///
    // Called by the drawable to initiate OpenGL rendering by the client.
    ///
    public void display( GLAutoDrawable drawable )
    {
        // get GL
        GL3 gl3 = (drawable.getGL()).getGL3();

	if( animating ) {
	    animate();
	}

        // clear the frame buffer
        gl3.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

	// check to see if we need to recreate our buffers
	if( updateNeeded ) {
	    createBuffers( gl3, shapeBuffers, myCanvas );
	}

        // bind our vertex buffer
        gl3.glBindBuffer( GL.GL_ARRAY_BUFFER, shapeBuffers.vbuffer );

        // bind our element array buffer
        gl3.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, shapeBuffers.ebuffer );

	// ensure we have selected the correct shader program
        gl3.glUseProgram( program );

        // set up our attribute variables
        gl3.glEnableVertexAttribArray( vPosition );
        gl3.glVertexAttribPointer( vPosition, 4, GL.GL_FLOAT, false, 0, 0l );

        // pass in our rotations as a uniform variable
        gl3.glUniform3fv( theta, 1, angles, 0 );

        // draw our shapes
        // gl3.glDrawElements( GL.GL_TRIANGLES, shapeBuffers.numElements,
                            // GL.GL_UNSIGNED_INT, 0l );
	gl3.glDrawArrays( GL.GL_TRIANGLES, 0, shapeBuffers.numElements );
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
        System.out.println(key);
        // Respond appropriately
        switch( key ) {

	    // automated animation
            case 'a': case 'A':
	        animating = true;
		anime.start();
		break;

	    // incremental rotation along the axes
            case 'x': angles[0] -= angleInc; break;
            case 'y': angles[1] -= angleInc; break;
            case 'z': angles[2] -= angleInc; break;
            case 'X': angles[0] += angleInc; break;
            case 'Y': angles[1] += angleInc; break;
            case 'Z': angles[2] += angleInc; break;

	    // shape selection
            case '1': case 'c': currentShape = CUBE; createNewShape(); break;
            case '2': case 'C': currentShape = CYLINDER; createNewShape(); break;
            case '3': case 'n': currentShape = CONE; createNewShape(); break;
            case '4': case 's': currentShape = SPHERE; createNewShape(); break;

	    // tessellation factors
            case '+': division1++; createNewShape(); break;
            case '=': division2++; createNewShape(); break;
            case '-': if( division1 > 1 ) {
	                division1--;
			createNewShape();
		      }
		      break;
            case '_': if( division2 > 1 ) {
                        division2--;
                        if( currentShape != CUBE ) createNewShape();
                      }
                      break;

	    // reset rotation
	    case 'r': case 'R':
		angles[0] = anglesReset[0];
		angles[1] = anglesReset[1];
		angles[2] = anglesReset[2];
	        break;

	    // termination
	    case 033:  // Escape key
            case 'q': case 'Q':
		frame.dispose();
                System.exit( 0 );
                break;
        }

        // do a redraw
        updateNeeded = true;
        myGLCanvas.display();
    }

    ///
    // Rotates the shapes along x,y,z.
    // 
    // Causes gimbal lock which also happened on Apollo 11
    // http://en.wikipedia.org/wiki/Gimbal_lock#Gimbal_lock_on_Apollo_11
    // Solution? Use Quaternions (Taught in Comp. Animation: Algorithms)
    // 
    // TIDBIT:
    // Quaternion plaque on Brougham (Broom) Bridge, Dublin, which says:
    // 
    //  "Here as he walked by
    //  on the 16th of October 1843
    //  
    //  Sir William Rowan Hamilton 
    //  
    //  in a flash of genius discovered
    //  the fundamental formula for
    //  quaternion multiplication
    //  i^2 = j^2 = k^2 = ijk = -1
    //  & cut it on a stone of this bridge"
    ///
    public void animate() {

	if( level >= 450 ) {
	    level = 0;
	    animating = false;
	}

	if( !animating ) {
	    anime.stop();
	    return;
	}

	if( level < 150 ) {
            angles[0] -= angleInc / 3;
	} else if( level < 300 ) {
            angles[1] -= angleInc / 3;
	} else {
            angles[2] -= angleInc / 3;
	}

	++level;

	updateNeeded = true;
        // myGLCanvas.display();
    }

    ///
    // main program
    ///
    public static void main(String [] args)
    {
        // GL setup
        GLProfile glp = GLProfile.get( GLProfile.GL3 );
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas glcanvas = new GLCanvas(caps);

        // create your tessMain
        tessMain myMain = new tessMain( glcanvas );

        frame = new Frame("Lab 4 - Tessellation");
        frame.setSize( w_width, w_height );
        frame.add( glcanvas );
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
