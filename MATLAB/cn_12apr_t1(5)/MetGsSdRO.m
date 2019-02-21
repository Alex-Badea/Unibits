function [ x_aprox_opt_, n_opt, sig_opt ] = MetGsSdRO( A, a_, eps, trials_no )
steps_ = zeros(trials_no - 1, 1);
for t = 1:(trials_no - 1)
    % NU FUNCTIONEAZA PENTRU sig in (0, 2) !!!
    % FUNCTIONEAZA APARENT PENTRU sig in (0, 2/norm(A, inf))
    sig_crt = 2*t/(norm(A, inf)*trials_no);
    [~, n] = MetGsSdR(A, a_, eps, sig_crt);
    steps_(t) = n;
end

t_opt = find(steps_ == min(steps_), 1, 'first');
sig_opt = 2*t_opt/(norm(A, inf)*trials_no);
[x_aprox_opt_, n_opt] = MetGsSdR(A, a_, eps, sig_opt);
end

