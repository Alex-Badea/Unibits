function [ x_aprox_, n ] = MetJacDDL( A, a_, eps )
for i = 1:size(A, 1)
    if abs(A(i, i)) <= sum(abs(A(i, [1:(i-1) (i+1):end])))
        disp("Mat. nu este diag. dom. pe linii");
        return
    end
end

x_prec_ = zeros(size(A, 1), 1);
k = 0;

B = eye(size(A)) - A./diag(A);
b_ = a_./diag(A);
q = norm(B, inf);

flag = 1;
while flag == 1
    k = k + 1;
    x_crt_ = B*x_prec_ + b_;
    if norm(a_, inf)*q^k / (1 - q) <= eps
        flag = 0;
    end
    x_prec_ = x_crt_;
end
x_aprox_ = x_crt_;
n = k;
end

