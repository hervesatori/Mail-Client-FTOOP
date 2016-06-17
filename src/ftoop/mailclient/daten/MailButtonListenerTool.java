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

import ftoop.mailclient.gui.DeleteMailWorker;
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
				PopupMailFrame newMailFrame = new PopupMailFrame("Neue E-Mail senden",mailWindows,PopupMailFrame.NEW_MAIL_FRAME, folderSelectionListener);
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
				PopupMailFrame newMailFrame = new PopupMailFrame("Auf E-Mail antworten",mailWindows,PopupMailFrame.NEW_MAIL_FRAME, folderSelectionListener);
				

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
				PopupMailFrame newMailFrame = new PopupMailFrame("E-Mail weiterleiten",mailWindows,PopupMailFrame.NEW_MAIL_FRAME, folderSelectionListener);
				
				

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
			    final MailControl mc = folderSelectionListener.getSelectedMailControl();
				
				if(table.getRowCount() > 0){
						((MailTableModel) table.getModel()).removeRow(modelRow);
				}
				
				deleteMailUsingWorker(mailToDelete, mc);
			}
		};
		return deleteMailAL;
	}	
	/**
	 * Dieser AL wird benötigt von einem externen Mailframe, example PopupMailFrame
	 * @param toBeDeleted
	 * @param mailTable
	 * @param currentMc
	 * @param folderSelectionListener
	 * @return
	 */
	public static ActionListener getDeleteMailActionListener(Mail toBeDeleted, JTable mailTable, MailControl currentMc, FolderSelectionListener folderSelectionListener){
		final ActionListener deleteMailAL = new ActionListener(){
			private final Mail mailToDelete = toBeDeleted;
			private final  JTable mTable = mailTable;
			private final MailControl mc = currentMc; 
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				deleteMailUsingWorker(mailToDelete, mc);
				if(mTable.equals(folderSelectionListener.getCurrentTable())){
				    final int tRow = mTable.getSelectedRow();
				    final int modelRow =  mTable.convertRowIndexToModel(tRow);
				    mTable.clearSelection();
					if(mTable.getRowCount() > 0){
						((MailTableModel) mTable.getModel()).removeRow(modelRow);
					}
				}		
				
			}
		};
		return deleteMailAL;
	}
	private static void deleteMailUsingWorker(Mail mailToDelete, MailControl currentMc){
		DeleteMailWorker workerLoeschen = new DeleteMailWorker(mailToDelete, currentMc);
	    workerLoeschen.execute();
	}
}
