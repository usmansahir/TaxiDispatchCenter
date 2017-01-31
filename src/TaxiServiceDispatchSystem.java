

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.border.EmptyBorder;
import javax.swing.Timer;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;


public class TaxiServiceDispatchSystem extends JFrame {

	private JPanel contentPane;
	private ArrayList<ClientRequest> requests;
	private DispatchCenter dispatcher;
	private Timer timer = null;
	private int[][] stats;
	private int rate = 1;
	private JList<String> list;
	private JList<String> list_1;
	private JList<String> list_2;
	private JList<String> list_3;
	private JList<String> list_4;
	private JList<String> list_5;
	private JList<String> list_6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TaxiServiceDispatchSystem frame = new TaxiServiceDispatchSystem();
					frame.setVisible(true);
					frame.setTitle("Taxi Service Dispatching System");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TaxiServiceDispatchSystem() {
		stats = new int[6][6];
		dispatcher = new DispatchCenter(50);
		requests = new ArrayList<ClientRequest>();
		list = new JList<String>(getModel(requests));
		list_1 = new JList<String>(getModel(dispatcher.getTaxis("Downtown")));
		list_2 = new JList<String>(getModel(dispatcher.getTaxis("Airport")));
		list_3 = new JList<String>(getModel(dispatcher.getTaxis("North")));
		list_4 = new JList<String>(getModel(dispatcher.getTaxis("South")));
		list_5 = new JList<String>(getModel(dispatcher.getTaxis("East")));
		list_6 = new JList<String>(getModel(dispatcher.getTaxis("West")));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 873, 464);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setToolTipText("aeg");
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Simulation");
		menuBar.add(mnNewMenu);

		final ActionListener timerListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ClientRequest request = new ClientRequest(dispatcher
						.getArea((int) (6 * Math.random())), dispatcher
						.getArea((int) (6 * Math.random())));
				requests.add(request);
				list.setModel(getModel(requests));
				ArrayList<Taxi> busy = dispatcher.getBusyTaxis();
				if (busy != null) {
					for (int i = 0; i < busy.size(); i++) {
						Taxi t = busy.get(i);
						t.setEstimatedTimeToDest(t
								.getEstimatedTimeToDestination() - 1);
						if (t.getEstimatedTimeToDestination() == 0) {
							t.setAvailable(true);
							int m = dispatcher.checkArea(t
									.getDeparture());
							int n = dispatcher.checkArea(t
									.getDestination());
							stats[m][n]++;
						}
					}
				}
				updateFields();
			}

		};
		JMenuItem mntmNewMenuItem = new JMenuItem("Start Clients Coming");
		final JFrame temp = this;
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				timer = new Timer(1000 / rate, timerListener);
				timer.start();
			}
		});
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Stop Clients Coming");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (timer != null) {
					timer.stop();
					timer = null;
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);

		JMenu mnNewMenu_2 = new JMenu("Settings");
		menuBar.add(mnNewMenu_2);

		JMenuItem mntmSimulationRate = new JMenuItem("Simulation Rate");
		mntmSimulationRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = JOptionPane
						.showInputDialog("Iterations per second (1 to 20)");
				try {
					if (text != null) {
						int input = Integer.valueOf(text);
						if ((input > 0) && (input < 21)) {
							rate = input;
							if (timer.isRunning()) {
								timer = new Timer(1000 / rate, timerListener);
								timer.restart();
							}
						} else {
							throw new NumberFormatException();
						}
					} else {
						throw new NumberFormatException();
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(temp, "Input error!",
							"Iterations per second (1 to 20)",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnNewMenu_2.add(mntmSimulationRate);

		JMenu mnNewMenu_3 = new JMenu("Admin");
		menuBar.add(mnNewMenu_3);

		JMenuItem mntmStatistics = new JMenuItem("Statistics");
		mntmStatistics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Statistics dialog = new Statistics(stats);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnNewMenu_3.add(mntmStatistics);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblNewLabel = new JLabel("Incoming");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);

		JLabel lblNewLabel_3 = new JLabel("North");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 3;
		gbc_lblNewLabel_3.gridy = 0;
		panel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("South");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 4;
		gbc_lblNewLabel_4.gridy = 0;
		panel.add(lblNewLabel_4, gbc_lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("East");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 5;
		gbc_lblNewLabel_5.gridy = 0;
		panel.add(lblNewLabel_5, gbc_lblNewLabel_5);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panel.add(scrollPane, gbc_scrollPane);

		scrollPane.setViewportView(list);

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 1;
		panel.add(scrollPane_1, gbc_scrollPane_1);

		scrollPane_1.setViewportView(list_1);

		JLabel lblNewLabel_6 = new JLabel("West");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.gridy = 0;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_6.gridx = 6;
		panel.add(lblNewLabel_6, gbc_lblNewLabel_6);

		JScrollPane scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 2;
		gbc_scrollPane_2.gridy = 1;
		panel.add(scrollPane_2, gbc_scrollPane_2);

		scrollPane_2.setViewportView(list_2);

		JScrollPane scrollPane_3 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_3 = new GridBagConstraints();
		gbc_scrollPane_3.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_3.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_3.gridx = 3;
		gbc_scrollPane_3.gridy = 1;
		panel.add(scrollPane_3, gbc_scrollPane_3);

		scrollPane_3.setViewportView(list_3);

		JScrollPane scrollPane_4 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_4 = new GridBagConstraints();
		gbc_scrollPane_4.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_4.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_4.gridx = 4;
		gbc_scrollPane_4.gridy = 1;
		panel.add(scrollPane_4, gbc_scrollPane_4);

		scrollPane_4.setViewportView(list_4);

		JScrollPane scrollPane_5 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_5 = new GridBagConstraints();
		gbc_scrollPane_5.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_5.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_5.gridx = 5;
		gbc_scrollPane_5.gridy = 1;
		panel.add(scrollPane_5, gbc_scrollPane_5);

		scrollPane_5.setViewportView(list_5);

		JLabel lblNewLabel_1 = new JLabel("Downtown");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 0;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Airport");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.ABOVE_BASELINE;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 2;
		gbc_lblNewLabel_2.gridy = 0;
		panel.add(lblNewLabel_2, gbc_lblNewLabel_2);

		JScrollPane scrollPane_6 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_6 = new GridBagConstraints();
		gbc_scrollPane_6.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_6.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_6.gridx = 6;
		gbc_scrollPane_6.gridy = 1;
		panel.add(scrollPane_6, gbc_scrollPane_6);

		scrollPane_6.setViewportView(list_6);

		JButton btnNewButton = new JButton("Dispatch");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (dispatcher.sendTaxiForRequest(requests.get(0)) != null) {
					requests.remove(0);
					updateFields();
				}

			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 2;
		panel.add(btnNewButton, gbc_btnNewButton);
	}

	public DefaultListModel getModel(ArrayList data) {
		DefaultListModel model = new DefaultListModel();
		for (int i = 0; i < data.size(); i++) {
			model.addElement(data.get(i).toString());
		}
		return model;
	}

	public void updateFields() {
		list.setModel(getModel(requests));
		list_1.setModel(getModel(dispatcher.getTaxis("Downtown")));
		list_2.setModel(getModel(dispatcher.getTaxis("Airport")));
		list_3.setModel(getModel(dispatcher.getTaxis("North")));
		list_4.setModel(getModel(dispatcher.getTaxis("South")));
		list_5.setModel(getModel(dispatcher.getTaxis("East")));
		list_6.setModel(getModel(dispatcher.getTaxis("West")));
	}
}
