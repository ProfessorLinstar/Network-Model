package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

import networks.AttachSiteRandom;
import networks.Graph;
import networks.Networks;
import networks.Graph.Cluster;
import ui.AttachmentPanel.ButtonTitle;
import ui.AttachmentPanel.FieldTitle;

public class Gui extends JFrame implements ActionListener {

	
	
	
	private static final long serialVersionUID = 478465345018090817L;

	private double p = .5;
	private int time = 0;
	private int m = 1;
	private int n = 1;
	private int step = 1;
	private int dimBH = 10;
	private double threshold = 1;
	private double interact = .5;
	private networks.Generator.closedGen lambda = k -> 1;
	
	private networks.Graph graph = new networks.Graph();
	private networks.AttachSiteRandom graphAttachRandomSite = new networks.AttachSiteRandom();
	private networks.AttachSiteDegree graphAttachDegreeSite = new networks.AttachSiteDegree();
	private networks.CoalescenceHeteroTMN graphAttachBondHeteroM = new networks.CoalescenceHeteroTMN(
			lambda, dimBH, threshold, interact);
	private networks.CoalescenceHeteroField graphAttachBondHeteroF = new networks.CoalescenceHeteroField(
			lambda, dimBH, threshold, interact);
	private networks.CoalescenceHeteroIndividual graphAttachBondHeteroI = new networks.CoalescenceHeteroIndividual(
			lambda, dimBH, threshold, interact);
	private networks.CoalescenceHeteroIndividual graphAttachBondHomoI = new networks.CoalescenceHeteroIndividual(
			null, dimBH, threshold, interact);
	
	private int maxGraphSize = 10;
	private int displaySize = 400;

	
	
	private JPanel contentPane;
	private JPanel modePane;
	private JPanel dataSettingsPane;
	private JPanel inPaneGroup;
	private JPanel settingsGroup;
	private JPanel outputPane;
	private PrintPanel printPane = new PrintPanel(this);
	
	private ErdosRenyiPanel inputPaneER = new ErdosRenyiPanel(this);
	private AttachSitePanel inputPaneASR = new AttachSitePanel(this);
	private AttachSitePanel inputPaneASD = new AttachSitePanel(this);
	private CoalescencePanel inputPaneBHm = new CoalescencePanel(this);
	private CoalescencePanel inputPaneBHf = new CoalescencePanel(this);
	private CoalescencePanel inputPaneBHi = new CoalescencePanel(this);
	private CoalescencePanel inputPaneBHh = new CoalescencePanel(this);
	private List<JPanel> inputPanes = List.of(inputPaneER, inputPaneASR, inputPaneASD, 
			inputPaneBHm, inputPaneBHf, inputPaneBHi, inputPaneBHh);
	
	private DisplayPanel displayPane = new DisplayPanel(graph, displaySize);
	private HistogramPanel histogramPane = new HistogramPanel(graph, displaySize - 10);
	private LineGraphPanel lineGraphPane = new LineGraphPanel(graph, displaySize - 10);
	private ClusterSizePanel clusterSizePane = new ClusterSizePanel(graph, displaySize - 25);
	private SequencePanel sequencePane = new SequencePanel(displaySize - 10, new networks.ADJM());
	private ClusterDistPanel clusterDistPane = new ClusterDistPanel(graph, displaySize - 25);
	private List<JPanel> dataPanes = List.of(histogramPane, lineGraphPane, 
			clusterSizePane, sequencePane, clusterDistPane);

	private JButton modeButtonER = new JButton("Erdos-Renyi");
	private JButton modeButtonASR = new JButton("Random Site Attachment");
	private JButton modeButtonASD = new JButton("Degree Site Attachment");
	private JButton modeButtonBHm = new JButton("Heterogeneous Bond Aggregation (HBA) (m)");
	private JButton modeButtonBHf = new JButton("HBA (Field model)");
	private JButton modeButtonBHi = new JButton("HBA (Individual model)");
	private JButton modeButtonBHh = new JButton("HBAI (Homogeneic)");
	private List<JButton> modeButtons = List.of(modeButtonER, modeButtonASR, 
			modeButtonASD, modeButtonBHm, modeButtonBHf, modeButtonBHi, modeButtonBHh);
	private final int mButtonRow = 4;
	
