#pragma once
#include "glm/glm.hpp"

struct Vertex
{
	const glm::vec3 pos;
	const glm::vec3 norm;
	const glm::vec2 tex;
	Vertex(const glm::vec3& pos, const glm::vec3& norm, const glm::vec2& tex) :
		pos(pos),
		norm(norm),
		tex(tex)
	{};
};

