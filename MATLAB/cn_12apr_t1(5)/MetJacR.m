function [ x_aprox_, n ] = MetJacR( A, a_, eps, sig )
B = eye(size(A)) - sig*A;
b_ = sig*a_;

x_prec_ = zeros(size(A, 1), 1);
k = 0;

flag = 1;
while flag == 1
    k = k + 1;
    x_crt_ = B*x_prec_ + b_;
    
    err_ = x_crt_ - x_prec_;
    if dot(A*err_, err_)/dot(A*x_prec_, x_prec_) <= eps
        flag = 0;
    end
    x_prec_ = x_crt_;
end
x_aprox_ = x_crt_;
n = k;
end

