#define GLM_ENABLE_EXPERIMENTAL
#define _CRT_SECURE_NO_WARNINGS

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <limits>
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
#include "glm/gtx/norm.hpp"
#include "myHeader.h"

#define cglm(msg, glmvar) ;std::cout << msg << ": " << #glmvar << " = " << glm::to_string(glmvar) << std::endl;
#define cc ;std::cout <<			//thank you, whoever you are; all credits due
#define ce ;std::cout << std::endl; 

//////////////////////////////////////

//program
GLuint
VaoId,
VboId,
sunCboId,
earthCboId,
moonCboId,
venusCboId,
EboId,
ProgramId;

//obiecte
enum Planet{ EARTH, MOON, SUN, VENUS };
//poziția 0 în shader
std::vector<glm::vec3> sphereVertices;
//poziția 1 în shader
std::vector<glm::vec4> sunColors;
//poziția 2 în shader
std::vector<glm::vec4> earthColors;
//poziția 3 în shader
std::vector<glm::vec4> moonColors;
//poziția 4 în shader
std::vector<glm::vec4> venusColors;
std::vector<GLuint> sphereIndices;

//spațiul vizual
//UNGHIURILE SUNT MODULO 36000
unsigned sunRotAngle;
unsigned earthRotAngle;
unsigned earthRevAngle;
unsigned moonRotAngle;
unsigned moonRevAngle;
glm::vec3 sunPos;
glm::vec3 earthPos;
glm::vec3 moonPos;
const glm::vec3 originPos(0.0f, 0.0f, 0.0f);
glm::mat4 modelMatrix;
glm::vec3 eyePos;
glm::vec3 lookPos;
glm::vec3 upDir;
glm::mat4 viewMatrix;
glm::mat4 projectionMatrix;
glm::mat4 mvpMatrix;

//cel mai bine funcționează cu fișiere exportate din blender; netestat în alte cazuri
void parseObj(const char* filename, std::vector<glm::vec3>& outVertices, std::vector<GLuint>& outIndices) {
	std::ifstream in(filename);
	std::string junk;

	//omiterea header-ului
	while (in >> junk && junk != "v");

	GLfloat x, y, z;
	//citirea vârfurilor
	while (in >> x, in >> y, in >> z, in >> junk && junk == "v")
		outVertices.push_back(glm::vec3(x, y, z));
	outVertices.push_back(glm::vec3(x, y, z));

	//omiterea normalelor
	while (in >> junk && junk != "f");

	//citirea indicilor (obj-urile sunt 1-indexate)
	while (in >> x, in >> junk, in >> y, in >> junk, in >> z, in >> junk, in >> junk && junk == "f") {
		outIndices.push_back(x - 1);
		outIndices.push_back(y - 1);
		outIndices.push_back(z - 1);
	}
	outIndices.push_back(x - 1);
	outIndices.push_back(y - 1);
	outIndices.push_back(z - 1);
}

void generateRandomPlanetColoration(unsigned verticesNo, Planet planet, std::vector<glm::vec4>& outColors) {
	if (planet == Planet::SUN) {
		srand(time(0));
		float r = 1.00f, g = 0.75f, b = 0.0f, a = 0.0f;
		int weight = 1.0f;
		for (int i = 0; i < verticesNo; i++) {
			if (weight == 1.0f) {
				weight = rand() % 10 < 5 ? -1.0f : 1.0f;
			}
			else if (weight == -1.0f) {
				weight = rand() % 10 < 5 ? 1.0f : -1.0f;
			}
			r = r + weight*(rand() % 1000 / 100000.0f);
			g = 1 - g + 0.75f;
			outColors.push_back(glm::vec4(r, g, b, a));
		}
	}
	else if (planet == Planet::EARTH) {
		srand(time(0));
		float r = 0.0f, g = 0.5f, b = 0.5f, a = 0.0f;
		int weight = 1.0f;
		for (int i = 0; i < verticesNo; i++) {
			if (weight == 1.0f) {
				weight = rand() % 10 < 5 ? -1.0f : 1.0f;
			}
			else if (weight == -1.0f) {
				weight = rand() % 10 < 5 ? 1.0f : -1.0f;
			}
			g = g + weight*(rand() % 1000 / 100000.0f);
			b = 1 - g;
			outColors.push_back(glm::vec4(r, g, b, a));
		}
	}
	else if (planet == Planet::MOON) {
		srand(time(0));
		float r = 0.75f, g = 0.75f, b = 0.75f, a = 0.0f;
		int weight = 1.0f;
		for (int i = 0; i < verticesNo; i++) {
			if (weight == 1.0f) {
				weight = rand() % 10 < 5 ? -1.0f : 1.0f;
			}
			else if (weight == -1.0f) {
				weight = rand() % 10 < 5 ? 1.0f : -1.0f;
			}
			float amount = weight*(rand() % 1000 / 1000000.0f);
			r += amount;
			g += amount;
			b += amount;
			outColors.push_back(glm::vec4(r, g, b, a));
		}
	}
	else if (planet == Planet::VENUS) {
		srand(time(0));
		float r = 0.7f, g = 0.4f, b = 0.0f, a = 0.0f;
		int weight = 1.0f;
		for (int i = 0; i < verticesNo; i++) {
			if (weight == 1.0f) {
				weight = rand() % 10 < 5 ? -1.0f : 1.0f;
			}
			else if (weight == -1.0f) {
				weight = rand() % 10 < 5 ? 1.0f : -1.0f;
			}
			r = r + weight*(rand() % 1000 / 100000.0f);
			g = r + weight*0.1f - 0.2f;
			outColors.push_back(glm::vec4(r, g, b, a));
		}
	}
	else throw "Invalid planet";
}

