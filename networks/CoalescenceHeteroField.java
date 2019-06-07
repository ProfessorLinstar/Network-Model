package networks;

import java.util.stream.Collectors;

public class CoalescenceHeteroField extends Coalescence {
	public CoalescenceHeteroField(Generator.closedGen lambda, int dim, 
			double threshold, double interact) {
		super(lambda, dim, threshold, interact);
	}
	
	public ADJM snapshot(int t, int n, int m) { // n and m are dummy variables in override
		int randToV, randFromV, from, to;
		
		for (int i = 0; i < t; i++) {
			
			from = 0;
			while (from < clustersX.size()) {
				to = 0;
				while (to < from) {
					if (Math.random() < interact) {
						if (clustersX.get(from).interact(clustersX.get(to))) {
							for (int k = -1; k < Math.pow(interact, 2) * 
									clustersX.get(from).getVertices().size() * 
									clustersX.get(to).getVertices().size(); 
									k++) {
								randToV = clustersX.get(to).getRandVert().getLabel();
								randFromV = clustersX.get(from).getRandVert().getLabel();
								
								vertsX.get(randToV).getNeighbors().add(randFromV);
								vertsX.get(randFromV).getNeighbors().add(randToV);
							}
							clustersX.get(to).getVertices().addAll(clustersX.remove(from).getVertices());
							clustersX.get(to).updateCharacter();
							from--;
						}
					}
					to++; 
				}
				from++;
			}
			
		}
		verts = vertsX.stream().map(vX -> vX.toVertex()).collect(Collectors.toList());
		clusters = clustersOf(new ADJM(verts));
		return new ADJM(verts);
	}
	
	
}
