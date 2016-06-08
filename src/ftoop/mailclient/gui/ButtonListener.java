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
import javax.swing.WindowConstants;

import ftoop.mailclient.daten.Mail;

public class ButtonListener implements ActionListener {
	
	private JSplitPane splitPane;
	private JPanel panelCenter;
	private static MailWindows mailWindows;
	private static String senden;
	private JTable table;
	private JButton send;
	private JButton neue;
	private JButton antworten;
	private JButton weiterleiten;
	private JButton loeschen;
	private JButton ordnerSync;
//	private MailControl mailControl;
	
	public ButtonListener(JSplitPane splitPane,JPanel panelCenter,JButton send,JButton neue,JButton antworten,
			JButton weiterleiten,JButton loeschen,JButton ordnerSync) {
		
		this.splitPane = splitPane;
		this.panelCenter = panelCenter;
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
	    	  panelCenter.add(new Konfiguration(send,neue,antworten,weiterleiten,loeschen,ordnerSync).getPanelUnten(),BorderLayout.NORTH);
	          splitPane.setRightComponent(panelCenter);
	    	  break;
	       case "Neue E-Mail":
	    	  mailWindows = new MailWindows(null,"neueMail");
	    	  mailWindows.getMsgPane().setContentType("text/plain");
	    	  panelCenter.add(mailWindows.getPanelUnten(),BorderLayout.CENTER);
	          splitPane.setRightComponent(panelCenter);
	          send.setEnabled(true);
	          senden = "neue";
	    	  break;
	       case "Löschen":
	    	   table = FolderSelectionListener.getCurrentTable();
	    	   row = table.getSelectedRow();
	    	   String msgId = FolderSelectionListener.getSelectedMail().getMessageID().replaceAll("[<>]","");
	    	   System.out.println(msgId);
	    	   if(table.getRowCount() > 0){
	    	   ((MailTableModel) table.getModel()).removeRow(row);
	    	   }
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
	    	  break;
	       case "Antworten":
	    	  mailWindows = new MailWindows(FolderSelectionListener.getSelectedMail(),"antworten");
	    	  panelCenter.add(mailWindows.getPanelUnten(),BorderLayout.CENTER);
	    	  splitPane.setRightComponent(panelCenter);
	    	  send.setEnabled(true);
	    	  senden = "antworten";
	    	  break;
	       case "Weiterleiten":
	    	  mailWindows = new MailWindows(FolderSelectionListener.getSelectedMail(),"weiterleiten");
		   	  panelCenter.add(mailWindows.getPanelUnten(),BorderLayout.CENTER);
		      splitPane.setRightComponent(panelCenter);
		      send.setEnabled(true);
		      senden = "weiterleiten";
	    	  break;
	       case "senden":
	    	  sendenMail(senden); 	
	    	  panelCenter.add(new JTextArea("MAIL  sent successfully"));
	    	  splitPane.setRightComponent(panelCenter);
	    	  splitPane.validate();
	   	     
		      break;
	       case "schliessen":
	    	   panelCenter = new JPanel();
	    	   splitPane.setRightComponent(panelCenter);
	    	   splitPane.validate();
	    	   send.setEnabled(false);
	       		  break;
	       default:
	    	  return;
	     }
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
