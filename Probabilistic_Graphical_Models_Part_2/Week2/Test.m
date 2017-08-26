load PA4Sample.mat;

%toCompare = InitPotential.RESULT;
%result = ComputeInitialPotentials(InitPotential.INPUT);

%toCompare = GetNextC.RESULT1;
%result = GetNextCliques(GetNextC.INPUT1, GetNextC.INPUT2);

%toCompare = SumProdCalibrate.RESULT;
%result = CliqueTreeCalibrate(SumProdCalibrate.INPUT, 0);

%toCompare = ExactMarginal.RESULT;
%result = ComputeExactMarginalsBP(ExactMarginal.INPUT, [], 0);
%i = 0;

%toCompare = FactorMax.RESULT;
%result = FactorMaxMarginalization(FactorMax.INPUT1, FactorMax.INPUT2);

%toCompare = MaxSumCalibrate.RESULT;
%result = CliqueTreeCalibrate(MaxSumCalibrate.INPUT, 1);

%toCompare = MaxMarginals.RESULT;
%result = ComputeExactMarginalsBP(MaxMarginals.INPUT, [], 1);

toCompare = MaxDecoded.RESULT;
result = MaxDecoding(MaxDecoded.INPUT);

i = 0;