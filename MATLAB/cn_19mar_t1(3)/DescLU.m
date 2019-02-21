function [ L, U, x_ ] = DescLU( A, b_ )
%DESCLU Summary of this function goes here
%   Detailed explanation goes here
n = size(b_, 1);
U = zeros(n);
L = zeros(n);
x_ = zeros(n, 1);
U(1, :) = A(1, :);
if U(1, 1) == 0
    disp('A nu admite fact. LU');
    return
end
L(:, 1) = A(:, 1)/U(1, 1);
for k = 2:n
    U(k, k:n) = A(k, k:n) - L(k, 1:k-1)*U(1:k-1, k:n);
    if U(k, k) == 0
        disp('A nu admite fact. LU')
        return
    end
    L(k:n, k) = (1/U(k, k))*(A(k:n, k) - L(k:n, 1:k-1)*U(1:k-1, k));
end
y_ = SubsAsc(L, b_);
x_ = SubsDesc(U, y_);
end
