%CLIQUETREECALIBRATE Performs sum-product or max-product algorithm for 
%clique tree calibration.

%   P = CLIQUETREECALIBRATE(P, isMax) calibrates a given clique tree, P 
%   according to the value of isMax flag. If isMax is 1, it uses max-sum
%   message passing, otherwise uses sum-product. This function 
%   returns the clique tree where the .val for each clique in .cliqueList
%   is set to the final calibrated potentials.
%
% Copyright (C) Daphne Koller, Stanford University, 2012

function P = CliqueTreeCalibrate(P, isMax)


% Number of cliques in the tree.
N = length(P.cliqueList);

% Setting up the messages that will be passed.
% MESSAGES(i,j) represents the message going from clique i to clique j. 
MESSAGES = repmat(struct('var', [], 'card', [], 'val', 0), N, N);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% We have split the coding part for this function in two chunks with
% specific comments. This will make implementation much easier.
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% YOUR CODE HERE
% While there are ready cliques to pass messages between, keep passing
% messages. Use GetNextCliques to find cliques to pass messages between.
% Once you have clique i that is ready to send message to clique
% j, compute the message and put it in MESSAGES(i,j).
% Remember that you only need an upward pass and a downward pass.
%
 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%todo populate message leafs

if (isMax == 1)
  for k = 1:length(P.cliqueList)
      P.cliqueList(k).val = log(P.cliqueList(k).val);
  end;
end;

while (true)
  [i, j] = GetNextCliques(P, MESSAGES);
  if (i == 0 && j == 0)
    break;
  end;
  
  msg = [];
  for k = 1:N
    if (P.edges(k, i) == 1 && k != j)
      if (isempty(msg))
        msg = MESSAGES(k, i);
      else
        if (isMax == 0)
          msg = FactorProduct(msg, MESSAGES(k, i));
        else
          msg = FactorAdd(msg, MESSAGES(k, i));
        end;
      end;
    end;
  end;
  
  psi = P.cliqueList(i);
  if (!isempty(msg))
    if isMax == 0
      msg = FactorProduct(psi, msg);
    else
      msg = FactorAdd(psi, msg);
    end;
  else
    msg = psi;
  end;
  
  vars = setdiff(psi.var, P.cliqueList(j).var);
  if isMax == 0
    msg = FactorMarginalization(msg, vars);
  else
    msg = FactorMaxMarginalization(msg, vars);
  end;
  
  if isMax == 0
    msg.val = msg.val / sum(msg.val);
  end;

  MESSAGES(i,j) = msg;  
end;


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% YOUR CODE HERE
%
% Now the clique tree has been calibrated. 
% Compute the final potentials for the cliques and place them in P.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
for i = 1:N
  psi = P.cliqueList(i);
  for k = 1:N
    if (P.edges(k, i) == 1)
      if (isMax == 1)
        psi = FactorAdd(psi, MESSAGES(k,i));
      else
        psi = FactorProduct(psi, MESSAGES(k,i));
      end;
    end;
  end;
  
  P.cliqueList(i) = psi;
end;

return
