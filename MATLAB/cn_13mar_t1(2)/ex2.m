f = @(x) x.^3 - 18*x - 10;

%% a
x_ = -5:0.1:5;
plot(x_, f(x_));

%% d
% Intervalul [a, b] trebuie sã respecte urmãtoarele ipoteze:
% f(a)f(b) < 0 i.e. f admite solu?ie unicã pe [a, b]
% f'(x) != 0 or. x în [a, b] i.e. f este monotonã pe [a, b]
% I1 = [-5, -3]
% I2 = [-2, 1]
% I3 = [3, 5]

eps = 10^-3;
x1 = MetSec(f, -5, -3, 'eps', eps);
x2 = MetSec(f, -2, 1, 'eps', eps);
x3 = MetSec(f, 3, 5, 'eps', eps);

figure, hold on
title("Met. Sec.")
plot(x_, f(x_));
plot(x1, f(x1), 'r+');
plot(x2, f(x2), 'r+');
plot(x3, f(x3), 'r+');

%% e
% I1 = [-5, -1]
% I2 = [-3, 3]
% I3 = [1, 5]

eps = 10^-3;
x1 = MetPozF(f, -5, -1, 'eps', eps);
x2 = MetPozF(f, -3, 3, 'eps', eps);
x3 = MetPozF(f, 1, 5, 'eps', eps);

figure, hold on
title("Met. Poz. False")
plot(x_, f(x_));
plot(x1, f(x1), 'r+');
plot(x2, f(x2), 'r+');
plot(x3, f(x3), 'r+');

plot(x1, f(x1), 'r+');
plot(x2, f(x2), 'r+');
plot(x3, f(x3), 'r+');