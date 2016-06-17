package ftoop.mailclient.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.NoSuchElementException;

import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailControl;

public class DeleteMailWorker extends SwingWorker<Void, Void>{
	private final Mail mailToDelete;
	private final MailControl mc; 
	
	
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
