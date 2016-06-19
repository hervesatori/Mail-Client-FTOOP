package ftoop.mailclient.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.mail.NoSuchProviderException;
import javax.swing.JTable;

import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailControl;
/**
 * Events um die JButtons senden, antworten, weiterleiten und löschen zu steuern werden hier implementiert
 * 
 * @author Dominique Borer & Herve Satori
 *
 */
public class MailButtonListenerTool {
	
	private MailButtonListenerTool(){
		
	}
	/**
	 * 
	 * @param mW
	 * @return
	 */
	public static ActionListener getSendenActionListener(MailPane mW){
		final MailPane mailWindow = mW;
		final ActionListener sendenAL = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
		    	   try {
		    		   System.out.println("Versende nun Mail an: " + mailWindow.getAn() + "mit Inhalt: "+mailWindow.getMessageText());
					   String to = mailWindow.getAn();
					   String from = mailWindow.getCurrentMC().getCurrentKonto().getEmail();
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
	/**
	 * 
	 * @param pMF
	 * @param mW
	 * @return
	 */
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
	/**
	 * 
	 * @param singleTo
	 * @return
	 */
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
	/**
	 * 
	 * @param mailWindow
	 */
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
	/**
	 * 
	 * @param fSL
	 * @return
	 */
	public static ActionListener getNewMailActionListener(FolderSelectionListener fSL){
		final FolderSelectionListener folderSelectionListener = fSL;
		final ActionListener newMailAL = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MailControl currentMailControl = folderSelectionListener.getSelectedMailControl();
				MailPane mailWindows = new MailPane(null,MailPane.MailWindowType.NEW,currentMailControl);
				PopupMailFrame newMailFrame = new PopupMailFrame("Neue E-Mail senden",mailWindows,PopupMailFrame.NEW_MAIL_FRAME, folderSelectionListener);
				newMailFrame.setVisible(true);
			}
			
		};
		return newMailAL;
	}
	/**
	 * 
	 * @param folderSelectionListener
	 * @return
	 */
	public static ActionListener getRespondToMailActionListener(FolderSelectionListener folderSelectionListener){
		final ActionListener respondMailAL = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MailControl currentMailControl = folderSelectionListener.getSelectedMailControl();
				Mail mail = folderSelectionListener.getSelectedMail();
				MailPane mailWindows = new MailPane(mail,MailPane.MailWindowType.RESPOND,currentMailControl);
				PopupMailFrame newMailFrame = new PopupMailFrame("Auf E-Mail antworten",mailWindows,PopupMailFrame.NEW_MAIL_FRAME, folderSelectionListener);
				newMailFrame.setVisible(true);

			}
			
		};
		return respondMailAL;
	}
	/**
	 * 
	 * @param toBeResponded
	 * @param currentMc
	 * @param folderSelectionListener
	 * @return
	 */
	public static ActionListener getRespondToMailActionListener(Mail toBeResponded,  MailControl currentMc,FolderSelectionListener folderSelectionListener){
		final ActionListener respondMailAL = new ActionListener(){
			private final Mail tBR = toBeResponded;
			private final MailControl cMC = currentMc;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MailPane mailWindows = new MailPane(tBR,MailPane.MailWindowType.RESPOND,cMC);
				PopupMailFrame newMailFrame = new PopupMailFrame("Auf E-Mail antworten",mailWindows,PopupMailFrame.NEW_MAIL_FRAME, folderSelectionListener);
				newMailFrame.setVisible(true);

			}
			
		};
		return respondMailAL;
	}
	/**
	 * 
	 * @param folderSelectionListener
	 * @return
	 */
	public static ActionListener getForwardToMailActionListener(FolderSelectionListener folderSelectionListener){
		final ActionListener forwardMailAL = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MailControl currentMailControl = folderSelectionListener.getSelectedMailControl();
				Mail mail = folderSelectionListener.getSelectedMail();
				MailPane mailWindows = new MailPane(mail,MailPane.MailWindowType.FORWARD,currentMailControl);
				PopupMailFrame newMailFrame = new PopupMailFrame("E-Mail weiterleiten",mailWindows,PopupMailFrame.NEW_MAIL_FRAME, folderSelectionListener);
				newMailFrame.setVisible(true);
				

			}
			
		};
		return forwardMailAL;
	}	
	/**
	 * 
	 * @param toBeForwarded
	 * @param currentMc
	 * @param folderSelectionListener
	 * @return
	 */
	public static ActionListener getForwardToMailActionListener(Mail toBeForwarded,  MailControl currentMc,FolderSelectionListener folderSelectionListener){
		final ActionListener forwardMailAL = new ActionListener(){
			private final Mail tBF = toBeForwarded;
			private final MailControl cMC = currentMc;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MailPane mailWindows = new MailPane(tBF,MailPane.MailWindowType.FORWARD,cMC);
				PopupMailFrame newMailFrame = new PopupMailFrame("E-Mail weiterleiten",mailWindows,PopupMailFrame.NEW_MAIL_FRAME, folderSelectionListener);
				newMailFrame.setVisible(true);
				

			}
			
		};
		return forwardMailAL;
	}	
	/**
	 * 
	 * @param folderSelectionListener
	 * @return
	 */
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
	/**
	 * 
	 * @param mailToDelete
	 * @param currentMc
	 */
	private static void deleteMailUsingWorker(Mail mailToDelete, MailControl currentMc){
		DeleteMailWorker workerLoeschen = new DeleteMailWorker(mailToDelete, currentMc);
	    workerLoeschen.execute();
	}
}
