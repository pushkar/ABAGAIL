import sys
import os
import time

import opt.example.FlipFlopEvaluationFunction as FlipFlopEvaluationFunction
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

N = 80
fill = [2] * N
ranges = array('i', fill)
    
ef = FlipFlopEvaluationFunction()
odd = DiscreteUniformDistribution(ranges)
nf = DiscreteChangeOneNeighbor(ranges)
mf = DiscreteChangeOneMutation(ranges)
cf = SingleCrossOver()
df = DiscreteDependencyTree(.1, ranges)
hcp = GenericHillClimbingProblem(ef, odd, nf)
gap = GenericGeneticAlgorithmProblem(ef, odd, mf, cf)
pop = GenericProbabilisticOptimizationProblem(ef, odd, df)

rhc = RandomizedHillClimbing(hcp)     
fit = FixedIterationTrainer(rhc, 200000)
fit.train()
print(ef.value(rhc.getOptimal()))

sa = SimulatedAnnealing(100, .95, hcp)
fit = FixedIterationTrainer(sa, 200000)
fit.train()
print(ef.value(sa.getOptimal()))

ga = StandardGeneticAlgorithm(200, 100, 20, gap)
fit = FixedIterationTrainer(ga, 1000)
fit.train()
print(ef.value(ga.getOptimal()))

mimic = MIMIC(200, 5, pop)
fit = FixedIterationTrainer(mimic, 1000)
fit.train()
print(ef.value(mimic.getOptimal()))