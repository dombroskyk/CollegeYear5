#version 150

// incoming vertex attributes
in vec4 vPosition;
in vec4 vColor;

// boundaries for drawing
uniform vec2 wh;

// outgoing color sent to the fragment shader
out vec4 rescolor;

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
