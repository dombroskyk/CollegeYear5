//
//  Viewing.java
//
//  Created by Warren R. Carithers 2016/11/11.
//  Based on code created by Joe Geigel on 1/23/13.
//  Copyright 2016 Rochester Institute of Technology.  All rights reserved.
//
//  Simple class for setting up the viewing and projection transforms
//  for the Transformation Assignment.
//
//  Contributor:  YOUR_NAME_HERE
//

import javax.media.opengl.*;
import javax.media.opengl.fixedfunc.*;

public class Viewing
{

    ///
    // constructor
    ///
    public Viewing()
    {
    }


    ///
    // This function sets up the view and projection parameter for a frustum
    // projection of the scene. See the assignment description for the values
    // for the projection parameters.
    //
    // You will need to write this function, and maintain all of the values
    // needed to be sent to the vertex shader.
    //
    // @param program - The ID of an OpenGL (GLSL) shader program to which
    //    parameter values are to be sent
    // @param gl3 - GL3 object on which all OpenGL calls are to be made
    ///
    public void setUpFrustum( int program, GL3 gl3 )
    {
    	int leftPLoc, rightPLoc, topPLoc, bottomPLoc,
    	nearPLoc, farPLoc, orthoPLoc, frustumPLoc;
    	leftPLoc = gl3.glGetUniformLocation( program, "scaleT" );
    	rightPLoc = gl3.glGetUniformLocation( program, "rotateT" );
    	topPLoc = gl3.glGetUniformLocation( program, "topP" );
    	bottomPLoc = gl3.glGetUniformLocation( program, "bottomP" );
    	nearPLoc = gl3.glGetUniformLocation( program, "nearP" );
    	farPLoc = gl3.glGetUniformLocation( program, "farP" );
    	orthoPLoc = gl3.glGetUniformLocation( program, "orthoP" );
    	frustumPLoc = gl3.glGetUniformLocation( program, "frustumP" );
    	gl3.glUniform1f( leftPLoc, -1.0f );
    	gl3.glUniform1f( rightPLoc, 1.0f );
    	gl3.glUniform1f( topPLoc, 1.0f );
    	gl3.glUniform1f( bottomPLoc, -1.0f );
    	gl3.glUniform1f( nearPLoc, 0.9f );
    	gl3.glUniform1f( farPLoc, 4.5f );
    	gl3.glUniform1i( orthoPLoc, 0 );
    	gl3.glUniform1i( frustumPLoc, 1 );
    }


    ///
     //This function sets up the view and projection parameter for an
    // orthographic projection of the scene. See the assignment description
    // for the values for the projection parameters.
    //
    // You will need to write this function, and maintain all of the values
    // needed to be sent to the vertex shader.
    //
    // @param program - The ID of an OpenGL (GLSL) shader program to which
    //    parameter values are to be sent
    // @param gl3 - GL3 object on which all OpenGL calls are to be made
    ///
    public void setUpOrtho( int program, GL3 gl3 )
    {
        int leftPLoc, rightPLoc, topPLoc, bottomPLoc,
        	nearPLoc, farPLoc, orthoPLoc, frustumPLoc;
        leftPLoc = gl3.glGetUniformLocation( program, "scaleT" );
        rightPLoc = gl3.glGetUniformLocation( program, "rotateT" );
        topPLoc = gl3.glGetUniformLocation( program, "topP" );
        bottomPLoc = gl3.glGetUniformLocation( program, "bottomP" );
        nearPLoc = gl3.glGetUniformLocation( program, "nearP" );
        farPLoc = gl3.glGetUniformLocation( program, "farP" );
        orthoPLoc = gl3.glGetUniformLocation( program, "orthoP" );
        frustumPLoc = gl3.glGetUniformLocation( program, "frustumP" );
        gl3.glUniform1f( leftPLoc, -1.0f );
        gl3.glUniform1f( rightPLoc, 1.0f );
        gl3.glUniform1f( topPLoc, 1.0f );
        gl3.glUniform1f( bottomPLoc, -1.0f );
        gl3.glUniform1f( nearPLoc, 0.9f );
        gl3.glUniform1f( farPLoc, 4.5f );
        gl3.glUniform1i( orthoPLoc, 1 );
        gl3.glUniform1i( frustumPLoc, 0 );
        
    }


    ///
    // This function clears any transformations, setting the values to the
    // defaults: no scaling (scale factor of 1), no rotation (degree of
    // rotation = 0), and no translation (0 translation in each direction).
    //
    // You will need to write this function, and maintain all of the values
    // which must be sent to the vertex shader.
    //
    // @param program - The ID of an OpenGL (GLSL) shader program to which
    //    parameter values are to be sent
    // @param gl3 - GL3 object on which all OpenGL calls are to be made
    ///
    public void clearTransforms( int program, GL3 gl3 )
    {
        int scaleTLoc, rotateTLoc, translateTLoc;
        scaleTLoc = gl3.glGetUniformLocation( program, "scaleT" );
        rotateTLoc = gl3.glGetUniformLocation( program, "rotateT" );
        translateTLoc = gl3.glGetUniformLocation( program, "translateT" );
        gl3.glUniform3f( scaleTLoc, 1.0f, 1.0f, 1.0f );
        gl3.glUniform3f( rotateTLoc, 0.0f, 0.0f, 0.0f );
        gl3.glUniform3f( translateTLoc, 0.0f, 0.0f, 0.0f );
    }


