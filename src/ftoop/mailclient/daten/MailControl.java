package ftoop.mailclient.daten;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.io.*;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.pop3.POP3SSLStore;
/**
 * 
 * @author Herve Satori & Dominique Borer
 *
 */
public class MailControl {
	
	public static final String fileInbox = "MailsInbox";
	public static final String fileOutbox = "MailsOutbox";
	private Speichern speichernInbox;
	private Speichern speichernOutbox;
	private EmailKonto currentKonto;
	private MailContainer inbox;
	private MailContainer outbox;
	private  final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
  public MailControl(EmailKonto currentKonto) {
	  this.currentKonto = currentKonto;
	  this.inbox = inbox;
	  this.outbox = outbox;
	  speichernInbox = new Speichern();
	  speichernOutbox = new Speichern();
	  
  }
  /**
   * 
   * @throws NoSuchProviderException
   * 
   */
  public void receiveMsg() throws NoSuchProviderException {
	 
	  Store store = null;
	  Folder emailFolder = null;
	 
	
	  //properties wird erstellt
	  //The Properties class represents a persistent set of properties
      Properties properties = new Properties();

      properties.put("mail.pop3.socketFactory.class", SSL_FACTORY);
      properties.put("mail.pop3.socketFactory.fallback", "false");
      properties.put("mail.pop3.port", Integer.toString(currentKonto.getPop3Port()));
      properties.put("mail.pop3.socketFactory.port",Integer.toString(currentKonto.getPop3Port()));

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
	  // Array Messages wird instanziert	
		 Message[] messages = emailFolder.getMessages();
	  // Messages werden an Console angezeigt
		 printMessages(messages);
	  // Messages werden in File "MailsInbox gespeichert
		 safeContainers(messages,fileInbox);
		 
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
		  Message message = msg[i];
		  System.out.println("---------------------------------");
		  System.out.println("Email-Nr. " + (i + 1));
		  System.out.println("gesendet am: " + message.getSentDate());
		  System.out.println("Subject: " + message.getSubject());
		  System.out.println("From: " + message.getFrom()[0]);
		  System.out.println("Text: " );
          for (String line : inputStreamToStrings(message.getInputStream())) { 

              System.out.println(line); 
              

          } 

	  }
    }catch (Exception e) { 
        e.printStackTrace(); 
    } 
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
  
  private void safeContainers(Message[] msg,String file) {
	  speichernInbox.xmlParser(MailControl.fileInbox);
	    try {

		  for (int i = 0, n = msg.length; i < n; i++) {
			  speichernInbox.setMail();
			  Message message = msg[i];
			  speichernInbox.setDate(message.getSentDate());
			  speichernInbox.setSubject(message.getSubject());
			  speichernInbox.setFrom(message.getFrom()[0].toString());
	          for (String line : inputStreamToStrings(message.getInputStream())) { 

	        	  speichernInbox.setMessage(line);

	          } 

		  }
	    }catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    speichernInbox.speichern(file);
	  } 
  private void safeContainers(MimeMessage msg,String file,Date d) {
	  speichernOutbox.xmlParser(MailControl.fileOutbox);
		 
	    try {
	    	speichernOutbox.setMail();
			speichernOutbox.setDate(d);
	    	speichernOutbox.setSubject(msg.getSubject());
	    	speichernOutbox.setFrom(msg.getFrom()[0].toString());

	    	speichernOutbox.setMessage((String) msg.getContent());
		  
	    }catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    speichernOutbox.speichern(file);
	  } 
  
  public void sendMsg(Mail mail) throws NoSuchProviderException {
	  
	  
	//Objekt Session wird erstellt  
	  Properties properties = new Properties(); 
	  properties.put("mail.smtp.host", currentKonto.getSmtpServer());  
	  properties.put("mail.smtp.socketFactory.port", Integer.toString(currentKonto.getSmtpPort()));  
	  properties.put("mail.smtp.socketFactory.class", SSL_FACTORY);  
	  properties.put("mail.smtp.auth", "true");  
	  properties.put("mail.smtp.port", Integer.toString(currentKonto.getSmtpPort()));  
	   
	  Session session = Session.getInstance(properties,new javax.mail.Authenticator() {  
		 protected PasswordAuthentication getPasswordAuthentication() {  
			 return new PasswordAuthentication(currentKonto.getBenutzerNameSmtp(),currentKonto.getPasswortSmtp());
		 }  
	  });  
	  
	//message wird geschrieben
	  Transport transport = session.getTransport("smtp");
	  try {  
	   MimeMessage message = new MimeMessage(session);  
	   message.setFrom(new InternetAddress(mail.getFrom()));
	   message.addRecipient(Message.RecipientType.TO,new InternetAddress(mail.getTo()));  
	   message.setSubject(mail.getSubject());  
	   message.setText(mail.getMessage());  
	     
	   //Message wird gesendet
	   
	   Transport.send(message);  
	  
	   System.out.println("message sent successfully");
	   Date d = new Date();
	   safeContainers(message,fileOutbox,d);
	   
	  } catch (MessagingException e) {
		  throw new RuntimeException(e);
	    }  finally { 

	        try { 
	            if (transport != null) { 
	                transport.close(); 
	            } 
	        } catch (MessagingException e) { 
	            e.printStackTrace(); 
	        } 
	   
    }  
	   
  }
}
  

