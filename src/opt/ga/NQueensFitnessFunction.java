/**
 * @author kmanda1
 */
package opt.ga;

import java.util.List;

import opt.EvaluationFunction;
import shared.Instance;

public class NQueensFitnessFunction implements EvaluationFunction {

	public NQueensFitnessFunction() {
		// TODO Auto-generated constructor stub
	}
	
	private NQueensBoardGame currentBoard;

	/**
	 * the number of moves the above algorithm takes to 
	 * find the first solution
	 */
	public double value(Instance d) {
		double fitness = 0;

		NQueensBoardGame board = getBoardForGivenInstance(d);
		currentBoard = board;
		int boardSize = board.getSize();

		// Calculate the number of non-attacking pairs of queens 
		List<BoardLocation> qPositions = board.getQueenPositions();
		//
		for (int fromX = 0; fromX < (boardSize - 1); fromX++) {
			for (int toX = fromX + 1; toX < boardSize; toX++) {
				int fromY = qPositions.get(fromX).getYCoOrdinate();
				boolean nonAttackingPair = true;
				// Check right beside
				int toY = fromY;
				if (board.queenExistsAt(new BoardLocation(toX, toY))) {
					nonAttackingPair = false;
				}
				// Check right and above
				toY = fromY - (toX - fromX);
				if (toY >= 0) {
					if (board.queenExistsAt(new BoardLocation(toX, toY))) {
						nonAttackingPair = false;
					}
				}
				// Check right and below
				toY = fromY + (toX - fromX);
				if (toY < boardSize) {
					if (board.queenExistsAt(new BoardLocation(toX, toY))) {
						nonAttackingPair = false;
					}
				}

				if (nonAttackingPair) {
					fitness += 1.0;
				}
			}
		}

		//System.out.println("===============");
		//System.out.println(board.toString());
		//System.out.println("================");
		
		return fitness;
	}

	/**
	 * 
	 * @param d
	 * @return
	 */
	public NQueensBoardGame getBoardForGivenInstance(Instance d) {
		int boardSize = d.size();
		NQueensBoardGame board = new NQueensBoardGame(boardSize);
		for (int i = 0; i < boardSize; i++) {
			int pos = d.getDiscrete(i);
			board.addQueenAt(new BoardLocation(i, pos));
		}

		return board;
	}
	
	public String boardPositions(){
		
		return currentBoard.toString();
	}
}
