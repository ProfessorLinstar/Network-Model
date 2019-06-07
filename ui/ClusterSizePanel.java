package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.AttachSitePanel.ButtonTitle;
import ui.AttachSitePanel.FieldTitle;

import networks.GeneratorWeighted;

public class ClusterSizePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8568534854969670976L;
	
	private final int displaySize;
	private final int labelWidth = 20;
	private networks.Graph graph;
	
	
	public ClusterSizePanel(networks.Graph graph, int displaySize) {
		this.displaySize = displaySize;
		setSize(displaySize, displaySize);
		setMinimumSize(new Dimension(displaySize,displaySize));
		this.graph = graph;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int x0 = labelWidth;
		int y0 = displaySize - labelWidth;
		int length = displaySize - labelWidth;
		
		g.drawLine(x0, y0, x0, y0 - length);
		g.drawLine(x0, y0, x0 + length, y0);
		
		Font bold = new Font("Arial", Font.BOLD, 12);
		drawCenteredString(g, "Cluster", (x0 + length) / 2, y0 + labelWidth/2, bold);
		drawCenteredString(g, "Largest", x0, y0 + labelWidth/2, bold);
		drawCenteredString(g, "Smallest", x0 + length, y0 + labelWidth/2, bold);
		
		
		int max = 1;
		int width = 0;

		List<Integer> clusterSizes = graph.getClusters().stream().mapToInt(c -> c.getVertices().size()).boxed().collect(Collectors.toList());
		Collections.sort(clusterSizes);
		Collections.reverse(clusterSizes);
		
		if (clusterSizes.size() > 0) {
			max = clusterSizes.stream().mapToInt(i->i).max().getAsInt();
			width = length / clusterSizes.size();
			drawCenteredString(g, Integer.toString(max), x0 - labelWidth/2, 6, bold);
		}
		
		
		int height;
		for (int i = 0; i < clusterSizes.size(); i++) {
			height = (int) ((double) clusterSizes.get(i) / max * length);
			g.drawRect(x0 +  length * i / clusterSizes.size(), y0 - height, 
					width, height);
		}
		
	}
	public void drawCenteredString(Graphics g, String text, int x, int y, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    x -= metrics.stringWidth(text) / 2;
	    y = y - metrics.getHeight() / 2 + metrics.getAscent();
	    g.setFont(font);
	    g.drawString(text, x, y);
	}
}
