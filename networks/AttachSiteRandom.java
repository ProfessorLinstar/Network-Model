package networks;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import networks.ADJM.Vertex;

public class AttachSiteRandom extends Attachment{
	
	
	
	public ADJM snapshot(int t, int m, int n) {
		if (n == 0) {
			return this.getADJM();
		}
		
		int randFrom, randTo, firstNew;

		for (int i = 0; i < t; i++) {
			firstNew = getDim();
			
			for (int j = 0; j < n; j++) {
				addVertex();
			}
			for (int j = 0; j < m; j++) {
				randFrom = (int) (Math.random()*verts.size());
				randTo = (int) (Math.random()*n) + firstNew;
				verts.get(randFrom).getNeighbors().add(randTo);
				verts.get(randTo).getNeighbors().add(randFrom);
				
				clusterMod(randFrom, randTo);
			}
		}
		
		return new ADJM(verts);
	}
	
}
