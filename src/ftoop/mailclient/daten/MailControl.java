package ftoop.mailclient.daten;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.sun.mail.pop3.POP3SSLStore;
import java.util.HashMap;
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
	private ArrayList<Folder> serverMailFolders;
	private HashMap<String, MailContainer> mailContainers;
//	private ArrayList<MailContainer> mailContainers;
	private Store store;

	private MailContainer inbox;
	private MailContainer outbox;
	private  final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
  public MailControl(EmailKonto currentKonto) {
	  this.currentKonto = currentKonto;
	  speichernInbox = new Speichern();
	  speichernOutbox = new Speichern();
	  
	  
	  //Initialisieren des Konto Stores f�r die Mailabfrage
	  Properties props = System.getProperties();
	  props.setProperty("mail.store.protocol", "imaps");
	  try {
		    Session session = Session.getDefaultInstance(props, null);
		    this.store = session.getStore("imaps");
		    store.connect(this.getCurrentKonto().getImapServer(), this.getCurrentKonto().getName(), this.getCurrentKonto().getPasswortPop());
		    initializeServerMailFolders();
	  } catch (MessagingException e) {
		    e.printStackTrace();
	  }
	  // �berpr�fen, ob bereits eine Konto Mailbox XML vorhanden ist und falls nicht,
	  //erstellen, plus herunterladen aller Mails vom Account. Andernfalls Aktualisieren der Mailbox
	  if(this.existsMailboxXML()){
		  try {
			this.updateMailFolders(Folder.READ_WRITE);
		} catch (MessagingException e) {
			System.out.println("Fehler beim Aktualisieren der Mailbox");
			e.printStackTrace();
		}
	  }else{
		  //********** Erstellen der Mailbox
		  this.initializeMailbox();
	  }
		
  }
  /**
   * Initialisiere ServerMailFolders - Reflektiert die Ordner auf dem Server, mit welchen sp�ter auch die direkte Mailabfrage gemacht wird
   */
  private void initializeServerMailFolders(){
	  try {
		    System.out.println("Initialisiere MailControl ServerMailFolders");
		    for (javax.mail.Folder folder:store.getDefaultFolder().list("*")) {
		        if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
		        	this.getServerMailFolders().add(folder);
		            System.out.println(folder.getFullName() + ": " + folder.getMessageCount());		            
		        }
		    }
		} catch (MessagingException e) {
		    e.printStackTrace();
		}
  }
  /**
 * @return the serverMailFolders
 */
