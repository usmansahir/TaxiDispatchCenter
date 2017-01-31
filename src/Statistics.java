

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JList;

public class Statistics extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private String[] area = { "North", "South", "East", "West", "Downtown",
	"Airport" };

	private int[][] stats;

	/**
	 * Create the dialog.
	 */
	public Statistics(int[][] s) {
		stats = s;
		setBounds(100, 100, 667, 421);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridLayout gbl_contentPanel = new GridLayout(stats.length+1, stats.length+1, 0,0);
		contentPanel.setLayout(gbl_contentPanel);
		contentPanel.add(new JLabel("FROM/TO"));
		JList[][] lists = new JList[stats.length][stats.length];

		int max = getMax(s);
		int min = getMin(s);
		for (int i = 1; i <= stats.length; i++ ){
			contentPanel.add(new JLabel(area[i-1]));
		}
		for (int i = 0; i < stats.length; i++ ) {
			for (int j = 0; j <= stats.length; j++ ) {
				if (j == 0) {
					contentPanel.add(new JLabel(area[i]));
				} else {
					lists[i][j-1] = new JList(getModel(stats[i][j-1]));
					lists[i][j-1].setEnabled(false);
					if (stats[i][j-1] == max) {
						lists[i][j-1].setBackground(Color.green);
					}
					if (stats[i][j-1] == min) {
						lists[i][j-1].setBackground(Color.red);
					}
					contentPanel.add(lists[i][j-1]);
				}
			}
		}

	}

	public int getMax(int [][] s) {
		int max = 0;
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s.length; j++) {
				if (s[i][j] > max) {
					max = s[i][j];
				}
			}
		}
		return max;
	}

	public int getMin(int [][] s) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s.length; j++) {
				if (s[i][j] < min) {
					min = s[i][j];
				}
			}
		}
		return min;
	}

	public DefaultListModel<Integer> getModel(int data) {
		DefaultListModel<Integer> model = new DefaultListModel<Integer>();
		model.addElement(data);
		return model;
	}
}
