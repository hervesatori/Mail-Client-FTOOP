package ftoop.mailclient.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class NewMailFrame extends JFrame {
	
	/**
	 *   new Fenster für neue , antworten und weiterleiten von Mails
	 */
	private static final long serialVersionUID = 1L;
	
	public NewMailFrame(String titel, JPanel newPanelMail) {
		
        this.add(newPanelMail);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAutoRequestFocus(true);
        this.setTitle(titel);
        this.pack();
        this.setVisible(true);
	}

}
