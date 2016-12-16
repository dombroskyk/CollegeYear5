#version 150

// Phong fragment shader
//
// Contributor:  Kevin Dombrosky

// INCOMING DATA
in vec4 vModel;
in vec3 fragN;
in vec4 sourcePosEye;

uniform vec4 sourceColor;
uniform vec4 ambientColor;

uniform vec4 aMatColor;
uniform vec4 dMatColor;
uniform vec4 sMatColor;

uniform float ka;
uniform float kd;
uniform float ks;
uniform float sExp;

// OUTGOING DATA
out vec4 fragmentColor;

void main()
{
	// get phong vectors
	vec4 L = normalize( sourcePosEye - vModel );
	vec4 N = normalize( vec4( fragN, 1.0 ) );

	vec4 V = normalize( -vModel );
	// something is wrong here
	vec4 R = reflect( -L, N );

	vec4 ambient = ambientColor * ka * aMatColor;
	vec4 diffuse = sourceColor * kd * dMatColor * max( dot( N, L ), 0.0);
	diffuse = clamp( diffuse, 0.0, 1.0 );
	// something is wrong here
	vec4 specular;
	if( dot( N, L ) < 0.0 ){
		specular = vec4( 0.0, 0.0, 0.0, 1.0 );
	} else {
		specular = sourceColor * ks * sMatColor * pow( max( dot( R, V ), 0.0 ), sExp );
	}
	specular = clamp( specular, 0.0, 1.0 );

    fragmentColor = ambient + diffuse + specular;
    //fragmentColor = ambient + specular;
    //fragmentColor = sourcePosEye;
    //fragmentColor = sourceColor * ks;
	//fragmentColor = vec4( pow( dot( R, V ), sExp ), 0, 0, 1);
}
