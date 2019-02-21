function [ y_ ] = SplineC( x_train_, y_train_, fpa, fpb, x_ )
%SPLINEC Summary of this function goes here
%   Detailed explanation goes here
n = length(x_train_) - 1;

h_ = x_train_(2:n+1) - x_train_(1:n);
assert(all(abs(h_ - h_(1)) < 1e-12));
h = h_(1);
a_ = y_train_(1:n);
B = generateCubicSplineBMatrix(n+1);
by_ = (3/h)*(y_train_(3:n+1) - y_train_(1:n-1));
by_ = [fpa; by_; fpb];
b_ = B\by_;
c_ = (3/h^2).*(y_train_(2:n+1) - y_train_(1:n)) - (b_(2:n+1) + 2*b_(1:n))/h;
d_ = (-2/h^3)*(y_train_(2:n+1) - y_train_(1:n)) + (1/h^2)*(b_(2:n+1) + b_(1:n));

Indices = betweenWhich2(x_, x_train_);
left_index_ = Indices(:, 1);

AUX1 = a_(left_index_);
AUX2 = b_(left_index_);
AUX3 = x_ - x_train_(left_index_);
AUX4 = c_(left_index_);
AUX5 = (x_ - x_train_(left_index_)).^2;
AUX6 = d_(left_index_);
AUX7 = (x_ - x_train_(left_index_)).^3;

y_ =  AUX1 + AUX2.*AUX3 + AUX4.*AUX5 + AUX6.*AUX7;
end

