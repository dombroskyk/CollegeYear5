//
//  Shapes.java
//
//  Routines for tessellating a number of basic shapes
//
//  Students are to supply their implementations for the functions in
//  this file using the function "addTriangle()" to do the tessellation.
//
//  Contributor: Kevin Dombrosky
//

import java.awt.*;
import java.nio.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import java.io.*;


public class Shapes {
    ///
    // Canvas to use when creating objects
    ///
    private Canvas canvas;

    ///
    // constructor
    ///
    public Shapes( Canvas C )
    {
        canvas = C;
    }

    ///
    // makeCube - Create a unit cube, centered at the origin, with a given
    // number of subdivisions in each direction on each face.
    //
    // @param subdivision - number of equal subdivisions to be made in each 
    //        direction along each face
    //
    // Can only use calls to canvas.addTriangle()
    ///
    public void makeCube (int subdivisions)
    {
        if( subdivisions < 1 )
            subdivisions = 1;

        //increment for the distance to move for each subdivision
        float increment = 1.0f / (float) subdivisions;
        
        // add triangles for front/back faces
        for(float currYCount = 0.0f; currYCount < subdivisions; currYCount++){
        	float currY = increment * currYCount - 0.5f;
        	for(float currXCount = 0.0f; currXCount < subdivisions; currXCount++){
        		float currX = increment * currXCount - 0.5f;
        		// front face
        		canvas.addTriangle(
        				new Vertex(currX + increment, currY + increment, .5f),
        				new Vertex(currX, currY, .5f),
        				new Vertex(currX + increment, currY, .5f));
        		canvas.addTriangle(
        				new Vertex(currX + increment, currY + increment, .5f),
        				new Vertex(currX, currY + increment, .5f),
        				new Vertex(currX, currY, .5f));
        		
        		// back face
        		canvas.addTriangle(
        				new Vertex(currX, currY + increment, -.5f),
        				new Vertex(currX + increment, currY, -.5f),
        				new Vertex(currX, currY, -.5f));
        		canvas.addTriangle(
        				new Vertex(currX, currY + increment, -.5f),
        				new Vertex(currX + increment, currY + increment, -.5f),
        				new Vertex(currX + increment, currY, -.5f));
        	}
        }
        
        for(float currZCount = 0.0f; currZCount < subdivisions; currZCount++){
        	float currZ = increment * currZCount - 0.5f;
        	for(float currYCount = 0.0f; currYCount < subdivisions; currYCount++){
        		float currY = increment * currYCount - 0.5f;
        		// right face
        		canvas.addTriangle(
        				new Vertex(.5f, currY + increment, currZ),
        				new Vertex(.5f, currY, currZ + increment),
        				new Vertex(.5f, currY, currZ));
        		canvas.addTriangle(
        				new Vertex(.5f, currY + increment, currZ),
        				new Vertex(.5f, currY + increment, currZ + increment),
        				new Vertex(.5f, currY, currZ + increment));
        		
        		// left face
        		canvas.addTriangle(
        				new Vertex(-.5f, currY + increment, currZ + increment),
        				new Vertex(-.5f, currY, currZ),
        				new Vertex(-.5f, currY, currZ + increment));
        		canvas.addTriangle(
        				new Vertex(-.5f, currY + increment, currZ + increment),
        				new Vertex(-.5f, currY + increment, currZ),
        				new Vertex(-.5f, currY, currZ));
        	}
        }
        
        for(float currXCount = 0.0f; currXCount < subdivisions; currXCount++){
        	float currX = increment * currXCount - 0.5f;
        	for(float currZCount = 0.0f; currZCount < subdivisions; currZCount++){
        		float currZ = increment * currZCount - 0.5f; 
        		// top face
        		canvas.addTriangle(
        				new Vertex(currX + increment, .5f, currZ + increment),
        				new Vertex(currX + increment, .5f, currZ),
        				new Vertex(currX, .5f, currZ));
        		canvas.addTriangle(
        				new Vertex(currX + increment, .5f, currZ + increment),
        				new Vertex(currX, .5f, currZ),
        				new Vertex(currX, .5f, currZ + increment));
        		
        		// bottom face
        		canvas.addTriangle(
        				new Vertex(currX, -.5f, currZ + increment),
        				new Vertex(currX, -.5f, currZ),
        				new Vertex(currX + increment, -.5f, currZ));
        		canvas.addTriangle(
        				new Vertex(currX, -.5f, currZ + increment),
        				new Vertex(currX + increment, -.5f, currZ),
        				new Vertex(currX + increment, -.5f, currZ + increment));
        	}
        }
        
    }

