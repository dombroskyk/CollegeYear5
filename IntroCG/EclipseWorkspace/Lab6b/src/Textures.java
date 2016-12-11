//
//  Textures.java
//
//  Created by Warren R. Carithers 2016/11/22.
//  Based on code created by Joe Geigel on 1/23/13.
//  Copyright 2016 Rochester Institute of Technology.  All rights reserved.
//
//  Contributor:  YOUR_NAME_HERE
//
//  Simple class for setting up texture mapping parameters.
//

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.*;
import com.jogamp.opengl.util.texture.*;

public class Textures
{

	private Texture tex_id1;
	private Texture tex_id2;
	private int texLoc;
	
    ///
    // constructor
    ///
    public Textures()
    {
    }

    ///
    // This functions loads texture data to the GPU.
    //
    // You will need to write this function, and maintain all of the values
    // needed to be sent to the various shaders.
    //
    // @param gl3 - GL3 object on which all OpenGL calls are to be made
    //
    ///
    public void loadTexture( GL3 gl3 )
    {
    	
    	try {
    		InputStream stream1 = new FileInputStream( "smiley2.png" );
    		InputStream stream2 = new FileInputStream( "frowny2.png" );
    		tex_id1 = TextureIO.newTexture( stream1, false, "png");
    		tex_id2 = TextureIO.newTexture( stream2, false, "png");
    	} catch ( IOException exc ) {
    		exc.printStackTrace();
    		System.exit(1);
    	}
    }

    ///
    // This functions sets up the parameters
    // for texture use.
    //
    // You will need to write this function, and maintain all of the values
    // needed to be sent to the various shaders.
    //
    // @param program - The ID of an OpenGL (GLSL) program to which
    //    parameter values are to be sent
    //
    // @param gl3 - GL3 object on which all OpenGL calls are to be made
    //
    ///
    public void setUpTexture( int program, GL3 gl3 )
    {

        int tex1;
        int tex2;
    }
}
