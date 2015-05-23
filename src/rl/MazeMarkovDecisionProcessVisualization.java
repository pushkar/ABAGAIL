package rl;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A class for visualizing a maze markov decision process
 * @author guillory
 * @version 1.0
 */
public class MazeMarkovDecisionProcessVisualization {
    /** How many pixels each square in the maze should be */
    private int RESOLUTION = 20;
    
    /**
     * The maze that is being visualized
     */
    private MazeMarkovDecisionProcess mmdp;
    
    /**
     * Make a new maze markov decision process
     * @param mmdp the maze to visualize
     */
    public MazeMarkovDecisionProcessVisualization(MazeMarkovDecisionProcess mmdp) {
        this.mmdp = mmdp;
    }
    
    /**
     * Get the width of the maze visualization
     * @return the width (in pixels)
     */
    public int getWidth() {
        return mmdp.getWidth() * RESOLUTION;
    }
    
    /**
     * Get the height of the maze visualization
     * @return the height (in pixels) 
     */
    public int getHeight() {
        return mmdp.getHeight() * RESOLUTION;
    }
    
    /**
     * Draw the maze onto this graphics object
     * @param g the graphics to draw on
     */
    public void drawMaze(Graphics g) {
        g.setColor(Color.BLACK);
        for (int x = 0; x < getWidth(); x += RESOLUTION) {
            for (int y = 0; y < getWidth(); y += RESOLUTION) {
                if (mmdp.isObstacle(x, y)) {
                    g.fillRect(x, y, x + RESOLUTION, y + RESOLUTION);
                } else {
                    g.drawRect(x, y, x + RESOLUTION, y + RESOLUTION);
                }
            }
        }
    }
    
    /**
     * Get a string visualization of the maze with a policy
     * @param p the policy
     * @return the string
     */
    public String toString(Policy p) {
        String ret = "";
        for (int y = 0; y < mmdp.getHeight(); y++) {
            for (int x = 0; x < mmdp.getWidth(); x++) {
                if (mmdp.isObstacle(x, y)) {
                    ret += MazeMarkovDecisionProcess.OBSTACLE;
                } else if (mmdp.isTerminalState(mmdp.stateFor(x, y))) {
                	ret += MazeMarkovDecisionProcess.GOAL;
                } else {
                    int a = p.getAction(mmdp.stateFor(x,y));
                    switch(a) {
                    	case MazeMarkovDecisionProcess.MOVE_DOWN:
                    	    ret += 'V';
                    		break;
                    	case MazeMarkovDecisionProcess.MOVE_UP:
                    	    ret += '^';
                    		break;
                    	case MazeMarkovDecisionProcess.MOVE_LEFT:
                    	    ret += '<';
                    		break;
                    	case MazeMarkovDecisionProcess.MOVE_RIGHT:
                    	    ret += '>';
                    		break;                    
                    }
                }
            }
            ret += "\n";
        }
        return ret;
    }
    
    public String toString() {
        return mmdp.toString();
    }

}