    ///
    // makeCylinder - Create polygons for a cylinder with unit height, centered
    // at the origin, with separate number of radial subdivisions and height 
    // subdivisions.
    //
    // @param radius - Radius of the base of the cylinder
    // @param radialDivision - number of subdivisions on the radial base
    // @param heightDivisions - number of subdivisions along the height
    //
    // Can only use calls to canvas.addTriangle()
    ///
    public void makeCylinder (float radius, int radialDivisions, int heightDivisions)
    {
        if( radialDivisions < 3 )
            radialDivisions = 3;

        if( heightDivisions < 1 )
            heightDivisions = 1;
        
        // radial and height increments
        double alphaIncrement = 2.0d * Math.PI / (double) radialDivisions;
        float heightIncrement = 1.0f / (float) heightDivisions;
        
        for(double alphaCount = 0.0d; alphaCount < radialDivisions; alphaCount++){
        	double alpha = alphaCount * alphaIncrement;
        	float currX = (float) ((double) radius * Math.cos(alpha));
        	float nextX = (float) ((double) radius * Math.cos(alpha + alphaIncrement));
        	float currZ = (float) ((double) radius * Math.sin(alpha));
        	float nextZ = (float) ((double) radius * Math.sin(alpha + alphaIncrement));
        	
        	// top disk
        	canvas.addTriangle(
        			new Vertex(0.0f, 0.5f, 0.0f),
        			new Vertex(currX, 0.5f, currZ),
        			new Vertex(nextX, 0.5f, nextZ));
        	
        	// bottom disk
        	canvas.addTriangle(
        			new Vertex(0.0f, -0.5f, 0.0f),
        			new Vertex(nextX, -0.5f, nextZ),
        			new Vertex(currX, -0.5f, currZ));
        	
        	for(float hCount = 0.0f; hCount < heightDivisions; hCount++){
        		float h = hCount * heightIncrement - 0.5f;
        		// bottom left triangle
        		canvas.addTriangle(
        				new Vertex(currX, h + heightIncrement, currZ),
        				new Vertex(currX, h, currZ),
        				new Vertex(nextX, h, nextZ));
        		
        		// top right triangle
        		canvas.addTriangle(
        				new Vertex(currX, h + heightIncrement, currZ),
        				new Vertex(nextX, h, nextZ),
        				new Vertex(nextX, h + heightIncrement, nextZ));
        	}
        }
        
        

    }

