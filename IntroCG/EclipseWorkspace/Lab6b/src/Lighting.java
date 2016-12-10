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
    private float[] sourceColor;
    private float[] sourcePos;
    private float[] ambientColor;
    
    private float[] aMatColor;
    private float[] dMatColor;
    private float[] sMatColor;
    
    private float ka;
    private float kd;
    private float ks;
    private float sExp;

    ///
    // constructor
    ///
    public Lighting()
    {
    	sourceColor = new float[]{ 1.0f, 1.0f, 0.0f, 1.0f };
    	sourcePos = new float[]{ 0.0f, 5.0f, 2.0f, 1.0f };
    	ambientColor = new float[]{ 0.5f, 0.5f, 0.5f, 1.0f };
    	
    	aMatColor = new float[]{ 0.5f, 0.1f, 0.9f, 1.0f };
    	dMatColor = new float[]{ 0.89f, 0.0f, 0.0f, 1.0f };
    	sMatColor = new float[]{ 1.0f, 1.0f, 1.0f, 1.0f };
    	
    	ka = 0.5f;
    	kd = 0.7f;
    	ks = 1.0f;
    	sExp = 10.0f;
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
    	int sourceColorLoc = gl3.glGetUniformLocation( program , "sourceColor" );
        int sourcePosLoc = gl3.glGetUniformLocation( program , "sourcePos" );
        int ambientColorLoc = gl3.glGetUniformLocation( program , "ambientColor" );
        int aMatColorLoc = gl3.glGetUniformLocation( program , "aMatColor" );
        int dMatColorLoc = gl3.glGetUniformLocation( program , "dMatColor" );
        int sMatColorLoc = gl3.glGetUniformLocation( program , "sMatColor" );
        int kaLoc = gl3.glGetUniformLocation( program , "ka" );
        int kdLoc = gl3.glGetUniformLocation( program , "kd" );
        int ksLoc = gl3.glGetUniformLocation( program , "ks" );
        int sExpLoc = gl3.glGetUniformLocation( program , "sExp" );

        gl3.glUniform4fv( sourceColorLoc, 1, sourceColor, 0 );
        gl3.glUniform4fv( sourcePosLoc, 1, sourcePos, 0 );
        gl3.glUniform4fv( ambientColorLoc, 1, ambientColor, 0 );
        gl3.glUniform4fv( aMatColorLoc, 1, aMatColor, 0 );
        gl3.glUniform4fv( dMatColorLoc, 1, dMatColor, 0 );
        gl3.glUniform4fv( sMatColorLoc, 1, sMatColor, 0 );
        gl3.glUniform1f( kaLoc, ka);
        gl3.glUniform1f( kdLoc, kd);
        gl3.glUniform1f( ksLoc, ks);
        gl3.glUniform1f( sExpLoc, sExp);
    }
}
