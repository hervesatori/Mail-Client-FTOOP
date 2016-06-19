package ftoop.mailclient.gui;

import java.util.NoSuchElementException;

import javax.mail.MessagingException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailControl;
/**
 * Versucht Mail vom Server zu löschen
 * Die abstrakte Klasse SwingWorker  wird implementiert
 * 
 * @author Dominique Borer & Herve Satori
 *
 */
public class DeleteMailWorker extends SwingWorker<Void, Void>{
	private final Mail mailToDelete;
	private final MailControl mc; 
	
	/**
	 * 
	 * @param toBeDeleted
	 * @param currentMc
	 */
	public DeleteMailWorker(Mail toBeDeleted, MailControl currentMc){
		this.mailToDelete = toBeDeleted;
		this.mc = currentMc; 
	}
	  @Override
      protected Void doInBackground()
      {
		  final String msgId = mailToDelete.getMessageID();
		  System.out.println("Mail mit ID: "+ msgId+"   ...wird gelöscht!");
	
	  	    try {					    			   
	    		//Lösche die Mail vom Server						  	    	
	  	    	mc.deleteMail(msgId);
	  	    	
			} catch (MessagingException e1) {
				e1.printStackTrace();
			} catch (NoSuchElementException e2){
				String message = e2.getMessage();
				JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
		}
        return null;
      }
}