void CreateVBO(void)
{
	parseObj("objects/sphere.obj", ::sphereVertices, ::sphereIndices);
	generateRandomPlanetColoration(sphereVertices.size(), Planet::SUN, ::sunColors);
	generateRandomPlanetColoration(sphereVertices.size(), Planet::EARTH, ::earthColors);
	generateRandomPlanetColoration(sphereVertices.size(), Planet::MOON, ::moonColors);
	generateRandomPlanetColoration(sphereVertices.size(), Planet::VENUS, ::venusColors);

	glGenVertexArrays(1, &VaoId);
	glBindVertexArray(VaoId); {
		glGenBuffers(1, &VboId);
		glBindBuffer(GL_ARRAY_BUFFER, VboId); {
			glBufferData(GL_ARRAY_BUFFER, sphereVertices.size() * sizeof(glm::vec3), &sphereVertices[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
		glGenBuffers(1, &sunCboId);
		glBindBuffer(GL_ARRAY_BUFFER, sunCboId); {
			glBufferData(GL_ARRAY_BUFFER, sunColors.size() * sizeof(glm::vec4), &sunColors[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, GL_FALSE, 0, nullptr);
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
		glGenBuffers(1, &earthCboId);
		glBindBuffer(GL_ARRAY_BUFFER, earthCboId); {
			glBufferData(GL_ARRAY_BUFFER, earthColors.size() * sizeof(glm::vec4), &earthColors[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 4, GL_FLOAT, GL_FALSE, 0, nullptr);
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
		glGenBuffers(1, &moonCboId);
		glBindBuffer(GL_ARRAY_BUFFER, moonCboId); {
			glBufferData(GL_ARRAY_BUFFER, moonColors.size() * sizeof(glm::vec4), &moonColors[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(3);
			glVertexAttribPointer(3, 4, GL_FLOAT, GL_FALSE, 0, nullptr);
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
		glGenBuffers(1, &venusCboId);
		glBindBuffer(GL_ARRAY_BUFFER, venusCboId); {
			glBufferData(GL_ARRAY_BUFFER, venusColors.size() * sizeof(glm::vec4), &venusColors[0], GL_STATIC_DRAW);
			glEnableVertexAttribArray(4);
			glVertexAttribPointer(4, 4, GL_FLOAT, GL_FALSE, 0, nullptr);
		} glBindBuffer(GL_ARRAY_BUFFER, 0);
		glGenBuffers(1, &EboId);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EboId); {
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, sphereIndices.size() * sizeof(GLfloat), &sphereIndices[0], GL_STATIC_DRAW);
		} glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	} glBindVertexArray(0);
}

void CreateSunShaders(void) {
	ProgramId = LoadShaders("shaders/sunShader.vert", "shaders/shader.frag");
	glUseProgram(ProgramId);
}
void CreateEarthShaders(void) {
	ProgramId = LoadShaders("shaders/earthShader.vert", "shaders/shader.frag");
	glUseProgram(ProgramId);
}
void CreateMoonShaders(void) {
	ProgramId = LoadShaders("shaders/moonShader.vert", "shaders/shader.frag");
	glUseProgram(ProgramId);
}
void CreateVenusShaders(void) {
	ProgramId = LoadShaders("shaders/venusShader.vert", "shaders/shader.frag");
	glUseProgram(ProgramId);
}
void DeleteShaders(void) {
	glUseProgram(0);
	glDeleteProgram(ProgramId);
}

void Initialize(void)
{
	glutSetCursor(GLUT_CURSOR_NONE);
	glEnable(GL_DEPTH_TEST);
	CreateVBO();

	sunRotAngle = 0.0f;
	sunPos = originPos;
	earthPos = glm::vec3(0.0f, 0.0f, -5.0f);
	moonPos = glm::vec3(0.0f, 0.0f, -7.0f);
	modelMatrix = glm::mat4(1.0f);
	eyePos = glm::vec3(0, 0, 2);
	lookPos = glm::vec3(0, 0, 0);
	upDir = glm::vec3(0, 1, 0);
	projectionMatrix = glm::perspective(glm::radians(60.0f), glutGet(GLUT_WINDOW_WIDTH) / (float)glutGet(GLUT_WINDOW_HEIGHT), 0.01f, 100.0f);
}

void RenderFunction(void)
{
	glClearColor(0.0f, 0.0f, 0.3f, 0.0f); // culoarea de fond a ecranului
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	viewMatrix = glm::lookAt(eyePos, lookPos, upDir);
	sunRotAngle = (sunRotAngle + 10) % 36000;

	CreateSunShaders(); {
		modelMatrix = glm::rotate(glm::mat4(1.0f), glm::radians(sunRotAngle / 100.0f), glm::vec3(0.0f, 1.0f, 0.0f));
		mvpMatrix = projectionMatrix * viewMatrix * modelMatrix;
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "mvpMatrix"), 1, GL_FALSE, &mvpMatrix[0][0]);
		glBindVertexArray(VaoId); {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EboId); {
				glDrawElements(GL_TRIANGLES, sphereIndices.size() * sizeof(GLuint), GL_UNSIGNED_INT, 0);
			} glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		} glBindVertexArray(0);
	} DeleteShaders();

	CreateEarthShaders(); {
		earthPos = (glm::vec3)(glm::rotate(glm::mat4(1.0f), glm::radians(sunRotAngle / 100.0f), glm::vec3(0.0f, 1.0f, 0.0f)) * glm::vec4(0.0f, 0.0f, -5.0f, 1.0f));
		modelMatrix = glm::translate(glm::mat4(1.0f), earthPos)
			* glm::scale(glm::mat4(1.0f), glm::vec3(0.3f, 0.3f, 0.3f));
		mvpMatrix = projectionMatrix * viewMatrix * modelMatrix;
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "mvpMatrix"), 1, GL_FALSE, &mvpMatrix[0][0]);
		glBindVertexArray(VaoId); {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EboId); {
				glDrawElements(GL_TRIANGLES, sphereIndices.size() * sizeof(GLuint), GL_UNSIGNED_INT, 0);
			} glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		} glBindVertexArray(0);
	} DeleteShaders();

	CreateMoonShaders(); {
		glm::vec3 earthSunDist = sunPos - earthPos;
		moonPos = (glm::vec3)(glm::translate(glm::mat4(1.0f), -earthSunDist)
			* glm::rotate(glm::mat4(1.0f), glm::radians(4 * (sunRotAngle / 100.0f)), glm::vec3(0.0f, 1.0f, 0.0f))
			* glm::translate(glm::mat4(1.0f), earthSunDist)
			* glm::translate(glm::mat4(1.0f), 2.0f * glm::normalize(-earthSunDist))
			* glm::vec4(earthPos, 1.0f));
		modelMatrix = glm::translate(glm::mat4(1.0f), moonPos)
			* glm::scale(glm::mat4(1.0f), glm::vec3(0.2f, 0.2f, 0.2f));
		mvpMatrix = projectionMatrix * viewMatrix * modelMatrix;
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "mvpMatrix"), 1, GL_FALSE, &mvpMatrix[0][0]);
		glBindVertexArray(VaoId); {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EboId); {
				glDrawElements(GL_TRIANGLES, sphereIndices.size() * sizeof(GLuint), GL_UNSIGNED_INT, 0);
			} glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		} glBindVertexArray(0);
	} DeleteShaders();

	CreateVenusShaders(); {
		modelMatrix = glm::rotate(glm::mat4(1.0f), -glm::radians(sunRotAngle / 100.0f), glm::vec3(0.0f, 1.0f, 0.0f))
			* glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, 0.0f, -9.0f))
			* glm::scale(glm::mat4(1.0f), glm::vec3(0.7f, 0.7f, 0.7f));
		mvpMatrix = projectionMatrix * viewMatrix * modelMatrix;
		glUniformMatrix4fv(glGetUniformLocation(ProgramId, "mvpMatrix"), 1, GL_FALSE, &mvpMatrix[0][0]);
		glBindVertexArray(VaoId); {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EboId); {
				glDrawElements(GL_TRIANGLES, sphereIndices.size() * sizeof(GLuint), GL_UNSIGNED_INT, 0);
			} glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
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
		eyePos = eyePos + SPEED*BOOST*forwardDir;
		lookPos = lookPos + SPEED*BOOST*forwardDir;
		break;
	case 's':
		eyePos = eyePos - SPEED*BOOST*forwardDir;
		lookPos = lookPos - SPEED*BOOST*forwardDir;
		break;
	case 'a':
		eyePos = eyePos + SPEED*BOOST*leftDir;
		lookPos = lookPos + SPEED*BOOST*leftDir;
		break;
	case 'd':
		eyePos = eyePos - SPEED*BOOST*leftDir;
		lookPos = lookPos - SPEED*BOOST*leftDir;
		break;
	}
	glutPostRedisplay();
}

