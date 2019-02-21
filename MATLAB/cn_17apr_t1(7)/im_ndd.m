function [ y_ ] = im_ndd( x_train_, y_train_, x_ )
%IM_NDD Returneaza imaginea polinomului pe intervalul discret (vector) x_
%calculat prin metoda NDD folosind diviziunea x_train_ si valoarea
%functiei de interpolat in punctele diviziunii, y_train_
y_ = zeros(size(x_));
for i = 1:length(x_)
    y_(i) = MetNDD(x_train_, y_train_, x_(i));
end
end

