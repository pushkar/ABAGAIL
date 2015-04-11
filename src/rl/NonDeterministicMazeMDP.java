package rl;

import java.io.BufferedReader;
import java.io.FileReader;

import dist.DiscreteDistribution;

/**
 * A markov decision process representing a maze with non-deterministic actions
 * @author Yiqi (Victor) Chen yiqic1993@gmail.com
 * @version 1.0
 */
public class NonDeterministicMazeMDP extends MazeMarkovDecisionProcess {
    
    /** 
     * The probability of moving towards the direction of 
     * 90 degree * index, counterclockwise
     * TRANSITION_MODEL[4] = motion failure probability 
     */
    private static final double[] TRANSITION_MODEL = {0.8, 0.1, 0, 0.1, 0};
    
    /** The reward for success */
    private static final double SREWARD = 1;
    /** The reward for failure */
    private static final double FREWARD = -1;
    /** The reward for any step */
    private static final double STEPREWARD = -0.04;

    /** The character representing the success */
    public static final char SUCCESS = 'x';
    /** The character representing the failure */
    public static final char FAILURE = 'f';
    
    /** The transition model */
    private DiscreteDistribution transitionModel;
    /** The success goal state */
    private int success;
    /** The failure goal state */
    private int failure;

    /**
     * Make a new non-deterministic maze markov decision process
     * @param maze the maze itself
     * @param xSuccess the x success goal
     * @param ySuccess the y success goal
     * @param xFailure the x failure goal
     * @param yFailure the y failure goal
     * @param xInitial the initial x
     * @param yInitial the initial y
     * @param transitionModel the transition model of a given 
     */
    public NonDeterministicMazeMDP(char[][] maze, int xSuccess, int ySuccess, 
            int xFailure, int yFailure, int xInitial, int yInitial, 
            double[] transitionModel) {
        super(maze, xSuccess, ySuccess, xInitial, yInitial, 
                transitionModel.length > ACTIONS ? transitionModel[ACTIONS] : 0);
        this.maze = maze;
        this.success = stateFor(xSuccess, ySuccess);
        this.failure = stateFor(xFailure, yFailure);
        this.initial = stateFor(xInitial, yInitial);
        this.transitionModel = new DiscreteDistribution(transitionModel);
    }

    /**
     * @see rl.MarkovDecisionProcess#reward(int, int)
     */
    public double reward(int state, int action) {
        if (state == success) {
            return SREWARD;
        } else if (state == failure) {
            return FREWARD;
        } else {
            return STEPREWARD;
        }
    }

    /**
     * @see rl.MarkovDecisionProcess#transitionProbability(int, int, int)
     */
    public double transitionProbability(int i, int j, int a) {
        // This part isn't necessary but can speed up the code in most cases. 
        int startX = xFor(i), startY = yFor(i);
        int endX = xFor(j), endY = yFor(j);
        if (startX != endX && startY != endY) {
            return 0;
        }
        
        double prob = 0;
        for (int rotation = 0; rotation < ACTIONS; rotation++) {
            int realAction = (a + rotation) % ACTIONS;
            if (move(i, realAction) == j) {
                prob += transitionModel.getProbabilities()[rotation];
            }
        }
        if (i == j) {
            double[] tm = transitionModel.getProbabilities();
            prob += tm.length > ACTIONS ? tm[ACTIONS] : 0;
        }
        return prob;
    }

    /**
     * @see rl.MarkovDecisionProcess#sampleState(int, int)
     */
    public int sampleState(int i, int a) {
        int rotation = transitionModel.sample().getDiscrete();
        if (rotation == ACTIONS) {
            return i;
        }
        int realAction = (a + rotation) % ACTIONS;
        return move(i, realAction);
    }
    
    /**
     * The next state of moving toward direction a in state i
     * @param i the current state
     * @param a the direction
     * @return the next state
     */
    public int move(int i, int a) {
        int dx, dy, startX = xFor(i), startY = yFor(i);
        switch(a) {
            case MOVE_UP:
                dx = 0; dy = -1;
                break;
            case MOVE_DOWN:
                dx = 0; dy = 1;
                break;
            case MOVE_LEFT:
                dx = -1; dy = 0;
                break;
            case MOVE_RIGHT:
                dx = 1; dy = 0;
                break;
            default:
                dx = 0; dy = 0;
                break;
        }
        if (startX + dx >= getWidth() || startX + dx < 0 ||
                startY + dy >= getHeight() || startY + dy < 0 ||
                isObstacle(startX + dx, startY + dy)) {
            return i;
        } else {
            return stateFor(startX + dx, startY + dy);
        }
    }

    /**
     * @see rl.MarkovDecisionProcess#isTerminalState(int)
     */
    public boolean isTerminalState(int state) {
        return state == success || state == failure;
    }
    
    /**
     * Load a maze from a text file
     * @param fileName the file to read from
     * @throws an exception when there's an error reading
     * the file
     */
    public static NonDeterministicMazeMDP load(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        int height = 1;
        String line = br.readLine();
        int width = line.length();
        while((line = br.readLine()) != null) {
            height++;
        }
        br.close();
        char[][] maze = new char[height][width];
        br = new BufferedReader(new FileReader(fileName));
        int successX = -1, successY = -1, failureX = -1, failureY = -1;
        int initialX = -1, initialY = -1;
        for (int i = 0; i < maze.length; i++) {
            line = br.readLine();
            for (int j = 0; j < maze[i].length; j++) {
                char c = line.charAt(j);
                if (c == AGENT) {
                    initialX = j;
                    initialY = i;
                    maze[i][j] = EMPTY;
                } else if (c == SUCCESS) {
                    successX = j;
                    successY = i;
                    maze[i][j] = EMPTY;
                } else if (c == FAILURE) {
                    failureX = j;
                    failureY = i;
                    maze[i][j] = EMPTY;
                } else if (c == OBSTACLE) {
                    maze[i][j] = OBSTACLE;
                } else {
                    maze[i][j] = EMPTY;
                }
            }
        }
        br.close();
        return new NonDeterministicMazeMDP(maze, successX, successY, failureX, failureY, 
                initialX, initialY, TRANSITION_MODEL);
    }
    
    /**
     * Return a string representation
     * @return the string representation
     */
    public String toString() {
        String ret = "";
        int initialX = xFor(initial), initialY = yFor(initial);
        int successX = xFor(success), successY = yFor(success);
        int failureX = xFor(failure), failureY = yFor(failure);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (i == initialY && j == initialX) {
                    ret += "o";
                } else if (i == successY && j == successX) {
                    ret += "x";
                } else if (i == failureY && j == failureX) {
                    ret += "f";
                } else {
                    ret += maze[i][j];
                }
            }
            ret += "\n";
        }
        return ret;
    }    

}
