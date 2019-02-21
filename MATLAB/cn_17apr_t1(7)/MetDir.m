function [ p ] = MetDir( x_, y_)
deg = size(x_, 1) - 1;
A = zeros(deg + 1);
for i = 0:deg
    A(:, i+1) = x_.^i;
end
a_ = A \ y_;

p = @(x) 0;
for i = 0:deg
    p = @(x) p(x) + a_(i+1).*x.^i;
end
end