package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import static org.jenetics.engine.limit.bySteadyFitness;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JSeparator;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jenetics.Chromosome;
import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.MeanAlterer;
import org.jenetics.Mutator;
import org.jenetics.Optimize;
import org.jenetics.Phenotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.engine.codecs;
import org.jenetics.util.IntRange;

import ga.FitnessCalculator;
import ga.PlayerDetails;
import objects.PlayerStatistics;
import enums.OtherPlayerTypes;
import enums.PitchType;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;;

public class MainWindow {

	private JFrame frame;
	private JTextField txtNoOfBatsmen;
	private JTextField txtNoOfBowlers;
	private JTextField txtNoOfAllRounders;
	private JTable tblCurrentSquad;
	private JTable tblSelectedTeam;
	private JTextField txtPlayerID;
	private JTextField txtPlayerName;
	private JTextField txtPlayerRole;
	private JTextField txtOtherTypes;
	private JTextField txtMatchesPlayed;
	private JTextField txtBattingRuns;
	private JTextField txtBattingAvg;
	private JTextField txtBattingSR;
	private JTextField txtBatting100s;
	private JTextField txtBatting50s;
	private JTextField txtBowlingInns;
	private JTextField txtBowlingOvers;
	private JTextField txtBowlingWkts;
	private JTextField txtBowlingAvg;
	private JTextField txtBowlingEcon;
	private JTextField txtBowlingSR;
	private JComboBox cmbPitchType; 
	
	private static int noOfBatsmen = 0;
	private static int noOfBowlers = 0;
	private static int noOfAllRounders = 0;
	private static PitchType pitchType;
	private JTextField txtTotalFitness;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		PlayerDetails.ReadStatistics();
		
		DefaultTableModel model = (DefaultTableModel)tblCurrentSquad.getModel();
		model.addColumn("Player ID"); 
		model.addColumn("Player Name");
		
		List<PlayerStatistics> currentSquad = PlayerDetails.getEntireSquad();
		for (PlayerStatistics playerStatistics : currentSquad) {
			// Append a row 
			model.addRow(new Object[]{playerStatistics.playerId, playerStatistics.playerName});
		}
		
