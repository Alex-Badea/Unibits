#define GLM_ENABLE_EXPERIMENTAL
#define _CRT_SECURE_NO_WARNINGS
#define TINYOBJLOADER_IMPLEMENTATION
#define STB_IMAGE_IMPLEMENTATION

#include <iostream>
#include <vector>
#include <string>
#include <windows.h>  // biblioteci care urmeaza sa fie incluse
#include <stdlib.h> // necesare pentru citirea shader-elor
#include <stdio.h>
#include <conio.h>
#include <time.h> 
#include "GL/glew.h" // glew apare inainte de freeglut
#include "GL/freeglut.h" // nu trebuie uitat freeglut.h
#include "glm/glm.hpp"
#include "glm/gtc/matrix_transform.hpp"
#include "glm/gtx/string_cast.hpp"
#include "myHeader.h"
#include "Vertex.h"
#include "tiny_obj_loader.h"
#include "stb_image.h"

#define cglm(msg, glmvar) ;std::cout << msg << ": " << #glmvar << " = " << glm::to_string(glmvar) << std::endl;
#define cc ;std::cout <<
#define ce ;std::cout << std::endl; 

//////////////////////////////////////

//program
GLuint
VaoIdLambo,
VboIdLambo,
TexIdLamboBody,
TexIdLamboWheels,
TexIdLamboGlass,
TexIdLamboPaint,

VaoIdHouse,
VboIdHouse,
TexIdHouse,

VaoIdHyp,
VboIdHyp,
CboIdHyp,

VaoIdKlein,
VboIdKlein,
CboIdKlein,

ProgramId;

//obiecte
std::vector<Vertex> verticesLambo;
std::vector<GLuint> componentsPerShapeLambo;

std::vector<Vertex> verticesHouse;

std::vector<glm::vec3> verticesHyp;
std::vector<glm::vec3> colorsHyp;
std::vector<GLuint> indicesHyp;

std::vector<glm::vec3> verticesKlein;
std::vector<glm::vec3> colorsKlein;
std::vector<GLuint> indicesKlein;

//spațiul vizual
const glm::vec3 originPos(0.0f, 0.0f, 0.0f);
glm::vec3 lightPos;
glm::mat4 modelMatrix;
glm::vec3 eyePos;
glm::vec3 lookPos;
glm::vec3 upDir;
glm::mat4 viewMatrix;
glm::mat4 projectionMatrix;


