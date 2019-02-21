f = @sin;
a = -pi/2;
b = pi/2;
discr = 100;
deg = 3;

%% 2 + 3
i_ = linspace(a, b, discr)';
train_x_ = linspace(a, b, deg+1)';
train_y_ = f(train_x_);

% Metoda directa:
figure('pos', [200 200 1000 400]), hold on
subplot(1,2,1);
plot(i_, f(i_), i_, im_dir(train_x_, train_y_, i_), '--');
grid on
legend('sin(x)', ['Met. dir. in pct. [' num2str(train_x_') ']']);

subplot(1,2,2);
plot(i_, f(i_) - im_dir(train_x_, train_y_, i_), 'k-', 'LineWidth', 2);
grid on
legend('Eroarea sin(x) vs. Polin. Lagr.');

% Metoda Lagrange:
figure('pos', [200 200 1000 400]), hold on
subplot(1,2,1);
plot(i_, f(i_), i_, im_lg(train_x_, train_y_, i_), '--');
grid on
legend('sin(x)', ['Met. Lagr. in pct. [' num2str(train_x_') ']']);

subplot(1,2,2);
plot(i_, f(i_) - im_lg(train_x_, train_y_, i_), 'k-', 'LineWidth', 2);
grid on
legend('Eroarea sin(x) vs. Polin. Lagr.');

% Metoda Newton:
figure('pos', [200 200 1000 400]), hold on
subplot(1,2,1);
plot(i_, f(i_), i_, im_n(train_x_, train_y_, i_), '--');
grid on
legend('sin(x)', ['Met. Newt. in pct. [' num2str(train_x_') ']']);

subplot(1,2,2);
plot(i_, f(i_) - im_n(train_x_, train_y_, i_), 'k-', 'LineWidth', 2);
grid on
legend('Eroarea sin(x) vs. Polin. Lagr.');

% Metoda NDD:
figure('pos', [200 200 1000 400]), hold on
subplot(1,2,1);
plot(i_, f(i_), i_, im_ndd(train_x_, train_y_, i_), '--');
grid on
legend('sin(x)', ['Met. NDD in pct. [' num2str(train_x_') ']']);

subplot(1,2,2);
plot(i_, f(i_) - im_ndd(train_x_, train_y_, i_), 'k-', 'LineWidth', 2);
grid on
legend('Eroarea sin(x) vs. Polin. Lagr.');

%% 4
deg = 4;
train_x_ = linspace(a, b, deg+1)';
train_y_ = f(train_x_);

figure, hold on
plot(i_, f(i_), i_, im_n(train_x_, train_y_, i_), '--');
grid on
legend('sin(x)', ['Met. Newt. in pct. [' num2str(train_x_') ']']);

deg = 5;
train_x_ = linspace(a, b, deg+1)';
train_y_ = f(train_x_);

figure, hold on
plot(i_, f(i_), i_, im_n(train_x_, train_y_, i_), '--');
grid on
legend('sin(x)', ['Met. Newt. in pct. [' num2str(train_x_') ']']);

deg = 6;
train_x_ = linspace(a, b, deg+1)';
train_y_ = f(train_x_);

figure, hold on
plot(i_, f(i_), i_, im_n(train_x_, train_y_, i_), '--');
grid on
legend('sin(x)', ['Met. Newt. in pct. [' num2str(train_x_') ']']);

deg = 64;
train_x_ = linspace(a, b, deg+1)';
train_y_ = f(train_x_);

figure, hold on
plot(i_, f(i_), i_, im_n(train_x_, train_y_, i_), '--');
title(['Polin. Lagr. degenereaza pentru gradul ' num2str(deg-1)])
grid on
legend('sin(x)', ['Met. Newt. in ' num2str(deg) ' de puncte']);