private ArrayList<Folder> getServerMailFolders() {
	return serverMailFolders;
}
private boolean existsMailboxXML(){
	  boolean exists = false;
	  File mailboxXML = new File("Mailbox"+this.getCurrentKonto().getKonto()+".xml");
	  if(mailboxXML.exists() && !mailboxXML.isDirectory()) { 
		  exists = true;
	  }	  
	  return exists;
  }
  public EmailKonto getCurrentKonto() {
	return currentKonto;
  }
	public void setCurrentKonto(EmailKonto currentKonto) {
		this.currentKonto = currentKonto;
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
	        	  emailFolder.close(false); // false -> Mails die DELETED markiert sind, werden nicht gel�scht
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
  
	public HashMap<String, MailContainer> getMailContainers() {
		return mailContainers;
	}
	public void setMailContainers(HashMap<String, MailContainer> mailContainers) {
		this.mailContainers = mailContainers;
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
  public void loadMailFolders(String pathToXML){	  	 
	  SAXBuilder builder = new SAXBuilder();
	  File xmlFile = new File(pathToXML);

	  try {

		Document document = (Document) builder.build(xmlFile);
		Element rootNode = document.getRootElement();
		List<Element> folders = rootNode.getChildren("Folder");
		
		//Jeden Folder im XML durchloopen und 
		for (Element folder:folders) {
			//.. f�r jeden einzelnen Folder einen neuen MailContainer erstellen
			MailContainer newContainer = new MailContainer(folder.getAttribute("fullName").toString());
			//hinzuf�gen der einzelnen Mails aus dem XML zum Container
			List<Element> mails = folder.getChildren("Mail");
			for (Element xmlMail:mails){
				 //Laden der einzelnen Kontoattributen und hinzuf�gen zu einem neuen Konto
				ArrayList<File> attachmentPaths = new ArrayList<File>();
				for(Element attachment:xmlMail.getChildren("Attachment")){
					attachmentPaths.add(new File(attachment.getText()));
				}
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
				Date date = null;
				try {
					date = format.parse(xmlMail.getChildText("Date"));
				} catch (ParseException e) {
					System.out.println("Konnte das Datum von String "+ xmlMail.getChildText("Date")+" nicht zu Date konvertieren");
					e.printStackTrace();
				}
				Mail mailToAdd = new Mail(xmlMail.getChildText("MessageID"),date, xmlMail.getChildText("To"), xmlMail.getChildText("CC"),xmlMail.getChildText("BCC"), xmlMail.getChildText("From"), xmlMail.getChildText("Subject"), xmlMail.getChildText("Message"), attachmentPaths);
				newContainer.addMailToContainer(mailToAdd);
			}
			//MailContainer zum MailControl hinzuf�gen
			this.getMailContainers().put(folder.getName(),newContainer);
		}
		 
	
	  } catch (IOException io) {
		System.out.println(io.getMessage());
	  } catch (JDOMException jdomex) {
		System.out.println(jdomex.getMessage());
	  }
  }
  private Store getKontoStore(){
	  return this.store; 
	  
  }
  /**
   * 
   * @param readOrWriteFolderModus Zu setzen �ber Folder.Read_Write oder Folder.Read_Only
   * @throws MessagingException
   */
  public void updateMailFolders(int readOrWriteFolderModus) throws MessagingException{
		if(readOrWriteFolderModus == Folder.READ_WRITE || readOrWriteFolderModus == Folder.READ_ONLY){
			for(Folder folder:this.getMailFolders()){
				folder.open(readOrWriteFolderModus);
			  // Array Messages wird instanziert	
				 for(Message message:folder.getMessages()){
					try {
						this.getMailContainers().get(folder.getName()).addMailToContainer(this.generateMailFromMessage(message));
					} catch (IOException e) {
						System.out.println("Konnte kein Mailobjekt erstellen: ");
						e.printStackTrace();
					}
				 }
			}
		}else{
			try {
				throw new Exception("readOrWriteFolderModus muss entweder f�r Folder.READ_WRITE:"+Folder.READ_WRITE+ " oder f�r Folder.READ_ONLY:"+ Folder.READ_ONLY+" sein."  );
			} catch (Exception e) {				
				e.printStackTrace();
			}
				
		}
	}
	public Mail generateMailFromMessage(Message msg) throws MessagingException, IOException{
		String messageID = msg.getHeader("Message-ID")[0];
		Date received = msg.getSentDate();
		String to = this.getToAddresses(msg);
		String cc = this.getToAddresses(msg);
		String bcc = this.getToAddresses(msg);
		String from = this.getFromAddresses(msg);
		String subject = msg.getSubject();
		String message = this.getMessageContent(msg);
		ArrayList<File>  attachments = new ArrayList<File>();
		 		  
	  //Attachments hinzuf�gen
	  for(String attachment:this.handleAttachement(msg)){
		  attachments.add(new File(attachment));
	  }
		Mail mail = new Mail(messageID, received, to, cc, bcc, from, subject, message, attachments);
		return mail;
	}
	/**
	 * Speichert die geladenen MailContainers zur�ck in ein XML
	 */
	public void saveMailContainers(){
		 //Root Element Erstellen
		  Element root = new Element("MailFolders");
		  //neues Dokument 
		  org.jdom2.Document doc = new Document(root);
		  //Pro MailContailer / Folder eigenen XML Zweig hinzuf�gen
		  for(MailContainer mc:this.getMailContainers().values()){  

				Element xmlFolder = new Element("Folder");
				Attribute atFullName = new Attribute("fullName", mc.getFolderFullPath());
				xmlFolder.setAttribute(atFullName);
			  //Erstellen aller Mails des Folders  
			  
				for(Mail mail:mc.getContainingMails()){
					  Element xmlMail = new Element("Mail");				 	 
					  
					  //Mesage-ID speichern 
					  Element xmlMessageID =  new Element("MessageID"); 
					  xmlMessageID.setText(mail.getMessageID());
					  xmlMail.addContent(xmlMessageID); 
					  
					  //Datum speichern
					  Element xmlDate =  new Element("Date"); 
					  SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
					  xmlDate.setText(sdf.format(mail.getReceived()));
					  xmlMail.addContent(xmlDate); 
					  
					  // Betreff speichern
					  Element xmlSubject =  new Element("Subject");  
					  xmlSubject.setText(mail.getSubject());
					  xmlMail.addContent(xmlSubject); 
					  
					  //Mailinhalt speichern
					  Element xmlMessage =  new Element("Message");  
					  xmlMessage.setText(mail.getMessage());
					  xmlMail.addContent(xmlMessage);
					  
					  //Adressaten speichern
					  Element xmlTo =  new Element("To");  
					  xmlTo.setText(mail.getTo());
					  xmlMail.addContent(xmlTo); 
					  
					  //CC Adressaten speichern
					  Element xmlCC =  new Element("CC");  
					  xmlCC.setText(mail.getCc());
					  xmlMail.addContent(xmlCC); 
					  
					  //From speichern
					  Element xmlFrom =  new Element("From");  
					  xmlFrom.setText(mail.getFrom());
					  xmlMail.addContent(xmlFrom); 
					  
					  //Attachments hinzuf�gen
					  for(File attachment:mail.getAttachments()){
						  Element xmlAttachement = new Element("Attachment");
						  xmlAttachement.setText(attachment.getName());
						  xmlMail.addContent(xmlAttachement); 
					  }
					  //Nach Erstellung des XML Nodes hinzuf�gen zum Parent
					  xmlFolder.addContent(xmlMail);
				  }
			  
		  
	  }
	  try {
	      //Normal Anzeige mit getPrettyFormat()
	      XMLOutputter outputFile = new XMLOutputter(Format.getPrettyFormat());
	     
	      outputFile.output(doc, new FileOutputStream("Mailbox"+this.getCurrentKonto().getKonto() +".xml"));
	   }
	   catch (java.io.IOException e){
		   System.out.println("Konnte die Konten nicht im XML Format speichern.");
		   e.printStackTrace();
	   }
		  
	}
	/**
	 * Speichert das ganze Postfach 1:1 vom Server in MailContainers, analog zu den Anzahl Folders, sp�ter in ein XML, wird ben�tigt f�r erst Initialiseriungen
	 */
	public void initializeMailbox(){
		try {
			this.updateMailFolders(Folder.READ_ONLY);
			this.saveMailContainers();
		} catch (MessagingException e) {
			System.out.println("Konnte die Mailbox nicht initialisieren");
			e.printStackTrace();
		}
  }
  public ArrayList<Folder> getMailFolders() {
	return serverMailFolders;
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
  
  private ArrayList<String> handleAttachement(Message message) throws MessagingException, IOException{
	  // suppose 'message' is an object of type Message
	  ArrayList<String> nameGuids = new ArrayList<String>();
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
		    	  String nameGuid = UUID.randomUUID()+"-"+part.getFileName();
		    	  nameGuids.add(nameGuid);
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
	  return nameGuids;
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
private void setMailFolders(ArrayList<Folder> serverMailFolders) {
	this.serverMailFolders = serverMailFolders;
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
  

