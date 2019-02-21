// Shader-ul de varfuri  
 
 #version 400


layout(location = 0) in vec3 background_Position;
layout(location = 1) in vec4 background_Color;
//layout(location = 2) in vec4 rect_Position;

out vec4 ex_Color;
uniform mat4 mvpMatrix;


void main(void) {
	gl_Position = mvpMatrix * vec4(background_Position, 1.0f);
    ex_Color = background_Color;
} 
 