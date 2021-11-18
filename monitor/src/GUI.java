import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JTree;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;

public class GUI {

	private JFrame frmThreadmonitorGui;
	private JTextField periodField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmThreadmonitorGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmThreadmonitorGui = new JFrame();
		frmThreadmonitorGui.setResizable(false);
		frmThreadmonitorGui.setTitle("JVM Thread Monitor GUI");
		frmThreadmonitorGui.setBounds(100, 100, 480, 398);
		frmThreadmonitorGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(480, 360));
		frmThreadmonitorGui.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		JTree tree = new JTree();
		scrollPane.setViewportView(tree);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(rightPanel);
		
		JButton pauseButton = new JButton("Pause");
		
		JLabel periodLabel = new JLabel("Refresh period (seconds)");
		
		periodField = new JTextField();
		periodField.setHorizontalAlignment(SwingConstants.TRAILING);
		periodField.setText("10");
		periodField.setColumns(10);
		
		JLabel timeLabel = new JLabel("Time of last refresh");
		
		JLabel clockLabel = new JLabel("00:00:00");
		clockLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel selectedLabel = new JLabel("Selected:");
		
		JTextPane detailsPane = new JTextPane();
		detailsPane.setEditable(false);
		
		JLabel typeLabel = new JLabel("None");
		GroupLayout gl_rightPanel = new GroupLayout(rightPanel);
		gl_rightPanel.setHorizontalGroup(
			gl_rightPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(timeLabel)
						.addComponent(periodLabel))
					.addPreferredGap(ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(periodField, 0, 0, Short.MAX_VALUE)
						.addComponent(clockLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				.addComponent(pauseButton, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addComponent(selectedLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
					.addComponent(typeLabel))
				.addComponent(detailsPane, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
		);
		gl_rightPanel.setVerticalGroup(
			gl_rightPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(selectedLabel)
						.addComponent(typeLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(detailsPane, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(timeLabel)
						.addComponent(clockLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(periodLabel)
						.addComponent(periodField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(pauseButton))
		);
		rightPanel.setLayout(gl_rightPanel);
	}
}
