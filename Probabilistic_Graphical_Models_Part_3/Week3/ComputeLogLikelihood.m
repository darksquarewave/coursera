function loglikelihood = ComputeLogLikelihood(P, G, dataset)
% returns the (natural) log-likelihood of data given the model and graph structure
%
% Inputs:
% P: struct array parameters (explained in PA description)
% G: graph structure and parameterization (explained in PA description)
%
%    NOTICE that G could be either 10x2 (same graph shared by all classes)
%    or 10x2x2 (each class has its own graph). your code should compute
%    the log-likelihood using the right graph.
%
% dataset: N x 10 x 3, N poses represented by 10 parts in (y, x, alpha)
% 
% Output:
% loglikelihood: log-likelihood of the data (scalar)
%
% Copyright (C) Daphne Koller, Stanford Univerity, 2012

N = size(dataset,1); % number of examples
K = length(P.c); % number of classes

loglikelihood = 0;
% You should compute the log likelihood of data as in eq. (12) and (13)
% in the PA description
% Hint: Use lognormpdf instead of log(normpdf) to prevent underflow.
%       You may use log(sum(exp(logProb))) to do addition in the original
%       space, sum(Prob).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% YOUR CODE HERE
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

for j = 1 : N
  o_poses = reshape(dataset(j,:,:), [10 3]);

  logProb = zeros(1, K);
  
  for k = 1 : K
    for i = 1 : length(G)
      par_i = G(i,2);
      params = P.clg(i);
      theta = params.theta;
      sigma_y = params.sigma_y(k);
      sigma_x = params.sigma_x(k);
      sigma_angle = params.sigma_angle(k);
    
      y = o_poses(i, 1);
      x = o_poses(i, 2);
      angle = o_poses(i, 3);
        
      if par_i > 0
        par = o_poses(par_i, :);
        y_par = par(1);
        x_par = par(2);
        angle_par = par(3);
        mu_y = theta(k, 1) + theta(k, 2) * y_par + theta(k, 3) * x_par + theta(k, 4) * angle_par;
        mu_x = theta(k, 5) + theta(k, 6) * y_par + theta(k, 7) * x_par + theta(k, 8) * angle_par;
        mu_angle = theta(k, 9) + theta(k, 10) * y_par + theta(k, 11) * x_par + theta(k, 12) * angle_par;
      else
        mu_y = params.mu_y(k);
        mu_x = params.mu_x(k);
        mu_angle = params.mu_angle(k);
      end;
    
      logProb_y = lognormpdf(y, mu_y, sigma_y);
      logProb_x = lognormpdf(x, mu_x, sigma_x);
      logProb_angle = lognormpdf(angle, mu_angle, sigma_angle);
      
      logProb(k) += logProb_y + logProb_x + logProb_angle;
    end;
    
    logProb(k) += log(P.c(k));
    
  end;
  
  loglikelihood += log(sum(exp(logProb)));
  
end;