    ///
    // makeCone - Create polygons for a cone with unit height, centered at the
    // origin, with separate number of radial subdivisions and height 
    // subdivisions.
    //
    // @param radius - Radius of the base of the cone
    // @param radialDivision - number of subdivisions on the radial base
    // @param heightDivisions - number of subdivisions along the height
    //
    // Can only use calls to canvas.addTriangle()
    ///
    public void makeCone (float radius, int radialDivisions, int heightDivisions)
    {
        if( radialDivisions < 3 )
            radialDivisions = 3;

        if( heightDivisions < 1 )
            heightDivisions = 1;
        
        // radial increment and height proportion increment
        double alphaIncrement = 2.0d * Math.PI / (double) radialDivisions;
        float heightPortion = 1.0f / (float) heightDivisions;
        
        // add triangles
    	for(double alphaCount = 0.0d; alphaCount < radialDivisions; alphaCount++){
    		double alpha = alphaCount * alphaIncrement;
    		// bottom disk triangle
    		float currX = (float) ((double) radius * Math.cos(alpha));
    		float nextX = (float) ((double) radius * Math.cos(alpha + alphaIncrement));
    		float currZ = (float) ((double) radius * Math.sin(alpha));
    		float nextZ = (float) ((double) radius * Math.sin(alpha + alphaIncrement));
    		canvas.addTriangle(new Vertex(0.0f,  -0.5f,  0.0f), new Vertex(currX, -0.5f, currZ), new Vertex(nextX, -0.5f, nextZ));
    		
    		for( float currPortion = 1.0f; currPortion > heightPortion; currPortion -= heightPortion ){
    			// vertical triangles except top most subdivision
    			float currBottomX = currX * currPortion;
    			float nextBottomX = nextX * currPortion;
    			float currBottomZ = currZ * currPortion;
    			float nextBottomZ = nextZ * currPortion;
    			float bottomY = 1.0f - currPortion - 0.5f;
    		
	    		float currTopX = currX * (currPortion - heightPortion);
	    		float nextTopX = nextX * (currPortion - heightPortion);
	    		float currTopZ = currZ * (currPortion - heightPortion);
	    		float nextTopZ = nextZ * (currPortion - heightPortion);
	    		float topY = 1.0f - currPortion + heightPortion - 0.5f;
	    		
	    		// bottom left triangle
        		canvas.addTriangle(new Vertex(nextTopX, topY, nextTopZ), new Vertex(nextBottomX, bottomY, nextBottomZ), new Vertex(currBottomX, bottomY, currBottomZ));
        		// top right triangle
        		canvas.addTriangle(new Vertex(currTopX, topY, currTopZ), new Vertex(nextTopX, topY, nextTopZ), new Vertex(currBottomX, bottomY, currBottomZ));
        	}
    		
    		// top most subdivision triangles, not cross hatch triangles
    		currX *= heightPortion;
    		nextX *= heightPortion;
    		currZ *= heightPortion;
    		nextZ *= heightPortion;
    		float bottomY = 0.5f - heightPortion; // can do this because unit height
    		canvas.addTriangle(new Vertex(0.0f,  0.5f,  0.0f), new Vertex(nextX, bottomY, nextZ), new Vertex(currX, bottomY, currZ));
        }

    }


    ///
    // makeSphere - Create sphere of a given radius, centered at the origin, 
    // using spherical coordinates with separate number of thetha and 
    // phi subdivisions.
    //
    // @param radius - Radius of the sphere
    // @param slides - number of subdivisions in the theta direction
    // @param stacks - Number of subdivisions in the phi direction.
    //
    // Can only use calls to canvas.addTriangle
    ///
    public void makeSphere (float radius, int slices, int stacks)
    {
	
        if( slices < 3 )
        	slices = 3;

        if( stacks < 3 )
        	stacks = 3;

        // radial increment and height proportion increment
        double thetaIncrement = 2.0d * Math.PI / (double) slices;
        double phiIncrement = Math.PI / (double) stacks;
        
        for(double slice = 0.0d; slice < slices; slice++){
        	double theta = slice * thetaIncrement;
        	for(double stack = 0.0d; stack < stacks; stack++){
        		double phi = stack * phiIncrement;
        		
        		// floating point error
        		float currBottomX = (float) ((double) radius * Math.cos(theta) * Math.sin(phi));
        		float currBottomY = (float) ((double) radius * Math.sin(theta) * Math.sin(phi));
        		float currTopX = (float) ((double) radius * Math.cos(theta) * Math.sin(phi + phiIncrement));
        		float currTopY = (float) ((double) radius * Math.sin(theta) * Math.sin(phi + phiIncrement));
        		
        		float bottomZ = (float) ((double) radius * Math.cos(phi));
        		float topZ = (float) ((double) radius * Math.cos(phi + phiIncrement));
        		
        		float nextBottomX = (float) ((double) radius * Math.cos(theta + thetaIncrement) * Math.sin(phi));
        		float nextBottomY = (float) ((double) radius * Math.sin(theta + thetaIncrement) * Math.sin(phi));
        		float nextTopX = (float) ((double) radius * Math.cos(theta + thetaIncrement) * Math.sin(phi + phiIncrement));
        		float nextTopY = (float) ((double) radius * Math.sin(theta + thetaIncrement) * Math.sin(phi + phiIncrement));
        		
        		// bottom left triangle
        		canvas.addTriangle(
        				new Vertex(nextTopX, nextTopY, topZ),
        				new Vertex(nextBottomX, nextBottomY, bottomZ),
        				new Vertex(currBottomX, currBottomY, bottomZ));
        		// top right triangle
        		canvas.addTriangle(
        				new Vertex(nextTopX, nextTopY, topZ),
        				new Vertex(currBottomX, currBottomY, bottomZ),
        				new Vertex(currTopX, currTopY, topZ));
        	}
        }

    }
}
