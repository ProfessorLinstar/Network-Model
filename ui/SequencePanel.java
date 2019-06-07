package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.AttachSitePanel.ButtonTitle;
import ui.AttachSitePanel.FieldTitle;

public class SequencePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8568534854969670976L;

	private final int nodeSize = 6;
	private final int displaySize;
	private final int labelWidth = 20;
	private final int padding = 3;
	private List<networks.ADJM> sequence;
	
	
	public SequencePanel(int displaySize, List<networks.ADJM> sequence) {
		this.displaySize = displaySize;
		setSize(displaySize, displaySize);
		setMinimumSize(new Dimension(displaySize,displaySize));
		this.sequence = sequence;
	}
	public SequencePanel(int displaySize, networks.ADJM ... sequence) {
		this.displaySize = displaySize;
		setSize(displaySize, displaySize);
		setMinimumSize(new Dimension(displaySize,displaySize));
		this.sequence = new ArrayList<>(Arrays.asList(sequence));
	}
	
	public void updateSequence(List<networks.ADJM> sequence) {
		this.sequence = sequence;
		repaint();
	}
	public void updateSequence(networks.ADJM ... sequence) {
		this.sequence = new ArrayList<>(Arrays.asList(sequence));
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int x0 = labelWidth + padding;
		int y0 = displaySize - labelWidth + padding;
		int length = displaySize - labelWidth - padding;
		
		g.drawLine(x0, y0, x0, y0 - length);
		g.drawLine(x0, y0, x0 + length, y0);
		
		Font bold = new Font("Arial", Font.BOLD, 12);
		drawCenteredString(g, "Time", (x0 + length) / 2, y0 + labelWidth/2, bold);
		drawCenteredString(g, "0", x0, y0 + labelWidth/2, bold);
		
		
		double[] yVals = sequence.stream().mapToDouble(adjm -> networks.Graph.gcOf(adjm)).toArray();
		if (yVals.length > 0) {
			double max = Arrays.stream(yVals).max().getAsDouble();
			if (max > 0) {
				drawCenteredString(g, Integer.toString(yVals.length), x0 + length, y0 + labelWidth/2, bold);
				drawCenteredString(g, Integer.toString((int) max), x0 - labelWidth/2, 6, bold);
				
				double xa, ya, xb, yb;
				xb = x0; yb = yVals[0] / max * length;
				xa = xb; ya = yb;
				g.fillOval((int) (xb - nodeSize/2),(int) (y0 - yb - nodeSize/2), nodeSize, nodeSize);
				for (int i = 1; i < yVals.length; i++) {
					xb += (double) length / yVals.length; yb = yVals[i] / max * length;
					g.fillOval((int) (xb - nodeSize/2), (int) (y0 - yb - nodeSize/2), nodeSize, nodeSize);
					g.drawLine((int) xb, (int) (y0 - yb), (int) xa, (int) (y0 - ya));
					xa = xb; ya = yb;
				}
			}
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
