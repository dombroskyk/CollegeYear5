//
// This is an alternate version of the standard fragment shader
// for the 2D assignments in this course.  This is NOT the shader
// that will be used when you submit your solution, but it can
// be used with older OpenGL/GLSL versions to perform the same
// operations.
//
// To use this shader, either rename the file "shader.frag", or
// replace the "shader.frag" string in the call to shaderSetup()
// in the main program source file with "alt.frag".
//

#version 120

// incoming color from the vertex shader
varying vec4 rescolor;

void main()
{
    gl_FragColor = rescolor;
}
