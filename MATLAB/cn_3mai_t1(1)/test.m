f = @sin;
x_ = (-10:0.1:10)';
y_ = f(x_);

figure, hold on
plot(x_, y_);
dy_ = MetRichVec(f, x_, 0.01, 5, 'ordder', 1);
plot(x_, dy_);

