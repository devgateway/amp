package dgfoundation.amp.seleniumTest;
import java.awt.Rectangle;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;


public class UIRunner extends JApplet {

    private JPanel jContentPane = null;
    private JButton jButton = null;
    private JList jList = null;
    private JButton jButton1 = null;
    private JButton jButton11 = null;
    private JLabel jLabel = null;
    /**
     * This is the xxx default constructor
     */
    public UIRunner() {
        super();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    public void init() {
        this.setSize(404, 265);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(195, 21, 111, 16));
            jLabel.setText("<--- Select the test cases ");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButton(), null);
            jContentPane.add(getJList(), null);
            jContentPane.add(getJButton1(), null);
            jContentPane.add(getJButton11(), null);
            jContentPane.add(jLabel, null);
        }
        return jContentPane;
    }

    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Run Test");
            jButton.setBounds(new Rectangle(216, 151, 140, 32));
            jButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                SeleniumTestRunner runner=  new SeleniumTestRunner();
                    try {
                        runner.setUp();
                        runner.runTestSuite();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    
                }
            });
        }
        return jButton;
    }

    /**
     * This method initializes jList    
     *  
     * @return javax.swing.JList    
     */
    private JList getJList() {
        if (jList == null) {
            jList = new JList();
            jList.setBounds(new Rectangle(2, 2, 138, 259));
        }
        return jList;
    }

    /**
     * This method initializes jButton1 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setBounds(new Rectangle(217, 188, 148, 24));
            jButton1.setText("Upload Results");
        }
        return jButton1;
    }

    /**
     * This method initializes jButton11    
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButton11() {
        if (jButton11 == null) {
            jButton11 = new JButton();
            jButton11.setBounds(new Rectangle(218, 222, 150, 26));
            jButton11.setText("View Results");
        }
        return jButton11;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
