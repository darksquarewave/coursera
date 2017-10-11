function accuracy = ClassifyDataset(dataset, labels, P, G)
% returns the accuracy of the model P and graph G on the dataset 
%
% Inputs:
% dataset: N x 10 x 3, N test instances represented by 10 parts
% labels:  N x 2 true class labels for the instances.
%          labels(i,j)=1 if the ith instance belongs to class j 
% P: struct array model parameters (explained in PA description)
% G: graph structure and parameterization (explained in PA description) 
%
% Outputs:
% accuracy: fraction of correctly classified instances (scalar)
%
% Copyright (C) Daphne Koller, Stanford Univerity, 2012

N = size(dataset, 1);
accuracy = 0.0;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% YOUR CODE HERE
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

K = length(P.c); % number of classes
M = length(G); % number of poses
S = 0;

for j = 1 : N
  o_poses = reshape(dataset(j,:,:), [M 3]);
  true_label = find(labels(j, :) == max(labels(j,:)));
  
  logProb = zeros(1, K);
  
  for k = 1 : K
    for i = 1 : M
      par_i = G(i,2);
      params = P.clg(i);
     
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
        theta = params.theta;
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
  
  proposed_label = find(logProb == max(logProb));
  
  if true_label == proposed_label
    S++;
  end;
  
end;

accuracy = S / N;

fprintf('Accuracy: %.2f\n', accuracy);