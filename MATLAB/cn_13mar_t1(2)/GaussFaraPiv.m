function [x] = GaussFaraPiv(A, b)
A=[A,b];% Matricea extinsa
n=size(A,1);
xindice=1:n;%Numerotare initiala necunoscute
for k=1:n-1
    flag = 0;
    for p = k:n
        if A(p, k) ~= 0
            flag = 1;
            break;
        end
    end
    if flag == 0
        disp('Sistem incomp. sau sist. comp. nedet.');
        return;
    end
    
    if p ~= k
        A([p,k],:) = A([k,p],:);
    end
    for l=k+1:n
        A(l,:) = A(l,:)-A(l,k)/A(k,k)*A(k,:);
    end
end
if A(n,n) == 0
    disp('Sist. incomp. sau comp. nedet.')
end
xschimbat=SubsDesc(A(1:n,1:n),A(:,n+1)); 
for i = 1:n
    x(xindice(i)) = xschimbat(i); %Revenire la indicii initiali
end
end
