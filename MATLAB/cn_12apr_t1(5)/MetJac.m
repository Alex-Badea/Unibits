function [ x_aprox_, n ] = MetJac( A, a_, eps )
x_aprox_ = 0;
n = 0;

q = norm(eye(size(A)) - A);
if q >= 1
    disp("Met. Jacobi nu asigura conv.");
    return
end

B = eye(size(A)) - A;
k = 0;
x_prec_ = zeros(size(A, 1), 1);

flag = 1;
while flag == 1
    k = k + 1;
    x_crt_ = B*x_prec_ + a_;
    if norm(a_)*q^k / (1 - q) <= eps
        flag = 0;
    end
    x_prec_ = x_crt_;
end
x_aprox_ = x_crt_;
n = k;
end

