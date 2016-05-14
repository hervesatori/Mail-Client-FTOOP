package ftoop.mailclient.daten;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

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
		testControl.newKonto(konto, name, email, pop3Server, pop3Port,
				   benutzerNamePop, passwortPop, smtpServer, smtpPort,
				   benutzerNameSmtp,
				   passwortSmtp);
	
		String fileName = "kontos_test.xml";

		testControl.saveKonten(fileName);
		File currentDirectory = new File(new File(".").getAbsolutePath());
		System.out.println(fileName + " saved to: " + currentDirectory.getCanonicalPath());
		
		assertTrue(true);
	}
	public void testLoardEmailKontos(){
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
	}
	public void testSendMail(){
		EmailKontoControl testControl = new EmailKontoControl();		
		
		testControl.loadKonten("kontos.xml");
		
		MailControl mailControl = new MailControl(testControl.getKontos().get(1));
		Mail testMail = new Mail("ftoop.zh@gmail.com", "ftoop.zh@gmail.com", "Most important test subject","this is a spam test mail");
		
		try {
			mailControl.sendMsg(testMail);
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TestCase.fail();
		}
		
	}

}
