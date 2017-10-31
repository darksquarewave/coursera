%COMPUTEEXACTMARGINALSBP Runs exact inference and returns the marginals
%over all the variables (if isMax == 0) or the max-marginals (if isMax == 1). 
%
%   M = COMPUTEEXACTMARGINALSBP(F, E, isMax) takes a list of factors F,
%   evidence E, and a flag isMax, runs exact inference and returns the
%   final marginals for the variables in the network. If isMax is 1, then
%   it runs exact MAP inference, otherwise exact inference (sum-prod).
%   It returns an array of size equal to the number of variables in the 
%   network where M(i) represents the ith variable and M(i).val represents 
%   the marginals of the ith variable. 
%
% Copyright (C) Daphne Koller, Stanford University, 2012


function M = ComputeExactMarginalsBP(F, E, isMax)

% initialization
% you should set it to the correct value in your code
M = [];

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% YOUR CODE HERE
%
% Implement Exact and MAP Inference.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

P = CreateCliqueTree(F, E);
P = CliqueTreeCalibrate(P, isMax);
N = max(arrayfun(@(struct)max(struct.var(:)),F));

for i = 1:N
  for j = 1:N
    clique = P.cliqueList(j);
    if any(clique.var == i)
      vars = [];
      for k = 1:length(clique.var)
        if (clique.var(k) != i)
          vars = [vars clique.var(k)];
        end;
      end;
      if isMax == 0
        marginalized = FactorMarginalization(clique, vars);
        marginalized.val = marginalized.val / sum(marginalized.val);
      else
        marginalized = FactorMaxMarginalization(clique, vars);
      end;
      M = [M marginalized];
      break;
    end;
  end;
end;

end
