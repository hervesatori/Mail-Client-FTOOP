package ftoop.mailclient.daten;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
/**
 * 
 * @author Herve Satori & Dominique Borer
 *
 */
public class MailControl {
	private EmailKonto currentKonto;
	private ArrayList<Folder> serverMailFolders;
	private HashMap<String, MailContainer> mailContainers;
	private Store store;
	private  final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private String mailboxName; 
	private String attachmentPath;
	
	private ArrayList<String> folderContainer;
	private HashMap<String, ArrayList<String>> parentContainer;
	private Set<String> folderWithoutParent;
	
  public MailControl(EmailKonto currentKonto) {
	  //Initialisieren der Variablen
	  this.currentKonto = currentKonto;
	  this.serverMailFolders = new ArrayList<Folder>();
	  this.mailContainers = new HashMap<String, MailContainer>();
	  this.attachmentPath = "Attachment/";
	  
	  this.folderContainer = new ArrayList<String>();
	  this.parentContainer= new HashMap<String,ArrayList<String>>();
	  this.folderWithoutParent = new TreeSet<String>();
	  //Setzen des Mailboxnamens
	  this.mailboxName = "Mailbox-"+this.getCurrentKonto().getKonto() +".xml";
  }
  
public void mailReceive(){
	  
	  //Initialisieren des Konto Stores für die Mailabfrage
	  Properties props = System.getProperties();
	  props.setProperty("mail.store.protocol", "imaps");
	  try {
		    Session session = Session.getDefaultInstance(props, null);
		    this.store = session.getStore("imaps");
		    store.connect(this.getCurrentKonto().getImapServer(), this.getCurrentKonto().getName(), this.getCurrentKonto().getPasswortPop());
		    initializeServerMailFolders();
	 
		  
	  // Überprüfen, ob bereits eine Konto Mailbox XML vorhanden ist und falls nicht,
	  //erstellen, plus herunterladen aller Mails vom Account. Andernfalls Aktualisieren der Mailbox
	  if(this.existsMailboxXML()){
		  System.out.println("Mailbox "+this.getMailboxName()+" zum Konto "+this.getCurrentKonto().getKonto()+" wurde gefunden");
		  try {
			this.loadMailFolders(this.getMailboxName());
			this.updateMailFolders(Folder.READ_WRITE);
		} catch (MessagingException e) {
			System.out.println("Fehler beim Aktualisieren der Mailbox");
			e.printStackTrace();
		}
	  }else{
		  //********** Erstellen der Mailbox
		  System.out.println("Neu Initialisierung der Mailbox "+this.getMailboxName()+" zum Konto "+this.getCurrentKonto().getKonto()+".");
		  this.initializeMailbox();
	  }
	  } catch (MessagingException e) {
		    e.printStackTrace();
	        JOptionPane option = new JOptionPane("AUTHENTICATIONFAILED", JOptionPane.ERROR_MESSAGE);
	         JDialog dialog = option.createDialog("Message Dialog");
	         dialog.setAlwaysOnTop(true); //<-- this line
	         dialog.setModal(true);
	         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	         dialog.setVisible(true);
	  }finally {
		  try {
			  if (store != null && store.isConnected()) {  
		    	store.close();
			  }
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
		
  }
  /**
 * @return the mailboxName
 */
public String getMailboxName() {
	return mailboxName;
}
/**
   * Initialisiere ServerMailFolders - Reflektiert die Ordner auf dem Server, mit welchen später auch die direkte Mailabfrage gemacht wird
   */
  private void initializeServerMailFolders(){
	  try {
		   System.out.println("Initialisiere MailControl ServerMailFolders");
		   for (javax.mail.Folder folder:store.getDefaultFolder().list("*")) {
		        if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
		        	this.getServerMailFolders().add(folder);
		            System.out.println(folder.getFullName() + ": " + folder.getMessageCount()); 
		            System.out.println(folder.getParent() + ": " + folder.getMessageCount());	
		           
		                
		      if(folder.getParent().toString()=="") { 
	        	    System.out.println("KEINE");
		        	folderWithoutParent.add(folder.getFullName());
		        }else {
		          for(Folder fold : folder.getParent().list()) {
		        	System.out.println(fold.getName());
		        	    folderContainer.add(fold.getName());
		        }
		        if(!parentContainer.containsKey(folder.getParent().toString())) {
		            parentContainer.put(folder.getParent().toString(),folderContainer);
		        }          
	          }         
		    folderContainer = new ArrayList<String>();             
		   }
	     }
		    
	 } catch (MessagingException e) {
		    e.printStackTrace();
	 }
	  Set<String> key = parentContainer.keySet(); 
	  System.out.println(key.size());
	 for(String folderFullPath : key ){
        System.out.println(folderFullPath+"      **********");     
	 }
  }
  public HashMap<String, ArrayList<String>> getParentContainer() {
	  return parentContainer;
  }

  public Set<String> getFolderWithoutParent() {
	  return folderWithoutParent;
  }
 
  /**
 * @return the serverMailFolders
 */
public ArrayList<Folder> getServerMailFolders() {
	return serverMailFolders;
}
public boolean existsMailboxXML(){
	  boolean exists = false;
	  File mailboxXML = new File(this.getMailboxName());
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
	  System.out.println("Laden des XML "+ this.getMailboxName());
	  SAXBuilder builder = new SAXBuilder();
	  File xmlFile = new File(pathToXML);

	  try {

		Document document = (Document) builder.build(xmlFile);
		Element rootNode = document.getRootElement();
		List<Element> folders = rootNode.getChildren("Folder");
		
		//Jeden Folder im XML durchloopen und 
		for (Element folder:folders) {
			//.. für jeden einzelnen Folder einen neuen MailContainer erstellen
			MailContainer newContainer = new MailContainer(folder.getAttribute("fullName").getValue());
			//hinzufügen der einzelnen Mails aus dem XML zum Container
			List<Element> mails = folder.getChildren("Mail");
			for (Element xmlMail:mails){
				 //Laden der einzelnen Mailattributen und hinzufügen zu einem MailContainer
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
				mailToAdd.setIsRead(true);
				newContainer.addMailToContainer(mailToAdd);
			}
			//MailContainer zum MailControl hinzufügen
			this.getMailContainers().put(newContainer.getFolderFullPath(),newContainer);
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
  private void addMailContainer(Folder folder){
	  this.getMailContainers().put(folder.getFullName(), new MailContainer(folder.getFullName()));
  }
  /**
   * 
   * @param readOrWriteFolderModus Zu setzen über Folder.Read_Write oder Folder.Read_Only
   * @throws MessagingException
   */
  public void updateMailFolders(int readOrWriteFolderModus) throws MessagingException{
		if(readOrWriteFolderModus == Folder.READ_WRITE || readOrWriteFolderModus == Folder.READ_ONLY){
			for(Folder folder:this.getMailFolders()){
				System.out.println("Aktualisieren des Folders "+folder.getFullName());
				folder.open(readOrWriteFolderModus);
				//Falls es den Container noch nicht gibt..
				String searchKey = folder.getFullName();
				if(this.getMailContainers().get(searchKey) == null){
					//Container über Folder Objekt erstellen.
					System.out.println("Folder "+folder.getFullName()+ " gibt es als MailContainer noch nicht, erstelle ihn." );
					this.addMailContainer(folder);
				}
				MailContainer mc = this.getMailContainers().get(folder.getFullName());
				HashSet<String> stillExistingIDs = new HashSet<String>();
				// Array Messages wird instanziert	
				 for(Message message:folder.getMessages()){
					try {
						String mID = this.getMessageID(message.getHeader("Message-ID"));
						stillExistingIDs.add(mID);
						boolean exists = mc.existsMail(mID);
						if(exists){
							System.out.println("Mail mit ID "+mID + " existiert bereits und wird nicht erneut heruntergeladen");
							

						}else{
							System.out.println("Mail mit ID "+mID + " existiert noch nicht, wird heruntergeladen");
							Mail mail = this.generateMailFromMessage(message);
							mail.setIsRead(false);
							mc.addMailToContainer(mail);
						}
					} catch (IOException e) {
						System.out.println("Konnte kein Mailobjekt erstellen: ");
						e.printStackTrace();
					}
				 }
				 this.removeLocalMail(stillExistingIDs,mc);
			}
		}else{
			try {
				throw new Exception("readOrWriteFolderModus muss entweder für Folder.READ_WRITE:"+Folder.READ_WRITE+ " oder für Folder.READ_ONLY:"+ Folder.READ_ONLY+" sein."  );
			} catch (Exception e) {				
				e.printStackTrace();
			}
				
		}
	}
    private synchronized void removeLocalMail(HashSet<String> stillExistingIDs, MailContainer mc) {
    	Iterator<Mail> mails = mc.getContainingMails().iterator();
    	while(mails.hasNext()){
    		Mail mailInMc = mails.next();
    		if(stillExistingIDs.contains(mailInMc.getMessageID()) == false){
    			System.out.println("Mail +" + mailInMc.getSubject() + " von: "+ mailInMc.getFrom() + " mit ID: "+ mailInMc.getMessageID() + " existiert nicht mehr und wird lokal gelöscht.");
    			mails.remove();
    		}
    	}
	
    }

	private String getMessageID(String[] mIDArr){
    	String mID = "";
    	for(int i = 0; i <mIDArr.length; i++){
    		mID += mIDArr[i];
    	}
    	return mID;
    }
	public Mail generateMailFromMessage(Message msg) throws MessagingException, IOException{
		String messageID = this.getMessageID(msg.getHeader("Message-ID"));
		Date received = msg.getSentDate();
		String to = this.getToAddresses(msg);
		String cc = this.getToAddresses(msg);
		String bcc = this.getToAddresses(msg);
		String from = this.getFromAddresses(msg);
		String subject = msg.getSubject();
		String message = this.getMessageContent(msg);
		ArrayList<File>  attachments = new ArrayList<File>();
		 		  
	  //Attachments hinzufügen
	  for(String attachment:this.handleAttachment(msg)){
		  attachments.add(new File(attachment));
	  }
		Mail mail = new Mail(messageID, received, to, cc, bcc, from, subject, message, attachments);
		return mail;
	}
	/**
	 * Speichert die geladenen MailContainers zurück in ein XML
	 */
	public void saveMailContainers(){
		 //Root Element Erstellen
		  Element root = new Element("MailFolders");
		  //neues Dokument 
		  org.jdom2.Document doc = new Document(root);
		  //Pro MailContailer / Folder eigenen XML Zweig hinzufügen
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
					  
					  //Attachments hinzufügen
					  for(File attachment:mail.getAttachments()){
						  Element xmlAttachement = new Element("Attachment");
						  xmlAttachement.setText(attachment.getName());
						  xmlMail.addContent(xmlAttachement); 
					  }
					  //Nach Erstellung des XML Nodes hinzufügen zum Parent
					  xmlFolder.addContent(xmlMail);
				  }
			  
		  root.addContent(xmlFolder);
	  }
	  try {
	      //Normal Anzeige mit getPrettyFormat()
	      XMLOutputter outputFile = new XMLOutputter(Format.getPrettyFormat());
	     
	      outputFile.output(doc, new FileOutputStream(this.getMailboxName()));
	   }
	   catch (java.io.IOException e){
		   System.out.println("Konnte die Konten nicht im XML Format speichern.");
		   e.printStackTrace();
	   }
		  
	}
	/**
	 * Speichert das ganze Postfach 1:1 vom Server in MailContainers, analog zu den Anzahl Folders, später in ein XML, wird benötigt für erst Initialiseriungen
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
	            }
	        }
	     }
	     else{         
	         content= msg.getContent().toString();
	     }
	  return content;
  }
  public void deleteMail(String messageID) throws MessagingException{
	  for(MailContainer mc:this.getMailContainers().values()){
		  Iterator<Mail> mcIterator = mc.getContainingMails().iterator();
		  while(mcIterator.hasNext()){
			  Mail mail = (Mail) mcIterator.next();
			  if(mail.getMessageID().equals(messageID)){
				  //Löschen aus dem lokalen Container
				  mcIterator.remove();
				  //Löschen des physikalischen Attachments, falls vorhanden
				  this.removeAttachment(mail);
				  //Löschen vom Online Account
				  
				  for(Folder f:this.getMailFolders()){
					  //Überprüfen,dass Folder Connection offen ist und Schreibrechte gesetzt sind
					  if(!f.isOpen() || f.getMode() != Folder.READ_WRITE){
						  this.reopenFolderConnection(f, Folder.READ_WRITE);
					  }
					  if(f.getFullName().equals(mc.getFolderFullPath())){
						  SearchTerm st = new MessageIDTerm(messageID);
						  Message[] msgArr = f.search(st);
						  //Überprüfung ob über die ID wirklich nur eine Mail gefunden wird
						  if(msgArr.length==1){
							  msgArr[0].setFlag(Flags.Flag.DELETED, true);
						  }else{
							  if(msgArr.length== 0){								  
								throw new NoSuchElementException("Konnte Online die zu löschende Mail nicht finden mit ID "+messageID);				
							  }
						  }
					  }
					  //Damit die Flags effektiv gesetzt und verarbeitet werden, jeweiligen folder schliessen und neu öffnen
					  this.reopenFolderConnection(f);
				  }

			  }
		  }
	  }
  }
  private void reopenFolderConnection(Folder f) throws MessagingException{
	  int folderWriteMode = Folder.READ_ONLY;
	  if(f.isOpen()){
		  folderWriteMode = 	f.getMode();  
		  f.close(true);
		  
	  }
	  f.open(folderWriteMode);
  }
  private void reopenFolderConnection(Folder f, int folderWriteMode) throws MessagingException{
	  if(f.isOpen()){
		  f.close(true);
		  
	  }
	  f.open(folderWriteMode);
  }
  private void removeAttachment(Mail mail){
	  for(File attachment:mail.getAttachments()){
		  try{
	    		if(attachment.delete()){
	    			System.out.println("Attachment "+attachment.getName() + " is deleted!");
	    		}else{
	    			System.out.println("ATtachment delete operation is failed.");
	    		}
	    	   
	    	}catch(Exception e){
	    		
	    		e.printStackTrace();
	    		
	    	}
	  }
  }
  private String removeSpecialCharactersFromFileName(String filename){
	  Objects.requireNonNull(filename);
	  String result = filename.replaceAll("[<>:/\\|?*\"]","");
	  return result;
  }
  private ArrayList<String> handleAttachment(Message message) throws MessagingException, IOException{
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
		    	  final String filename = this.removeSpecialCharactersFromFileName(part.getFileName());
		    	  String nameGuid = UUID.randomUUID()+"-"+filename;
		    	  nameGuids.add(nameGuid);
		    	  String destFilePath = this.getAttachmentPath() + nameGuid;
		    	  
		    	  //Überpüfen, ob der Attachment Pfad existiert und falls nicht, erstellen der Ordner
		    	  File atPath = new File(this.getAttachmentPath());
		    	  if(!atPath.exists()){
		    		  System.out.println("Ausgabepfad für Attachments fehlte, erstelle diesen: "+atPath.getAbsolutePath());
		    		  atPath.mkdirs();
		    	  }
		    	  
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
  /**
 * @return the attachmentPath
 */
public String getAttachmentPath() {
	return attachmentPath;
}
/**
 * @param attachmentPath the attachmentPath to set
 */
public void setAttachmentPath(String attachmentPath) {
	this.attachmentPath = attachmentPath;
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
		if (msg.getAllRecipients()== null) return addresses;
		for(Address ad:msg.getAllRecipients()){
			addresses += ad.toString() +";";
		}
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return addresses;
	
	
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
	     
	   
	   //Falls die Mail noch Attachments beinhalten soll, diese anhängen
	   if(mail.getAttachments()!= null){
		   	// Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText(mail.getMessage());

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);
	         
	         for(File f:mail.getAttachments()){
		         // Part two is attachment
	        	 try{	        		 
	        		 if(!f.exists()){
	        			 throw new FileNotFoundException();
	        		 }
			         messageBodyPart = new MimeBodyPart();
			         DataSource source = new FileDataSource(f.getAbsolutePath());		         
			         messageBodyPart.setDataHandler(new DataHandler(source));
			         String filename = f.getName();
			         messageBodyPart.setFileName(filename);
			         multipart.addBodyPart(messageBodyPart);
	        	 }catch(FileNotFoundException e){
	        		 System.out.println("Konnte das Attachment nicht finden und überspringe dieses.");
	        		 e.printStackTrace();
	        	 }
	         }

	         // Send the complete message parts
	         message.setContent(multipart);
	   }else{
		   //Standard Mail und nur den Textinhalt setzen
		   message.setText(mail.getMessage());
	   }
	     
	   //Message wird gesendet
	   System.out.println(currentKonto.getBenutzerNameSmtp() +" " +currentKonto.getPasswortSmtp());
	   transport.connect();
	   transport.sendMessage(message, message.getAllRecipients());  
	  
	   System.out.println("message sent successfully to" + mail.getTo());
	   
	  } catch (MessagingException e) {
		   JOptionPane.showMessageDialog(null, "Keine gültige E-Mail Adresse", "Fehler", JOptionPane.ERROR_MESSAGE);
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
  public void closeAllFolderConnections() throws MessagingException{
	  for(Folder f:this.getMailFolders()){
		  if(f.isOpen()){
			  f.close(true);
		  }
	  }
	  this.getKontoStore().close();
  }
}
  

