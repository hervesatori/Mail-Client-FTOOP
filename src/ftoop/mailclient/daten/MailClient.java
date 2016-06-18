package ftoop.mailclient.daten;

import java.io.File;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

public class MailClient {
	
	
	/**
	 * Diese Klasse wird lediglich zum testen einzelner Businesslogik Klassen verwendet
	 * @param args
	 */
	public static void main(String[] args) {	
		//**********Programm Initialisierung
		//**********Laden der Konti
		EmailKontoControl kontoControl = new EmailKontoControl();
		
		kontoControl.loadKonten("kontos.xml");
		
		//********** Verwenden eines Kontos mit MailControl
		MailControl mailControl = new MailControl(kontoControl.getKontos().get(0));
		mailControl.mailReceive();
		mailControl.saveMailContainers();
		
//		mailControl.deleteMail(messageID);
		
		
		
		//********** Abrufen neuer Mails
		
		

		
		//**********Versenden einer neuen Mail 
		ArrayList<File> ats = new ArrayList<File>();
		ats.add(new File("TestAttachments/random1.jpg"));
		ats.add(new File("TestAttachments/random2.jpg"));
		Mail testMail = new Mail(null, null, "ftoop.zh@gmail.com", null, null, "ftoop.zh@gmail.com", "Much attachment, very spam","spam text with attachment", ats);
		Mail testMail2 = new Mail(null, null, "ftoop.zh@gmail.com", null, null, "ftoop.zh@gmail.com", "Most important test subject","this is a spam test mail", null);
		
		
		
		//**********Über MailControl die Mail versenden
		try {
			mailControl.sendMsg(testMail);
			mailControl.sendMsg(testMail2);
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//********** Löschen einer Mail
		String messageID = "&lt;1665404403.0.1463557238977.JavaMail.Morpheus852@GOTTESWERK&gt;";
		try {			
			mailControl.deleteMail(messageID);
		} catch (MessagingException e) {
			System.out.println("Fehler beim Löschen der Mail" + messageID);
			e.printStackTrace();
		}
		
		//********** Schliessen aller offenen Verbindungen
		try {
			mailControl.closeAllFolderConnections();
		} catch (MessagingException e) {
			System.out.println("Fehler beim Schliessen der offenen Verbindungen");
			e.printStackTrace();
		}
	}
	
	

}
