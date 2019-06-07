package networks;




public class Networks {
	public static void main(String args[]) {
		
		int dim = 10;
		ADJM adjm = new ADJM(dim);
		Graph graph_from_adjm = new Graph(adjm);
		String print = graph_from_adjm.latexPrint((float) .2, (float) 3);
//		System.out.println(print);
		
		
		Generator gen = new Generator(k -> Math.pow(2, -k-1),100);
		Graph graph_from_gen = Graph.configModel(20, gen);
		
		double p = .1;
		Graph graph_ER = Graph.erdosRenyi(dim, p);
		
		AttachSiteRandom attach = new AttachSiteRandom();
		ADJM snap = attach.snapshot(20, 4, 1);
		System.out.println(new Graph(snap).latexPrint((float) .2, 3));
		
		
		
		
		
	}
}
