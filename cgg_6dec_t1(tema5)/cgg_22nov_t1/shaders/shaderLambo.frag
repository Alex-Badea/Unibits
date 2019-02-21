
// Shader-ul de fragment / Fragment shader  
 
 #version 400

in vec2 ex_Texture;
in vec3 ex_Normal;
in vec3 ex_FragPos;
in vec3 ex_EyePos;
in vec3 ex_LightPos;

out vec4 out_Color;

uniform sampler2D texSampler;

void main(void) {
	vec4 color = texture2D(texSampler, ex_Texture);
	if (color.a < 1.0)
		discard;

	//diff
	vec3 lightVector = normalize(ex_LightPos - ex_FragPos);
	float brightness = dot(lightVector, ex_Normal);	
	vec4 diffuseLight = vec4(brightness, brightness, brightness, 1.0);

	//spec
	vec3 reflectedLightVector = reflect(lightVector, ex_Normal);
	vec3 eyeVector = normalize(ex_EyePos - ex_FragPos);
	float specularity = pow(dot(reflectedLightVector, eyeVector), 10);
	vec4 specularLight = vec4(specularity, specularity, specularity, 1.0);
   
	out_Color = color * (clamp(diffuseLight, 0, 1) + clamp(specularLight, 0, 1));
} 
 