		tblCurrentSquad.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            // do some actions here, for example
	            // print first column value from selected row
	        	if (!event.getValueIsAdjusting() && tblCurrentSquad.getSelectedRow() != -1) {
	        		int playerId = Integer.parseInt(tblCurrentSquad.getValueAt(tblCurrentSquad.getSelectedRow(), 0).toString());
	        		SetPlayerDetails(playerId);
	        	}
	        }
	    });
		

		model = (DefaultTableModel)tblSelectedTeam.getModel();
		model.addColumn("Player ID"); 
		model.addColumn("Player Name");
		model.addColumn("Player Role");
	}
	
	private void SetPlayerDetails(int playerId){
		PlayerStatistics playerStats = PlayerDetails.getPlayerDetails(playerId);
		
		txtPlayerID.setText(Integer.toString(playerStats.playerId));
		txtPlayerName.setText(playerStats.playerName);
		txtPlayerRole.setText(playerStats.playerType.toString());
		
		String otherTypes = "";
		for (int i = 0; i < playerStats.otherPlayerTypes.size(); i++) {
			otherTypes += playerStats.otherPlayerTypes.get(i).toString() + ",";
		}
		txtOtherTypes.setText(otherTypes.trim());
		txtMatchesPlayed.setText(Integer.toString(playerStats.matchesPlayed));
		
		
		txtBattingRuns.setText(Integer.toString(playerStats.totalRuns));
		txtBattingAvg.setText(Double.toString(playerStats.battingAvg));
		txtBattingSR.setText(Double.toString(playerStats.battingStrikeRate));
		txtBatting100s.setText(Integer.toString(playerStats.hundredScored));
		txtBatting50s.setText(Integer.toString(playerStats.fiftiesScored));
		
		
		txtBowlingInns.setText(Integer.toString(playerStats.bowlingInnings));
		txtBowlingOvers.setText(Double.toString(playerStats.oversBowled));
		txtBowlingWkts.setText(Integer.toString(playerStats.wicketsTaken));
		txtBowlingAvg.setText(Double.toString(playerStats.bowlingAvg));
		txtBowlingEcon.setText(Double.toString(playerStats.bowlingEconomyRate));
		txtBowlingSR.setText(Double.toString(playerStats.bowlingStrikeRate));
	}
	
	private void runAlgorithm(){
		
		noOfBatsmen = Integer.parseInt(txtNoOfBatsmen.getText());
		noOfBowlers = Integer.parseInt(txtNoOfBowlers.getText());
		noOfAllRounders = Integer.parseInt(txtNoOfAllRounders.getText());
		pitchType = PitchType.valueOf(cmbPitchType.getSelectedItem().toString().toLowerCase());
		
		final Engine<IntegerGene, Double> engine = Engine
				// Create a new builder with the given fitness
				// function and chromosome.
				.builder(MainWindow::teamFitness,
					codecs.ofVector(IntRange.of(1, 26), 11))
				.populationSize(1000)
				.optimize(Optimize.MAXIMUM)
				.alterers(
						new Mutator<>(0.05),
						new MeanAlterer<>(0.2))
				.build();
			
		// Create evolution statistics consumer.
		final EvolutionStatistics<Double, ?>
			statistics = EvolutionStatistics.ofNumber();
		final EvolutionStatistics<Double, ?>
			compStatistics = EvolutionStatistics.ofComparable();

		final Phenotype<IntegerGene, Double> best = engine.stream()
			// Truncate the evolution stream after 20 "steady"
			// generations.
			.limit(bySteadyFitness(50))
			// The evolution will stop after maximal 100
			// generations.
			.limit(1000)
			// Update the evaluation statistics after
			// each generation
			.peek(statistics)
			//.peek(compStatistics)
			// Collect (reduce) the evolution stream to
			// its best phenotype.
			.collect(toBestPhenotype());

		System.out.println(statistics);
		//System.out.println(compStatistics);
		System.out.println(best);
		
		int [] bestTeam = new int [11];
		Genotype<IntegerGene> bestTeamGenotype = best.getGenotype();
		for (Chromosome<IntegerGene> chromosome : bestTeamGenotype) {
			for (int i = 0; i < chromosome.length(); i++) {
				bestTeam[i] = chromosome.getGene(i).intValue();
			} 
		}
		
		txtTotalFitness.setText(Double.toString(best.getFitness()));
		List<PlayerStatistics> bestPlayers = PlayerDetails.getTeamDetails(bestTeam);
		
		DefaultTableModel model = (DefaultTableModel)tblSelectedTeam.getModel();
		
		int rowCount = model.getRowCount();
		//Remove rows one by one from the end of the table
		for (int i = rowCount - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		
		for (PlayerStatistics playerStatistics : bestPlayers) {
			// Append a row 
			model.addRow(new Object[] { playerStatistics.playerId, playerStatistics.playerName, playerStatistics.playerType });
		}
		
		tblSelectedTeam.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            // do some actions here, for example
	            // print first column value from selected row
	        	if (!event.getValueIsAdjusting() && tblSelectedTeam.getSelectedRow() != -1) {
	        		int playerId = Integer.parseInt(tblSelectedTeam.getValueAt(tblSelectedTeam.getSelectedRow(), 0).toString());
	        		SetPlayerDetails(playerId);
	        	}
	        }
	    });
			
	}
	
	private static double teamFitness(final int[] team)
    {		
    	return FitnessCalculator.calculateTeamFitness(team,  noOfBatsmen, noOfBowlers, noOfAllRounders, pitchType);
    }

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 11));
		frame.setBounds(100, 100, 778, 546);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Cricket Team Assistant");
		lblNewLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 16));
		
		JPanel panel = new JPanel();
		panel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_3 = new JPanel();
		
		JPanel panel_2 = new JPanel();
		
		JLabel lblPitchType = new JLabel("Match Details");
		lblPitchType.setFont(new Font("Segoe UI", Font.BOLD, 12));
		
		JLabel lblNewLabel_2 = new JLabel("Pitch Type");
		lblNewLabel_2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		cmbPitchType = new JComboBox();
		cmbPitchType.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		cmbPitchType.setModel(new DefaultComboBoxModel(new String[] {"", "Batting", "Spinning", "Bouncy"}));
		
		JButton btnGenerateTeam = new JButton("Generate Best Team");
		btnGenerateTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runAlgorithm();
			}
		});
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnGenerateTeam)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_panel_2.createSequentialGroup()
									.addGap(10)
									.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(lblPitchType, Alignment.LEADING))
							.addGap(18)
							.addComponent(cmbPitchType, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addComponent(lblPitchType)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
						.addComponent(cmbPitchType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
					.addComponent(btnGenerateTeam)
					.addContainerGap())
		);
		panel_2.setLayout(gl_panel_2);
		
		JPanel panel_4 = new JPanel();
		
		JLabel lblSelectedTeam = new JLabel("Selected Team");
		lblSelectedTeam.setFont(new Font("Segoe UI", Font.BOLD, 12));
		
		JScrollPane scrollPane_1 = new JScrollPane((Component) null);
		scrollPane_1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		tblSelectedTeam = new JTable();
		tblSelectedTeam.setRowSelectionAllowed(false);
		tblSelectedTeam.setShowGrid(false);
		tblSelectedTeam.setShowVerticalLines(false);
		tblSelectedTeam.setShowHorizontalLines(false);
		tblSelectedTeam.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		scrollPane_1.setViewportView(tblSelectedTeam);
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addComponent(lblSelectedTeam, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addComponent(lblSelectedTeam)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
		);
		panel_4.setLayout(gl_panel_4);
		
		JLabel lblTotalFitness = new JLabel("Total Fitness");
		lblTotalFitness.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		txtTotalFitness = new JTextField();
		txtTotalFitness.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtTotalFitness.setEditable(false);
		txtTotalFitness.setColumns(10);
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblNewLabel)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(lblTotalFitness, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
							.addComponent(txtTotalFitness, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
						.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
					.addGap(13))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTotalFitness, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtTotalFitness, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		
			    
				tblCurrentSquad = new JTable();
				tblCurrentSquad.setRowSelectionAllowed(false);
				tblCurrentSquad.setShowVerticalLines(false);
				tblCurrentSquad.setFont(new Font("Segoe UI", Font.PLAIN, 11));
				tblCurrentSquad.setShowGrid(false);
				tblCurrentSquad.setShowHorizontalLines(false);
				JScrollPane scrollPane = new JScrollPane(tblCurrentSquad);
				scrollPane.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblCurrentSquad_1 = new JLabel("Current Squad");
		lblCurrentSquad_1.setFont(new Font("Segoe UI", Font.BOLD, 12));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addComponent(lblCurrentSquad_1, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(118, Short.MAX_VALUE))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addComponent(lblCurrentSquad_1, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
		);
		panel_3.setLayout(gl_panel_3);
		
		JLabel lblNewLabel_1 = new JLabel("Player Details");
		lblNewLabel_1.setFont(new Font("Segoe UI Light", Font.PLAIN, 16));
		
		JLabel lblNewLabel_3 = new JLabel("Player ID");
		lblNewLabel_3.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblPlayerName = new JLabel("Player Name");
		lblPlayerName.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		txtPlayerID = new JTextField();
		txtPlayerID.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtPlayerID.setEditable(false);
		txtPlayerID.setColumns(10);
		
		txtPlayerName = new JTextField();
		txtPlayerName.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtPlayerName.setEditable(false);
		txtPlayerName.setColumns(10);
		
		JLabel lblPlayerType = new JLabel("Player Role");
		lblPlayerType.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblOtherTypes = new JLabel("Other Types");
		lblOtherTypes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblMatchesPlayed = new JLabel("Matches Played");
		lblMatchesPlayed.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblBattingAvg = new JLabel("Batting Runs");
		lblBattingAvg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblBattingSr = new JLabel("Batting Avg.");
		lblBattingSr.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblBattingSr_1 = new JLabel("Batting SR");
		lblBattingSr_1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblsScored = new JLabel("No. of 100s Scored");
		lblsScored.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblNoOfs = new JLabel("No. of 50s Scored");
		lblNoOfs.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblBowlingInns = new JLabel("Bowling Inns.");
		lblBowlingInns.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblBowlingOvers = new JLabel("Bowling Overs");
		lblBowlingOvers.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblBowlingWkts = new JLabel("Bowling Wkts");
		lblBowlingWkts.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblBowlingAvg = new JLabel("Bowling Avg.");
		lblBowlingAvg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblBowlingEconomy = new JLabel("Bowling Economy");
		lblBowlingEconomy.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblBowlingSr = new JLabel("Bowling SR");
		lblBowlingSr.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		txtPlayerRole = new JTextField();
		txtPlayerRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtPlayerRole.setEditable(false);
		txtPlayerRole.setColumns(10);
		
		txtOtherTypes = new JTextField();
		txtOtherTypes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtOtherTypes.setEditable(false);
		txtOtherTypes.setColumns(10);
		
		txtMatchesPlayed = new JTextField();
		txtMatchesPlayed.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtMatchesPlayed.setEditable(false);
		txtMatchesPlayed.setColumns(10);
		
		txtBattingRuns = new JTextField();
		txtBattingRuns.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBattingRuns.setEditable(false);
		txtBattingRuns.setColumns(10);
		
		txtBattingAvg = new JTextField();
		txtBattingAvg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBattingAvg.setEditable(false);
		txtBattingAvg.setColumns(10);
		
		txtBattingSR = new JTextField();
		txtBattingSR.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBattingSR.setEditable(false);
		txtBattingSR.setColumns(10);
		
		txtBatting100s = new JTextField();
		txtBatting100s.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBatting100s.setEditable(false);
		txtBatting100s.setColumns(10);
		
		txtBatting50s = new JTextField();
		txtBatting50s.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBatting50s.setEditable(false);
		txtBatting50s.setColumns(10);
		
		txtBowlingInns = new JTextField();
		txtBowlingInns.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBowlingInns.setEditable(false);
		txtBowlingInns.setColumns(10);
		
		txtBowlingOvers = new JTextField();
		txtBowlingOvers.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBowlingOvers.setEditable(false);
		txtBowlingOvers.setColumns(10);
		
		txtBowlingWkts = new JTextField();
		txtBowlingWkts.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBowlingWkts.setEditable(false);
		txtBowlingWkts.setColumns(10);
		
		txtBowlingAvg = new JTextField();
		txtBowlingAvg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBowlingAvg.setEditable(false);
		txtBowlingAvg.setColumns(10);
		
		txtBowlingEcon = new JTextField();
		txtBowlingEcon.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBowlingEcon.setEditable(false);
		txtBowlingEcon.setColumns(10);
		
		txtBowlingSR = new JTextField();
		txtBowlingSR.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBowlingSR.setEditable(false);
		txtBowlingSR.setColumns(10);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblNewLabel_3)
								.addComponent(lblPlayerName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addComponent(txtPlayerID, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtPlayerName, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblPlayerType, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addComponent(txtPlayerRole, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblOtherTypes, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addComponent(txtOtherTypes, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblMatchesPlayed, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addComponent(txtMatchesPlayed, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblBattingAvg, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addComponent(txtBattingRuns, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblBattingSr, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addComponent(txtBattingAvg, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblBattingSr_1, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addComponent(txtBattingSR, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblsScored, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
							.addComponent(txtBatting100s, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblNoOfs, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
							.addComponent(txtBatting50s, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblBowlingInns, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addComponent(txtBowlingInns, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblBowlingOvers, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addComponent(txtBowlingOvers, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblBowlingWkts, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addComponent(txtBowlingWkts, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblBowlingAvg, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
							.addComponent(txtBowlingAvg, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblBowlingEconomy, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
							.addComponent(txtBowlingEcon, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblBowlingSr, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
							.addComponent(txtBowlingSR, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_3)
						.addComponent(txtPlayerID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlayerName, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtPlayerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlayerType, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtPlayerRole, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOtherTypes, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtOtherTypes, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMatchesPlayed, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtMatchesPlayed, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBattingAvg, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBattingRuns, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBattingSr, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBattingAvg, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBattingSr_1, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBattingSR, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblsScored, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBatting100s, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNoOfs, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBatting50s, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBowlingInns, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBowlingInns, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBowlingOvers, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBowlingOvers, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBowlingWkts, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBowlingWkts, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBowlingAvg, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBowlingAvg, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBowlingEconomy, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBowlingEcon, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBowlingSr, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBowlingSR, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(58, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		JLabel lblTeamComposition = new JLabel("Team Composition");
		lblTeamComposition.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblTeamComposition.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblNoOfBatsmen = new JLabel("No. of Batsmen");
		lblNoOfBatsmen.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblNoOfBowlers = new JLabel("No. of Bowlers");
		lblNoOfBowlers.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		JLabel lblNoOfAllrounders = new JLabel("No. of All-Rounders");
		lblNoOfAllrounders.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		txtNoOfBatsmen = new JTextField();
		txtNoOfBatsmen.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtNoOfBatsmen.setColumns(10);
		
		txtNoOfBowlers = new JTextField();
		txtNoOfBowlers.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtNoOfBowlers.setColumns(10);
		
		txtNoOfAllRounders = new JTextField();
		txtNoOfAllRounders.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtNoOfAllRounders.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNoOfAllrounders))
						.addComponent(lblTeamComposition)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNoOfBowlers))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNoOfBatsmen)))
					.addGap(21)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(txtNoOfBatsmen, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtNoOfBowlers, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtNoOfAllRounders, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
					.addGap(37))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblTeamComposition)
					.addGap(6)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNoOfBatsmen)
						.addComponent(txtNoOfBatsmen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(6)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNoOfBowlers)
						.addComponent(txtNoOfBowlers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(6)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNoOfAllrounders)
						.addComponent(txtNoOfAllRounders, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
	}
}
