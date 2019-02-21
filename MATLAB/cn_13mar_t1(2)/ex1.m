%% b
f = @(x) 8*x^3 + 4*x - 1;
df = @(x) 24*x^2 + 4;

disp('MetNR');
MetNR(f, df, 0, 'maxpasi', 2)
disp('MetSec:');
MetSec(f, 0, 1, 'maxpasi', 1)
disp('MetPozF:');
MetPozF(f, 0, 1, 'maxpasi', 2)
