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
	 String smtpServer = "";
	 String benutzerNameSmtp = "";
	 String passwortSmtp = "";
	 
	 Speichern.xmlParser(MailControl.file);
	 
	 EmailKonto eKonto = new EmailKonto(konto,name,email,pop3Server,pop3Port,benutzerNamePop,passwortPop,smtpServer,
		 benutzerNameSmtp,passwortSmtp);
	 MailControl mCtrl = new MailControl(eKonto);
	 try {
		mCtrl.receiveMsg();
	} catch (NoSuchProviderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	

}
