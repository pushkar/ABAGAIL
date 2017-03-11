package rl.tester;

import rl.Policy;
import rl.MarkovDecisionProcess;

/**
 * Expected Reward Test Metric
 * @author Daniel Cohen
 * @version 1.0
 */
public class ExpectedRewardTestMetric {
	
	private Policy policy;
	private MarkovDecisionProcess mdp;
	
	/**
	 * Main constructor
     */
	public ExpectedRewardTestMetric(Policy p, MarkovDecisionProcess mdp) {
		this.policy = p;
		this.mdp = mdp;
	}
	
	/**
     * Computes the expected value by testing the provided policy
	 */
	public double compute(int trials, int iterations) {
		double totalReward = 0.0;
		for (int t = 0; t < trials; t++) {
			int currentState = this.mdp.sampleInitialState();
			for (int i = 0; i < iterations; i++) {
				int action = this.policy.getAction(currentState);				
				totalReward += this.mdp.reward(currentState, action);
				currentState = this.mdp.sampleState(currentState, action);
				
				if (currentState >= this.mdp.getStateCount()) {
					currentState = this.mdp.getStateCount() - 1;
				}
			}	
		}

		return totalReward / (double) trials / (double) iterations;
	}
}