import sys
import os
import time

import opt.example.SixPeaksEvaluationFunction as SixPeaksEvaluationFunction
import dist.DiscreteUniformDistribution as DiscreteUniformDistribution
import opt.DiscreteChangeOneNeighbor as DiscreteChangeOneNeighbor
import opt.ga.DiscreteChangeOneMutation as DiscreteChangeOneMutation
import opt.ga.SingleCrossOver as SingleCrossOver
import dist.DiscreteDependencyTree as DiscreteDependencyTree
import opt.GenericHillClimbingProblem as GenericHillClimbingProblem
import opt.ga.GeneticAlgorithmProblem as GeneticAlgorithmProblem
import opt.prob.GenericProbabilisticOptimizationProblem as GenericProbabilisticOptimizationProblem
import opt.ga.GenericGeneticAlgorithmProblem as GenericGeneticAlgorithmProblem
import opt.RandomizedHillClimbing as RandomizedHillClimbing
import shared.FixedIterationTrainer as FixedIterationTrainer
import opt.SimulatedAnnealing as SimulatedAnnealing
import opt.ga.StandardGeneticAlgorithm as StandardGeneticAlgorithm
import opt.prob.MIMIC as MIMIC
from array import array

N = 10
T = N / 5
fill = [2] * N
ranges = array('i', fill)

ef = SixPeaksEvaluationFunction(T)
odd = DiscreteUniformDistribution(ranges)
nf = DiscreteChangeOneNeighbor(ranges)
mf = DiscreteChangeOneMutation(ranges)
cf = SingleCrossOver()
df = DiscreteDependencyTree(.1, ranges)
hcp = GenericHillClimbingProblem(ef, odd, nf)
gap = GenericGeneticAlgorithmProblem(ef, odd, mf, cf)
pop = GenericProbabilisticOptimizationProblem(ef, odd, df)

rhc = RandomizedHillClimbing(hcp)
fit = FixedIterationTrainer(rhc, 5)
fit.train()
print("RHC: ", ef.value(rhc.getOptimal()))

sa = SimulatedAnnealing(1E11, .95, hcp)
fit = FixedIterationTrainer(sa, 200000)
fit.train()
print("SA: ", ef.value(sa.getOptimal()))

ga = StandardGeneticAlgorithm(200, 100, 10, gap)
fit = FixedIterationTrainer(ga, 1000)
fit.train()
print("GA: ", ef.value(ga.getOptimal()))

mimic = MIMIC(200, 20, pop)
fit = FixedIterationTrainer(mimic, 1000)
fit.train()
print("MIMIC: ", ef.value(mimic.getOptimal()))