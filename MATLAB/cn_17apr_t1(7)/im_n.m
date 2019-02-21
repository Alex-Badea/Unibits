function [ y_ ] = im_n( x_train_, y_train_, x_ )
%IM_N Returneaza imaginea polinomului pe intervalul discret (vector) x_
%calculat prin metoda newton folosind diviziunea x_train_ si valoarea
%functiei de interpolat in punctele diviziunii, y_train_
p_n = MetN(x_train_, y_train_);
y_ = p_n(x_);
end

