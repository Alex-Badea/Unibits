function [ dy_ ] = DerivNum( x_, y_, varargin)
%DERIVNUM Calculeaz� valorile derivatei numerice f' �n punctele x_, unde
%y_ = f(x_), folosind metoda primit� ca parametru
%   Metoda este furnizat� prin parametrul op?ional 'met' ?i poate fi
%   'difipro' (Diferen?e finite progresive), 'difireg' (Diferen?e finite
%   regresive), sau 'dificen' (Diferen?e finite centrale). C�nd nu se
%   furnizeaz� metoda, se folose?te metoda Diferen?e finite progresive
%   implicit.
inp = inputParser;
addOptional(inp, 'met', 'difipro');
parse(inp, varargin{:});
met = inp.Results.met;

switch met
    case 'difipro'
        dy_ = (y_(2:end) - y_(1:end-1))./(x_(2:end) - x_(1:end-1));
    case 'difireg'
        dy_ = (y_(1:end-1) - y_(2:end))./(x_(1:end-1) - x_(2:end));
    case 'dificen'
        dy_ = (y_(3:end) - y_(1:end-2))./(x_(3:end) - x_(1:end-2));
end
end

