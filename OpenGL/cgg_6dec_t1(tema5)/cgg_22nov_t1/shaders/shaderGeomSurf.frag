
// Shader-ul de fragment / Fragment shader  
 
 #version 400

in vec3 ex_Color;

out vec4 out_Color;

uniform sampler2D texSampler;

void main(void) {
	out_Color = vec4(ex_Color, 1.0);
} 
 