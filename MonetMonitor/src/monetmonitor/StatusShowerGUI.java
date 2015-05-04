package monetmonitor;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;
/**
 * @author acartaleanu
 * status shower that updates the label in GUI
 */	
public class StatusShowerGUI implements StatusShower {
	
	/**
	 * @param status String to update the label in the middle of the window 
	 */
	public void showStatus(final String  status) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MonetMonitorGUI.updateServerStatus(status);
			}
		});
	}
	/**
	 * initializes the Swing window (configured in MonetMonitorGUI)
	 */
	@Override
	public void start() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MonetMonitorGUI ex = new MonetMonitorGUI();
                ex.showFrame();
            }
        });
	}
}
 