% Copyright (C) Daphne Koller, Stanford University, 2012

function EUF = CalculateExpectedUtilityFactor( I )

  % Inputs: An influence diagram I with a single decision node and a single utility node.
  %         I.RandomFactors = list of factors for each random variable.  These are CPDs, with
  %              the child variable = D.var(1)
  %         I.DecisionFactors = factor for the decision node.
  %         I.UtilityFactors = list of factors representing conditional utilities.
  % Return value: A factor over the scope of the decision rule D from I that
  % gives the conditional utility given each assignment for D.var
  %
  % Note - We assume I has a single decision node and utility node.
  EUF = [];
  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  %
  % YOUR CODE HERE...
  %
  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
  
  F = [I.RandomFactors I.DecisionFactors];
  U = I.UtilityFactors(1);
  
  decisionVar = I.DecisionFactors.var(1);
  utilityVar = U.var(1);
  for i=1:length(U.var)
    if U.var(i) != decisionVar
      utilityVar = U.var(i);
    end;
  end;
   
  notActionVars = [];
  for i=1:length(F)
    for j=1:length(F(i).var)
      if all(I.DecisionFactors.var != F(i).var(j))
        notActionVars = [notActionVars F(i).var(j)];
      end;
    end;
  end;
   
  factors = I.RandomFactors;
   
  product = factors(1);
  for i=2:length(factors)
    product = FactorProduct(product, factors(i));
  end;
  
  utilityProduct = FactorProduct(product, U);
  EUF = VariableElimination(utilityProduct, unique(notActionVars));

end  
