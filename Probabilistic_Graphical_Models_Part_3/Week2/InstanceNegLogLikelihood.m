% function [nll, grad] = InstanceNegLogLikelihood(X, y, theta, modelParams)
% returns the negative log-likelihood and its gradient, given a CRF with parameters theta,
% on data (X, y). 
%
% Inputs:
% X            Data.                           (numCharacters x numImageFeatures matrix)
%              X(:,1) is all ones, i.e., it encodes the intercept/bias term.
% y            Data labels.                    (numCharacters x 1 vector)
% theta        CRF weights/parameters.         (numParams x 1 vector)
%              These are shared among the various singleton / pairwise features.
% modelParams  Struct with three fields:
%   .numHiddenStates     in our case, set to 26 (26 possible characters)
%   .numObservedStates   in our case, set to 2  (each pixel is either on or off)
%   .lambda              the regularization parameter lambda
%
% Outputs:
% nll          Negative log-likelihood of the data.    (scalar)
% grad         Gradient of nll with respect to theta   (numParams x 1 vector)
%
% Copyright (C) Daphne Koller, Stanford Univerity, 2012

function [nll, grad] = InstanceNegLogLikelihood(X, y, theta, modelParams)

    % featureSet is a struct with two fields:
    %    .numParams - the number of parameters in the CRF (this is not numImageFeatures
    %                 nor numFeatures, because of parameter sharing)
    %    .features  - an array comprising the features in the CRF.
    %
    % Each feature is a binary indicator variable, represented by a struct 
    % with three fields:
    %    .var          - a vector containing the variables in the scope of this feature
    %    .assignment   - the assignment that this indicator variable corresponds to
    %    .paramIdx     - the index in theta that this feature corresponds to
    %
    % For example, if we have:
    %   
    %   feature = struct('var', [2 3], 'assignment', [5 6], 'paramIdx', 8);
    %
    % then feature is an indicator function over X_2 and X_3, which takes on a value of 1
    % if X_2 = 5 and X_3 = 6 (which would be 'e' and 'f'), and 0 otherwise. 
    % Its contribution to the log-likelihood would be theta(8) if it's 1, and 0 otherwise.
    %
    % If you're interested in the implementation details of CRFs, 
    % feel free to read through GenerateAllFeatures.m and the functions it calls!
    % For the purposes of this assignment, though, you don't
    % have to understand how this code works. (It's complicated.)
    
    featureSet = GenerateAllFeatures(X, modelParams);

    % Use the featureSet to calculate nll and grad.
    % This is the main part of the assignment, and it is very tricky - be careful!
    % You might want to code up your own numerical gradient checker to make sure
    % your answers are correct.
    %
    % Hint: you can use CliqueTreeCalibrate to calculate logZ effectively. 
    %       We have halfway-modified CliqueTreeCalibrate; complete our implementation 
    %       if you want to use it to compute logZ.
    
    nll = 0;
    grad = zeros(size(theta));

    %%%
    % Your code here:
    
    function feature = evaluateFeature(y, var, assignment)
      if (y(var) == assignment)
        feature = 1;
      else
        feature = 0;
      end;
    end;

    cliqueTree = struct(
      'cliqueList', [],
      'edges', zeros(modelParams.numObservedStates, modelParams.numObservedStates));
       
    featureCounts = repmat([0], length(theta), 1);
    modelFeatureCounts = repmat([0], length(theta), 1);
     
    D = [modelParams.numHiddenStates modelParams.numHiddenStates];  
    
    for i = 1 : modelParams.numObservedStates
      cliqueTree.cliqueList(end + 1) = struct('var', [i, i + 1], 'card', D, 'val', ones(prod(D), 1)');
    end;
    
    for i = 1 : modelParams.numObservedStates
      for j = 1 : modelParams.numObservedStates
        if j == i + 1 || j == i - 1;
          cliqueTree.edges(i, j) = 1;
        end;
      end;
    end;
      
    for i = 1 : length(featureSet.features)
      feature = featureSet.features(i);
      featureCounts(feature.paramIdx) += evaluateFeature(y, feature.var, feature.assignment);

      for j = 1 : length(cliqueTree.cliqueList)
        if ismember(feature.var, cliqueTree.cliqueList(j).var)
          if length(feature.var) == 1
            if feature.var != 1 && feature.var == cliqueTree.cliqueList(j).var(1)
              continue
            end;
            for k = 1 : modelParams.numHiddenStates
              if feature.var == cliqueTree.cliqueList(j).var(1)
                assignment = [feature.assignment k];
              else
                assignment = [k feature.assignment];
              end;
              index = AssignmentToIndex(assignment, D);
              cliqueTree.cliqueList(j).val(index) *= exp(theta(feature.paramIdx));
            end;
          else
            index = AssignmentToIndex(feature.assignment, D);
            cliqueTree.cliqueList(j).val(index) *= exp(theta(feature.paramIdx));
          end;         
        end;
      end;
    end;
    
    [calibratedCliqueTree, logZ] = CliqueTreeCalibrate(cliqueTree, 0);
    
    for i = 1 : length(calibratedCliqueTree.cliqueList)
      calibratedCliqueTree.cliqueList(i).val = calibratedCliqueTree.cliqueList(i).val / sum(calibratedCliqueTree.cliqueList(i).val);
    end;
    
    for i = 1 : length(featureSet.features)
      feature = featureSet.features(i);
      for j = 1 : length(calibratedCliqueTree.cliqueList)
        if length(setdiff(feature.var, calibratedCliqueTree.cliqueList(j).var)) == 0
          targetClique = calibratedCliqueTree.cliqueList(j);
          break;
        end;
      end;

      marginalizeVar = setdiff(targetClique.var, feature.var);
      
      if (length(marginalizeVar) > 0)
        targetClique = FactorMarginalization(targetClique, marginalizeVar);
      end;
      
      modelFeatureCounts(feature.paramIdx) += targetClique.val(AssignmentToIndex(feature.assignment, targetClique.card));
    end;
     
    modelFeatureCounts = modelFeatureCounts';
    featureCounts = featureCounts';
    
    weightedFeatureCounts = theta .* featureCounts;
    regularizationCost = (modelParams.lambda / 2) * sum(theta .* theta);
    
    regularizationGradient = modelParams.lambda .* theta;
    
    nll = logZ - sum(weightedFeatureCounts) + regularizationCost;
    grad = modelFeatureCounts - featureCounts + regularizationGradient;
end
