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
	
	private static JSplitPane splitPane;
	private static JPanel panelCenter;
	private static MailWindows mailWindows;
	private static String senden;
	private JTable table;
	private JButton send;
	private JButton neue;
	private JButton antworten;
	private JButton weiterleiten;
	private JButton loeschen;
	private JButton ordnerSync;
	private static JFrame newMailFrame;
//	private MailControl mailControl;
	
	public ButtonListener(JSplitPane splitPaneBis,JPanel panelCenterBis,JButton send,JButton neue,JButton antworten,
			JButton weiterleiten,JButton loeschen,JButton ordnerSync) {
		
		splitPane = splitPaneBis;
		panelCenter = panelCenterBis;
		this.send = send;
		this.neue = neue;
		this.antworten = antworten;
		this.weiterleiten = weiterleiten;
		this.loeschen = loeschen;
		this.ordnerSync = ordnerSync;
		
	}
	
	
	  public void actionPerformed(ActionEvent e) {
		int row;  
		panelCenter = new JPanel(new BorderLayout(5,5));   
		String str =e.getActionCommand();
		switch (str) {
	       case "Ordner synchronisieren":
	    	  MainView.init(false);
	    	  break;
	       case "Konfiguration":
	    	  panelCenter.add(new Konfiguration(send,neue,antworten,weiterleiten,loeschen,
	    			  ordnerSync).getPanelUnten(),BorderLayout.NORTH);
	    	  resizing();
	    	  break;
	       case "Neue E-Mail":	    	  
	    	  mailWindows = new MailWindows(null,"neueMail");
	    	  mailWindows.getMsgPane().setContentType("text/plain");
	    	  panelCenter.add(mailWindows.getPanelUnten(),BorderLayout.CENTER);
	          send.setEnabled(true);
	          senden = "neue";
	          newMailFrame = new NewMailFrame("Neue E-Mail senden",panelCenter);
	    	  break;
	       case "Löschen":
	    	   table = FolderSelectionListener.getCurrentTable();
	    	//   row = table.getSelectedRow();
	    	  row =  table.convertRowIndexToModel(table.getSelectedRow());
	    	   String msgId = FolderSelectionListener.getSelectedMail().getMessageID().replaceAll("[<>]","");
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
	    			   
	    			
	  	    	   FolderSelectionListener.getSelectedMailControl().deleteMail(FolderSelectionListener.getSelectedMail().getMessageID());
	  	    	
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
	    	  mailWindows = new MailWindows(FolderSelectionListener.getSelectedMail(),"antworten");
	    	  panelCenter.add(mailWindows.getPanelUnten(),BorderLayout.CENTER);
	    	  newMailFrame = new NewMailFrame("E-Mail antworten",panelCenter);
	    	  send.setEnabled(true);
	    	  senden = "antworten";
	    	  break;
	       case "Weiterleiten":
	    	  mailWindows = new MailWindows(FolderSelectionListener.getSelectedMail(),"weiterleiten");
		   	  panelCenter.add(mailWindows.getPanelUnten(),BorderLayout.CENTER);
		   	  newMailFrame = new NewMailFrame("E-Mail weiterleiten",panelCenter);
		      send.setEnabled(true);
		      senden = "weiterleiten";
	    	  break;
	       case "senden":
	    	  sendenMail(senden); 	
	    	  panelCenter.add(new JTextArea("MAIL  sent successfully"));
	    	  newMailFrame.dispose();;
	    	  resizing();
	    	  splitPane.validate();
	   	     
		      break;
	       case "schliessen":
	    	   panelCenter = new JPanel();
	    	   resizing();
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
	  
   public static void resizing() {
	   int pos = splitPane.getDividerLocation();
       splitPane.setRightComponent(panelCenter);
		  splitPane.setDividerLocation(pos);
   }
	
   public void sendenMail(String aktion) {
	  
		 switch (aktion) {
	       case "antworten":
	    	 if(FolderSelectionListener.getSelectedMail()!=null && FolderSelectionListener.getSelectedMailControl()!=null) {
	    	   try {
				   Mail mail = FolderSelectionListener.getSelectedMail();
				   String from = mail.getFrom();
				   String to = mail.getTo();
				   mail.setTo(from.replaceAll(";",""));
				   mail.setFrom(to.replaceAll(";",""));
				   mail.setMessage(mailWindows.getMsgPane().getText());
			//	   mail.setMessage(mailWindows.getMsgPane().getText());
				   System.out.println(mail.getFrom());
				   System.out.println(mailWindows.getMsgPane().getText());
				   FolderSelectionListener.getSelectedMailControl().sendMsg(mail);
			   } catch (NoSuchProviderException e1) {
				   // TODO Auto-generated catch block
			 	   e1.printStackTrace();
			     }
	    	 }
	    	   break;
	       case "weiterleiten":
	    	 if(FolderSelectionListener.getSelectedMail()!=null && FolderSelectionListener.getSelectedMailControl()!=null) {
	    	   try {
				   Mail mail = FolderSelectionListener.getSelectedMail();
				   String to = mail.getTo();
				   String from = mailWindows.getTxtAn().getText();
				   mail.setTo(from);
				   mail.setFrom(to.replaceAll(";",""));
			//	   mail.setMessage(mailWindows.getMsgPane().getText());
				   System.out.println(mail.getFrom());
				   System.out.println(mailWindows.getMsgPane().getText());
				   FolderSelectionListener.getSelectedMailControl().sendMsg(mail);
			   } catch (NoSuchProviderException e1) {
				   // TODO Auto-generated catch block
			 	   e1.printStackTrace();
			     }
	    	 }  
		    	  break;
	       case "neue":
	    	   try {
	    		   System.out.println(mailWindows.getTxtAn().getText());
				   String to = mailWindows.getTxtAn().getText();
				   String from = FolderSelectionListener.getSelectedMailControl().getCurrentKonto().getEmail().replaceAll(";","");
				   String subject = mailWindows.getTxtBetreff().getText();
				  System.out.println(mailWindows.getMsgPane().getText());
				   String msg = mailWindows.getMsgPane().getText();
				   Mail mail = new Mail(null, null, to, null, null, from, subject,msg, null);
				   FolderSelectionListener.getSelectedMailControl().sendMsg(mail);
			   } catch (NoSuchProviderException e1) {
				   // TODO Auto-generated catch block
			 	   e1.printStackTrace();
			     }
		    	  break;
	    	  
		 }	   

   }
   
		 	
}
