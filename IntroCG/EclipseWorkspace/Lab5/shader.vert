//
// Vertex shader for the transformation assignment
//
// Created by Warren R. Carithers 2016/04/22.
//
// Contributor: Kevin Dombrosky
//

#version 150

// attribute:  vertex position
in vec4 vPosition;
in vec3 scaleT;
in vec3 rotateT;
in vec3 translateT;
in vec3 eyepointC;
in vec3 lookatC;
in vec3 upC;
in float leftP;
in float rightP;
in float topP;
in float bottomP;
in float nearP;
in float farP;
in int orthoP;
in int frustumP;

void main()
{
	// I have no idea what's going wrong, it seems like the z coordinates
	// for everything is ending up behind the camera, but all of the code
	// looks correct scrolling through the lecture notes...
	// not sure if I'm missing a normalization call or some other small thing,
	// but it's hard to diagnose what's happening without print statements
	// ran out of time to convert to jama matrices and test that way,
	// spent too much time trying to make this work.
	
	
	// model transformation matrix calculation
	vec3 angles = radians(rotateT);
	vec3 c = cos(angles);
	vec3 s = sin(angles);
	mat4 scaleMat = mat4(scaleT.x, 0.0, 0.0, 0.0,
						0.0, scaleT.y, 0.0, 0.0,
						0.0, 0.0, scaleT.z, 0.0,
						0.0, 0.0, 0.0, 1.0);
	mat4 rotateMatF;
	rotateMatF[0] = vec4(c.z, s.z, 0.0, 0.0);
	rotateMatF[1] = vec4(-s.z, c.z, 0.0, 0.0);
	rotateMatF[2] = vec4(0.0, 0.0, 1.0, 0.0);
	rotateMatF[3] = vec4(0.0, 0.0, 0.0, 1.0);
	mat4 rotateMatS;
	rotateMatS[0] = vec4(c.y, 0.0, -s.y, 0.0);
	rotateMatS[1] = vec4(0.0, 1.0, 0.0, 0.0);
	rotateMatS[2] = vec4(s.y, 0.0, c.y, 0.0);
	rotateMatS[3] = vec4(0.0, 0.0, 0.0, 1.0);
	mat4 translateMat;
	translateMat[0] = vec4(1.0, 0.0, 0.0, 0.0);
	translateMat[1] = vec4(0.0, 1.0, 0.0, 0.0);
	translateMat[2] = vec4(0.0, 0.0, 1.0, 0.0);
	translateMat[3] = vec4(translateT.x, translateT.y, translateT.z, 1.0);
	mat4 modelMat = translateMat * rotateMatS * rotateMatF * scaleMat;

	// projection transformation matrix calculation
	mat4 projectionMat;
    if( orthoP != 0 ){
    	// frustum
    	projectionMat[0] = vec4(2.0/(rightP-leftP), 0.0, 0.0, 0.0);
    	projectionMat[1] = vec4(0.0, 2.0/(topP-bottomP), 0.0, 0.0);
    	projectionMat[2] = vec4(0.0, 0.0, -2.0/(farP-nearP), 0.0);
    	projectionMat[3][0] = -(rightP+leftP)/(rightP-leftP);
    	projectionMat[3][1] = -(topP+bottomP)/(topP-bottomP);
    	projectionMat[3][2] = -(farP+nearP)/(farP-nearP);
    	projectionMat[3][3] = 1.0;
    } else {
    	// ortho
    	projectionMat[0] = vec4((2.0*nearP)/(rightP-leftP), 0.0, 0.0, 0.0);
    	projectionMat[1] = vec4(0.0, (2.0*nearP)/(topP-bottomP), 0.0, 0.0);
    	projectionMat[2][0] = (leftP+rightP)/(rightP-leftP);
    	projectionMat[2][1] = (topP+bottomP)/(topP-bottomP);
    	projectionMat[2][2] = -(farP+nearP)/(farP-nearP);
    	projectionMat[2][3] = -1.0;
    	projectionMat[3] = vec4(0.0, 0.0, (-2.0*nearP*farP)/(farP-nearP), 0.0);
    }
    
    // camera transformation matrix calculation
    vec3 n = normalize(eyepointC - lookatC);
    vec3 u = normalize(cross(upC, n));
    vec3 v = normalize(cross(n, u));
    mat4 cameraMat;
    cameraMat[0] = vec4(u.x, v.x, n.x, 0.0);
    cameraMat[1] = vec4(u.y, v.y, n.y, 0.0);
    cameraMat[2] = vec4(u.z, v.z, n.z, 0.0);
    cameraMat[3] = vec4(-(dot(u, eyepointC)), -(dot(v, eyepointC)), -(dot(n, eyepointC)), 1.0);
    
    gl_Position = projectionMat * cameraMat * modelMat * vPosition;
}
