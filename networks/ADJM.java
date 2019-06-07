package networks;


import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;


public class ADJM {
	private int[][] adjmArray;
	int dim;
	
	public static class Vertex {
		private int label;
		private int degree;
		private List<Integer> neighbors;
		
		Vertex(int label, List<Integer> neighbors) {
			this.label = label;
			this.neighbors = new ArrayList<>(neighbors.size());
			for (Integer i: neighbors) {
				this.neighbors.add(i);
			}
			degree = neighbors.size();
		}
		
		public int getLabel() {
			return label;
		}
		public int getDegree() {
			return degree;
		}
		
		public List<Integer> getNeighbors() {
			return neighbors;
		}
		public void setNeighbors(List<Integer> neighbors) {
			this.neighbors = neighbors;
		}
	}
	public ADJM() {
		dim = 0;
		adjmArray = new int[0][0];
	}
	
	public ADJM(int[][] adjm) {
		this.dim = adjm.length;
		
		for (int[] array : adjm) {
			if (array.length != dim) {
				System.out.println("Error: inconsistent dimensions");
			}
		}
		this.setAdjmArray(adjm);
	}
	public ADJM(int dim) {
		this.dim = dim;
		this.setAdjmArray(new int[dim][dim]);
	}
	public ADJM(int dim, List<Edge> edges, boolean directed) {
		this.dim = dim;
		this.setAdjmArray(new int[dim][dim]);
		for (Edge edge: edges) {
			getAdjmArray()[edge.x][edge.y] ++;
		}
		if (!directed) {
			for (Edge edge: edges) {
				if (edge.y != edge.x) {
					getAdjmArray()[edge.y][edge.x]++;
				}
			}
		}
	}
	
	public ADJM(List<Vertex> vertices) {
		dim = vertices.size();
		setAdjmArray(new int[dim][dim]);
		
		for (Vertex vertex : vertices) {
			for (int neighbor : vertex.neighbors) {
				getAdjmArray()[vertex.label][neighbor]++;
			}
		}
	}
	
	public int[][] multiplyM(int[][] factor) {
		int width = factor.length;
		int[][] multiplied = new int[dim][width];
		
		
		for (int[] array : factor) {
			if (array.length != dim) {
				System.out.println("Error: inconsistent dimensions");
				return multiplied;
			}
		}
		
		
		
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < width; j++) {
				for (int k = 0; k < dim; k++) {
					multiplied[i][j] += getAdjmArray()[i][k] * factor[k][j];
				}
			}
		}
		
		
		return multiplied;
	}
	
	
	public int degree(int v) {
		int deg = 0;
		for (int i : getAdjmArray()[v]) {
			deg += i;
		}
		return deg;
	}
	public int[] neighborsout(int v) {
		List<Integer> neighborhood = new ArrayList<>();
		
		for (int i = 0; i < dim; i++) {
			if (getAdjmArray()[v][i] > 0) {
				neighborhood.add(i);
			}
		}
		
		return neighborhood.stream().mapToInt(i->i).toArray();
	}
	public int[] neighborsin(int v) {
		List<Integer> neighborhood = new ArrayList<>();
		
		for (int i = 0; i < dim; i++) {
			if (getAdjmArray()[i][v] > 0) {
				neighborhood.add(i);
			}
		}
		
		return neighborhood.stream().mapToInt(i->i).toArray();
	}
	
	
	public List<Vertex> getVertices() {
		List<Vertex> vertices = new ArrayList<>();
		List<Integer> neighbors = new ArrayList<>();
		int[][] adjmArray = getAdjmArray();
		for (int i = 0; i < dim; i++) {
			neighbors.clear();
			for (int j = 0; j < dim; j++) {
				for (int k = 0; k < adjmArray[i][j]; k++) {
					neighbors.add(j);
				}
			}
			vertices.add(new Vertex(i, neighbors));
		}
		
		
		return vertices;
		
	}
	
	public float degreeAverage() {
		int sum = 0;
		for (int[] row : getAdjmArray()) {
			for (int degree : row) {
				sum += degree;
			}
		}
		
		return (float) sum / dim;
	}
	
	
	
	
	public String toString() {
		if (dim == 0) {
			return "[]";
		}
		StringBuilder arrayString = new StringBuilder();
		for (int[] array : getAdjmArray()) {
			arrayString.append(Arrays.toString(array)+"\n");
		}
		return arrayString.toString();
	}
	
	public String stringhtml() {
		if (dim == 0) {
			return "[]";
		}
		StringBuilder arrayString = new StringBuilder("<html>");
		for (int[] array : getAdjmArray()) {
			arrayString.append(Arrays.toString(array)+"<br>");
		}
		arrayString.append("</html>");
		return arrayString.toString();
	}
	public int[][] getAdjmArray() {
		return adjmArray;
	}
	public void setAdjmArray(int[][] adjm) {
		this.adjmArray = adjm;
		this.dim = adjm.length;
	}
}
