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
plot(x_plot_, y_plot_original_, x_train_, y_train_, '*', x_plot_, SplineC(x_train_, y_train_, fpa, fpb, x_plot_));
legend('sin(x)', 'Punctele de interpolare', 'Fun. Spline Cub.');
title(['n = ' num2str(n)]);
% n = 4:
subplot(1, 3, 2);
n = 4;
x_train_ = linspace(a, b, n + 1)';
y_train_ = f(x_train_);
plot(x_plot_, y_plot_original_, x_train_, y_train_, '*', x_plot_, SplineC(x_train_, y_train_, fpa, fpb, x_plot_));
legend('sin(x)', 'Punctele de interpolare', 'Fun. Spline Cub.');
title(['n = ' num2str(n)]);
% n = 10:
subplot(1, 3, 3);
n = 10;
x_train_ = linspace(a, b, n + 1)';
y_train_ = f(x_train_);
plot(x_plot_, y_plot_original_, x_train_, y_train_, '*', x_plot_, SplineC(x_train_, y_train_, fpa, fpb, x_plot_));
legend('sin(x)', 'Punctele de interpolare', 'Fun. Spline Cub.');
title(['n = ' num2str(n)]);

%%
n = 4;
x_train_ = linspace(a, b, n + 1)';
y_train_ = f(x_train_);

y_original_ = f(x_plot_);
y_spline_ = SplineC(x_train_, y_train_, fpa, fpb, x_plot_);
dy_original_ = diff(y_original_)./diff(x_plot_);
dy_spline_ = diff(y_spline_)./diff(x_plot_);
ddy_original_ = diff(dy_original_)./diff(x_plot_(1:end-1));
ddy_spline_ = diff(dy_spline_)./diff(x_plot_(1:end-1));

figure('pos', [0 40 1400 600]), hold on
subplot(1, 2, 1);
plot(x_plot_(1:end-1), dy_original_, x_plot_(1:end-1), dy_spline_);
legend('sin(x) derivat', 'S(x) derivat');
title(['n = ' num2str(n)]);
subplot(1, 2, 2);
plot(x_plot_(1:end-2), ddy_original_, x_plot_(1:end-2), ddy_spline_);
legend('sin(x) derivat de 2 ori', 'S(x) derivat de 2 ori');
title(['n = ' num2str(n)]);