    ///
    // This function sets up the transformation parameters for the vertices
    // of the teapot.  The order of application is specified in the driver
    // program.
    //
    // You will need to write this function, and maintain all of the values
    // which must be sent to the vertex shader.
    //
    // @param program - The ID of an OpenGL (GLSL) shader program to which
    //    parameter values are to be sent
    // @param gl3 - GL3 object on which all OpenGL calls are to be made
    // @param xScale - amount of scaling along the x-axis
    // @param yScale - amount of scaling along the y-axis
    // @param zScale - amount of scaling along the z-axis
    // @param xRotate - angle of rotation around the x-axis, in degrees
    // @param yRotate - angle of rotation around the y-axis, in degrees
    // @param zRotate - angle of rotation around the z-axis, in degrees
    // @param xTranslate - amount of translation along the x axis
    // @param yTranslate - amount of translation along the y axis
    // @param zTranslate - amount of translation along the z axis
    ///
    public void setUpTransforms( int program, GL3 gl3,
        float xScale, float yScale, float zScale,
        float xRotate, float yRotate, float zRotate,
        float xTranslate, float yTranslate, float zTranslate )
    {
        int scaleTLoc, rotateTLoc, translateTLoc;
        scaleTLoc = gl3.glGetUniformLocation( program, "scaleT" );
        rotateTLoc = gl3.glGetUniformLocation( program, "rotateT" );
        translateTLoc = gl3.glGetUniformLocation( program, "translateT" );
        gl3.glUniform3f( scaleTLoc, xScale, yScale, zScale );
        gl3.glUniform3f( rotateTLoc, xRotate, yRotate, zRotate );
        gl3.glUniform3f( translateTLoc, xTranslate, yTranslate, zTranslate );
    }


    ///
    // This function clears any changes made to camera parameters, setting the
    // values to the defaults: eyepoint (0.0,0.0,0.0), lookat (0,0,0.0,-1.0),
    // and up vector (0.0,1.0,0.0).
    //
    // You will need to write this function, and maintain all of the values
    // which must be sent to the vertex shader.
    //
    // @param program - The ID of an OpenGL (GLSL) shader program to which
    //    parameter values are to be sent
    // @param gl3 - GL3 object on which all OpenGL calls are to be made
    ///
    void clearCamera( int program, GL3 gl3 )
    {
    	int eyepointCLoc, lookatCLoc, upCLoc;
        eyepointCLoc = gl3.glGetUniformLocation( program, "eyepointC" );
        lookatCLoc = gl3.glGetUniformLocation( program, "lookatC" );
        upCLoc = gl3.glGetUniformLocation( program, "upC" );
        gl3.glUniform3f( eyepointCLoc, 0.0f, 0.0f, 0.0f );
        gl3.glUniform3f( lookatCLoc, 0.0f, 0.0f, -1.0f );
        gl3.glUniform3f( upCLoc, 0.0f, 1.0f, 0.0f );
    }


    ///
    // This function sets up the camera parameters controlling the viewing
    // transformation.
    //
    // You will need to write this function, and maintain all of the values
    // which must be sent to the vertex shader.
    //
    // @param program - The ID of an OpenGL (GLSL) shader program to which
    //    parameter values are to be sent
    // @param gl3 - GL3 object on which all OpenGL calls are to be made
    // @param eyepointX - x coordinate of the camera location
    // @param eyepointY - y coordinate of the camera location
    // @param eyepointZ - z coordinate of the camera location
    // @param lookatX - x coordinate of the lookat point
    // @param lookatY - y coordinate of the lookat point
    // @param lookatZ - z coordinate of the lookat point
    // @param upX - x coordinate of the up vector
    // @param upY - y coordinate of the up vector
    // @param upZ - z coordinate of the up vector
    ///
    void setUpCamera( int program, GL3 gl3,
        float eyepointX, float eyepointY, float eyepointZ,
        float lookatX, float lookatY, float lookatZ,
        float upX, float upY, float upZ )
    {
    	int eyepointCLoc, lookatCLoc, upCLoc;
        eyepointCLoc = gl3.glGetUniformLocation( program, "eyepointC" );
        lookatCLoc = gl3.glGetUniformLocation( program, "lookatC" );
        upCLoc = gl3.glGetUniformLocation( program, "upC" );
        gl3.glUniform3f( eyepointCLoc, eyepointX, eyepointY, eyepointZ );
        gl3.glUniform3f( lookatCLoc, lookatX, lookatY, lookatZ );
        gl3.glUniform3f( upCLoc, upX, upY, upZ );
    }

}
