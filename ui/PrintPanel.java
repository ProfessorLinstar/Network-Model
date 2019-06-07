package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PrintPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6042425136916683619L;

	protected JButton printGraphButton = new JButton("Print TikzPicture in Console");
	
	protected JTextField vertexSizeField = new JTextField(".2");
	protected JTextField circleRadField = new JTextField("5");
	
	private JLabel vertexSizeLabel = new JLabel("Vertex size");
	private JLabel circleRadLabel = new JLabel("Circle radius");
	
	protected double vertexSize = .2;
	protected double circleRad = 6;
	
	
	
	public PrintPanel(ActionListener listener) {
		super(new GridBagLayout());
		add(vertexSizeField, createGbc(0, 0));
		add(printGraphButton, createGbc(1, 0));
		add(circleRadField, createGbc(2, 0));
		add(vertexSizeLabel, createGbc(0, 1));
		add(circleRadLabel, createGbc(2, 1));
		
		printGraphButton.addActionListener(listener);
		vertexSizeField.addActionListener(listener);
		circleRadField.addActionListener(listener);
		
	}
	
	private GridBagConstraints createGbc(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = .5;
		return gbc;
	}
}
