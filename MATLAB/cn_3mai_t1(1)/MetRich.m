function [ df ] = MetRich( f, x, h, n, varargin )
inp = inputParser;
addOptional(inp, 'ordder', 1);
parse(inp, varargin{:});
ordder = inp.Results.ordder;
if ordder == 1
    phi = @(x, h) (f(x+h) - f(x))/h;
elseif ordder == 2
    phi = @(x, h) (f(x+h) - 2*f(x) + f(x-h))/(h^2);
else
    error(['MetRich neimplementat pentru ordinul ' num2str(ordder)]);
end

Q = zeros(n);
for i = 1:n
    Q(i, 1) = phi(x, h/2^(i-1));
end

for i = 2:n
    for j = 2:i
        Q(i, j) = Q(i, j-1) + (1/(2^(j-1) - 1))*(Q(i, j-1) - Q(i-1, j-1));
    end
end

df = Q(n, n);
end