	private JButton dSetButtonHistogram = new JButton("Histogram");
	private JButton dSetButtonLineGraph = new JButton("Line Graph");
	private JButton dSetButtonClusterSize = new JButton("Cluster Size");
	private JButton dSetButtonSequence = new JButton("Sequence");
	private JButton dSetButtonClusterDist = new JButton("Cluster Dist");
	private List<JButton> dSetButtons = List.of(dSetButtonHistogram, dSetButtonLineGraph, 
			dSetButtonClusterSize, dSetButtonSequence, dSetButtonClusterDist);
	private final int dButtonRow = 4;
	
	
	private JLabel adjmLabel;
	private JLabel adjmDisplay;
	private JLabel degreeAverage;

	private int speed = 1000;
	private Timer timerAttachRandomSite = new Timer(speed, this);
	private Timer timerAttachDegreeSite = new Timer(speed, this);
	private Timer timerBondHeteroTMN = new Timer(speed, this);
	private Timer timerBondHeteroField = new Timer(speed, this);
	private Timer timerBondHeteroIndividual = new Timer(speed, this);
	private Timer timerBondHomoIndividual = new Timer(speed, this);
	
	private AttachmentListener attachListener = new AttachmentListener();
	
	
	
	
	
	
	private GridBagConstraints gbc = new GridBagConstraints();
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		gbc.weightx = .5;
		gbc.gridwidth = 1;
		
