function [ Indices ] = betweenWhich2( who_, where_ )
%BETWEENWHICH2 Summary of this function goes here
%   Detailed explanation goes here
Extended_where = repmat(where_', length(who_), 1);
Underspan = (who_ < Extended_where);
if (who_(end) == where_(end))
    Underspan(end, end) = 1;
end
Heatmap = gradient(Underspan);
[Indices, ~, ~] = find(Heatmap');
Indices = reshape(Indices, 2, [])';
end

