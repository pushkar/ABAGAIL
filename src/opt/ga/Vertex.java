package opt.ga;

import java.util.ArrayList;
import java.util.List;

public class Vertex {

	private List<Integer> adjacencyColorMatrix = new ArrayList<Integer>(1);
	
	private int adjMatrixSize = 1;

	public void setAdjMatrixSize(int size){
		adjMatrixSize = size;
	}
	
	public List<Integer> getAadjacencyColorMatrix(){
		return adjacencyColorMatrix;
	}

}
