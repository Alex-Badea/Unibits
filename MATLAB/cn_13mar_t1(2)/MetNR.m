function [x_aprox] = MetNR(f, df, x0, varargin)
p = inputParser;
addOptional(p, 'maxpasi', Inf);
addOptional(p, 'eps', 0);
parse(p, varargin{:});
maxpasi = p.Results.maxpasi;
eps = p.Results.eps;
if maxpasi == Inf && eps == 0
    error('Trebuie suplinit cel putin unul dintre argumentele optionale "eps" si "maxpasi"');
end

k = 0;
x_prec = x0;

flag = 1;
while flag == 1
    k = k + 1;
    x_crt = x_prec - f(x_prec)/df(x_prec);
    if abs(x_crt - x_prec)/abs(x_prec) < eps || k == maxpasi
        flag = 0;
    end
    x_prec = x_crt;
end
disp(['pasi: ' num2str(k)]);
x_aprox = x_crt;
end

