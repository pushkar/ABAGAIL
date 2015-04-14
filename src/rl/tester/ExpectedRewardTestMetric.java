package rl.tester;

import rl.Policy;
import rl.SimpleMarkovDecisionProcess;

/**
 * Expected Reward Test Metric
 * @author Daniel Cohen
 * @version 1.0
 */
public class ExpectedRewardTestMetric {
	
	private Policy policy;
	private SimpleMarkovDecisionProcess mdp;
	
	/**
	 * Main constructor
     */
	public ExpectedRewardTestMetric(Policy p, SimpleMarkovDecisionProcess mdp) {
		this.policy = p;
		this.mdp = mdp;
	}
	
	/**
     * Computes the expected value by testing the provided policy
	 */
	public double compute(int trials, int iterations) {
		double[] rewards = this.mdp.getRewards();
		double totalReward = 0.0;
		for (int t = 0; t < trials; t++) {
			int currentState = this.mdp.sampleInitialState();
			for (int i = 0; i < iterations; i++) {
				
				if (currentState >= this.mdp.getStateCount()) {
					currentState = this.mdp.getStateCount() - 1;
				} else if (currentState < 0) {
					currentState = 0;
				}
				
				totalReward += rewards[currentState];
				currentState = this.mdp.sampleState(currentState, this.policy.getAction(currentState));
			}	
		}

		return totalReward / (double) trials;
	}
}