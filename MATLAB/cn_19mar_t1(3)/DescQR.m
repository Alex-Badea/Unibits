function [ Q, R, x_ ] = DescQR( A, b_ )
%DESCQR Summary of this function goes here
%   Detailed explanation goes here
n = size(b_, 1);
Q = zeros(n);
R = zeros(n);
x_ = zeros(n, 1);
R(1, 1) = sqrt(sum(A(:, 1).^2));
Q(:, 1) = A(:, 1)/R(1, 1);
R(1, 2:n) = Q(:, 1)' * A(:, 2:n);
for k = 2:n
    R(k, k) = sqrt(sum(A(:, k).^2) - sum(R(1:k-1, k).^2));
    Q(:, k) = (1/R(k, k))*(A(:, k) - Q(:, 1:k-1)*R(1:k-1, k));
    R(k, k+1:n) = sum(Q(:, k)' * A(:, k+1:n));
end
x_ = SubsDesc(R, Q' * b_);
end