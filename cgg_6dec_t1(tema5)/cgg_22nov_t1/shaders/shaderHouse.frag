
// Shader-ul de fragment / Fragment shader  
 
 #version 400

in vec2 ex_Texture;
in vec3 ex_Normal;

out vec4 out_Color;

uniform sampler2D texSampler;

void main(void) {
	vec4 color = texture2D(texSampler, ex_Texture);
	if (color.a < 1.0)
		discard;

	out_Color = color;
} 
 