void CreateVBOLambo(void) {
	tinyobj::attrib_t attrib;
	std::vector<tinyobj::material_t> materials;
	std::vector<tinyobj::shape_t> shapes;

	std::string err;
	bool ret = tinyobj::LoadObj(&attrib, &shapes, &materials, &err, "objects/lambo_smooth.obj");

	if (!err.empty()) { // `err` may contain warning message.
		std::cerr << err << std::endl;
	}

	// Loop over shapes
	for (size_t s = 0; s < shapes.size(); s++) {
		// Loop over faces(polygon)
		size_t index_offset = 0;
		for (size_t f = 0; f < shapes[s].mesh.num_face_vertices.size(); f++) {
			int fv = shapes[s].mesh.num_face_vertices[f];

			// Loop over vertices in the face.
			for (size_t v = 0; v < fv; v++) {
				// access to vertex
				tinyobj::index_t idx = shapes[s].mesh.indices[index_offset + v];
				tinyobj::real_t vx = attrib.vertices[3 * idx.vertex_index + 0];
				tinyobj::real_t vy = attrib.vertices[3 * idx.vertex_index + 1];
				tinyobj::real_t vz = attrib.vertices[3 * idx.vertex_index + 2];
				tinyobj::real_t nx = attrib.normals[3 * idx.normal_index + 0];
				tinyobj::real_t ny = attrib.normals[3 * idx.normal_index + 1];
				tinyobj::real_t nz = attrib.normals[3 * idx.normal_index + 2];
				tinyobj::real_t tx = attrib.texcoords[2 * idx.texcoord_index + 0];
				tinyobj::real_t ty = attrib.texcoords[2 * idx.texcoord_index + 1];

				verticesLambo.push_back(Vertex(glm::vec3(vx, vy, vz), glm::vec3(nx, ny, nz), glm::vec2(tx, ty)));
			}
			index_offset += fv;
			// per-face material
			shapes[s].mesh.material_ids[f];
		}
		componentsPerShapeLambo.push_back(verticesLambo.size());
	}
	glGenVertexArrays(1, &VaoIdLambo);
	glBindVertexArray(VaoIdLambo); {
		glGenBuffers(1, &VboIdLambo);
		glBindBuffer(GL_ARRAY_BUFFER, VboIdLambo); {
			glBufferData(GL_ARRAY_BUFFER, verticesLambo.size() * sizeof(Vertex), &verticesLambo[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), nullptr);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)sizeof(glm::vec3));
			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)(sizeof(glm::vec3) + sizeof(glm::vec3)));
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
	} glBindVertexArray(0);

	int width, height, bpp;
	unsigned char* texture;
	texture = stbi_load("objects/lambotex/nodamage.tga", &width, &height, &bpp, 4);
	glGenTextures(1, &TexIdLamboBody);
	glBindTexture(GL_TEXTURE_2D, TexIdLamboBody); {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
	}

	texture = stbi_load("objects/lambotex/tireA0.tga", &width, &height, &bpp, 4);
	glGenTextures(1, &TexIdLamboWheels);
	glBindTexture(GL_TEXTURE_2D, TexIdLamboWheels); {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
	}

	texture = stbi_load("objects/lambotex/glass0.tga", &width, &height, &bpp, 4);
	glGenTextures(1, &TexIdLamboGlass);
	glBindTexture(GL_TEXTURE_2D, TexIdLamboGlass); {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
	}

	texture = stbi_load("objects/lambotex/carbonFiber.tga", &width, &height, &bpp, 4);
	glGenTextures(1, &TexIdLamboPaint);
	glBindTexture(GL_TEXTURE_2D, TexIdLamboPaint); {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
	}
}
void CreateVBOHouse(void) {
	tinyobj::attrib_t attrib;
	std::vector<tinyobj::material_t> materials;
	std::vector<tinyobj::shape_t> shapes;

	std::string err;
	bool ret = tinyobj::LoadObj(&attrib, &shapes, &materials, &err, "objects/house.obj");

	if (!err.empty()) { // `err` may contain warning message.
		std::cerr << err << std::endl;
	}

	// Loop over shapes
	for (size_t s = 0; s < shapes.size(); s++) {
		// Loop over faces(polygon)
		size_t index_offset = 0;
		for (size_t f = 0; f < shapes[s].mesh.num_face_vertices.size(); f++) {
			int fv = shapes[s].mesh.num_face_vertices[f];

			// Loop over vertices in the face.
			for (size_t v = 0; v < fv; v++) {
				// access to vertex
				tinyobj::index_t idx = shapes[s].mesh.indices[index_offset + v];
				tinyobj::real_t vx = attrib.vertices[3 * idx.vertex_index + 0];
				tinyobj::real_t vy = attrib.vertices[3 * idx.vertex_index + 1];
				tinyobj::real_t vz = attrib.vertices[3 * idx.vertex_index + 2];
				tinyobj::real_t nx = attrib.normals[3 * idx.normal_index + 0];
				tinyobj::real_t ny = attrib.normals[3 * idx.normal_index + 1];
				tinyobj::real_t nz = attrib.normals[3 * idx.normal_index + 2];
				tinyobj::real_t tx = attrib.texcoords[2 * idx.texcoord_index + 0];
				tinyobj::real_t ty = attrib.texcoords[2 * idx.texcoord_index + 1];

				verticesHouse.push_back(Vertex(glm::vec3(vx, vy, vz), glm::vec3(nx, ny, nz), glm::vec2(tx, ty)));
			}
			index_offset += fv;
			// per-face material
			shapes[s].mesh.material_ids[f];
		}
	}
	glGenVertexArrays(1, &VaoIdHouse);
	glBindVertexArray(VaoIdHouse); {
		glGenBuffers(1, &VboIdHouse);
		glBindBuffer(GL_ARRAY_BUFFER, VboIdHouse); {
			glBufferData(GL_ARRAY_BUFFER, verticesHouse.size() * sizeof(Vertex), &verticesHouse[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), nullptr);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)sizeof(glm::vec3));
			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)(sizeof(glm::vec3) + sizeof(glm::vec3)));
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
	} glBindVertexArray(0);

	int width, height, bpp;
	unsigned char* texture;
	texture = stbi_load("objects/house.jpg", &width, &height, &bpp, 4);
	glGenTextures(1, &TexIdHouse);
	glBindTexture(GL_TEXTURE_2D, TexIdHouse); {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
	}
}
void CreateVBOHyp(unsigned nrTropice, unsigned nrMeridiane) {
	for (int i = 0; i <= nrTropice; i++) {
		float u = 2 * glm::pi <float>() * (0.5f - i / (float)nrTropice);

		for (int j = 0; j <= nrMeridiane; j++) {
			float v = 2 * glm::pi <float>() * (j / (float)nrMeridiane);

			auto s = [](float u, float v) -> glm::vec3 {
				return glm::vec3(glm::cosh(u) * glm::cos(v), glm::cosh(u) * glm::sin(v), glm::sinh(u));
			};

			verticesHyp.push_back(s(u, v));
		}
	}
	for (int i = 0; i < nrMeridiane * nrTropice + nrTropice; ++i) {
		indicesHyp.push_back(i);
		indicesHyp.push_back(i + nrMeridiane + 1);
		indicesHyp.push_back(i + nrMeridiane);

		indicesHyp.push_back(i + nrMeridiane + 1);
		indicesHyp.push_back(i);
		indicesHyp.push_back(i + 1);
	}

	//generare de culori aleatorii
	for (int i = 0; i < verticesHyp.size(); i++) {
		colorsHyp.push_back(glm::vec3(rand() % 1000 / 1000.0f, rand() % 1000 / 1000.0f, rand() % 1000 / 1000.0f));
	}

	glGenVertexArrays(1, &VaoIdHyp);
	glBindVertexArray(VaoIdHyp); {
		glGenBuffers(1, &VboIdHyp);
		glBindBuffer(GL_ARRAY_BUFFER, VboIdHyp); {
			glBufferData(GL_ARRAY_BUFFER, verticesHyp.size() * sizeof(glm::vec3), &verticesHyp[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
		glGenBuffers(1, &CboIdHyp);
		glBindBuffer(GL_ARRAY_BUFFER, CboIdHyp); {
			glBufferData(GL_ARRAY_BUFFER, colorsHyp.size() * sizeof(glm::vec3), &colorsHyp[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
	} glBindVertexArray(0);
}
void CreateVBOKlein(unsigned nrTropice, unsigned nrMeridiane) {
	for (int i = 0; i <= nrTropice; i++) {
		float u = 2 * glm::pi <float>() * (i / (float)nrTropice);

		for (int j = 0; j <= nrMeridiane; j++) {
			float v = 2 * glm::pi <float>() * (j / (float)nrMeridiane);

			auto s = [](float u, float v) -> glm::vec3 {
				using glm::sin;
				float a = 3.0f;
				return glm::vec3(
					/*x=*/	(a + cos(1 / 2.0f*u)*sin(v) - sin(1 / 2.0f*u)*sin(2 * v))*cos(u),
					/*y=*/	(a + cos(1 / 2.0f*u)*sin(v) - sin(1 / 2.0f*u)*sin(2 * v))*sin(u),
					/*z=*/	sin(1 / 2.0f*u)*sin(v) + cos(1 / 2.0f*u)*sin(2 * v)
				);
			};

			verticesKlein.push_back(s(u, v));
		}
	}
	for (int i = 0; i < nrMeridiane * nrTropice + nrTropice; ++i) {
		indicesKlein.push_back(i);
		indicesKlein.push_back(i + nrMeridiane + 1);
		indicesKlein.push_back(i + nrMeridiane);

		indicesKlein.push_back(i + nrMeridiane + 1);
		indicesKlein.push_back(i);
		indicesKlein.push_back(i + 1);
	}

	//generare de culori aleatorii
	for (int i = 0; i < verticesKlein.size(); i++) {
		colorsKlein.push_back(glm::vec3(rand() % 1000 / 1000.0f, rand() % 1000 / 1000.0f, rand() % 1000 / 1000.0f));
	}

	glGenVertexArrays(1, &VaoIdKlein);
	glBindVertexArray(VaoIdKlein); {
		glGenBuffers(1, &VboIdKlein);
		glBindBuffer(GL_ARRAY_BUFFER, VboIdKlein); {
			glBufferData(GL_ARRAY_BUFFER, verticesKlein.size() * sizeof(glm::vec3), &verticesKlein[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
		glGenBuffers(1, &CboIdKlein);
		glBindBuffer(GL_ARRAY_BUFFER, CboIdKlein); {
			glBufferData(GL_ARRAY_BUFFER, colorsKlein.size() * sizeof(glm::vec3), &colorsKlein[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
	} glBindVertexArray(0);
}

void CreateShadersLambo(void) {
	ProgramId = LoadShaders("shaders/shaderLambo.vert", "shaders/shaderLambo.frag");
	glUseProgram(ProgramId);
}
void CreateShadersHouse(void) {
	ProgramId = LoadShaders("shaders/shaderHouse.vert", "shaders/shaderHouse.frag");
	glUseProgram(ProgramId);
}
void CreateShadersGeomSurf(void) {
	ProgramId = LoadShaders("shaders/shaderGeomSurf.vert", "shaders/shaderGeomSurf.frag");
	glUseProgram(ProgramId);
}
void DeleteShaders(void) {
	glUseProgram(0);
	glDeleteProgram(ProgramId);
}

void Initialize(void) {
	glutSetCursor(GLUT_CURSOR_NONE);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	CreateVBOLambo();
	CreateVBOHouse();
	CreateVBOHyp(100, 100);
	CreateVBOKlein(100, 100);

	lightPos = glm::vec3(0.0f, 10.0f, 0.0f);
	modelMatrix = glm::mat4(1.0f);
	eyePos = glm::vec3(0, 0, -3);
	lookPos = glm::vec3(0, 0, 0);
	upDir = glm::vec3(0, 1, 0);
	projectionMatrix = glm::perspective(glm::radians(60.0f), glutGet(GLUT_WINDOW_WIDTH) / (float)glutGet(GLUT_WINDOW_HEIGHT), 0.01f, 100.0f);
}

void RenderFunction(void) {
	glClearColor(0.0f, 0.0f, 0.3f, 0.0f); // culoarea de fond a ecranului
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	viewMatrix = glm::lookAt(eyePos, lookPos, upDir);

	//lambo
	modelMatrix = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, 0.14f, 1.2f)) * glm::scale(glm::mat4(1.0f), glm::vec3(0.2f, 0.2f, 0.2f));
	CreateShadersLambo(); {
		glUniform3f(glGetUniformLocation(ProgramId, "fs_EyePos"), eyePos.x, eyePos.y, eyePos.z);
		glUniform3f(glGetUniformLocation(ProgramId, "fs_LightPos"), lightPos.x, lightPos.y, lightPos.z);
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "modelMatrix"), 1, GL_FALSE, &modelMatrix[0][0]);
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "viewMatrix"), 1, GL_FALSE, &viewMatrix[0][0]);
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "projectionMatrix"), 1, GL_FALSE, &projectionMatrix[0][0]);
		glBindVertexArray(VaoIdLambo); {
			glBindTexture(GL_TEXTURE_2D, TexIdLamboWheels);
			glDrawArrays(GL_TRIANGLES, 0, componentsPerShapeLambo[1]);

			glBindTexture(GL_TEXTURE_2D, TexIdLamboGlass);
			glDrawArrays(GL_TRIANGLES, componentsPerShapeLambo[1], componentsPerShapeLambo[2] - componentsPerShapeLambo[1]);

			glBindTexture(GL_TEXTURE_2D, TexIdLamboWheels);
			glDrawArrays(GL_TRIANGLES, componentsPerShapeLambo[2], componentsPerShapeLambo[3] - componentsPerShapeLambo[2]);

			glBindTexture(GL_TEXTURE_2D, TexIdLamboBody);
			glDrawArrays(GL_TRIANGLES, componentsPerShapeLambo[3], componentsPerShapeLambo[4] - componentsPerShapeLambo[3]);

			glBindTexture(GL_TEXTURE_2D, TexIdLamboBody);
			glDrawArrays(GL_TRIANGLES, componentsPerShapeLambo[4], componentsPerShapeLambo[5] - componentsPerShapeLambo[4]);

			glBindTexture(GL_TEXTURE_2D, TexIdLamboWheels);
			glDrawArrays(GL_TRIANGLES, componentsPerShapeLambo[5], componentsPerShapeLambo[6] - componentsPerShapeLambo[5]);
		} glBindVertexArray(0);
	} DeleteShaders();

	//house
	modelMatrix = glm::scale(glm::mat4(1.0f), glm::vec3(0.1f, 0.1f, 0.1f));
	CreateShadersHouse(); {
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "modelMatrix"), 1, GL_FALSE, &modelMatrix[0][0]);
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "viewMatrix"), 1, GL_FALSE, &viewMatrix[0][0]);
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "projectionMatrix"), 1, GL_FALSE, &projectionMatrix[0][0]);
		glBindVertexArray(VaoIdHouse); {
			glBindTexture(GL_TEXTURE_2D, TexIdHouse);
			glDrawArrays(GL_TRIANGLES, 0, verticesHouse.size());
		} glBindVertexArray(0);
	} DeleteShaders();

	//suprafete parametrizate
	CreateShadersGeomSurf(); {
		//hyp
		modelMatrix = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, 1.0f, 0.0f)) * glm::scale(glm::mat4(1.0f), glm::vec3(0.01f, 0.01f, 0.01f));
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "modelMatrix"), 1, GL_FALSE, &modelMatrix[0][0]);
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "viewMatrix"), 1, GL_FALSE, &viewMatrix[0][0]);
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "projectionMatrix"), 1, GL_FALSE, &projectionMatrix[0][0]);
		glBindVertexArray(VaoIdHyp); {
			glDrawElements(GL_TRIANGLES, indicesHyp.size(), GL_UNSIGNED_INT, &indicesHyp[0]);
		} glBindVertexArray(0);
		//klein "figure-8" bottle
		modelMatrix = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, 1.0f, 0.0f)) * glm::scale(glm::mat4(1.0f), glm::vec3(0.1f, 0.1f, 0.1f));
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "modelMatrix"), 1, GL_FALSE, &modelMatrix[0][0]);
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "viewMatrix"), 1, GL_FALSE, &viewMatrix[0][0]);
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "projectionMatrix"), 1, GL_FALSE, &projectionMatrix[0][0]);
		glBindVertexArray(VaoIdKlein); {
			glDrawElements(GL_TRIANGLES, indicesKlein.size(), GL_UNSIGNED_INT, &indicesKlein[0]);
		} glBindVertexArray(0);
	} DeleteShaders();

	glutSwapBuffers();
	glutPostRedisplay();
}

