//
//  Lighting.java
//
//  Created by Warren R. Carithers 2016/11/22.
//  Based on code created by Joe Geigel on 1/23/13.
//  Copyright 2016 Rochester Institute of Technology.  All rights reserved.
//
//  Contributor:  YOUR_NAME_NERE
//
//  Simple class for setting up Phong illumination.
//

import javax.media.opengl.*;
import javax.media.opengl.fixedfunc.*;

public class Lighting
{
    // Add any data members you need here

    ///
    // constructor
    ///
    public Lighting()
    {
    }

    ///
    // This functions sets up the lighting, material, and shading parameters
    // for the Phong shader.
    //
    // You will need to write this function, and maintain all of the values
    // needed to be sent to the vertex shader.
    //
    // @param program - The ID of an OpenGL (GLSL) shader program to which
    // parameter values are to be sent
    //
    // @param gl3 - GL3 object on which all OpenGL calls are to be made
    //
    ///
    public void setUpPhong (int program, GL3 gl3)
    {
        // Add your code here.
    }
}
