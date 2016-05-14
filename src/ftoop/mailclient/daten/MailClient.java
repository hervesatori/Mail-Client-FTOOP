package ftoop.mailclient.daten;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class MailClient {
	
	

	public static void main(String[] args) {	
		//**********Programm Initialisierung
		//**********Laden der Konti
		EmailKontoControl kontoControl = new EmailKontoControl();
		
		kontoControl.loadKonten("kontos.xml");
		
		//********** Verwenden eines Kontos mit MailControl
		MailControl mailControl = new MailControl(kontoControl.getKontos().get(1));
		
		
		//********** Abrufen neuer Mails
		try {
			mailControl.receiveMsg();
		} catch (NoSuchProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//********** Erstellen der Mailbox
		ArrayList<Folder> rootContainer = new ArrayList<Folder>();
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		try {
		    Session session = Session.getDefaultInstance(props, null);
		    javax.mail.Store store = session.getStore("imaps");
		    store.connect("imap.gmail.com", "FTOOP.Zh@gmail.com", "ftoop.zh.1234");
		    javax.mail.Folder[] folders = store.getDefaultFolder().list("*");
		    for (javax.mail.Folder folder : folders) {
		        if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
		        	rootContainer.add(folder);
		            System.out.println(folder.getFullName() + ": " + folder.getMessageCount());		            
		        }
		    }
		} catch (MessagingException e) {
		    e.printStackTrace();
		}
		mailControl.setMailFolders(rootContainer);
		mailControl.saveMailFolders();
		
		
		
//		//**********Erstellen der Mail
//		Mail testMail = new Mail("ftoop.zh@gmail.com", "ftoop.zh@gmail.com", "Most important test subject","this is a spam test mail");
//		
//		
//		//**********Über MailControl die Mail versenden
//		try {
//			mailControl.sendMsg(testMail);
//		} catch (NoSuchProviderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	

}
