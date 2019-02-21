function [ p ] = MetN( x_, y_ )
deg = size(x_, 1) - 1;
A = zeros(deg + 1);
A(:, 1) = 1;
for i = 2:(deg + 1)
    for j = 2:i
        A(i, j) = prod(x_(i) - x_(1:j-1));
    end
end
c_ = SubsAsc(A, y_);

p = @(x) c_(1);
for i = 2:(deg + 1)
    p = @(x) p(x) + c_(i) ... 
        * prod(bsxfun(@minus, x, x_(1:i-1)'), 2);
end
end

