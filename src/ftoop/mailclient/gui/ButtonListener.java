package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import ftoop.mailclient.daten.Mail;

public class ButtonListener implements ActionListener {
	
	private JSplitPane splitPane;
	private JPanel panelCenter;
	private static MailWindows mailWindows;
	private static String senden;
	private JTable table;
//	private MailControl mailControl;
	
	public ButtonListener(JSplitPane splitPane,JPanel panelCenter) {
		
		this.splitPane = splitPane;
		this.panelCenter = panelCenter;
		
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
	    	  panelCenter.add(new Konfiguration().getPanelUnten(),BorderLayout.NORTH);
	          splitPane.setRightComponent(panelCenter);
	    	  break;
	       case "Neue E-Mail":
	    	  mailWindows = new MailWindows(null,"neueMail");
	    	  mailWindows.getMsgPane().setContentType("text/plain");
	    	  panelCenter.add(mailWindows.getPanelUnten(),BorderLayout.CENTER);
	          splitPane.setRightComponent(panelCenter);
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
			}
	    	  break;
	       case "Antworten":
	    	  mailWindows = new MailWindows(FolderSelectionListener.getSelectedMail(),"antworten");
	    	  panelCenter.add(mailWindows.getPanelUnten(),BorderLayout.CENTER);
	    	  splitPane.setRightComponent(panelCenter);
	    	  senden = "antworten";
	    	  break;
	       case "Weiterleiten":
	    	  mailWindows = new MailWindows(FolderSelectionListener.getSelectedMail(),"weiterleiten");
		   	  panelCenter.add(mailWindows.getPanelUnten(),BorderLayout.CENTER);
		      splitPane.setRightComponent(panelCenter);
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
