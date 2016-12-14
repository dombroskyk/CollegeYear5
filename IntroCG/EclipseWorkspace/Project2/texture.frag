#version 150

// Texture mapping vertex shader
//
// Contributor:  YOUR_NAME_HERE

// INCOMING DATA
in vec2 texCoord;
uniform sampler2D texSmile;
uniform sampler2D texFrown;

// OUTGOING DATA

out vec4 fragmentColor;

void main()
{
    // Replace with proper texture mapping code
	if( gl_FrontFacing ){
		fragmentColor = texture( texSmile, texCoord );
	} else {
		fragmentColor = texture( texFrown, texCoord );
	}

}
