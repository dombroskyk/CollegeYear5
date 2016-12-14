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

	private Texture tex1;
	private Texture tex2;
	private int texLoc1;
	private int texLoc2;
	
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
    	
    	gl3.glEnable(GL.GL_TEXTURE_2D);
    	
    	try {
    		InputStream stream1 = new FileInputStream( "smiley2.png" );
    		InputStream stream2 = new FileInputStream( "frowny2.png" );
    		tex1 = TextureIO.newTexture( stream1, false, "png");
    		tex2 = TextureIO.newTexture( stream2, false, "png");
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
		texLoc1 = tex1.getTextureObject( gl3 );
		texLoc2 = tex2.getTextureObject( gl3 );
		
		gl3.glActiveTexture( gl3.GL_TEXTURE0 );
		gl3.glBindTexture( gl3.GL_TEXTURE_2D, texLoc1 );
		gl3.glActiveTexture( gl3.GL_TEXTURE2 );
		gl3.glBindTexture( gl3.GL_TEXTURE_2D, texLoc2 );
		
		// Get locations of sampler variables
		int texSmileLoc = gl3.glGetUniformLocation( program, "texSmile" );
		int texFrownLoc = gl3.glGetUniformLocation( program, "texFrown" );
		// Connect samplers to texture units
		gl3.glUniform1i( texSmileLoc, 0 ); // Texture unit 0
		gl3.glUniform1i( texFrownLoc, 2 ); // Texture unit 2
    }
}
