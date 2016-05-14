package ftoop.mailclient.daten;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.io.*;
import java.text.SimpleDateFormat;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

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
	private ArrayList<Folder> mailFolders;
	private MailContainer inbox;
	private MailContainer outbox;
	private  final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
  public MailControl(EmailKonto currentKonto) {
	  this.currentKonto = currentKonto;
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
  public void saveMailFolders(){
	  //Root Element Erstellen
	  Element root = new Element("MailFolders");
	  //neues Dokument 
	  org.jdom2.Document doc = new Document(root);    
	  //Pro Konto eigenen XML Zweig hinzufügen
	  for(Folder folder:this.getMailFolders()){
	      //Objekt Folder wird instanziert
		  try {
			folder.open(Folder.READ_ONLY);
			Element xmlFolder = new Element("Folder");
			Attribute atFullName = new Attribute("fullName", folder.getFullName());
			xmlFolder.setAttribute(atFullName);
		  //Erstellen aller Mails des Folders  
		  
			for(Message msg:folder.getMessages()){
				  Element xmlMail = new Element("Mail");				 	 
				  
				  //Datum speichern
				  Element xmlDate =  new Element("Date"); 
				  SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
				  xmlDate.setText(sdf.format(msg.getSentDate()));
				  xmlMail.addContent(xmlDate); 
				  
				  // Betreff speichern
				  Element xmlSubject =  new Element("Subject");  
				  xmlSubject.setText(msg.getSubject());
				  xmlMail.addContent(xmlSubject); 
				  
				  //Mailinhalt speichern
				  Element xmlMessage =  new Element("Message");  
				  xmlMessage.setText(this.getMessageContent(msg));
				  xmlMail.addContent(xmlMessage);
				  
				  //Adressaten speichern
				  Element xmlTo =  new Element("To");  
				  xmlTo.setText(this.getToAddresses(msg));
				  xmlMail.addContent(xmlTo); 
				  
				  //CC Adressaten speichern
				  Element xmlCC =  new Element("CC");  
				  xmlCC.setText(this.getToAddresses(msg));
				  xmlMail.addContent(xmlCC); 
				  
				  //From speichern
				  Element xmlFrom =  new Element("From");  
				  xmlFrom.setText(this.getFromAddresses(msg));
				  xmlMail.addContent(xmlFrom); 
				  
				  //Attachment hinzufügen
				  Element xmlAttachement = new Element("Attachement");
				  xmlAttachement.setText(this.handleAttachement(msg));
				  xmlMail.addContent(xmlAttachement); 
				  
				  //Nach Erstellung des XML Nodes hinzufügen zum Parent
				  xmlFolder.addContent(xmlMail);
			  }
		 //Hinzufügen des Kontos zum root Zweig
		  root.addContent(xmlFolder);	
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (folder != null && folder.isOpen()) { 
		          try {         	  
		        	  folder.close(false); // false -> Mails die DELETED markiert sind, werden nicht gelöscht
		          } catch (Exception e) { 
		              e.printStackTrace(); 
		            } 
		    } 
//	        try { 
//	            if (store != null && store.isConnected()) { 
//	                store.close(); 
//	            } 
//	        } catch (MessagingException e) { 
//	            e.printStackTrace(); 
//	        } 
		 }   	  
	  }
	  
	  try {
	      //Normal Anzeige mit getPrettyFormat()
	      XMLOutputter outputFile = new XMLOutputter(Format.getPrettyFormat());
	     
	      outputFile.output(doc, new FileOutputStream("Mailbox.xml"));
	   }
	   catch (java.io.IOException e){
		   System.out.println("Konnte die Konten nicht im XML Format speichern.");
		   e.printStackTrace();
	   }
	  
  }
  public ArrayList<Folder> getMailFolders() {
	return mailFolders;
}
  private String getMessageContent(Message msg) throws IOException, MessagingException{
	  Object msgContent = msg.getContent();
	  String content ="";
	  /* Check if content is pure text/html or in parts */                     
	     if (msgContent instanceof Multipart) {
	    	 
	         Multipart multipart = (Multipart) msgContent;        

	         for (int j = 0; j < multipart.getCount(); j++) {

	          BodyPart bodyPart = multipart.getBodyPart(j);

	          String disposition = bodyPart.getDisposition();

	          if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { 
	              System.out.println("Mail have some attachment");

	              DataHandler handler = bodyPart.getDataHandler();
	              System.out.println("file name : " + handler.getName());    	              
	            }
	          else { 
	                  if (bodyPart.isMimeType("text/plain")){
	                	  content = (String) bodyPart.getContent();
	                  } else if (bodyPart.isMimeType("text/html")){
	                      content = (String) bodyPart.getContent();

	                  }

	        	  
	        	  
	        	  
	        	  
	        	  
	        	  
//	                  content = (String) bodyPart.getContent();  // the changed code         
	            }
	        }
	     }
	     else{         
	         content= msg.getContent().toString();
	     }
	  return content;
  }
  private String getTextFromMessage(Message message) throws Exception {
	    if (message.isMimeType("text/plain")){
	        return message.getContent().toString();
	    }else if (message.isMimeType("multipart/*")) {
	        String result = "";
	        MimeMultipart mimeMultipart = (MimeMultipart)message.getContent();
	        int count = mimeMultipart.getCount();
	        for (int i = 0; i < count; i ++){
	            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	            if (bodyPart.isMimeType("text/plain")){
	                result = result + "\n" + bodyPart.getContent();
	                break;  //without break same text appears twice in my tests
	            } else if (bodyPart.isMimeType("text/html")){
	                String html = (String) bodyPart.getContent();
	                result = html;//result + "\n" + Jsoup.parse(html).text();

	            }
	        }
	        return result;
	    }
	    return "";
  }
  
  private String handleAttachement(Message message) throws MessagingException, IOException{
	  // suppose 'message' is an object of type Message
	  String nameGuid = "";
	  String contentType = message.getContentType();
	   
	  if (contentType.contains("multipart")) {
	      // this message may contain attachment
		  Multipart multiPart = (Multipart) message.getContent();
		  
		  for (int i = 0; i < multiPart.getCount(); i++) {
		      MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
		      if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
		          // this part is attachment
		          // code to save attachment...
		    	// save an attachment from a MimeBodyPart to a file
		    	//Erstellen des Filenamen plus universally unique identifier, welches als Referenz im XML dient
		    	  nameGuid = UUID.randomUUID()+"-"+part.getFileName();
		    	  String destFilePath = "Attachment/" + nameGuid;
		    	  
		    	  
		    	  FileOutputStream output = new FileOutputStream(destFilePath);
		    	   
		    	  InputStream input = part.getInputStream();
		    	   
		    	  byte[] buffer = new byte[4096];
		    	   
		    	  int byteRead;
		    	   
		    	  while ((byteRead = input.read(buffer)) != -1) {
		    	      output.write(buffer, 0, byteRead);
		    	  }
		    	  output.close();
		      }
		  }
	  }
	  return nameGuid;
  }
  private String getFromAddresses(Message msg){
	  String fromAddress ="";
		try {
			for(Address ad:msg.getReplyTo()){
				fromAddress += ad.toString() +";";
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  return fromAddress;
  }
private String getToAddresses(Message msg){
	String addresses ="";
	try {
		for(Address ad:msg.getAllRecipients()){
			addresses += ad.toString() +";";
		}
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return addresses;
	
	
}
public void setMailFolders(ArrayList<Folder> mailFolders) {
	this.mailFolders = mailFolders;
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
//	          for (String line : inputStreamToStrings(message.getInputStream())) { 
//
//	        	  speichernInbox.setMessage(line);
//
//	          } 
			  speichernInbox.setMessage(message.getContent().toString());
		  }
	    }catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    speichernInbox.speichern(file);
	  } 
  private void saveContainers(MimeMessage msg,String file,Date d) {
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
	  properties.put("mail.smtp.starttls.enable", "true"); 
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
//	   transport.connect(currentKonto.getBenutzerNameSmtp(),currentKonto.getPasswortSmtp());
	   System.out.println(currentKonto.getBenutzerNameSmtp() +" " +currentKonto.getPasswortSmtp());
	   transport.connect();
	   transport.sendMessage(message, message.getAllRecipients());  
	  
	   System.out.println("message sent successfully");
	   Date d = new Date();
	   saveContainers(message,fileOutbox,d);
	   
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
  

