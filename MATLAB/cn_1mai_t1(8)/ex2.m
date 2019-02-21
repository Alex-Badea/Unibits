f = @sin;
fp = @cos;
a = -pi/2;
b = pi/2;
fpa = fp(a);
fpb = fp(b);

%%
x_plot_ = linspace(a, b)';
y_plot_original_ = f(x_plot_);

%%
figure('pos', [100 200 1300 600]), hold on
% n = 2:
subplot(1, 3, 1);
n = 2;
x_train_ = linspace(a, b, n + 1)';
y_train_ = f(x_train_);
plot(x_plot_, y_plot_original_, x_train_, y_train_, '*', x_plot_, SplineL(x_train_, y_train_, x_plot_));
legend('sin(x)', 'Punctele de interpolare', 'Fun. Spline Lin.');
title(['n = ' num2str(n)]);
% n = 4:
subplot(1, 3, 2);
n = 4;
x_train_ = linspace(a, b, n + 1)';
y_train_ = f(x_train_);
plot(x_plot_, y_plot_original_, x_train_, y_train_, '*', x_plot_, SplineL(x_train_, y_train_, x_plot_));
legend('sin(x)', 'Punctele de interpolare', 'Fun. Spline Lin.');
title(['n = ' num2str(n)]);
% n = 10:
subplot(1, 3, 3);
n = 10;
x_train_ = linspace(a, b, n + 1)';
y_train_ = f(x_train_);
plot(x_plot_, y_plot_original_, x_train_, y_train_, '*', x_plot_, SplineL(x_train_, y_train_, x_plot_));
legend('sin(x)', 'Punctele de interpolare', 'Fun. Spline Lin.');
title(['n = ' num2str(n)]);

