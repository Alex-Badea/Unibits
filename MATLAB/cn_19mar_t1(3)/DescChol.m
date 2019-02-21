function [ L, x_ ] = DescChol( A, b_ )
%DESCCHOL Summary of this function goes here
%   Detailed explanation goes here
n = size(b_, 1);
L = zeros(n);
x_ = zeros(n, 1);
a = A(1, 1);
if a <= 0
    disp('A nu este pozitiv definita')
    return
end
L(1, 1) = sqrt(a);
L(2:n) = A(2:n, 1)/L(1, 1);
for k = 2:n
    a = A(k, k) - sum(L(k, 1:k-1).^2);
    if a <= 0
        disp('A nu este pozitiv definita');
        return
    end
    L(k, k) = sqrt(a);
    L(k+1:n, k) = (1/L(k, k))*(A(k+1:n, k) - L(k+1:n, 1:k-1)*L(k, 1:k-1)');
end
y_ = SubsAsc(L, b_);
x_ = SubsDesc(L', y_);
end

