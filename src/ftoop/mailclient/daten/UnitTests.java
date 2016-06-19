package ftoop.mailclient.daten;

import java.io.File;
import java.io.IOException;

import javax.mail.NoSuchProviderException;

import org.junit.Test;

import junit.framework.TestCase;

public class UnitTests extends TestCase{

	@Test
	public void testEmailKontoControl() throws IOException {
		EmailKontoControl testControl = new EmailKontoControl();
		
		String konto = "TestKonto";
		String name = "Test Benutzername";
		String email ="dieseadressegibt@es.nicht";
		String pop3Server = "pop3.not.exist";
		int pop3Port = 3456;
		String benutzerNamePop = "Test Benutzername";
		String passwortPop ="worstPassword";
		String smtpServer = "smtp.not.exist";
		int smtpPort = 3456;
		String benutzerNameSmtp = "Test Benutzername";
		String passwortSmtp = "worstPassword";
		String imapServer = "imap.not.exist";
		int imapPort = 1234;
		testControl.newKonto(konto, name, email, pop3Server, pop3Port,
				   benutzerNamePop, passwortPop, smtpServer, smtpPort,
				   benutzerNameSmtp,
				   passwortSmtp,imapServer,
					  imapPort);
	
		String fileName = "kontos.xml";

		testControl.saveKonten(fileName);
		File currentDirectory = new File(new File(".").getAbsolutePath());
		System.out.println(fileName + " saved to: " + currentDirectory.getCanonicalPath());
		
		assertTrue(true);
	}
	public void testLoadEmailKontos(){
		EmailKontoControl testControl = new EmailKontoControl();
		

		testControl.loadKonten("kontos.xml");
		
		System.out.println(testControl.toString());
		EmailKonto konto = testControl.getKontos().get(0);
		TestCase.assertEquals("Test Benutzername", konto.getBenutzerNameSmtp());
		TestCase.assertEquals("dieseadressegibt@es.nicht", konto.getEmail());
		TestCase.assertEquals("Test Benutzername", konto.getName());
		TestCase.assertEquals("worstPassword", konto.getPasswortPop());
		TestCase.assertEquals(3456, konto.getPop3Port());
		TestCase.assertEquals("pop3.not.exist", konto.getPop3Server());
		TestCase.assertEquals(3456, konto.getSmtpPort());
		TestCase.assertEquals("smtp.not.exist", konto.getSmtpServer());
		TestCase.assertEquals(1234, konto.getImapPort());
		TestCase.assertEquals("imap.not.exist", konto.getImapServer());
	}
	/**
	 * Testfunktion funktioniert nur, wenn aus dem Array(1) ein gültiges Konto geladen wird
	 */
	public void testSendMail(){
		EmailKontoControl testControl = new EmailKontoControl();		
		
		testControl.loadKonten("kontos.xml");
		
		MailControl mailControl = new MailControl(testControl.getKontos().get(1));
		Mail testMail = new Mail(null, null, "ftoop.zh@gmail.com",null,null, "ftoop.zh@gmail.com", "Most important test subject","this is a spam test mail", null);
		
		try {
			mailControl.sendMsg(testMail);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			TestCase.fail();
		}
		
	}

}
