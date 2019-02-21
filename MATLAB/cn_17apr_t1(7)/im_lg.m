function [ y_ ] = im_lg( x_train_, y_train_, x_ )
%IM_LG Returneaza imaginea polinomului pe intervalul discret (vector) x_
%calculat prin metoda lagrange folosind diviziunea x_train_ si valoarea
%functiei de interpolat in punctele diviziunii, y_train_
p_lg = MetLg(x_train_, y_train_);
y_ = p_lg(x_);
end

