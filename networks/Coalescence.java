package networks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import networks.Graph.Cluster;

public class Coalescence extends Attachment{
	protected List<ClusterX> clustersX;
	protected List<VerteX> vertsX;
	private Generator.closedGen lambda;
	private Generator characterGen;
	private double[] kVals;
	protected double threshold;
	protected double interact;
	protected int dim;
	
	public Coalescence(Generator.closedGen lambda, int dim, double threshold, double interact) {
		reconstruct(lambda, dim, threshold, interact);
	}
	
	public void reconstruct(Generator.closedGen lambda, int dim, double threshold, double interact) {
		this.interact = interact;
		this.threshold = threshold;
		this.dim = dim;
		this.lambda = lambda;
		reset();
	}
	
	public void homogenize() {
		for (VerteX vertX : vertsX) {
			vertX.setCharacter(1);
		}
	}
	
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	public void setInteract(double interact) {
		this.interact = interact;
	}
	public void setDim(int dim) {
		this.dim = dim;
		reset();
	}
	public void setADJM(ADJM adjm) {
		super.setADJM(adjm);
		clustersUpdate();
		if (lambda == null) {
			kVals = new double[] {0,0};
			characterGen = new Generator();
		} else {
			kVals = IntStream.range(0, dim).mapToDouble(i -> (double) i / dim).toArray();
			characterGen = new Generator(lambda, kVals);
			characterGen.normalize();
		}
		
		vertsX = new ArrayList<>();
		int k = 0;
		double[] cdf = characterGen.cumul();
		for (int i = 0; i < dim; i++) {
			if ((double) i / dim >= cdf[k]) {
				k++;
			}
			vertsX.add(new VerteX(i, verts.get(i).getNeighbors(), 
					randomDoubleRange(kVals[k],kVals[k+1])));
//			System.out.println("latest character:"+vertsX.get(vertsX.size()-1).getCharacter());
		}
		clustersX = new ArrayList<>();
		for (Cluster cluster : clusters) {
			clustersX.add(new ClusterX(cluster.getVertices().stream().map(
					v -> vertsX.get(v.getLabel())).collect(Collectors.toList())));
		}
		
		
	}
	public void reset() {
		setADJM(new ADJM(dim));
	}
	
	protected class VerteX extends ADJM.Vertex {
		private double character;
		
		VerteX(int label, double character) {
			super(label, new ArrayList<>());
			this.character = character;
		}
		VerteX(int label, List<Integer> neighbors, double character) {
			super(label, neighbors);
			this.character = character;
		}
		public boolean interact(VerteX vert) {
			if (vert == this) {
				return false;
			}
			double similarity = 1 - Math.abs(character - vert.getCharacter());
			if (Math.random() < similarity * threshold) {
				return true;
			}
			return false;
		}
		
		public double getCharacter() { return character; }
		public void setCharacter(double character) {
			this.character = character;
		}
		public ADJM.Vertex toVertex() {
			return new ADJM.Vertex(getLabel(), getNeighbors());
		}
	}
	protected class ClusterX {
		List<VerteX> vertices;
		double character;
		
		ClusterX(List<VerteX> vertices) {
			this.vertices = vertices;
			updateCharacter();
		}
		ClusterX(VerteX ... vertices) {
			this.vertices = new ArrayList<>(Arrays.asList(vertices));
			updateCharacter();
		}
		public boolean interact(ClusterX cluster) {
			if (cluster == this) {
				return false;
			}
			double similarity = 1 - Math.abs(character - cluster.getCharacter());
			if (Math.random() < similarity * threshold) {
				return true;
			}
			return false;
		}
		public void updateCharacter() {
			character = this.vertices.stream().mapToDouble(v -> v.getCharacter()).sum() / this.vertices.size();
		}
		public double getCharacter() {
			return character;
		}
		public List<VerteX> getVertices() {
			return vertices;
		}
		public VerteX getRandVert() {
			return vertices.get((int)(Math.random() * vertices.size()));
		}
		
		
	}
	public List<ClusterX> getClustersX() {
		return clustersX;
	}
	
	private double randomDoubleRange(double min, double max) {
		return Math.random() * (max - min) + min;
	}
}
