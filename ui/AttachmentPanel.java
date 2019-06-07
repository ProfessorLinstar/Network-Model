package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AttachmentPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4912835286763565414L;

	enum FieldTitle {
		ATTACHEDGENUM("m-number"), ATTACHVERTNUM("n-number"), STEP("Step"), 
		DIM("Dim (BH)"), THRESHOLD("Threshold (BH)"), INTERACT("Interact (BH)");
		private String title;
		private FieldTitle(String title) { this.title = title; }
		public String getTitle() { return title; }
	}
	enum ButtonTitle {
		RESET("Reset"), GOSTEP("Go <Step> times"), GO("Go"), STOP("Stop"), SEQUENCE("Sequence");
		private String title;
		private ButtonTitle(String title) { this.title = title; }
		public String getTitle() { return title; }
	}
	
	protected Map<FieldTitle, JTextField> fieldMap = new HashMap<>();
	protected Map<ButtonTitle, JButton> buttonMap = new HashMap<>();

	protected JLabel timeDisplay;
	protected JLabel timeLabel;
	protected JLabel nodeNumDisplay;
	protected JLabel nodeNumLabel;
	
	
	
	public AttachmentPanel(ActionListener listener) {
		setLayout(new GridBagLayout());
		
		int buttonNum = ButtonTitle.values().length;
		int fieldNum = FieldTitle.values().length;
		
		for (int i = 0; i < fieldNum; i++) {
			FieldTitle fieldTitle = FieldTitle.values()[i];
			add(new JLabel(fieldTitle.getTitle()+":"), createGbc(0, i));
			
			JTextField textField = new JTextField("1");
			add(textField, createGbc(1, i));
			textField.addActionListener(listener);
			
			fieldMap.put(fieldTitle, textField);
		}
		for (int i = 0; i < buttonNum; i++) {
			ButtonTitle buttonTitle = ButtonTitle.values()[i];
			JButton button = new JButton(buttonTitle.getTitle());
			add(button, createGbc(1, i + fieldNum));
			button.addActionListener(listener);
			
			buttonMap.put(buttonTitle, button);
		}

		timeDisplay = new JLabel("0");
		add(timeDisplay, createGbc(1, fieldNum + buttonNum));
		timeLabel = new JLabel("Time: ");
		add(timeLabel, createGbc(0, fieldNum + buttonNum));
		nodeNumDisplay = new JLabel("0");
		add(nodeNumDisplay, createGbc(1, fieldNum + buttonNum + 1));
		nodeNumLabel = new JLabel("Node number: ");
		add(nodeNumLabel, createGbc(0, fieldNum + buttonNum + 1));
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
