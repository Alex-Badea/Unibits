function [ B ] = generateCubicSplineBMatrix( size )
%GENERATECUBICSPLINEBMATRIX Summary of this function goes here
%   Detailed explanation goes here
Left = [eye(size-2) zeros(size-2, 2)];
Center = [zeros(size-2, 1) 4*eye(size-2) zeros(size-2, 1)];
Right = [zeros(size-2, 2) eye(size-2)];
A = Left + Center + Right;
A(end+1, end) = 1;
A = flip(A);
A(end+1, 1) = 1;
A = flip(A);
B = A;
end

