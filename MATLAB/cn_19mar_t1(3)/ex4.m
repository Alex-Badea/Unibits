%% d
A1 = [1 2 4;
    3 8 14;
    2 6 13];

A2 = [4 2 2;
    2 10 4;
    2 4 6];

A3 = [0 4 5;
    -1 -2 -3;
    0 0 1];

b_ = [1; 2; 3];

[L, U, x_] = DescLU(A1, b_)
[L, x_] = DescChol(A1, b_)
[Q, R, x_] = DescQR(A1, b_)

[L, U, x_] = DescLU(A2, b_)
[L, x_] = DescChol(A2, b_)
[Q, R, x_] = DescQR(A2, b_)


[L, U, x_] = DescLU(A3, b_)
[L, x_] = DescChol(A3, b_)
[Q, R, x_] = DescQR(A3, b_)