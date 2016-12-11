#version 150

// Phong fragment shader
//
// Contributor:  Kevin Dombrosky

// INCOMING DATA
in vec4 vModel;
in vec3 fragN;

uniform vec4 sourceColor;
uniform vec4 sourcePos;
uniform vec4 ambientColor;

uniform vec4 aMatColor;
uniform vec4 dMatColor;
uniform vec4 sMatColor;

uniform float ka;
uniform float kd;
uniform float ks;
uniform float sExp;

uniform vec3 cPosition;

// OUTGOING DATA
out vec4 fragmentColor;

void main()
{
    // Add all necessary code to implement the
    // fragment shader portion of Phong shading
	vec4 L = normalize( sourcePos - vModel );
	vec4 N = vec4( fragN, 1.0 ); //is this ok?
	vec4 V = normalize( -1.0 * vec4( cPosition, 1.0 ) );
	vec4 R = normalize( -1.0 * reflect( L, N ) );

	vec4 ambient = ambientColor * ka * aMatColor;
	vec4 diffuse = sourceColor * kd * dMatColor * dot( L, N );
	vec4 specular = sourceColor * ks * sMatColor * pow( dot( R, V ), sExp );

    fragmentColor = ambient + diffuse + specular;
	//fragmentColor = ambient + diffuse;
	//fragmentColor = vec4( dot( L, N ), 0.0, 0.0, 0.0 );
	//fragmentColor = N;
}
