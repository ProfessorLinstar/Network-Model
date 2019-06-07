package networks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import networks.ADJM.Vertex;
import networks.Graph.Cluster;

public class Attachment extends Graph{
	protected List<Vertex> verts;
	
	public Attachment() {
		super(1);
		verts = getADJM().getVertices();
		clusters = new ArrayList<>(Arrays.asList(new Cluster(verts)));
	}
	
	public void setADJM(ADJM adjm) {
		super.setADJM(adjm);
		verts = getADJM().getVertices();
	}
	
	public void reset() {
		setADJM(new ADJM(1));
		verts = getADJM().getVertices();
		clusters = new ArrayList<>(Arrays.asList(new Cluster(verts)));
	}
	
	
	public void clusterMod(int vert1, int vert2) {
		int cluster1 = -1, cluster2 = -1;
		Cluster cluster;
		for (int i = 0; i < clusters.size(); i++) {
			cluster = clusters.get(i);
			if (cluster.getVertices().stream().anyMatch(v -> v.getLabel() == vert1)) {
				cluster1 = i;
			}
			if (cluster.getVertices().stream().anyMatch(v -> v.getLabel() == vert2)) {
				cluster2 = i;
			}
			if (cluster1 != -1 && cluster2 != -1) {
				if (cluster1 == cluster2) {
					return;
				} else {
					clusters.get(cluster2).getVertices().addAll(clusters.remove(cluster1).getVertices());
					return;
				}
			}
		}
		System.out.println("clusters needs to be restarted:");
		for (Cluster cluster_ : clusters) {
			System.out.println(cluster_.getVertices().stream().mapToInt(v -> v.getLabel()).boxed().collect(Collectors.toList()));
		}
		updateVertsToADJM();
		super.clustersUpdate();
		clusterMod(vert1, vert2);
	}
	
	public void addVertex() {
		Vertex vert = new Vertex(getDim(), new ArrayList<>());
		verts.add(vert);
		clusters.add(new Cluster(new ArrayList<>(Arrays.asList(vert))));
		setDim(getDim() + 1);
	}
	public void updateVertsToADJM() {
		 setADJM(new ADJM(verts));
	}


	public ADJM snapshot(int t, int m, int n) { return getADJM(); }
	
	public void movingStep(int t, int m, int n) {
		this.setADJM(snapshot(t, m, n));
	}
	public List<ADJM> sequence(int t, int m, int n) {
		List<ADJM> adjms = new ArrayList<>();
		adjms.add(getADJM());
		for (int i = 0; i < t; i++) {
			movingStep(1, m, n);
			adjms.add(this.getADJM());
		}
		this.setADJM(adjms.get(0));
		
		
		return adjms;
	}
	
}
