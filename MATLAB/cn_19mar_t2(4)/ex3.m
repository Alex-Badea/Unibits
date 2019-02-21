A = [10 7 8 7;
    7 5 6 5;
    8 6 10 9;
    7 5 9 10];
b_ = [32; 23; 33; 31];

%% a
x_ = inv(A)*b_

%% b
APtb = [10 7 8.1 7.2;
    7.08 5.04 6 5;
    8 5.98 9.89 9;
    6.99 4.99 9 9.98];
b_Ptb = [32.1; 22.9; 33.1; 30.9];

% Datoritã faptului cã numerele de condi?ionare sunt mult mai mari decât 1,
% o perturbare micã în sistem duce la o perturbare foarte mare în solu?ie.
x_Ptb = inv(APtb)*b_Ptb

%% c
cond(A, 1)
cond(A, 2)
cond(A, inf)