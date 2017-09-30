% MHUNIFORMTRANS
%
%  MCMC Metropolis-Hastings transition function that
%  utilizes the uniform proposal distribution.
%  A - The current joint assignment.  This should be
%      updated to be the next assignment
%  G - The network
%  F - List of all factors
%
% Copyright (C) Daphne Koller, Stanford University, 2012

function A = MHUniformTrans(A, G, F)

% Draw proposed new state from uniform distribution
A_prop = ceil(rand(1, length(A)) .* G.card);

p_acceptance = 0.0;
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% YOUR CODE HERE
% Compute acceptance probability
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

p1 = LogProbOfJointAssignment(F, A);
p2 = LogProbOfJointAssignment(F, A_prop);
p_acceptance = min(1, exp(p2-p1));

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Accept or reject proposal
if rand() < p_acceptance
    % disp('Accepted');
    A = A_prop;
end