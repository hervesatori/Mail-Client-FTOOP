package ftoop.mailclient.daten;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.io.*;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.pop3.POP3SSLStore;
/**
 * 
 * @author Herve Satori & Dominique Borer
 *
 */
public class MailControl {
	
	public static final String file = "MailsInbox";
	private EmailKonto currentKonto;
	private MailContainer inbox;
	private MailContainer outbox;
	
  public MailControl(EmailKonto currentKonto) {
	  this.currentKonto = currentKonto;
	  this.inbox = inbox;
	  this.outbox = outbox;
	  
  }
  /**
   * 
   * @throws NoSuchProviderException
   * 
   */
  public void receiveMsg() throws NoSuchProviderException {
	 
	  Store store = null;
	  Folder emailFolder = null;
	  final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	  //properties wird erstellt
	  //The Properties class represents a persistent set of properties
      Properties properties = new Properties();

      properties.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
      properties.setProperty("mail.pop3.socketFactory.fallback", "false");
      properties.setProperty("mail.pop3.port",  Integer.toString(currentKonto.getPop3Port()));
      properties.setProperty("mail.pop3.socketFactory.port",Integer.toString(currentKonto.getPop3Port()));

      URLName url = new URLName("pop3", currentKonto.getPop3Server(),currentKonto.getPop3Port(), "",
    		  currentKonto.getBenutzerNamePop(), currentKonto.getPasswortPop());
      //Objekt Session kapselt die Verbindung an Mail Server
      Session emailSession = Session.getDefaultInstance(properties);
     
      //Objekt Store + Verbindung an POP3 Server
     
	try {
		 store = new POP3SSLStore(emailSession, url);
		 store.connect();

      //Objekt Folder wird instanziert
		emailFolder = store.getFolder("INBOX");
		emailFolder.open(Folder.READ_ONLY);
		
		 Message[] messages = emailFolder.getMessages();
		 printMessages(messages);
		 
	} catch (MessagingException e) {
		
		e.printStackTrace();
	}finally {
		if (emailFolder != null && emailFolder.isOpen()) { 
	          try {         	  
	        	  emailFolder.close(false); // false -> Mails die DELETED markiert sind, werden nicht gelöscht
	          } catch (Exception e) { 
	              e.printStackTrace(); 
	            } 
	    } 
        try { 
            if (store != null && store.isConnected()) { 
                store.close(); 
            } 
        } catch (MessagingException e) { 
            e.printStackTrace(); 
        } 
	 }
    }
  

  private void printMessages(Message[] msg) {
    try {
	  System.out.println(msg.length+" Mails");
	

	  for (int i = 0, n = msg.length; i < n; i++) {
		  Speichern.setMail();
		  Message message = msg[i];
		  System.out.println("---------------------------------");
		  System.out.println("Email-Nr. " + (i + 1));
		  System.out.println("gesendet am: " + message.getSentDate());
		  Speichern.setSentDate(message.getSentDate());
		  System.out.println("Subject: " + message.getSubject());
		  Speichern.setSubject(message.getSubject());
		  System.out.println("From: " + message.getFrom()[0]);
		  Speichern.setFrom(message.getFrom()[0].toString());
		  System.out.println("Text: " );
          for (String line : inputStreamToStrings(message.getInputStream())) { 

              System.out.println(line); 
              Speichern.setMessage(line);

          } 

	  }
    }catch (Exception e) { 
        e.printStackTrace(); 
    } 
    Speichern.speichern(file);
  } 
  private static List<String> inputStreamToStrings(InputStream is) {
      InputStreamReader isr = new InputStreamReader(is);
      List<String> strings = new LinkedList<>();
      try (BufferedReader reader = new BufferedReader(isr)) {
          String line = reader.readLine();
          while (line != null) {
              strings.add(line);
              line = reader.readLine();
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
      return strings;
  }
  
}
