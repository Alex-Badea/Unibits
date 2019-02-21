function [ x_aprox_, n ] = MetGsSdR( A, a_, eps, sig )
x_prec_ = zeros(size(A, 1), 1);
k = 0;

flag = 1;
while flag == 1
    k = k + 1;
    x_crt_ = zeros(size(A, 1), 1);
    for i = 1:size(A, 1)
        x_crt_(i) = (1 - sig)*x_prec_(i) + sig/A(i, i) * (a_(i) - A(i, i+1:size(A, 1))*x_prec_(i+1:size(A, 1)) - A(i, 1:i-1)*x_prec_(1:i-1));
    end
    
    err_ = x_crt_ - x_prec_;
    if dot(A*err_, err_)/dot(A*x_prec_, x_prec_) <= eps
        flag = 0;
    end
    x_prec_ = x_crt_;
end
x_aprox_ = x_crt_;
n = k;
end

