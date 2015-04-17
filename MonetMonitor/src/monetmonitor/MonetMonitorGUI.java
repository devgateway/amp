package monetmonitor;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author acartaleanu
 * Swing window for displaying status
 * */
public class MonetMonitorGUI {

	private static MonitorFrame frame;
	public MonetMonitorGUI() {
		frame = new MonitorFrame();
	}

	public void showFrame() {
		frame.setVisible(true);
	}

	public static void updateServerStatus(String status) {
		frame.statusLabel.setText(status);
	}
	
	@SuppressWarnings("serial")
	class MonitorFrame extends JFrame {
		JLabel statusLabel;

		private void initUI() {
	        statusLabel = new JLabel("Unknown");
	        GridLayout g1 =new GridLayout(4,3);
	        JPanel panel = new JPanel();
	        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	        panel.setLayout(g1);
	        add(panel);
	        panel.add(statusLabel);
			setTitle("MonetDB monitor");
			setSize(300, 200);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	    public MonitorFrame() {
	    	initUI();
	    }
	}
}