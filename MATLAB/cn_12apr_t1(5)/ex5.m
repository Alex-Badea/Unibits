a_ = [1 2 3]';
eps = 1e-4;

%% 2)
A1 = [0.2 0.01 0;
    0 1 0.04;
    0 0.02 1];

A2 = [4 1 2;
    0 3 1;
    2 4 8];

x1_ = MetJac(A1, a_, eps)
verif1_ = A1*x1_

x2_ = MetJacDDL(A2, a_, eps)
verif2_ = A2*x2_

%% 3)
A3 = [4 2 2;
    2 10 4;
    2 4 6];

sigs10_ = linspace(0, 2/norm(A3, inf), 10);
sigs20_ = linspace(0, 2/norm(A3, inf), 20);
sigs50_ = linspace(0, 2/norm(A3, inf), 50);

spec_rad = max(abs(eig(A3)));
sigs10_sr_ = linspace(0, 2/spec_rad, 10);
sigs20_sr_ = linspace(0, 2/spec_rad, 20);
sigs50_sr_ = linspace(0, 2/spec_rad, 50);

v_ = zeros(10, 1);
v_sr_ = zeros(10, 1);
for i = 2:9
    [~, n] = MetJacR(A3, a_, eps, sigs10_(i));
    v_(i) = n;
    [~, n] = MetJacR(A3, a_, eps, sigs10_sr_(i));
    v_sr_(i) = n;
end
figure, hold on
title("Jacobi - Discretizare 10");
plot(sigs10_, v_, 'b-');
plot(sigs10_sr_, v_sr_, 'r-');
legend("Interval (0, 2/norm(A3, inf))", "Interval (0, raza spectrala)");

v_ = zeros(20, 1);
v_sr_ = zeros(20, 1);
for i = 2:19
    [~, n] = MetJacR(A3, a_, eps, sigs20_(i));
    v_(i) = n;
    [~, n] = MetJacR(A3, a_, eps, sigs20_sr_(i));
    v_sr_(i) = n;
end
figure, hold on
title("Jacobi - Discretizare 20");
plot(sigs20_, v_, 'b-');
plot(sigs20_sr_, v_sr_, 'r-');
legend("Interval (0, 2/norm(A3, inf))", "Interval (0, raza spectrala)");

v_ = zeros(50, 1);
v_sr_ = zeros(50, 1);
for i = 2:49
    [~, n] = MetJacR(A3, a_, eps, sigs50_(i));
    v_(i) = n;
    [~, n] = MetJacR(A3, a_, eps, sigs50_sr_(i));
    v_sr_(i) = n;
end
figure, hold on
title("Jacobi - Discretizare 50");
plot(sigs50_, v_, 'b-');
plot(sigs50_sr_, v_sr_, 'r-');
legend("Interval (0, 2/norm(A3, inf))", "Interval (0, raza spectrala)");

% Se observa ca nu este necesar sa se caute sigma optima in intervalul
% (0, raza spectrala) fiindca aceasta se afla in intervalul
% (0, 2/norm(A3, inf))

%% 4)
x3_jc_ = MetJacRO(A3, a_, eps, 50)
verif3_jc_ = A3*x3_

%% 5)
v_ = zeros(10, 1);
v_sr_ = zeros(10, 1);
for i = 2:9
    [~, n] = MetGsSdR(A3, a_, eps, sigs10_(i));
    v_(i) = n;
    [~, n] = MetGsSdR(A3, a_, eps, sigs10_sr_(i));
    v_sr_(i) = n;
end
figure, hold on
title("Gauss-Seidel - Discretizare 10");
plot(sigs10_, v_, 'b-');
plot(sigs10_sr_, v_sr_, 'r-');
legend("Interval (0, 2/norm(A3, inf))", "Interval (0, raza spectrala)");

v_ = zeros(20, 1);
v_sr_ = zeros(20, 1);
for i = 2:19
    [~, n] = MetGsSdR(A3, a_, eps, sigs20_(i));
    v_(i) = n;
    [~, n] = MetGsSdR(A3, a_, eps, sigs20_sr_(i));
    v_sr_(i) = n;
end
figure, hold on
title("Gauss-Seidel - Discretizare 20");
plot(sigs20_, v_, 'b-');
plot(sigs20_sr_, v_sr_, 'r-');
legend("Interval (0, 2/norm(A3, inf))", "Interval (0, raza spectrala)");

v_ = zeros(50, 1);
v_sr_ = zeros(50, 1);
for i = 2:49
    [~, n] = MetGsSdR(A3, a_, eps, sigs50_(i));
    v_(i) = n;
    [~, n] = MetGsSdR(A3, a_, eps, sigs50_sr_(i));
    v_sr_(i) = n;
end
figure, hold on
title("Gauss-Seidel - Discretizare 50");
plot(sigs50_, v_, 'b-');
plot(sigs50_sr_, v_sr_, 'r-');
legend("Interval (0, 2/norm(A3, inf))", "Interval (0, raza spectrala)");

x3_gs_ = MetGsSdRO(A3, a_, eps, 50)
verif3_gs_ = A3*x3_