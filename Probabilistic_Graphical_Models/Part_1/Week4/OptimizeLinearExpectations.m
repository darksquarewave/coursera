% Copyright (C) Daphne Koller, Stanford University, 2012

function [MEU OptimalDecisionRule] = OptimizeLinearExpectations( I )
  % Inputs: An influence diagram I with a single decision node and one or more utility nodes.
  %         I.RandomFactors = list of factors for each random variable.  These are CPDs, with
  %              the child variable = D.var(1)
  %         I.DecisionFactors = factor for the decision node.
  %         I.UtilityFactors = list of factors representing conditional utilities.
  % Return value: the maximum expected utility of I and an optimal decision rule 
  % (represented again as a factor) that yields that expected utility.
  % You may assume that there is a unique optimal decision.
  %
  % This is similar to OptimizeMEU except that we will have to account for
  % multiple utility factors.  We will do this by calculating the expected
  % utility factors and combining them, then optimizing with respect to that
  % combined expected utility factor.  
  MEU = [];
  OptimalDecisionRule = [];
  
  MEU = 0;
  OptimalDecisionRule = I.DecisionFactors(1);
  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  %
  % YOUR CODE HERE
  %
  % A decision rule for D assigns, for each joint assignment to D's parents, 
  % probability 1 to the best option from the EUF for that joint assignment 
  % to D's parents, and 0 otherwise.  Note that when D has no parents, it is
  % a degenerate case we can handle separately for convenience.
  %
  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  

  MEU = 0;
  OptimalDecisionRule = I.DecisionFactors(1);
  
  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  %
  % YOUR CODE HERE
  %
  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

  L = [];
  for i = 1:length(I.UtilityFactors)
    a.RandomFactors = I.RandomFactors;
    a.DecisionFactors = I.DecisionFactors;
    a.UtilityFactors = I.UtilityFactors(i);
    L = [L CalculateExpectedUtilityFactor(a)];
  end;
  
  S = L(1);
  for i = 2:length(L)
    S = FactorAdd(S, L(i));
  end;
    
  decisionVar = I.DecisionFactors(1).var;
  decisionCard = I.DecisionFactors(1).card(1);
  
  vector = reshape(S.val, S.card, []);
  if (decisionVar == S.var)
    vector = vector';
  end;
  
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
  
  val = repmat(S.val, 1);
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
  OptimalDecisionRule = struct('var', S.var, 'card', S.card, 'val', val);
  


end
