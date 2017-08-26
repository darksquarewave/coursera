% Copyright (C) Daphne Koller, Stanford University, 2012

function EU = SimpleCalcExpectedUtility(I)

  % Inputs: An influence diagram, I (as described in the writeup).
  %         I.RandomFactors = list of factors for each random variable.  These are CPDs, with
  %              the child variable = D.var(1)
  %         I.DecisionFactors = factor for the decision node.
  %         I.UtilityFactors = list of factors representing conditional utilities.
  % Return Value: the expected utility of I
  % Given a fully instantiated influence diagram with a single utility node and decision node,
  % calculate and return the expected utility.  Note - assumes that the decision rule for the 
  % decision node is fully assigned.

  % In this function, we assume there is only one utility node.
  F = [I.RandomFactors I.DecisionFactors];
  U = I.UtilityFactors(1);
  EU = [];
  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  %
  % YOUR CODE HERE
  %
  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  
   decisionVar = I.DecisionFactors.var(1);
   utilityVar = U.var(1);
   for i=1:length(U.var)
     if U.var(i) != decisionVar
       utilityVar = U.var(i);
     end;
   end;
   
   allVars = [];
   for i=1:length(F)
     for j=1:length(F(i).var)
       if F(i).var(j) != utilityVar && F(i).var(j) != decisionVar
         allVars = [allVars F(i).var(j)];
       end;
     end;
   end;
   
   factors = VariableElimination(F, decisionVar);
   
   product = factors(1);
   for i=2:length(factors)
     product = FactorProduct(product, factors(i));
   end;
   
   product = FactorProduct(product, I.DecisionFactors);
   
   EU = sum(FactorProduct(VariableElimination(product, unique(allVars)), U).val);  
end
