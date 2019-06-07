package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JPanel;

class DisplayPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3905268398981157079L;
	
	private final int nodeSize = 6;
	private final int displaySize;
	private final int nodeOrbit;
	private networks.Graph graph;
	
	public DisplayPanel(networks.Graph graph, int displaySize) {
		this.displaySize = displaySize;
		nodeOrbit = displaySize - nodeSize;
		this.graph = graph;
		
		
		setSize(displaySize, displaySize);
		setMinimumSize(new Dimension(displaySize, displaySize));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.RED);
		
		for (int i = 0; i < graph.getDim(); i++) {;
			for (int j = 0; j < graph.getDim(); j++) {
				if (graph.getADJM().getAdjmArray()[i][j] > 0) {
					g.drawLine(xCoord(i), yCoord(i), xCoord(j), yCoord(j));
				}
			}
			g.fillOval(xCoord(i) - nodeSize/2, yCoord(i) - nodeSize/2, nodeSize, nodeSize);
		}
		
//		g.drawOval(getWidth()/2 - displaySize/2, getHeight()/2 - displaySize/2, 
//				displaySize, displaySize);
		g.drawOval(getWidth()/2 - displaySize/2 + nodeSize/2, 
				getHeight()/2 - displaySize/2 + nodeSize/2, 
				nodeOrbit, nodeOrbit);
	}
	
	private int xCoord(int index) {
		return (int) (Math.cos((double) index/graph.getDim() * 2*Math.PI) * (double) nodeOrbit/2 + getWidth()/2);
	}
	private int yCoord(int index) {
		return (int) (Math.sin((double) index/graph.getDim() * 2*Math.PI) * (double) nodeOrbit/2 + getHeight()/2);
	}
}
