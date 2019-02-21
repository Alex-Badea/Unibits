function [x_aprox] = MetSec(f, a, b, varargin)
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
x0 = input(['Introduceti x0 intre ', num2str(a), ' si ', num2str(b), ': ']);
x1 = input(['Introduceti x1 intre ', num2str(a), ' si ', num2str(b), ': ']);
x_anteprec = x0;
x_prec = x1;
x_crt = Inf;

while abs(x_prec - x_anteprec)/abs(x_anteprec) >= eps && k ~= maxpasi
    k = k + 1;
    x_crt = (x_anteprec*f(x_prec) - x_prec*f(x_anteprec))/(f(x_prec) - f(x_anteprec));
    if x_crt < a || x_crt > b
        disp('Introduceti alte valori pentru x0, x1');
        break;
    end
    x_anteprec = x_prec;
    x_prec = x_crt;
end
disp(['pasi: ' num2str(k)]);
x_aprox = x_crt;
end

