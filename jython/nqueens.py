"""
N-Queens Test. Inspired from the Java file.

-Athira Nair
"""

import random

import opt.ga.NQueensFitnessFunction as NQueensFitnessFunction
import dist.DiscreteDependencyTree as DiscreteDependencyTree
import dist.DiscretePermutationDistribution as DiscretePermutationDistribution
import dist.DiscreteUniformDistribution as DiscreteUniformDistribution
import dist.Distribution as Distribution
import opt.DiscreteChangeOneNeighbor as DiscreteChangeOneNeighbor
import opt.EvaluationFunction as EvaluationFunction
import opt.GenericHillClimbingProblem as GenericHillClimbingProblem
import opt.HillClimbingProblem as HillClimbingProblem
import opt.NeighborFunction as NeighborFunction
import opt.RandomizedHillClimbing as RandomizedHillClimbing
import opt.SimulatedAnnealing as SimulatedAnnealing
import opt.SwapNeighbor as SwapNeighbor
from opt.example import *
import opt.ga.CrossoverFunction as CrossoverFunction
import opt.ga.DiscreteChangeOneMutation as DiscreteChangeOneMutation
import opt.ga.SingleCrossOver as SingleCrossOver
import opt.ga.GenericGeneticAlgorithmProblem as GenericGeneticAlgorithmProblem
import opt.ga.GeneticAlgorithmProblem as GeneticAlgorithmProblem
import opt.ga.MutationFunction as MutationFunction
import opt.ga.StandardGeneticAlgorithm as StandardGeneticAlgorithm
import opt.ga.SwapMutation as SwapMutation
import opt.prob.GenericProbabilisticOptimizationProblem as GenericProbabilisticOptimizationProblem
import opt.prob.MIMIC as MIMIC
import opt.prob.ProbabilisticOptimizationProblem as ProbabilisticOptimizationProblem
import shared.FixedIterationTrainer as FixedIterationTrainer

N = 50

# ranges = [random.randint(1, N) for i in range(N)]
ef = NQueensFitnessFunction()
odd = DiscretePermutationDistribution(N)
nf = SwapNeighbor()
mf = SwapMutation()
cf = SingleCrossOver()
df = DiscreteDependencyTree(.1)
hcp = GenericHillClimbingProblem(ef, odd, nf)
gap = GenericGeneticAlgorithmProblem(ef, odd, mf, cf)
pop = GenericProbabilisticOptimizationProblem(ef, odd, df)

rhc = RandomizedHillClimbing(hcp)
fit = FixedIterationTrainer(rhc, 200000)
fit.train()
rhc_opt = ef.value(rhc.getOptimal())
print "RHC: " + str(rhc_opt)
# print "RHC: Board Position: "
# print(ef.boardPositions())

print("============================")

sa = SimulatedAnnealing(1E1, .1, hcp)
fit = FixedIterationTrainer(sa, 200000)
fit.train()
sa_opt = ef.value(sa.getOptimal())
print "SA: " + str(sa_opt)
# print("SA: Board Position: ")
# print(ef.boardPositions())

print("============================")

ga = StandardGeneticAlgorithm(200, 0, 10, gap)
fit = FixedIterationTrainer(ga, 1000)
fit.train()
ga_opt = ef.value(ga.getOptimal())
print "GA: " + str(ga_opt)
# print("GA: Board Position: ")
# print(ef.boardPositions())
print("============================")

mimic = MIMIC(200, 10, pop)
fit = FixedIterationTrainer(mimic, 1000)
fit.train()
mimic_opt = ef.value(mimic.getOptimal())
print "MIMIC: " + str(mimic_opt)
# print("MIMIC: Board Position: ")
# print(ef.boardPositions())
