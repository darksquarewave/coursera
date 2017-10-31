% Copyright (C) Daphne Koller, Stanford University, 2012

function [MEU OptimalDecisionRule] = OptimizeMEU( I )

  % Inputs: An influence diagram I with a single decision node and a single utility node.
  %         I.RandomFactors = list of factors for each random variable.  These are CPDs, with
  %              the child variable = D.var(1)
  %         I.DecisionFactors = factor for the decision node.
  %         I.UtilityFactors = list of factors representing conditional utilities.
  % Return value: the maximum expected utility of I and an optimal decision rule 
  % (represented again as a factor) that yields that expected utility.
  
  % We assume I has a single decision node.
  % You may assume that there is a unique optimal decision.
  D = I.DecisionFactors(1);
  
  MEU = 0;
  OptimalDecisionRule = I.DecisionFactors(1);
  D = I.DecisionFactors(1);

  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  %
  % YOUR CODE HERE...
  % 
  % Some other information that might be useful for some implementations
  % (note that there are multiple ways to implement this):
  % 1.  It is probably easiest to think of two cases - D has parents and D 
  %     has no parents.
  % 2.  You may find the Matlab/Octave function setdiff useful.
  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
   
  EUF = CalculateExpectedUtilityFactor(I);
  
  decisionVar = I.DecisionFactors(1).var;
  decisionCard = I.DecisionFactors(1).card(1);
  
  vector = reshape(EUF.val, EUF.card, []);
  if (decisionVar == EUF.var)
    vector = vector';
  end;
  
  MEU = 0;
  maxList = [];
  
  for i = 1:rows(vector)
    max = vector(i,1);
    for j = 1:length(vector(i,:))
      if max < vector(i,j)
        max = vector(i,j);
      end;
    end;
    maxList = [maxList max];
  end;
  
  val = repmat(EUF.val, 1);
  j = 1;
  for i = 1:length(val)
    if (j <= length(maxList) && maxList(j) == val(i))
      val(i) = 1;
      j = j + 1;
    else
      val(i) = 0;
    end;
  end;
  
  MEU = sum(maxList);
  OptimalDecisionRule = struct('var', EUF.var, 'card', EUF.card, 'val', val);

end
