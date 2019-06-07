package networks;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.*;

import networks.ADJM.Vertex;
import networks.Graph.Cluster;

/**
 * 
 * @author awaw6
 *
 */
public class AttachSiteDegree extends Attachment {
	
	
	/**
	 * 
	 * @param t
	 * @param m
	 * @return
	 */
	public ADJM snapshot(int t, int m, int n) {
		if (n == 0) {
			return this.getADJM();
		}
		
		int randTo, randFrom, firstNew;
		GeneratorWeighted vertGen = new GeneratorWeighted(IntStream.range(0, getDim()).map(i -> verts.get(i).getDegree()).boxed().collect(Collectors.toList()));

		for (int i = 0; i < t; i++) {
			firstNew = getDim();
			
			for (int j = 0; j < n; j++) {
				addVertex();
				vertGen.addWeight(0);
			}
			for (int j = 0; j < m; j++) {
//				System.out.println("vertGen cumul:"+Arrays.toString(vertGen.cumul()));
//				System.out.println("vertGen weights:"+vertGen.getWeights());
//				System.out.println("vertGen g0ps:"+Arrays.toString(vertGen.getg0ps()));
				
				randTo = vertGen.randomWeighted();
				randFrom = (int) (Math.random()*n) + firstNew;
				verts.get(randTo).getNeighbors().add(randFrom);
				verts.get(randFrom).getNeighbors().add(randTo);
				
				clusterMod(randFrom, randTo);
				vertGen.incrementWeight(randTo);
				vertGen.incrementWeight(randFrom);
				
			}
		}
		
		return new ADJM(verts);
	}
	
	
}
