function factors = ComputeTripletFactors (images, tripletList, K)
% This function computes the triplet factor values for one word.
%
% Input:
%   images: An array of structs containing the 'img' value for each
%     character in the word.
%   tripletList: An array of the character triplets we will consider (other
%     factor values should be 1). tripletList(i).chars gives character
%     assignment, and triplistList(i).factorVal gives the value for that
%     entry in the factor table.
%   K: The alphabet size (accessible in imageModel.K for the provided
%     imageModel).
%
% Hint: Every character triple in the word will use the same 'val' table.
%   Consider computing that array once and then resusing for each factor.
%
% Copyright (C) Daphne Koller, Stanford University, 2012


n = length(images);

% If the word has fewer than three characters, then return an empty list.
if (n < 3)
    factors = [];
    return
end

factors = repmat(struct('var', [], 'card', [], 'val', []), n - 2, 1);

% Your code here:
for i = 1:n-2
   c = 1;
   factors(i).card = [K,K,K];
   factors(i).var = [i,i+1,i+2];
   for j = 1:K
     for l = 1:K
       for m =1:K
          factors(i).val(c++,:) = 1;
       end;
     end;
   end;
   
   for o = 1:length(tripletList)
     j = tripletList(o).chars(1);
     l = tripletList(o).chars(2);
     m = tripletList(o).chars(3);
     
     t = K*K*(m-1) + K*(l-1) + j;
     factors(i).val(t) = tripletList(o).factorVal;
   end;
end;

end
