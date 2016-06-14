package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import ftoop.mailclient.daten.Mail;

public class ButtonListener implements ActionListener {
	
	public final static String SENDEN = "senden";
	private JSplitPane splitPane;
	private JPanel panelCenter;
	private MailPane mailWindows;
	private MainView mailClient;
	private static String senden;
	private JTable table;
	private JButton send;
	private JButton neue;
	private JButton antworten;
	private JButton weiterleiten;
	private JButton loeschen;
	private JButton ordnerSync;
	private JFrame newMailFrame;
	private final FolderSelectionListener folderSelectionListener;
//	private MailControl mailControl;
	
	public ButtonListener(JSplitPane splitPaneBis,JPanel panelCenterBis,JButton send,JButton neue,JButton antworten,
			JButton weiterleiten,JButton loeschen,JButton ordnerSync, MainView mailClient, FolderSelectionListener fSL) {
		
		splitPane = splitPaneBis;
		panelCenter = panelCenterBis;
		this.send = send;
		this.neue = neue;
		this.antworten = antworten;
		this.weiterleiten = weiterleiten;
		this.loeschen = loeschen;
		this.ordnerSync = ordnerSync;
		this.mailClient = mailClient;
		this.folderSelectionListener = fSL;
		
	}
	
	
	  public void actionPerformed(ActionEvent e) {
		int row;  
		panelCenter = new JPanel(new BorderLayout(5,5));   
		String str =e.getActionCommand();
		switch (str) {
	       case "Ordner synchronisieren":
	    	   mailClient.synchronizeFolders();
	    	  break;
	       case "Konfiguration":
	    	  panelCenter.add(new ConfigurationPanel(),BorderLayout.NORTH);
//	    	  resizing();
	    	  break;
	       case "Neue E-Mail":	    	  
//	    	  mailWindows = new MailWindows(null,"neueMail", this.mailClient );
//	    	  mailWindows.getMsgPane().setContentType("text/plain");
	    	  panelCenter.add(mailWindows,BorderLayout.CENTER);
	          send.setEnabled(true);
	          senden = "neue";
//	          newMailFrame = new PopupMailFrame("Neue E-Mail senden",mailWindows,PopupMailFrame.NEW_MAIL_FRAME, this);
	    	  break;
	       case "Löschen":
	    	   table = this.folderSelectionListener.getCurrentTable();
	    	//   row = table.getSelectedRow();
	    	  row =  table.convertRowIndexToModel(table.getSelectedRow());
	    	   String msgId = this.folderSelectionListener.getSelectedMail().getMessageID().replaceAll("[<>]","");
	    	   System.out.println(msgId);
	    	   if(table.getRowCount() > 0){
	    	   ((MailTableModel) table.getModel()).removeRow(row);
	    	   }
    	   //Thread work um mail zu löschen 	
	       SwingWorker<Void,Void> workerLoeschen = new SwingWorker<Void,Void>() {
	    			      @Override
	    			      protected Void doInBackground()
	    			      {
	    			      	
	  	    try {
	    			   
	    			
	  	    	folderSelectionListener.getSelectedMailControl().deleteMail(folderSelectionListener.getSelectedMail().getMessageID());
	  	    	
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchElementException e2){
				String message = e2.getMessage();
				String header = "Fehler";
				JFrame frame = new JFrame();
				frame.setSize(300,125);
				frame.setLayout(new GridBagLayout());
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.gridx = 0;
				constraints.gridy = 0;
				constraints.weightx = 1.0f;
				constraints.weighty = 1.0f;
				constraints.insets = new Insets(5, 5, 5, 5);
				constraints.fill = GridBagConstraints.BOTH;
				JLabel headingLabel = new JLabel(header);
				headingLabel.setOpaque(false);
				frame.add(headingLabel, constraints);
				constraints.gridx++;
				constraints.weightx = 0f;
				constraints.weighty = 0f;
				constraints.fill = GridBagConstraints.NONE;
				constraints.anchor = GridBagConstraints.NORTH;
				JButton cloesButton = new JButton("X");
				cloesButton.setMargin(new Insets(1, 4, 1, 4));
				cloesButton.setFocusable(false);
				frame.add(cloesButton, constraints);
				constraints.gridx = 0;
				constraints.gridy++;
				constraints.weightx = 1.0f;
				constraints.weighty = 1.0f;
				constraints.insets = new Insets(5, 5, 5, 5);
				constraints.fill = GridBagConstraints.BOTH;
				JLabel messageLabel = new JLabel("<HtMl>"+message);
				frame.add(messageLabel, constraints);
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
	        return null;
	    			      }
	    			   
	    			 
	    			  };
	    			  workerLoeschen.execute();
	    	  break;
	       case "Antworten":
//	    	  mailWindows = new MailWindows(FolderSelectionListener.getSelectedMail(),"antworten", this.mailClient);
//	    	  panelCenter.add(mailWindows,BorderLayout.CENTER);
//	    	  newMailFrame = new PopupMailFrame("E-Mail antworten",panelCenter);
	    	  send.setEnabled(true);
	    	  senden = "antworten";
	    	  break;
	       case "Weiterleiten":
//	    	  mailWindows = new MailWindows(FolderSelectionListener.getSelectedMail(),"weiterleiten", this.mailClient);
		   	  panelCenter.add(mailWindows,BorderLayout.CENTER);
//		   	  newMailFrame = new PopupMailFrame("E-Mail weiterleiten",panelCenter);
		      send.setEnabled(true);
		      senden = "weiterleiten";
	    	  break;
	       case SENDEN:
	    	  sendeMail(senden); 	
	    	  panelCenter.add(new JTextArea("MAIL  sent successfully"));
	    	  newMailFrame.dispose();;
//	    	  resizing();
	    	  splitPane.validate();
	   	     
		      break;
	       case "schliessen":
	    	   panelCenter = new JPanel();
//	    	   resizing();
	    	   splitPane.validate();
	    	   send.setEnabled(false);
	    	   loeschen.setEnabled(false);
	    	   weiterleiten.setEnabled(false);
	    	   antworten.setEnabled(false);
	       	   break;
	       default:
	    	  return;
	     }
	  }
	  
//   public static void resizing() {
//	   int pos = splitPane.getDividerLocation();
//       splitPane.setRightComponent(panelCenter);
//		  splitPane.setDividerLocation(pos);
//   }
//	
   public void sendeMail(String aktion) {
	  
		 switch (aktion) {
	       case "antworten":
	    	 if(this.folderSelectionListener.getSelectedMail()!=null && this.folderSelectionListener.getSelectedMailControl()!=null) {
	    	   try {
				   Mail mail = this.folderSelectionListener.getSelectedMail();
				   String from = mail.getFrom();
				   String to = mail.getTo();
				   mail.setTo(from.replaceAll(";",""));
				   mail.setFrom(to.replaceAll(";",""));
				   mail.setMessage(mailWindows.getMessageText());
			//	   mail.setMessage(mailWindows.getMsgPane().getText());
				   System.out.println(mail.getFrom());
				   System.out.println(mailWindows.getMessageText());
				   this.folderSelectionListener.getSelectedMailControl().sendMsg(mail);
			   } catch (NoSuchProviderException e1) {
				   // TODO Auto-generated catch block
			 	   e1.printStackTrace();
			     }
	    	 }
	    	   break;
	       case "weiterleiten":
	    	 if(this.folderSelectionListener.getSelectedMail()!=null && this.folderSelectionListener.getSelectedMailControl()!=null) {
	    	   try {
				   Mail mail = this.folderSelectionListener.getSelectedMail();
				   String to = mail.getTo();
				   String from = mailWindows.getAn();
				   mail.setTo(from);
				   mail.setFrom(to.replaceAll(";",""));
			//	   mail.setMessage(mailWindows.getMsgPane().getText());
				   System.out.println(mail.getFrom());
				   System.out.println(mailWindows.getMessageText());
				   this.folderSelectionListener.getSelectedMailControl().sendMsg(mail);
			   } catch (NoSuchProviderException e1) {
				   // TODO Auto-generated catch block
			 	   e1.printStackTrace();
			     }
	    	 }  
		    	  break;
	       case "neue":
	    	   try {
	    		   System.out.println(mailWindows.getAn());
				   String to = mailWindows.getAn();
				   String from = this.folderSelectionListener.getSelectedMailControl().getCurrentKonto().getEmail().replaceAll(";","");
				   String subject = mailWindows.getBetreff();
				  System.out.println(mailWindows.getMessageText());
				   String msg = mailWindows.getMessageText();
				   Mail mail = new Mail(null, null, to, null, null, from, subject,msg, null);
				   this.folderSelectionListener.getSelectedMailControl().sendMsg(mail);
			   } catch (NoSuchProviderException e1) {
				   // TODO Auto-generated catch block
			 	   e1.printStackTrace();
			     }
		    	  break;
	    	  
		 }	   

   }
   
		 	
}
