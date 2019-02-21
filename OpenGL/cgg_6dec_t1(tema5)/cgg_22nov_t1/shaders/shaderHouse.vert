// Shader-ul de varfuri  
 
 #version 400


layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec3 in_Normal;
layout(location = 2) in vec2 in_Texture;

out vec2 ex_Texture;
out vec3 ex_Normal;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;


void main(void) {
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1.0f);
	ex_Normal = in_Normal;
	ex_Texture = in_Texture;
} 
 