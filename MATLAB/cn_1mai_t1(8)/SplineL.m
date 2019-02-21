function [ y_ ] = SplineL( x_train_, y_train_, x_ )
%SPLINEL Summary of this function goes here
%   Detailed explanation goes here
n = length(x_train_) - 1;

a_ = y_train_(1:n);
b_ = (y_train_(2:n+1) - y_train_(1:n)) ...
    ./ (x_train_(2:n+1) - x_train_(1:n));

Indices = betweenWhich2(x_, x_train_);
left_index_ = Indices(:, 1);

AUX1 = a_(left_index_);
AUX2 = b_(left_index_);
AUX3 = x_ - x_train_(left_index_);

y_ =  AUX1 + AUX2.*AUX3;
end