void KeyboardFunction(unsigned char key, int x, int y) {
	glm::vec3 forwardDir = glm::normalize(lookPos - eyePos);
	glm::vec3 leftDir = glm::normalize(glm::cross(upDir, forwardDir));
	const float SPEED = 0.01;
	const float BOOST = glutGetModifiers() == GLUT_ACTIVE_SHIFT ? 5 : 1;
	switch (tolower(key)) {
	case 'w':
		//blocheaza observatorul dacă e prea aproape de centru
		if (glm::dot(eyePos - lookPos, eyePos - lookPos) <= 0.5f)
			break;
		eyePos = eyePos + SPEED*BOOST*forwardDir;
		break;
	case 's':
		eyePos = eyePos - SPEED*BOOST*forwardDir;
		break;
	case '[':
		lightPos += glm::vec3(0.0f, 0.0f, -5.0f);
		cc lightPos.z ce
			break;
	case ']':
		lightPos += glm::vec3(0.0f, 0.0f, 5.0f);
		break;
	}
	glutPostRedisplay();
}
void PassiveMouseFunction(int x, int y) {
	int centerX = glutGet(GLUT_WINDOW_WIDTH) / 2;
	int centerY = glutGet(GLUT_WINDOW_HEIGHT) / 2;
	if (x != centerX || y != centerY) {
		glutWarpPointer(centerX, centerY);
		float deltaX = x - centerX;
		float deltaY = y - centerY;
		const float SENSIVITY = 0.003;

		//mișcarea stâna-dreapta:
		eyePos = (glm::vec3)(glm::rotate(glm::mat4(1.0f), -SENSIVITY * deltaX, upDir) * glm::vec4(eyePos, 1.0f));
		//mișcarea sus-jos:
		glm::vec3 forwardDir = glm::normalize(lookPos - eyePos);
		glm::vec3 leftDir = glm::normalize(glm::cross(upDir, forwardDir));
		eyePos = static_cast<glm::vec3>(glm::rotate(glm::mat4(1.0f), SENSIVITY * deltaY, leftDir) * glm::vec4(eyePos, 1.0f));
		//regenerarea axei superioare denaturată
		//upDir = glm::normalize(glm::cross(forwardDir, leftDir));

		cc lookPos.x cc "," cc lookPos.y cc "," cc lookPos.z ce
	}
	glutPostRedisplay();
}

int main(int argc, char* argv[]) {
	srand(time(nullptr));
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowPosition(0, 0);
	glutInitWindowSize(1024, 600); //dimensiunile ferestrei
	glutCreateWindow("House of Geometry"); // titlul ferestrei

	glewInit(); // nu uitati de initializare glew; trebuie initializat inainte de a a initializa desenarea

	Initialize();
	glutDisplayFunc(RenderFunction);
	glutPassiveMotionFunc(PassiveMouseFunction);
	glutKeyboardFunc(KeyboardFunction);

	glutMainLoop();

	_getch();
}

