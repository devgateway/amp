package monetmonitor;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;

public class StatusShowerGUI implements StatusShower {
	
/*status shower that updates the label in GUI*/	
	public void showStatus(final String  status) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MonetMonitorGUI.updateServerStatus(status);
			}
		});
	}

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
 