void PassiveMouseFunction(int x, int y) {
	int centerX = glutGet(GLUT_WINDOW_WIDTH) / 2;
	int centerY = glutGet(GLUT_WINDOW_HEIGHT) / 2;
	const float SENSIVITY = 0.003;

	if (x != centerX || y != centerY) {
		glutWarpPointer(centerX, centerY);

		float deltaX = x - centerX;
		float deltaY = y - centerY;
		glm::vec3 frontDir = glm::normalize(lookPos - eyePos);
		glm::vec3 leftDir = glm::normalize(glm::cross(upDir, frontDir));

		glm::mat4 rotLeftMat = glm::rotate(glm::mat4(1.0f), -SENSIVITY*deltaX, upDir);
		glm::mat4 rotUpMat = glm::rotate(glm::mat4(1.0f), SENSIVITY*deltaY, leftDir);
		frontDir = (glm::vec3) (rotLeftMat * glm::vec4(frontDir, 1.0f));
		frontDir = (glm::vec3) (rotUpMat * glm::vec4(frontDir, 1.0f));
		lookPos = eyePos + frontDir;
		leftDir = glm::normalize(glm::cross(upDir, frontDir));
		//regenerarea axei superioare denaturate
		upDir = glm::normalize(glm::cross(frontDir, leftDir));

		/*float deltaX = x - centerX;
		float deltaY = y - centerY;

		glm::vec3 eyeOriginDist = originPos - eyePos;
		lookPos = lookPos + eyeOriginDist;
		//mișcarea stâna-dreapta:
		lookPos = (glm::vec3)(glm::rotate(glm::mat4(1.0f), -SENSIVITY * deltaX, upDir) * glm::vec4(lookPos, 1.0f));
		//mișcarea sus-jos:
		glm::vec3 forwardDir = glm::normalize(lookPos - eyePos);
		glm::vec3 leftDir = glm::normalize(glm::cross(upDir, forwardDir));
		lookPos = (glm::vec3)(glm::rotate(glm::mat4(1.0f), SENSIVITY * deltaY, leftDir) * glm::vec4(lookPos, 1.0f));
		lookPos = lookPos - eyeOriginDist;
		upDir = glm::normalize(glm::cross(forwardDir, leftDir));*/
	}
	glutPostRedisplay();
}

int main(int argc, char* argv[])
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowPosition(0, 0);
	glutInitWindowSize(1024, 600); //dimensiunile ferestrei
	glutCreateWindow("Mini-Sistem Solar"); // titlul ferestrei

	glewInit(); // nu uitati de initializare glew; trebuie initializat inainte de a a initializa desenarea

	Initialize();
	glutDisplayFunc(RenderFunction);
	glutPassiveMotionFunc(PassiveMouseFunction);
	glutKeyboardFunc(KeyboardFunction);

	glutMainLoop();

	_getch();
}

