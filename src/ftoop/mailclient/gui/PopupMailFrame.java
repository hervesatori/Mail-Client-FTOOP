package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JTable;

import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailControl;

public class PopupMailFrame extends JFrame {
	public static final int NEW_MAIL_FRAME = 1;
	public static final int READING_MAIL_FRAME = 2;	
	private MailPane mailWindow;
	private FolderSelectionListener folderSelectionListener;
	private final Mail currentMail;
	private final JTable mailTable;
	private final MailControl currentMc;
	/**
	 *   Popup Fenster für neue, antworten und weiterleiten von Mails, ebenso als lese Mailframe
	 */
	private static final long serialVersionUID = 1L;
	private String titel;
	
	public PopupMailFrame(String titel, MailPane mailWindow, int popupMailFrameType, FolderSelectionListener folderSelectionListener) {
		this.titel = titel;
		this.mailWindow = mailWindow;
		this.folderSelectionListener = folderSelectionListener;		
		currentMail = this.folderSelectionListener.getSelectedMail();
		mailTable = this.folderSelectionListener.getCurrentTable();
		currentMc = folderSelectionListener.getSelectedMailControl();
        this.initFrame(popupMailFrameType, mailWindow);
	}
	private void initFrame(int popupMailFrameType, MailPane mailWindow){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAutoRequestFocus(true);
        this.setTitle(titel);
        switch(popupMailFrameType){
	        case NEW_MAIL_FRAME:
	        	System.out.println("NEW_MAIL_FRAME selected");	    		
	            this.add(this.createNewMailMenuBar(), BorderLayout.NORTH);
	        	break;
	        case READING_MAIL_FRAME:
	        	System.out.println("READING_MAIL_FRAME selected");	    		
	            this.add(this.createMenuBar(), BorderLayout.NORTH);	            
	        	break;
	        default: 
	        	this.add(new JLabel("Fehler bei der Auswahl des popupMailFrameType"));
	        	break;
        }
        this.add(mailWindow, BorderLayout.CENTER);
	  //Hole Monitorresolution
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		this.setPreferredSize(new Dimension((int) (Math.round(width*0.6)),(int) (Math.round(height*0.6))));
        this.pack();
        this.setVisible(true);
        this.toFront();
	}
	private JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
	    JButton senden = new JButton("senden");
		JButton loeschen = new JButton("Löschen");
		JButton antworten = new JButton("Antworten");
		JButton weiterLeiten = new JButton("Weiterleiten");

		senden.setEnabled(false);
		antworten.setEnabled(true);
		weiterLeiten.setEnabled(true);
		loeschen.setEnabled(true);
		
		senden.addActionListener(MailButtonListenerTool.getSendenActionListener(this,mailWindow));
		loeschen.addActionListener(MailButtonListenerTool.getDeleteMailActionListener(currentMail, mailTable, currentMc, folderSelectionListener));
		loeschen.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PopupMailFrame.this.dispose();				
			}
			
		});
		antworten.addActionListener(MailButtonListenerTool.getRespondToMailActionListener(currentMail, currentMc,folderSelectionListener));
		antworten.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PopupMailFrame.this.dispose();				
			}
			
		});
		weiterLeiten.addActionListener(MailButtonListenerTool.getForwardToMailActionListener(currentMail, currentMc,folderSelectionListener));
		weiterLeiten.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PopupMailFrame.this.dispose();				
			}
			
		});
		//Die Buttons werden an JMenuBar angehängt
		menuBar.add(senden);
		menuBar.add(loeschen);
		menuBar.add(antworten);
		menuBar.add(weiterLeiten);
		return menuBar;
	}
	private JMenuBar createNewMailMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
	    JButton senden = new JButton("senden");
	    senden.addActionListener(MailButtonListenerTool.getSendenActionListener(this,mailWindow));
		senden.setEnabled(true);
	
		//Die Buttons werden an JMenuBar angehängt
		menuBar.add(senden);
		return menuBar;
	}	
}