		setTitle("Network Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1300, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridBagLayout());
		contentPane.setForeground(Color.BLACK);
		setContentPane(contentPane);
		
		
		
		settingsGroup = new JPanel(new GridBagLayout());
		
		modePane = new JPanel(new GridBagLayout());
		panelPresets(modePane, "Modes");
		int mBindex = 0; JButton mButton;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 0;
		for (int i = 0; i < 2; i++) {
			JPanel mPaneRow = new JPanel();
			for (int j = 0; j < mButtonRow; j++) {
				mButton = modeButtons.get(mBindex);
				mButton.addActionListener(this);
				mPaneRow.add(mButton);
				mBindex++;
				if (mBindex == modeButtons.size()) { break; }
			}
			gbc.gridy = i;
			modePane.add(mPaneRow, gbc);
			if (mBindex == modeButtons.size()) { break; }
		}
		settingsGroup.add(modePane);
		
		dataSettingsPane = new JPanel(new GridBagLayout());
		panelPresets(dataSettingsPane, "Settings");
		int dBindex = 0; JButton dButton;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 0;
		for (int i = 0; i < 2; i++) {
			JPanel dPaneRow = new JPanel();
			for (int j = 0; j < dButtonRow; j++) {
				dButton = dSetButtons.get(dBindex);
				dButton.addActionListener(this);
				dPaneRow.add(dButton);
				dBindex++;
				if (dBindex == dSetButtons.size()) { break; }
			}
			gbc.gridy = i;
			dataSettingsPane.add(dPaneRow, gbc);
			if (dBindex == modeButtons.size()) { break; }
		}
		settingsGroup.add(dataSettingsPane);
		
		
		
		outputPane = new JPanel();
		panelPresets(outputPane, "Network Analysis");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		
		adjmLabel = new JLabel("Adjacency Matrix");
		gbc.gridy = 0; outputPane.add(adjmLabel, gbc);
		adjmDisplay = new JLabel(new networks.Graph().getADJM().toString());
		gbc.gridy = 1; outputPane.add(adjmDisplay, gbc);
		degreeAverage = new JLabel("Average degree: "+graph.getADJM().degreeAverage());
		gbc.gridy = 2; outputPane.add(degreeAverage, gbc);
		
		
		
		inPaneGroup = new JPanel(new GridBagLayout());
		panelPresets(inPaneGroup, "InPane");
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridy = 0; 
		
		gbc.gridx = 1; for (JPanel pane : inputPanes) { inPaneGroup.add(pane, gbc); }
		gbc.gridx = 2; inPaneGroup.add(outputPane,gbc);
		gbc.gridx = 3; inPaneGroup.add(displayPane, gbc);
		gbc.gridx = 4; for (JPanel pane : dataPanes) { inPaneGroup.add(pane, gbc); }
		setInputPane(inputPaneBHm);
		setDataPane(histogramPane);
		setDisplaySettings(displayPane, histogramPane, lineGraphPane, 
				clusterSizePane, sequencePane, clusterDistPane);
		
		Container container = getContentPane();
		gbc.gridx = 0; 
		gbc.gridy = 0; container.add(settingsGroup, gbc);
		gbc.gridy = 1; container.add(inPaneGroup, gbc);
		gbc.gridy = 2; container.add(printPane, gbc);
		
		
		timerAttachDegreeSite.setRepeats(true);
		timerAttachRandomSite.setRepeats(true);
		timerBondHeteroTMN.setRepeats(true);
		timerBondHeteroField.setRepeats(true);
		timerBondHeteroIndividual.setRepeats(true);
		timerBondHomoIndividual.setRepeats(true);
	}
	
	
	private void panelPresets(JPanel pane, String name) {
		pane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(name), 
				BorderFactory.createEmptyBorder(5,5,5,5)));
		pane.setLayout(new GridBagLayout());
	}
	
	private void setDisplaySettings(JPanel ...displays) {
		for (JPanel display : displays) {
			display.setPreferredSize(new Dimension(displaySize, displaySize));
			display.setBounds(0, 0, displaySize, displaySize);			
		}
	}
	
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		Object source = e.getSource();
		
		printListen(source);
		
		for (int i = 0; i < modeButtons.size(); i++) {
			if (source == modeButtons.get(i)) {
				setInputPane(inputPanes.get(i));
			}
		}
		for (int i = 0; i < dSetButtons.size(); i++) {
			if (source == dSetButtons.get(i)) {
				setDataPane(dataPanes.get(i));
			}
		}
		
		if (source == timerAttachRandomSite) {
			attachListener.moveAttachModel(inputPaneASR, graphAttachRandomSite);
			graph.setADJM(graphAttachRandomSite.getADJM());
			if (clusterSizePane.isVisible() || clusterDistPane.isVisible()) {
				graph.setClusters(graphAttachRandomSite.getClusters());
			}
		} else if (source == timerAttachDegreeSite) {
			attachListener.moveAttachModel(inputPaneASD, graphAttachDegreeSite);
			graph.setADJM(graphAttachDegreeSite.getADJM());
			if (clusterSizePane.isVisible() || clusterDistPane.isVisible()) {
				graph.setClusters(graphAttachDegreeSite.getClusters());
			}
		} else if (source == timerBondHeteroTMN) {
			attachListener.moveAttachModel(inputPaneBHm, graphAttachBondHeteroM);
			graph.setADJM(graphAttachBondHeteroM.getADJM());
			if (clusterSizePane.isVisible() || clusterDistPane.isVisible()) {
				graph.setClusters(graphAttachBondHeteroM.getClusters());
			}
		} else if (source == timerBondHeteroField) {
			attachListener.moveAttachModel(inputPaneBHf, graphAttachBondHeteroF);
			graph.setADJM(graphAttachBondHeteroF.getADJM());
			if (clusterSizePane.isVisible() || clusterDistPane.isVisible()) {
				graph.setClusters(graphAttachBondHeteroF.getClusters()); 
			}
		} else if (source == timerBondHeteroIndividual) {
			attachListener.moveAttachModel(inputPaneBHi, graphAttachBondHeteroI);
			graph.setADJM(graphAttachBondHeteroI.getADJM());
			if (clusterSizePane.isVisible() || clusterDistPane.isVisible()) {
				graph.setClusters(graphAttachBondHeteroI.getClusters()); 
			}
		} else if (source == timerBondHomoIndividual) {
			attachListener.moveAttachModel(inputPaneBHh, graphAttachBondHomoI);
			graph.setADJM(graphAttachBondHomoI.getADJM());
			if (clusterSizePane.isVisible() || clusterDistPane.isVisible()) {
				graph.setClusters(graphAttachBondHomoI.getClusters()); 
			}
			
			
		} else if (inputPaneER.isVisible()) {
			if (source == inputPaneER.randomizeButton) {
				graph.erdosRenyi(p);
			} else if (source == inputPaneER.dimInput) {
				graph.setDim(getStringInt(inputPaneER.dimInput.getText(), graph.getDim()));
			} else if (source == inputPaneER.pInput) {
				p = getStringDouble(inputPaneER.pInput.getText(), p);
			} else if (source == inputPaneER.execute) {
				System.out.println("destroying");
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
			if (clusterSizePane.isVisible() || clusterDistPane.isVisible()) {
				graph.clustersUpdate();
			}
			
			
		} else if (inputPaneASR.isVisible()) {
			attachListener.attachActions(inputPaneASR, graphAttachRandomSite, timerAttachRandomSite, source);
		} else if (inputPaneASD.isVisible()) {
			attachListener.attachActions(inputPaneASD, graphAttachDegreeSite, timerAttachDegreeSite, source);
		} else if (inputPaneBHm.isVisible()) {
			attachListener.attachActions(inputPaneBHm, graphAttachBondHeteroM, timerBondHeteroTMN, source);
			attachListener.coalescenceActions(inputPaneBHm, graphAttachBondHeteroM, timerBondHeteroTMN, source);
		} else if (inputPaneBHf.isVisible()) {
			attachListener.attachActions(inputPaneBHf, graphAttachBondHeteroF, timerBondHeteroField, source);
			attachListener.coalescenceActions(inputPaneBHf, graphAttachBondHeteroF, timerBondHeteroField, source);
		} else if (inputPaneBHi.isVisible()) {
			attachListener.attachActions(inputPaneBHi, graphAttachBondHeteroI, timerBondHeteroIndividual, source);
			attachListener.coalescenceActions(inputPaneBHi, graphAttachBondHeteroI, timerBondHeteroIndividual, source);
		} else if (inputPaneBHh.isVisible()) {
			attachListener.attachActions(inputPaneBHh, graphAttachBondHomoI, timerBondHomoIndividual, source);
			attachListener.coalescenceActions(inputPaneBHh, graphAttachBondHomoI, timerBondHomoIndividual, source);
		}
		
		
		
		if (graph.getDim() > maxGraphSize) {
			adjmDisplay.setText("[...]");
		} else {
			adjmDisplay.setText(graph.getADJM().stringhtml());
		}
		degreeAverage.setText("Average degree: "+graph.getADJM().degreeAverage());
		
		displayPane.repaint();
		for (JPanel pane : dataPanes) {
			if (pane.isVisible()) {
				pane.repaint();
			}
		}
	}
	
	private void setInputPane(JPanel inputPane) {
		for (JPanel pane : inputPanes) {
			if (pane != inputPane) {
				pane.setVisible(false);
			}
		}
		inputPane.setVisible(true);
	}
	private void setDataPane(JPanel dataPane) {
		for (JPanel pane : dataPanes) {
			if (pane != dataPane) {
				pane.setVisible(false);
			}
		}
		dataPane.setVisible(true);
		if (dataPane == clusterSizePane || dataPane == clusterDistPane) {
			graph.clustersUpdate();
		}
	}
	
	
	public class AttachmentListener {
		private void attachActions(AttachmentPanel attachPane, networks.Attachment graphAttach, 
				Timer timer, Object source) {
			if (source == attachPane.fieldMap.get(FieldTitle.ATTACHEDGENUM)) {
				m = getStringInt(attachPane.fieldMap.get(FieldTitle.ATTACHEDGENUM).getText(), m);
			} else if (source == attachPane.fieldMap.get(FieldTitle.STEP)) {
				step = getStringInt(attachPane.fieldMap.get(FieldTitle.STEP).getText(), step);
			} else if (source == attachPane.fieldMap.get(FieldTitle.ATTACHVERTNUM)) {
				n = getStringInt(attachPane.fieldMap.get(FieldTitle.ATTACHVERTNUM).getText(), n);
			} else if (source == attachPane.buttonMap.get(ButtonTitle.RESET)) {
				attachModelReset(attachPane, graphAttach);
			} else if (source == attachPane.buttonMap.get(ButtonTitle.GOSTEP)) {
				moveAttachModel(attachPane, graphAttach);
			} else if (source == attachPane.buttonMap.get(ButtonTitle.GO)) {
				timer.start();
			} else if (source == attachPane.buttonMap.get(ButtonTitle.STOP)) {
				timer.stop();
			} else if (source == attachPane.buttonMap.get(ButtonTitle.SEQUENCE)) {
				sequenceModel(attachPane, graphAttach);
				setDataPane(sequencePane);
			}
			graph.setADJM(graphAttach.getADJM());
			graph.setClusters(graphAttach.getClusters());
		}
		private void coalescenceActions(CoalescencePanel coalescePane, 
				networks.Coalescence graphCoalesce, Timer timer, Object source) {
			if (source == coalescePane.fieldMap.get(FieldTitle.DIM) && 
					coalescePane.isVisible()) {
				dimBH = getStringInt(coalescePane.fieldMap.get(FieldTitle.DIM).getText(), dimBH);
				graphCoalesce.setDim(dimBH);
				graphCoalesce.reset();
			} else if (source == coalescePane.fieldMap.get(FieldTitle.THRESHOLD) && 
					coalescePane.isVisible()) {
				graphCoalesce.setThreshold(getStringDouble(
						coalescePane.fieldMap.get(FieldTitle.THRESHOLD).getText(), threshold));
			} else if (source == coalescePane.fieldMap.get(FieldTitle.INTERACT)) {
				interact = getStringDouble(coalescePane.fieldMap.get(FieldTitle.INTERACT).getText(), interact);
				graphCoalesce.setInteract(interact);
			}
			graph.setADJM(graphCoalesce.getADJM());
			graph.setClusters(graphCoalesce.getClusters());
		}
		private void moveAttachModel(AttachmentPanel attachPane, networks.Attachment graphAttach) {
			graphAttach.movingStep(step, m, n);
			time += step;
			attachPane.timeDisplay.setText(Integer.toString(time));
			attachPane.nodeNumDisplay.setText(Integer.toString(graphAttach.getDim()));
		}
		public void attachModelReset(AttachmentPanel attachPane, networks.Attachment graphAttach) {
			graphAttach.reset();
			sequencePane.updateSequence();
			time = 0;
			attachPane.timeDisplay.setText(Integer.toString(time));
			attachPane.nodeNumDisplay.setText(Integer.toString(graphAttach.getDim()));
		}
		
		public void sequenceModel(AttachmentPanel attachPane, networks.Attachment graphAttach) {
			sequencePane.updateSequence(graphAttach.sequence(step, m, n));
			attachPane.nodeNumDisplay.setText(Integer.toString(graphAttach.getDim()));
		}
		
	}
	
	private void printListen(Object source) {
		if (source == printPane.printGraphButton) {
			System.out.println(new String(new char[300]).replace("\0", "\n"));
			System.out.println(graph.latexPrint(printPane.vertexSize, printPane.circleRad));
		} else if (source == printPane.vertexSizeField) {
			printPane.vertexSize = getStringDoubleAny(printPane.vertexSizeField.getText(), printPane.vertexSize);
		} else if (source == printPane.circleRadField) {
			printPane.circleRad = getStringDoubleAny(printPane.circleRadField.getText(), printPane.circleRad);
		}
	}
	
	private int getStringInt(String numString, int def) {
		try {
			int n = Integer.parseInt(numString);
			if (n >= 0) {
				return n;
			} else {
				JOptionPane.showMessageDialog(this, "Eggs are not supposed to be blue.");
			}
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Eggs are not supposed to be green.");
		}
		return def;
	}
	private double getStringDouble(String numString, double def) {
		try {
			double doub = Double.parseDouble(numString);
			if (0 <= doub && doub <= 1) {
				return doub;
			} else {
				JOptionPane.showMessageDialog(this, "Eggs are not supposed to be blue.");
			}
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Eggs are not supposed to be green.");
		}
		return def;
	}
	
	private double getStringDoubleAny(String numString, double def) {
		try {
			return Double.parseDouble(numString);
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Eggs are not supposed to be green.");
		}
		return def;
	}
}
