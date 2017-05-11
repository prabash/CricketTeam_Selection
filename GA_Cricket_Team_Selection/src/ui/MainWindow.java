package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Rectangle;
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

public class MainWindow {

	private JFrame frame;
	private JTextField txtNoOfBatsmen;
	private JTextField txtNoOfBowlers;
	private JTextField txtNoOfAllRounders;
	private JTable tblCurrentSquad;
	private JTable tblSelectedTeam;

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
		//PlayerDetails.ReadStatistics();
		
		DefaultTableModel model = (DefaultTableModel)tblCurrentSquad.getModel();
		
		// Create a couple of columns 
		model.addColumn("Player ID"); 
		model.addColumn("Player Name"); 

		// Append a row 
		model.addRow(new Object[]{"1", "AP Aponso"});
		
	    model = (DefaultTableModel)tblSelectedTeam.getModel();
		
		// Create a couple of columns 
		model.addColumn("Player ID"); 
		model.addColumn("Player Name"); 

		// Append a row 
		model.addRow(new Object[]{"1", "AP Aponso"});
		model.addRow(new Object[]{"2", "D Chandimal"});
		
		tblSelectedTeam.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            // do some actions here, for example
	            // print first column value from selected row
	        	if (!event.getValueIsAdjusting() && tblSelectedTeam.getSelectedRow() != -1) 
	        		System.out.println(tblSelectedTeam.getValueAt(tblSelectedTeam.getSelectedRow(), 0).toString());
	        }
	    });
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
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
		
		JComboBox cmbPitchType = new JComboBox();
		cmbPitchType.setModel(new DefaultComboBoxModel(new String[] {"", "Batting", "Spinning", "Bouncy"}));
		
		JButton btnGenerateTeam = new JButton("Generate Best Team");
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
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
							.addGap(13))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
							.addGap(13))))
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
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNewLabel_3)
						.addComponent(lblPlayerName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(185, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_3)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblPlayerName, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(427, Short.MAX_VALUE))
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
		txtNoOfBowlers.setColumns(10);
		
		txtNoOfAllRounders = new JTextField();
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
