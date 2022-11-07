import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTree;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI {
	
	private ThreadMonitor monitor = new ThreadMonitor();

	private JFrame frmThreadmonitorGui;
	private JTree tree;
	private JScrollPane scrollPane;
	private JTextField periodField;
	private JLabel clockLabel;
	private JLabel typeLabel;
	private JTextPane detailsPane;
	private JButton pauseButton;
	private JButton interruptButton;
	private JButton createButton;
	
	Thread treeUpdaterThread;
	boolean paused = false;
	
	int period = 10;
	
	int noTestThreads = 0;
	
	TreeSelectionListener treeSelectListener = new TreeSelectionListener() {
	    public void valueChanged(TreeSelectionEvent e) {
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

	        // no item selected
	        if (node == null) {
	        	typeLabel.setText("None");
	        	return;
	        }
	        
	        Object nodeInfo = node.getUserObject();
	        if (node.isLeaf()) {
	        	// thread selected
	        	typeLabel.setText("Thread");
	        	Thread t = (Thread) nodeInfo;
	        	detailsPane.setText("Name: " + t.getName() + 
	        						"\nId: " + t.getId() +
	        						"\nState: " + t.getState() +
	        						"\nPriority: " + t.getPriority() +
	        						"\nDaemon: " + t.isDaemon());
	        	interruptButton.setEnabled(true);
	        	monitor.setSelectedThread(t);
	        } else {
	        	// thread group selected
	        	typeLabel.setText("Thread Group");
	        	ThreadGroup tg = (ThreadGroup) nodeInfo;
	        	detailsPane.setText("Name: " + tg.getName() +
	        						"\nMax. priority: " + tg.getMaxPriority());
	        	interruptButton.setEnabled(false);
	        }
	    }
	};

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
		
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
		
		Runnable treeUpdater =
				() -> {
					while(true) {
						if(paused) {
							// make the tree updater thread wait until its notified
							synchronized (treeUpdaterThread) {
								try {
									pauseButton.setText("Resume");
									treeUpdaterThread.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
						
						// update the tree
						tree = new JTree(monitor.buildTree());
						tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
						tree.addTreeSelectionListener(treeSelectListener);
						tree.setCellRenderer(new ThreadTreeRenderer());
						scrollPane.setViewportView(tree);
						
						// update the GUI
						clockLabel.setText(formatter.format(new Date()));
						typeLabel.setText("None");
						detailsPane.setText("");
						interruptButton.setEnabled(false);
						
						try {
							Thread.sleep(period * 1000);
						} catch(InterruptedException ie) {
							
						}
					}
				};
				
		treeUpdaterThread = new Thread(treeUpdater);
		treeUpdaterThread.setName("Tree Updater");
		treeUpdaterThread.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ImageIcon tgIcon = new ImageIcon(this.getClass().getResource("/res/tg_icon.png"));
		ImageIcon tIcon = new ImageIcon(this.getClass().getResource("/res/t_icon.png"));
		
		UIManager.put("Tree.closedIcon", tgIcon);
		UIManager.put("Tree.openIcon", tgIcon);
		UIManager.put("Tree.leafIcon", tIcon);
		
		frmThreadmonitorGui = new JFrame();
		frmThreadmonitorGui.setResizable(false);
		frmThreadmonitorGui.setTitle("JVM Thread Monitor GUI");
		frmThreadmonitorGui.setBounds(100, 100, 480, 398);
		frmThreadmonitorGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmThreadmonitorGui.setIconImage(tIcon.getImage());
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(480, 360));
		frmThreadmonitorGui.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		tree = new JTree();
		scrollPane.setViewportView(tree);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(rightPanel);
		
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(paused) {
					synchronized (treeUpdaterThread) {
						pauseButton.setText("Pause");
						paused = false;
						treeUpdaterThread.notify();
					}
				}
				else
				{
					synchronized (treeUpdaterThread) {
						paused = true;
						treeUpdaterThread.interrupt();
					}
				}
			}
		});
		
		JLabel periodLabel = new JLabel("Refresh period (seconds)");
		
		periodField = new JTextField();
		periodField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int fieldValue = Integer.parseInt(periodField.getText());
					
					if(fieldValue < 1 || fieldValue > 300) {
						// show error if the value is not in range
						JOptionPane.showMessageDialog(new JFrame(), "The period must be an integer between 1 and 300", "Error",
						        JOptionPane.ERROR_MESSAGE);
					}
					else {
						// update period and wake the tree updater thread up
						period = fieldValue;
						synchronized (treeUpdaterThread) {
							treeUpdaterThread.interrupt();
						}
						
					}
				} catch(NumberFormatException e1) {
					// show error if the value is not an integer
					JOptionPane.showMessageDialog(new JFrame(), "The period must be an integer between 1 and 300", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		periodField.setHorizontalAlignment(SwingConstants.TRAILING);
		periodField.setText("10");
		periodField.setColumns(10);
		
		JLabel timeLabel = new JLabel("Time of last refresh");
		
		clockLabel = new JLabel("00:00:00");
		clockLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel selectedLabel = new JLabel("Selected:");
		
		typeLabel = new JLabel("None");
		
		detailsPane = new JTextPane();
		detailsPane.setEditable(false);
		
		interruptButton = new JButton("Interrupt thread");
		interruptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(monitor.getSelectedThread().isAlive()) {
					monitor.interruptSelectedThread();
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(), "The thread has already been terminated", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		interruptButton.setEnabled(false);
		
		createButton = new JButton("Create test thread");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				noTestThreads++;
				Runnable testRunnable =
						() -> {
							while(true) {
								try {
									Thread.sleep(20000);
								} catch(InterruptedException ie) {
									Thread.currentThread().stop();
								}
							}
						};  
				Thread t = new Thread(testRunnable);
				t.setName("Test Thread " + noTestThreads);
				t.start();
			}
		});

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
					.addPreferredGap(ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
					.addComponent(typeLabel))
				.addComponent(detailsPane, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
				.addComponent(interruptButton, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
				.addComponent(createButton, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
		);
		gl_rightPanel.setVerticalGroup(
			gl_rightPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(selectedLabel)
						.addComponent(typeLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(detailsPane, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(interruptButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(createButton)
					.addPreferredGap(ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
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
