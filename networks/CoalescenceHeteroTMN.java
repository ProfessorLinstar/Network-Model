package networks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import networks.ADJM.Vertex;
import networks.Graph.Cluster;

public class CoalescenceHeteroTMN extends Coalescence{
	
	public CoalescenceHeteroTMN(Generator.closedGen lambda, int dim, double threshold, double interact) {
		super(lambda, dim, threshold, interact);
	}
	
	public ADJM snapshot(int t, int m, int n) { // n coagulation number
		int randToC, randFromC, randToV, randFromV;
		
		for (int i = 0; i < t; i++) {
			
			for (int j = 0; j < m; j++) {
				
				randToC = (int) (Math.random() * clustersX.size());
				randFromC = (int) (Math.random() * clustersX.size());
				
				if (clustersX.get(randFromC).interact(clustersX.get(randToC))) {
					for (int k = 0; k < n; k++) {
						randToV = clustersX.get(randToC).getRandVert().getLabel();
						randFromV = clustersX.get(randFromC).getRandVert().getLabel();

						vertsX.get(randToV).getNeighbors().add(randFromV);
						vertsX.get(randFromV).getNeighbors().add(randToV);

						
					}
					if (randToC > randFromC) {
						clustersX.get(randFromC).getVertices().addAll(clustersX.remove(randToC).getVertices());
						clustersX.get(randFromC).updateCharacter();
					} else {
						clustersX.get(randToC).getVertices().addAll(clustersX.remove(randFromC).getVertices());
						clustersX.get(randToC).updateCharacter();
					}
					
				}
			}
		}
		verts = vertsX.stream().map(vX -> vX.toVertex()).collect(Collectors.toList());
		clusters = clustersOf(new ADJM(verts));
		return new ADJM(verts);
	}
	
	
}
