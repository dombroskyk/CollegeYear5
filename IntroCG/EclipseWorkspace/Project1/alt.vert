//
// This is an alternate version of the standard vertex shader
// for the 2D assignments in this course.  This is NOT the shader
// that will be used when you submit your solution, but it can
// be used with older OpenGL/GLSL versions to perform the same
// operations.
//
// To use this shader, either rename the file "shader.vert" or
// replace the "shader.vert" string in the call to shaderSetup()
// in the main program source file with "alt.vert".
//

#version 120

// incoming vertex attributes
attribute vec4 vPosition;
attribute vec4 vColor;

// boundaries for drawing
uniform vec2 wh;

// outgoing color sent to the fragment shader
varying vec4 rescolor;

void main()
{
    // normalize the location in (x,y)
    float x = vPosition.x;
    float y = vPosition.y;
    
    float sx = 2.0 / (wh[0] - 1.0);
    float sy = 2.0 / (wh[1] - 1.0);

    x = x * sx - 1.0;
    y = y * sy - 1.0;

    vec4 newvert = vec4( x, y, vPosition.z, vPosition.w );

    gl_Position = newvert;
    rescolor = vColor;
}
