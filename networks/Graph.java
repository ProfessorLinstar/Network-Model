package networks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import networks.ADJM.Vertex;
import networks.Graph.Cluster;

import java.util.ListIterator;


public class Graph {
	private int dim;
	private ADJM adjm;
	Generator gen = new Generator();
	protected List<Cluster> clusters = new ArrayList<>();
	

	public void shallowCopy(Graph graph) {
		setADJM(graph.getADJM());	
		setClusters(graph.getClusters());
	}
	
	public class Cluster {
		List<Vertex> vertices;
		Cluster(List<Vertex> vertices) {
			this.vertices = new ArrayList<>(vertices);
		}
		Cluster(Vertex ... vertices) {
			this.vertices = new ArrayList<>(Arrays.asList(vertices));
		}
		public List<Vertex> getVertices() {
			return vertices;
		}
		public void addVertex(Vertex vertex) {
			vertices.add(vertex);
		}
	}
	public static List<Cluster> clustersOf(ADJM adjm) {
		Graph graph = new Graph(adjm);
		graph.clustersUpdate();
		return graph.getClusters();
	}
	public static int gcOf(ADJM adjm) {
		List<Cluster> clusters = clustersOf(adjm);
		if (clusters.isEmpty()) { return 0; }
		return clusters.stream().mapToInt(c -> c.getVertices().size()).max().getAsInt();
	}
	public void clustersUpdate() {
		clusters.clear();
		List<Integer> inClusters;
		List<Vertex> combineTo;
		for (Vertex vertex : adjm.getVertices()) {
			inClusters = new ArrayList<>();
			for (int i = 0; i < clusters.size(); i++) {
				if (clusters.get(i).getVertices().stream().anyMatch(vert -> 
				vert.getNeighbors().stream().anyMatch(n -> n == vertex.getLabel()))) {
					inClusters.add(i);
				}
			}
			if (inClusters.size() > 0) {
				combineTo = clusters.get(inClusters.remove(0)).getVertices();
				Collections.reverse(inClusters);
				for (int combine : inClusters) {
					combineTo.addAll(clusters.remove(combine).getVertices());
				}
				combineTo.add(vertex);
			} else {
				clusters.add(new Cluster(new ArrayList<>(Arrays.asList(vertex))));
			}
		}
	}
	public List<Cluster> getClusters() {
		return clusters;
	}
	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}
	
	public Graph() {
		setDim(0);
		adjm = new ADJM(0);
	}
	
	public Graph(int dim) {
		this.setDim(dim);
		adjm = new ADJM(dim);
	}
	
	public Graph(ADJM adjm) {
		this.setADJM(adjm);
		clustersUpdate();
	}
	
	public static Graph erdosRenyi(int dim, double p) {
		ADJM adjm = new ADJM(dim);
		
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < i+1; j++) {
				if (Math.random() < p) {
					adjm.getAdjmArray()[i][j]++;
					adjm.getAdjmArray()[j][i]++;
				}
			}
		}
		
		
		Graph graph = new Graph(adjm);
		return graph;
	}
	
	
	public static Graph configModel(int dim, Generator gen) {
		
		List<Integer> stubs = new ArrayList<>();
		List<Integer> nodes = IntStream.range(0, dim).boxed().collect(Collectors.toList());
		Collections.shuffle(nodes, new Random());
		
		int k = 0;
		double[] cdf = gen.cumul();
		for (int i = 0; i < dim; i++) {
			if ((float) i / dim > cdf[k]) {
				k++;
			}
			for (int j = 0; j < k; j++) {
				stubs.add(nodes.get(i));
			}
		}
		Collections.shuffle(stubs, new Random());

		ListIterator<Integer> it = stubs.listIterator();
		List<Edge> edges = new ArrayList<>();
		Integer a = it.next();
		Integer b;
		while (it.hasNext()) {
			b = a;
			a = it.next();
			
			edges.add(new Edge(a,b));
		}
		
		ADJM adjm = new ADJM(dim, edges, false);
		Graph graph = new Graph(adjm);
		return graph;
	}

	public void erdosRenyi(double p) {
		adjm = erdosRenyi(dim, p).getADJM();
	}
	public void configModel(Generator gen) {
		adjm = configModel(dim, gen).getADJM();
	}
	
	public double[] g0ps() {
		List<Integer> degreeDist = new ArrayList<>();
		List<Vertex> verts = adjm.getVertices();
		
		for (Vertex vert : verts) {
			while (degreeDist.size() < vert.getDegree()+1) {
				degreeDist.add(0);
			}
			degreeDist.set(vert.getDegree(), degreeDist.get(vert.getDegree()) + 1);
		}
		
		double[] g0ps = degreeDist.stream().mapToDouble(i -> (double) i / dim).toArray();
		
		
		gen.setg0ps(g0ps);
		return g0ps;
	}
	
	public ADJM getADJM() {
		return adjm;
	}

	public void setADJM(ADJM adjm) {
		this.dim = adjm.getAdjmArray().length;
		this.adjm = adjm;
	}

	public int getDim() {
		return dim;
	}

	public void setDim(int dim) {
		this.dim = dim;
		adjm = new ADJM(dim);
	}
	
	

	public String latexPrint(double d, double e) {
		if (dim == 0) {
			return "";
		}
		
		StringBuilder adjmStringBuilder = new StringBuilder();
		adjmStringBuilder.append("\n");
		for (int[] row : getADJM().getAdjmArray()) {
			adjmStringBuilder.append(Arrays.toString(row).replace(",", " &").substring(1, (getDim())*4-2)+" & \n");
		}
		String adjmString = adjmStringBuilder.substring(0, adjmStringBuilder.length() - 4).toString();
		
		
		String formatted = String.format(
"\\begin{tikzpicture}\r\n" + 
"	 \r\n" + 
"	 \r\n" + 
"	 \r\n" + 
"	 \r\n" + 
"	 \\newcounter{connect}\r\n" + 
"	 \\newcounter{from}\r\n" + 
"	 \\newcounter{to}\r\n" + 
"	 \\newcounter{vert}\r\n" + 
"	 \r\n" + 
"	 \r\n" + 
"	 \\pgfmathsetmacro{\\radius}{%1s}\r\n" + 
"	 \\pgfmathsetmacro{\\vertCircle}{%2s}\r\n" + 
"    \\pgfmathsetmacro{\\dim}{%3s}\r\n" +
"	 \r\n" + 
"	 \\newarray{adjm}\r\n" + 
"	 \\readarray{adjm}{%4s}\r\n" + 
"	 \\dataheight=\\dim\r\n" + 
"	 \r\n" + 
"    \\forloop{from}{1}{\\value{from}<\\dim+1}{\r\n" + 
"        \\forloop{to}{1}{\\value{to}<\\dim+1}{\r\n" + 
"            \\checkadjm(\\arabic{from},\\arabic{to})\r\n" + 
"            \\setcounter{connect}{\\cachedata}\r\n" + 
"            \\ifnum\\value{connect}>0\r\n" +
"                \\draw (360/\\dim*\\arabic{from}:\\radius)--(360/\\dim*\\arabic{to}:\\radius);\r\n" + 
"            \\fi\r\n" + 
"        }\r\n" + 
"    }\r\n" + 
"    \\forloop{vert}{0}{\\value{vert}<\\dim}{\r\n" + 
"        \\draw[fill=green!25] (360/\\dim*\\arabic{vert}:\\radius) circle (\\vertCircle);\r\n" + 
"        \\node at (360/\\dim*\\arabic{vert}:\\radius) {\\the\\numexpr\\arabic{vert}+1\\relax};\r\n" + 
"    }\r\n" + 
"\\end{tikzpicture}",e,d,getDim(),adjmString);
		
		return formatted;
		
		
		
	}

}
