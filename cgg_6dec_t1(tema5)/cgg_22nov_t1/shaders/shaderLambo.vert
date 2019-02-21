// Shader-ul de varfuri  
 
 #version 400


layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec3 in_Normal;
layout(location = 2) in vec2 in_Texture;

out vec2 ex_Texture;
out vec3 ex_Normal;
out vec3 ex_FragPos;
out vec3 ex_EyePos;
out vec3 ex_LightPos;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform vec3 fs_EyePos;
uniform vec3 fs_LightPos;


void main(void) {
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1.0f);
	ex_FragPos = in_Position;
	ex_Normal = in_Normal;
	ex_Texture = in_Texture;
	ex_EyePos = fs_EyePos;
	ex_LightPos = fs_LightPos;
} 
 