function [P loglikelihood] = LearnCPDsGivenGraph(dataset, G, labels)
%
% Inputs:
% dataset: N x 10 x 3, N poses represented by 10 parts in (y, x, alpha)
% G: graph parameterization as explained in PA description
% labels: N x 2 true class labels for the examples. labels(i,j)=1 if the 
%         the ith example belongs to class j and 0 elsewhere        
%
% Outputs:
% P: struct array parameters (explained in PA description)
% loglikelihood: log-likelihood of the data (scalar)
%
% Copyright (C) Daphne Koller, Stanford Univerity, 2012

N = size(dataset, 1);
K = size(labels,2);

loglikelihood = 0;
P.c = zeros(1,K);

% estimate parameters
% fill in P.c, MLE for class probabilities
% fill in P.clg for each body part and each class
% choose the right parameterization based on G(i,1)
% compute the likelihood - you may want to use ComputeLogLikelihood.m
% you just implemented.
%%%%%%%%%%%%%%%%%%%%%%%%%
% YOUR CODE HERE

load 'PA8SampleCases.mat';

M = length(G);
P.c = sum(labels) / sum(sum(labels));
V = repmat(struct('o', [], 'p', []), K, M);
P.clg = repmat(struct('mu_y', [], 'sigma_y', [], 'mu_x', [], 'sigma_x', [], 'mu_angle', [], 'sigma_angle', [], 'theta', []), 1, 10);

for j = 1 : N
  o_poses = reshape(dataset(j,:,:), [M 3]);
  label = labels(j, :);
  k = find(label == 1);
  
  for i = 1 : M
    pose = o_poses(i, :);
    par_i = G(i, 2);
    if par_i > 0
      par = o_poses(par_i, :);
      V(k, i).p = [V(k, i).p;par];
    end;
    V(k, i).o = [V(k, i).o;pose];
  end;
end;

for k = 1 : K
  for i = 1 : M
    v = V(k, i);
    if length(v.p) == 0
      [mu, sigma] = FitGaussianParameters(v.o);
      P.clg(i).mu_y = [P.clg(i).mu_y mu(1)];
      P.clg(i).sigma_y = [P.clg(i).sigma_y sigma(1)];
      P.clg(i).mu_x = [P.clg(i).mu_x mu(2)];
      P.clg(i).sigma_x = [P.clg(i).sigma_x sigma(2)];
      P.clg(i).mu_angle = [P.clg(i).mu_angle mu(3)];
      P.clg(i).sigma_angle = [P.clg(i).sigma_angle sigma(3)];
    else
      [beta_y, sigma_y] = FitLinearGaussianParameters(v.o(:,1), v.p);
      [beta_x, sigma_x] = FitLinearGaussianParameters(v.o(:,2), v.p);
      [beta_angle, sigma_angle] = FitLinearGaussianParameters(v.o(:,3), v.p);
      P.clg(i).theta = [P.clg(i).theta;
        beta_y(4), beta_y(1), beta_y(2), beta_y(3), beta_x(4), beta_x(1), beta_x(2), beta_x(3), beta_angle(4), beta_angle(1), beta_angle(2), beta_angle(3)];
      P.clg(i).sigma_y = [P.clg(i).sigma_y sigma_y];
      P.clg(i).sigma_x = [P.clg(i).sigma_x sigma_x];
      P.clg(i).sigma_angle = [P.clg(i).sigma_angle sigma_angle];
    end;
  end;
end;

loglikelihood = ComputeLogLikelihood(P, G, dataset);

fprintf('log likelihood: %f\n', loglikelihood);

