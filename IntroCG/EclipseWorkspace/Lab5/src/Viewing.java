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
        // Add your code here.
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
        // Add your code here.
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
        // Add your code here.
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
        // Add your code here.
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
        // Add your code here.
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
        // Add your code here.
    }

}
