function [ p ] = MetLg( x_, y_ )
deg = size(x_, 1) - 1;
p = @(x) 0;
for k = 1:deg+1
    lg_basis_func = l(deg, k);
    p = @(x) p(x) + lg_basis_func(x).*y_(k);
end
    function f = l(n, k)
        f = @(x) 1;
        for j = 1:(n + 1)
            if j == k, continue, end
            f = @(x) f(x).*(x - x_(j))./(x_(k) - x_(j));
        end
    end
end