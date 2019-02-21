function [x_aprox] = MetPozF(f, a, b, varargin)
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
a0 = a;
b0 = b;
x0 = (a0*f(b0) - b0*f(a0))/(f(b0) - f(a0));
a_prec = a0;
b_prec = b0;
x_prec = x0;

flag = 1;
while flag == 1
    k = k + 1;
    if f(x_prec) == 0
        x_crt = x_prec;
        break;
    elseif f(a_prec)*f(x_prec) < 0
        a_crt = a_prec;
        b_crt = x_prec;
        x_crt = (a_crt*f(b_crt) - b_crt*f(a_crt))/(f(b_crt) - f(a_crt));
    elseif f(a_prec)*f(x_prec) > 0
        a_crt = x_prec;
        b_crt = b_prec;
        x_crt = (a_crt*f(b_crt) - b_crt*f(a_crt))/(f(b_crt) - f(a_crt));
    end
    if abs(x_crt - x_prec)/abs(x_prec) < eps || k == maxpasi
        flag = 0;
    end
    x_prec = x_crt;
    a_prec = a_crt;
    b_prec = b_crt;
end
disp(['pasi: ' num2str(k)]);
x_aprox = x_crt;
end

