# -*- coding: utf-8 -*-
"""
from java version

"""
import os
import time

import opt.ga.MaxKColorFitnessMinmizeFunction as MaxKColorFitnessMinmizeFunction
import dist.DiscreteDependencyTree as DiscreteDependencyTree
import dist.DiscreteUniformDistribution as DiscreteUniformDistribution
import dist.Distribution as Distribution
import opt.GenericHillClimbingProblem as GenericHillClimbingProblem
import opt.HillClimbingProblem as HillClimbingProblem
import opt.NeighborFunction as NeighborFunction
import opt.RandomizedHillClimbing as RandomizedHillClimbing
import opt.SimulatedAnnealing as SimulatedAnnealing
import opt.SwapNeighbor as SwapNeighbor
import opt.ga.CrossoverFunction as CrossoverFunction
import opt.ga.GenericGeneticAlgorithmProblem as GenericGeneticAlgorithmProblem
import opt.ga.GeneticAlgorithmProblem as GeneticAlgorithmProblem
import opt.ga.MutationFunction as MutationFunction
import opt.ga.SingleCrossOver as SingleCrossOver
import opt.ga.StandardGeneticAlgorithm as StandardGeneticAlgorithm
import opt.ga.SwapMutation as SwapMutation
import opt.prob.GenericProbabilisticOptimizationProblem as GenericProbabilisticOptimizationProblem
import opt.prob.MIMIC as MIMIC
import opt.prob.ProbabilisticOptimizationProblem as ProbabilisticOptimizationProblem
import shared.DataSet as DataSet
import shared.FixedIterationTrainer as FixedIterationTrainer
import shared.Instance as Instance
import shared.reader.CSVDataSetReader as CSVDataSetReader
import shared.reader.DataSetReader as DataSetReader
import util.graph.Edge as Edge
import util.graph.Graph as Graph
import util.graph.Node as Node
import util.linalg.Vector as Vector

import java.util.ArrayList as ArrayList
import java.util.Arrays as Arrays
import java.util.HashSet as HashSet
import java.util.List as List
import java.util.Set as Set 
import java.io.File as File
from jarray import array, zeros

def edgeTuples(filename):
    dsr = CSVDataSetReader(filename)
    ds = dsr.read()
    return ds 

def makeNetwork(count, filename):
    zachNodeCount = count
    a = 0
    b = 0
    node = Node 
    nodes = ArrayList.newInstance()
    for i in range(0,zachNodeCount): 
        nodes.add(i, Node(i))
    
    for i in range(0, nodes.size()):
        node = Node(i)
        nodes.set(i, node)
        ds = edgeTuples(filename)
        edges = ArrayList(ds.size())
        instances = ds.getInstances()
        for inst in instances:
            a = inst.getDiscrete(0)
            b = inst.getDiscrete(1)
            edg = Edge()
            edges.add(edg)
            nodes.get(a).connect(nodes.get(b), edg)

        graph = Graph()
        graph.setNodes(nodes)
        print(graph)

        assert graph.getEdges().size() == edges.size();
        return graph

def makeMlRoseExample():
    #5-Node Graph from as shown in mlrose documentation https://mlrose.readthedocs.io/en/stable/source/fitness.html#mlrose.fitness.MaxKColor
    edges = ArrayList.newInstance()
    nodes = ArrayList.newInstance()
    for i in range(0,5): 
        nodes.add(i, Node(i))
    for i in range(0,6): 
        edge = Edge()
        edges.add(i, edge)
    nodes.get(0).connect(nodes.get(1), edges.get(0))
    nodes.get(0).connect(nodes.get(2), edges.get(1))
    nodes.get(0).connect(nodes.get(4), edges.get(2))
    nodes.get(1).connect(nodes.get(3), edges.get(3))
    nodes.get(2).connect(nodes.get(3), edges.get(4))
    nodes.get(3).connect(nodes.get(4), edges.get(5))

    graph = Graph()
    graph.setNodes(nodes)
    print(graph)
    return graph

def makeSEdges():
    #Simple 4 node with 5 edges, 3-color is deterministic
    #https://www.geeksforgeeks.org/m-coloring-problem-backtracking-5/
    edges = ArrayList.newInstance()
    nodes = ArrayList.newInstance()
    for i in range(0,4): 
        nodes.add(i, Node(i))
    for i in range(0,5): 
        edge = Edge()
        edges.add(i, edge)

    nodes.get(0).connect(nodes.get(1), edges.get(0))
    nodes.get(0).connect(nodes.get(2), edges.get(1))
    nodes.get(0).connect(nodes.get(3), edges.get(2))
    nodes.get(3).connect(nodes.get(2), edges.get(3))
    nodes.get(2).connect(nodes.get(1), edges.get(4))
    graph = Graph()
    graph.setNodes(nodes)
    print(graph)
    return graph

