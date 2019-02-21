function [Xaprox] = MetNR(f,df,x0,esp)
k = 1;
x = zeros();
x(1) = x0;
while true
    k = k + 1;
    x(k) = x(k-1) - f(x(k-1))/df(x(k-1));
    if abs(x(k) - x(k-1)) / abs(x(k-1)) >= esp
        break
    end
end
Xaprox = x(k);


end

