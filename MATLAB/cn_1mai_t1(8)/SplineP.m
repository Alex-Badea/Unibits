function [ y_ ] = SplineP( x_train_, y_train_, fpa, x_ )
%SPLINEP Summary of this function goes here
%   Detailed explanation goes here
n = length(x_train_) - 1;

h_ = x_train_(2:n+1) - x_train_(1:n);
a_ = y_train_(1:n);
b_ = zeros(n, 1);
b_(1) = fpa;
for i = 2:n
    b_(i) = (2/h_(i-1))*(y_train_(i) - y_train_(i-1)) - b_(i-1);
end
c_ = (1./(h_.^2)).*(y_train_(2:n+1) - y_train_(1:n) - h_.*b_);

Indices = betweenWhich2(x_, x_train_);
left_index_ = Indices(:, 1);

AUX1 = a_(left_index_);
AUX2 = b_(left_index_);
AUX3 = x_ - x_train_(left_index_);
AUX4 = c_(left_index_);
AUX5 = (x_ - x_train_(left_index_)).^2;

y_ =  AUX1 + AUX2.*AUX3 + AUX4.*AUX5;
end

