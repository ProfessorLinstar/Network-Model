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

public class AttachSitePanel extends AttachmentPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4912835286763565414L;

	enum FieldTitle {
		ATTACHEDGENUM("m-number"), ATTACHVERTNUM("n-number"), STEP("Step");
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
	
	public AttachSitePanel(ActionListener listener) {
		super(listener);
	}
}
