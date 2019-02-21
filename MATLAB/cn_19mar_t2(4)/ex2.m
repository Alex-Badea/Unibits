A = [1 -3 3;
    3 -5 3;
    6 -6 4];

%% a
norm2_A = max(sqrt(eigs(A'*A)))

%% b
norm2_AInv = max(sqrt(1 ./ eigs(A'*A)));
cond2_A = norm2_A*norm2_AInv

%% c
norm2_A_2 = norm(A, 2)
cond2_A_2 = cond(A, 2)

