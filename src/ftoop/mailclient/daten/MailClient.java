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
