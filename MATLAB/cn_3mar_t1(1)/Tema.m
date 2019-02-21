% Exercitiul 2
figure('Name','Exe2');
f = inline('x.^3-7*x.^2+14*x-6','x');
a = 0;
b = 1;
eps = 10^(-5);
x1 = MetBisectie(f,a,b,eps)


a = 1 ;
b = 3.2;
x2 = MetBisectie(f,a,b,eps)

a = 3.2 ;
b = 4;
x3 = MetBisectie(f,a,b,eps)


puncte = linspace(0,4,1000);
t = f(puncte);
plot(puncte,t,'Linewidth',3)
hold on;

plot(x1,f(x1),'*')
plot(x2,f(x2),'*')
plot(x3,f(x3),'*')


% Exercitiul 3
% a)
figure('Name','Exe3');
puncte1 = linspace(-4,4,1000);
y = inline('exp(1).^x - 2','x');
plot(puncte1,y(puncte1), 'r')
hold on;
grid on;
y = inline('cos(exp(1).^x - 2)','x') ;
plot(puncte1,y(puncte1), 'r')



% b)

f2 = inline('cos(exp(x)-2) - exp(x)+2','x');
a = 0.5;
b = 1.5;
x4 = MetBisectie(f,a,b,eps)

% Exercitul 4

f3 = inline('x - sqrt(3)','x');
a = 1;
b = 2
x5 = MetBisectie(f,a,b,eps)


% Exercitiul 6
% a

df = inline('3*x.^2-14*x+14','x');

eps2 = 10^(-3);
a61 = 0; b61 = 1;
a62 = 2 ; b62 = 2.7;
a63 = 2.7 ; b63 = 4;

s61 = 0.5;  s62 = 2.5; s63 = 2.8;
x61 = MetNR(f,df,s61,eps2)
x61 = MetNR(f,df,s62,eps2)
x61 = MetNR(f,df,s63,eps2)

figure;
hold on;






