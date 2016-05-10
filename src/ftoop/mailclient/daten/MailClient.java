package ftoop.mailclient.daten;

import java.io.File;

import javax.mail.NoSuchProviderException;

public class MailClient {

	public static void main(String[] args) {
//	 System.out.println("Starte Applikation...");	
//	 
//	 String konto = "";
//	 String name = "";
//	 String email = "";
//	 String pop3Server = "pop.gmail.com";
//	 int pop3Port = 995;
//	 String benutzerNamePop = "hervesatori@gmail.com";
//	 String passwortPop = "";
//	 String smtpServer = "smtp.mail.yahoo.fr";
//	 int smtpPort = 465;
//	 String benutzerNameSmtp = "satori_herve@yahoo.fr";
//	 String passwortSmtp = "";
//	 
//	 
//	 
//	 EmailKonto eKonto = new EmailKonto(konto,name,email,pop3Server,pop3Port,benutzerNamePop,passwortPop,smtpServer,
//		 smtpPort,benutzerNameSmtp,passwortSmtp);
//	 MailControl mCtrl = new MailControl(eKonto);
//	 try {
//		mCtrl.sendMsg(new Mail("hervesatori@gmail.com","satori_herve@yahoo.fr","TESTTEST","this is a test"));
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			
//			e.printStackTrace();
//		}
//		mCtrl.receiveMsg();
//	} catch (NoSuchProviderException e) {
//		
//		e.printStackTrace();
//	}
//	
//	 System.out.println("Beende Applikation...");	
		EmailKontoControl testControl = new EmailKontoControl();
		
		if (System.getProperty("os.name").toLowerCase().indexOf("win")<0) {
            System.err.println("Sorry, Windows only!");
            System.exit(1);
        }
        File desktopDir = new File(System.getProperty("user.home"), "Desktop");
        System.out.println(desktopDir.getPath() + " " + desktopDir.exists());
        
		testControl.loadKonten(desktopDir.getPath() + "/kontos.xml");
		
		MailControl mailControl = new MailControl(testControl.getKontos().get(1));
		Mail testMail = new Mail("ftoop.zh@gmail.com", "ftoop.zh@gmail.com", "Most important test subject","this is a spam test mail");
		
		try {
			mailControl.sendMsg(testMail);
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
