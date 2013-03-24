package rl;

import java.io.BufferedReader;
import java.io.FileReader;

import dist.Distribution;

/**
 * A markov decision process representing a maze
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MazeMarkovDecisionProcess implements MarkovDecisionProcess {
    /** The default failure probability */
    private static final double FAILURE_PROBABILITY = .01;
    
    /** The reward for solving the maze */
	private static final int REWARD = 100;
	
	/** The number of actions */
    public static final int ACTIONS = 4;    
    /** The move up action */
    public static final int MOVE_UP = 0;
    /** The move down action */
    public static final int MOVE_DOWN = 1;
    /** The move left action */
    public static final int MOVE_LEFT = 2;
    /** The move right action */
    public static final int MOVE_RIGHT = 3;
   
    /** The character representing an empty square */
    public static final char EMPTY = ' ';
    /** The character representing a closed square */
    public static final char OBSTACLE = '#';
    /** The character representing the agent */
    public static final char AGENT = 'o';
    /** The character representing the goal */
    public static final char GOAL = 'x';
    
    /**
     * The maze itself
     */
    private char[][] maze;
    /**
     * The probability of motion failing
     */
    private double motionFailureProbability;
    /**
     * The goal state
     */
    private int goal;
    /**
     * The initial state
     */
    private int initial;
    
    /**
     * Make a new maze markov decision process
     * @param maze the maze itself
     * @param xGoal the x goal
     * @param yGoal the y goal
     * @param xInitial the initial x
     * @param yInitial the initial y
     * @param motionFailureProbability the probability of motion failing
     */
    public MazeMarkovDecisionProcess(char[][] maze, int xGoal, int yGoal,
            int xInitial, int yInitial, double motionFailureProbability) {
        this.maze = maze;
        this.goal = stateFor(xGoal, yGoal);
        this.initial = stateFor(xInitial, yInitial);
        this.motionFailureProbability = motionFailureProbability;
    }
    /**
     * Determine if the state is blocked by an obstacle
     * @param state the state
     * @return true if it is
     */
    public boolean isObstacle(int state) {
        return isObstacle(xFor(state), yFor(state));
    }
    /**
     * Determine if the state is an obstacle
     * @param x the x location
     * @param y the y location
     * @return true if it is
     */
    public boolean isObstacle(int x, int y) {
        return maze[y][x] == OBSTACLE;
    }
    /**
     * Get the height of the maze
     * @return the height of the maze
     */
    public int getHeight() {
        return maze.length;
    }
    /**
     * Get the width of the maze
     * @return the width
     */
    public int getWidth() {
        return maze[0].length;
    }
    /**
     * Get the state for
     * @param x the x location
     * @param y the y location
     * @return the state number
     */
    public int stateFor(int x, int y) {
        return y + x * maze.length;
    }
    /**
     * Get the x coordinate for the given state
     * @param state the state
     * @return the x coordinate
     */
    public int xFor(int state) {
        return state / maze.length;
    }
    /**
     * Get the y coordinate for the given state
     * @param state the state
     * @return the y coordinate
     */
    public int yFor(int state) {
        return state % maze.length;
    }

    /**
     * @see rl.MarkovDecisionProcess#getStateCount()
     */
    public int getStateCount() {
        return maze.length * maze[0].length;
    }

    /**
     * @see rl.MarkovDecisionProcess#getActionCount()
     */
    public int getActionCount() {
        return ACTIONS;
    }

    /**
     * @see rl.MarkovDecisionProcess#reward(int, int)
     */
    public double reward(int state, int action) {
        if (state == goal) {
            return REWARD;
        } else {
            return transitionProbability(state, goal, action) * REWARD;
        }
    }

    /**
     * @see rl.MarkovDecisionProcess#transitionProbability(int, int, int)
     */
    public double transitionProbability(int i, int j, int a) {
        int startX = xFor(i), startY = yFor(i);
        int endX = xFor(j), endY = yFor(j);
        if (startX != endX && startY != endY) {
        	return 0;
        }
        int dx,dy;
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
        if (endX == startX && endY == startY) {
            if (startX + dx >= getWidth() || startX + dx < 0 ||
                    startY + dy >= getHeight() || startY + dy < 0 ||
                    isObstacle(startX + dx, startY + dy)) {
                return 1;
            } else {
                return motionFailureProbability;
            }
        } else if (endX == startX + dx && endY == startY + dy) {
            if (startX + dx >= getWidth() || startX + dx < 0 ||
                    startY + dy >= getHeight() || startY + dy < 0 ||
                    isObstacle(startX + dx, startY + dy)) {
                return 0;
            } else {
                return 1 - motionFailureProbability;
            }
        } else {
            return 0;
        }
    }

    /**
     * @see rl.MarkovDecisionProcess#sampleState(int, int)
     */
    public int sampleState(int i, int a) {
        if (Distribution.random.nextDouble() < motionFailureProbability) {
        	return i;
        }
        int nextState = -1;
        switch(a) {
        	case MOVE_UP:
        		nextState = stateFor(xFor(i), yFor(i) - 1);
        		break;
            case MOVE_DOWN:
            	nextState = stateFor(xFor(i), yFor(i) + 1);
            	break;
            case MOVE_LEFT:
            	nextState = stateFor(xFor(i) - 1, yFor(i));
            	break;
            case MOVE_RIGHT:
            	nextState = stateFor(xFor(i) + 1, yFor(i));
            	break;
            default:
            	nextState = i;
        }
        if (maze[yFor(nextState)][xFor(nextState)] == OBSTACLE) {
            nextState = i;
        }
        return nextState;
    }

    /**
     * @see rl.MarkovDecisionProcess#sampleInitialState()
     */
    public int sampleInitialState() {
        return initial;
    }

    /**
     * @see rl.MarkovDecisionProcess#isTerminalState(int)
     */
    public boolean isTerminalState(int state) {
        return state == goal;
    }
    
    /**
     * Load a maze from a text file
     * @param fileName the file to read from
     * @throws an exception when there's an error reading
     * the file
     */
    public static MazeMarkovDecisionProcess load(String fileName) throws Exception {
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
        int goalX = -1, goalY = -1;
        int initialX = -1, initialY = -1;
        for (int i = 0; i < maze.length; i++) {
            line = br.readLine();
            for (int j = 0; j < maze[i].length; j++) {
                char c = line.charAt(j);
                if (c == AGENT) {
                    initialX = j;
                    initialY = i;
                    maze[i][j] = EMPTY;
                } else if (c == GOAL) {
                    goalX = j;
                    goalY = i;
                    maze[i][j] = EMPTY;
                } else if (c == OBSTACLE) {
                    maze[i][j] = OBSTACLE;
                } else {
                    maze[i][j] = EMPTY;
                }
            }
        }
        br.close();
        return new MazeMarkovDecisionProcess(maze, goalX, goalY,
                initialX, initialY, FAILURE_PROBABILITY);
    }
    
    /**
     * Return a string representation
     * @return the string representation
     */
    public String toString() {
        String ret = "";
        int initialX = xFor(initial), initialY = yFor(initial);
        int goalX = xFor(goal), goalY = yFor(goal);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (i == initialY && j == initialX) {
                    ret += "o";
                } else if (i == goalY && j == goalX) {
                    ret += "x";
                } else {
                    ret += maze[i][j];
                }
            }
            ret += "\n";
        }
        return ret;
    }    
    

}
