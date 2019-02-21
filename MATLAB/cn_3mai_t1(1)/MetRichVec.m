function [ df_ ] = MetRichVec( f, x_, h, n, varargin )
%METRICHVEC Metoda Richardson cu parametru de intrare vectorizat
%   Ordinul derivatei de calculat este suplinit prin parametrul op?ional
%   'ordder' ?i poate fi 1 (implicit) sau 2.
df_ = zeros(size(x_));
for i = 1:length(x_)
    df_(i) = MetRich(f, x_(i), h, n, varargin{:});
end
end

