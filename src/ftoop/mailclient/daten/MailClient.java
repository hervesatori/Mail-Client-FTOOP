package ftoop.mailclient.daten;

import javax.mail.NoSuchProviderException;

public class MailClient {

	public static void main(String[] args) {
	 	
	 String konto = "";
	 String name = "";
	 String email = "";
	 String pop3Server = "pop.gmail.com";
	 int pop3Port = 995;
	 String benutzerNamePop = "hervesatori@gmail.com";
	 String passwortPop = "";
	 String smtpServer = "smtp.mail.yahoo.fr";
	 int smtpPort = 465;
	 String benutzerNameSmtp = "satori_herve@yahoo.fr";
	 String passwortSmtp = "";
	 
	 
	 
	 EmailKonto eKonto = new EmailKonto(konto,name,email,pop3Server,pop3Port,benutzerNamePop,passwortPop,smtpServer,
		 smtpPort,benutzerNameSmtp,passwortSmtp);
	 MailControl mCtrl = new MailControl(eKonto);
	 try {
		mCtrl.sendMsg(new Mail("hervesatori@gmail.com","satori_herve@yahoo.fr","TESTTEST","this is a test"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		mCtrl.receiveMsg();
	} catch (NoSuchProviderException e) {
		
		e.printStackTrace();
	}
	}
	
	

}
