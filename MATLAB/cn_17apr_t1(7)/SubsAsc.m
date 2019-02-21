function [ x_ ] = SubsAsc( A, b_ )
%SUBSASC Summary of this function goes here
%   Detailed explanation goes here
n = size(b_, 1);
x_ = zeros(n, 1);
x_(1) = (1/A(1, 1))*b_(1);
for k = 2:n
    x_(k) = (1/A(k, k))*(b_(k) - sum(A(k, 1:k - 1)' .* x_(1:k - 1)));
end
end