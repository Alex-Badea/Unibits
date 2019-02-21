function [ y_ ] = im_dir( x_train_, y_train_, x_ )
%IM_DIR Returneaza imaginea polinomului pe intervalul discret (vector) x_
%calculat prin metoda directa folosind diviziunea x_train_ si valoarea
%functiei de interpolat in punctele diviziunii, y_train_
p_dir = MetDir(x_train_, y_train_);
y_ = p_dir(x_);
end

