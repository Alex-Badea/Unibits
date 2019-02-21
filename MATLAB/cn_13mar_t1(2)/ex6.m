A1 = [0 1 1;
    2 1 5;
    4 2 1];
b1_ = [3; 5; 1];
GaussFaraPiv(A1, b1_)
GaussPivPart(A1, b1_)
GaussPivTot(A1, b1_)

A2 = [0 1 -2;
    1 -1 1;
    1 0 -1];
b2_ = [4; 6; 2];

b1_ = [3; 5; 1];
GaussFaraPiv(A2, b2_)
GaussPivPart(A2, b2_)
GaussPivTot(A2, b2_)