def makeWikiEdges():
    #6-Node Graph from wikipedia https://en.wikipedia.org/wiki/Graph_coloring#Polynomial_time
    return  makeNetwork(6,os.path.join('..', 'src', 'opt', 'test', 'kcolorcsv', 'wiki_graph.csv'))

def makeZachery():
    #From the well known Zachary Karate Club https://commons.wikimedia.org/wiki/File:Zachary%27s_karate_club.png
    return  makeNetwork(34,os.path.join('..', 'src', 'opt', 'test', 'kcolorcsv', 'zach_edgelist.csv'))

def makePetersen():
    #Petersen graph from wikipedia https://en.wikipedia.org/wiki/Petersen_graph
    return  makeNetwork(10,os.path.join('..', 'src', 'opt', 'test', 'kcolorcsv', 'petersen_graph_structure1.csv'))

def discreteColors(inst):
        #dColors = zeros(inst.size(), 'i')
        dColors=""
        data = inst.getData()
        for i in range(0, inst.size()):
            #dColors[i] = int(data.get(i))
            dColors=dColors + str(int(data.get(i))) +","
        return dColors[:-1]

###################################
#SET K AND GRAPH TYPE
###################################
K=3
graph = makeWikiEdges()
#graph = makeMlRoseExample()
#graph = makeZachery()
#graph = makeSEdges()
#graph = makePetersen()
edges = graph.getEdges()
###################################

ef = MaxKColorFitnessMinmizeFunction(edges.toArray(zeros(edges.size(), Edge)))
N = graph.getNodeCount()
ranges = zeros(N, 'i')
Arrays.fill(ranges, K)

odd = DiscreteUniformDistribution(ranges)
nf = SwapNeighbor()
mf = SwapMutation()
cf = SingleCrossOver()
hcp = GenericHillClimbingProblem(ef, odd, nf)
gap = GenericGeneticAlgorithmProblem(ef, odd, mf, cf)

#Important use DiscreteDependencyTree with ranges for MIMIC and k-color 
df = DiscreteDependencyTree(0.1, ranges)
pop = GenericProbabilisticOptimizationProblem(ef, odd, df)

#RHC
starttime = time.time()
rhc = RandomizedHillClimbing(hcp)
fit = FixedIterationTrainer(rhc, 20000)
fit.train()
print "RHC:" , ef.value(rhc.getOptimal()), " Colors:",  discreteColors(rhc.getOptimal())
print ef.foundConflict() 
print "Time:", (time.time() - starttime)
print "fEvals: ", ef.getFunctionEvaluations()
print("============================")
ef.resetFunctionEvaluationCount()

#SA
starttime = time.time()
sa = SimulatedAnnealing(1E12, .1, hcp)
fit = FixedIterationTrainer(sa, 20000)
fit.train()
print "SA:" , ef.value(sa.getOptimal()) , " Colors:" , discreteColors(sa.getOptimal())
print(ef.foundConflict())
print "Time: " , (time.time() - starttime)
print "fEvals: " , ef.getFunctionEvaluations()
print("============================")
ef.resetFunctionEvaluationCount()

#GA
starttime = time.time()
ga = StandardGeneticAlgorithm(200, 100, 20, gap)
fit = FixedIterationTrainer(ga, 50)
fit.train()
print "GA: " , ef.value(ga.getOptimal()) , " Colors: " , discreteColors(ga.getOptimal())
print(ef.foundConflict())
print "Time: " , (time.time() - starttime)
print "fEvals: " , ef.getFunctionEvaluations()
print("============================")
ef.resetFunctionEvaluationCount()

#MIMIC
starttime = time.time()
mimic = MIMIC(100, 10, pop)
fit = FixedIterationTrainer(mimic, 10)
fit.train()
print "MIMIC: " , ef.value(mimic.getOptimal()) , " Colors: " + discreteColors(mimic.getOptimal())
print(ef.foundConflict())
print "Time: " , (time.time() - starttime)
print "fEvals: " , ef.getFunctionEvaluations()