package ftoop.mailclient.daten;

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
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import ftoop.mailclient.gui.FolderSelectionListener;
import ftoop.mailclient.gui.MailPane;
import ftoop.mailclient.gui.MailTableModel;
import ftoop.mailclient.gui.PopupMailFrame;

public class MailButtonListenerTool {
	
	private MailButtonListenerTool(){
		
	}
	public static ActionListener getSendenActionListener(MailPane mW){
		final MailPane mailWindow = mW;
		final ActionListener sendenAL = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
		    	   try {
		    		   System.out.println("Versende nun Mail an: " + mailWindow.getAn() + "mit Inhalt: "+mailWindow.getMessageText());
					   String to = mailWindow.getAn();
					   String from = mailWindow.getCurrentMC().getCurrentKonto().getEmail();//FolderSelectionListener.getSelectedMailControl().getCurrentKonto().getEmail().replaceAll(";","");
					   String subject = mailWindow.getBetreff();
					   String msg = mailWindow.getMessageText();
					   Mail mail = new Mail(null, null, to, null, null, from, subject,msg, null);
					   //Mail ist erstellt, versende diese
					   mailWindow.getCurrentMC().sendMsg(mail);
				   } catch (NoSuchProviderException e1) {
				 	   e1.printStackTrace();
				     }
			}
			
		};
		return sendenAL;		
	}
	public static ActionListener getSendenActionListener(PopupMailFrame pMF,MailPane mW){
		final ActionListener sendenAL = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendNewMail(mW);
				pMF.dispose();
			}
			
		};
		return sendenAL;		
	}
	private static String convertMailNamestoNormalMail(String singleTo){
		   String tempTo ="";
		   if(singleTo.contains("<") && singleTo.contains(">")){
			   singleTo =  singleTo.split("<")[1];
			   singleTo = singleTo.split(">")[0];	
			   tempTo += singleTo;
			  }else{
				  tempTo += singleTo;
			  }
		   return tempTo;
	}
	private static void sendNewMail(MailPane mailWindow){
 	   try {
		   System.out.println("Versende nun Mail an: " + mailWindow.getAn() + "mit Inhalt: "+mailWindow.getMessageText());
		   String to = mailWindow.getAn();		
		   if(to.contains(";")){
			   //Auflösen von Namen zu Mailadressen: Hans Ueli <hans_ueli@yahoo.fr>; -> hans_ueli@yahoo.fr
			   String tempTo = "";
			   for(String singleTo : to.split(";")){
				   tempTo += convertMailNamestoNormalMail(singleTo);
			   }
			   to = tempTo;
		   }else{
			   //Einzelne Mailadresse
			   to = convertMailNamestoNormalMail(to);
		   }
		   
		   String from = mailWindow.getCurrentMC().getCurrentKonto().getEmail();//FolderSelectionListener.getSelectedMailControl().getCurrentKonto().getEmail().replaceAll(";","");
		   String subject = mailWindow.getBetreff();
		   String msg = mailWindow.getMessageText();
		   Mail mail = new Mail(null, null, to, null, null, from, subject,msg, null);
		   //Mail ist erstellt, versende diese
		   mailWindow.getCurrentMC().sendMsg(mail);
	   } catch (NoSuchProviderException e1) {
	 	   e1.printStackTrace();
	     }
	}
	public static ActionListener getNewMailActionListener(FolderSelectionListener fSL){
		final FolderSelectionListener folderSelectionListener = fSL;
		final ActionListener newMailAL = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MailControl currentMailControl = folderSelectionListener.getSelectedMailControl();
				MailPane mailWindows = new MailPane(null,MailPane.MailWindowType.NEW,currentMailControl);
				PopupMailFrame newMailFrame = new PopupMailFrame("Neue E-Mail senden",mailWindows,PopupMailFrame.NEW_MAIL_FRAME);
			}
			
		};
		return newMailAL;
	}
	public static ActionListener getRespondToMailActionListener(FolderSelectionListener folderSelectionListener){
		final ActionListener respondMailAL = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MailControl currentMailControl = folderSelectionListener.getSelectedMailControl();
				Mail mail = folderSelectionListener.getSelectedMail();
				MailPane mailWindows = new MailPane(mail,MailPane.MailWindowType.RESPOND,currentMailControl);
				PopupMailFrame newMailFrame = new PopupMailFrame("Auf E-Mail antworten",mailWindows,PopupMailFrame.NEW_MAIL_FRAME);
				

			}
			
		};
		return respondMailAL;
	}
	public static ActionListener getForwardToMailActionListener(FolderSelectionListener folderSelectionListener){
		final ActionListener forwardMailAL = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MailControl currentMailControl = folderSelectionListener.getSelectedMailControl();
				Mail mail = folderSelectionListener.getSelectedMail();
				MailPane mailWindows = new MailPane(mail,MailPane.MailWindowType.FORWARD,currentMailControl);
				PopupMailFrame newMailFrame = new PopupMailFrame("E-Mail weiterleiten",mailWindows,PopupMailFrame.NEW_MAIL_FRAME);
				
				

			}
			
		};
		return forwardMailAL;
	}	
	public static ActionListener getDeleteMailActionListener(FolderSelectionListener folderSelectionListener){
		final ActionListener deleteMailAL = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {	

				Mail mailToDelete = folderSelectionListener.getSelectedMail();
				JTable table = folderSelectionListener.getCurrentTable();
			    final int tRow = table.getSelectedRow();
			    final int modelRow =  table.convertRowIndexToModel(tRow);
				
				String msgId = folderSelectionListener.getSelectedMail().getMessageID().replaceAll("[<>]","");
				System.out.println("Mail mit ID: "+ msgId+"   ...wird gelöscht!");
				if(table.getRowCount() > 0){
						((MailTableModel) table.getModel()).removeRow(modelRow);
				}
				
			    //Thread work um mail zu löschen 	
				SwingWorker<Void,Void> workerLoeschen = new SwingWorker<Void,Void>() {
    			      @Override
    			      protected Void doInBackground()
    			      {
		    			      	
					  	    try {					    			   
					    		//Lösche die Mail vom Server	
					  	    	String messageID = mailToDelete.getMessageID();
					  	    	folderSelectionListener.getSelectedMailControl().deleteMail(messageID);
					  	    	
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
			}
		};
		return deleteMailAL;
	}
}
