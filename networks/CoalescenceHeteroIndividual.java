package networks;

import java.util.stream.Collectors;

public class CoalescenceHeteroIndividual extends Coalescence {
	public CoalescenceHeteroIndividual(Generator.closedGen lambda, int dim, 
			double threshold, double interact) {
		super(lambda, dim, threshold, interact);
	}
	
	public ADJM snapshot(int t, int n, int m) { // n and m are dummy variables in override
		for (int i = 0; i < t; i++) {
			
			for (int from = 0; from < vertsX.size(); from++) {
				for (int to = 0; to < from; to++) {
					if (Math.random() < interact) {
						if (vertsX.get(from).interact(vertsX.get(to))) {
							vertsX.get(from).getNeighbors().add(to);
							vertsX.get(to).getNeighbors().add(from);
							verts.get(from).getNeighbors().add(to);
							verts.get(to).getNeighbors().add(from);
							
							clusterMod(from, to);
						}
					}
				}
			}
			
		}
		return new ADJM(verts);
	}
	
	
}
