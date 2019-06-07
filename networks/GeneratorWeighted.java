package networks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneratorWeighted extends Generator{
	private List<Integer> weights;
	
	public GeneratorWeighted() {
		weights = new ArrayList<>();
	}
	
	public GeneratorWeighted(List<Integer> weights) {
		boolean nonzero = false;
		for (int weight : weights) {
			if (weight != 0) {
				nonzero = true;
			}
		}
		if (!nonzero) {
			for (int i = 0; i < weights.size(); i++) {
				weights.set(i, 1);
			}
		}
		this.weights = weights;
		normalize();
	}

	
	public void incrementWeight(int index) {
		weights.set(index, weights.get(index) + 1);
		normalize();
	}
	public void addWeight(int weight) {
		weights.add(weight);
		normalize();
	}
	

	public List<Integer> getWeights() {
		return weights;
	}
	public void setWeights(List<Integer> weights) {
		this.weights = weights;
		normalize();
	}

	public double[] normalize() {
		
		if (weights.isEmpty()) {
			return new double[] {};
		}
		int sum = weights.stream().mapToInt(i->i).sum();
		g0ps = weights.stream().mapToDouble(i -> (double) i/sum).toArray();
		cumul = cumul();
		
		return g0ps;
	}
}
