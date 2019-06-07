package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ErdosRenyiPanel extends JPanel{

	protected JButton randomizeButton;
	protected JButton execute;
	protected JTextField dimInput;
	protected JTextField pInput;
	
	protected JLabel dimLabel;
	protected JLabel pLabel;
	
	
	private GridBagConstraints gbc;
	/**
	 * 
	 */
	private static final long serialVersionUID = -95064456731685878L;
	
	public ErdosRenyiPanel(ActionListener listener) {
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		
		gbc.weightx = .5;
		gbc.gridwidth = 1;
		
		
		randomizeButton = new JButton("Randomize");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(randomizeButton, gbc);
		randomizeButton.addActionListener(listener);
		
		dimInput = new JTextField("0");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 1;
		this.add(dimInput, gbc);
		dimInput.addActionListener(listener);
		dimInput.setPreferredSize(new Dimension(100, 20));
		
		dimLabel = new JLabel("Dimensions: ");
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(dimLabel, gbc);
		dimLabel.setLabelFor(dimInput);
		
		pInput = new JTextField(".5");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 2;
		this.add(pInput, gbc);
		pInput.addActionListener(listener);;
		pInput.setPreferredSize(new Dimension(100, 20));
		
		pLabel = new JLabel("Edge probability: ");
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(pLabel, gbc);
		pLabel.setLabelFor(pInput);
		
		execute = new JButton("destroy");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 3;
		this.add(execute, gbc);
		execute.addActionListener(listener);
	}
}
