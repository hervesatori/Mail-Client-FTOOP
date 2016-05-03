package ftoop.mailclient.daten;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.*;

public class Speichern {
	 private static  org.jdom2.Document doc;
	 private static Element root;
	 private static Element mail;
	 private static Element sentDate;
	 private static Element to;
	 private static Element from;
	 private static Element subject;
	 private static Element message;
	 private static SAXBuilder sxb;
	 private static SimpleDateFormat sdf;
	
	 
	 
	// erstellt der XML File 
	public static void  createFile() {
	  //Würzel erstellen
	   root = new Element("mails");
	  //neu Document 
	   doc = new Document(root);    
	}
	
	public static void xmlParser(String file) {
		 sxb = new SAXBuilder();
	      try {
	         //new JDOM mit file Mails wird erstellt
	         //file ist geparst;)
	         doc = sxb.build(new File(file));
	         root=doc.getRootElement();
	      }
	      catch(Exception e){ 
	    	  createFile(); 
	    	  
    	  }
	}
	public static void setMail() {
		 mail = new Element("mail");
	     root.addContent(mail);
	}
	
	public static org.jdom2.Document getDoc() {
		return doc;
	}
	
	public static void setDocNull() {
		doc=null;
	}
	public static void setSentDate(Date date) {
		  sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
	      sentDate = new Element("gesendet");
	      sentDate.setText(sdf.format(date));
	      mail.addContent(sentDate);
	     
	}
	
	public static void setTo(String str) {
	      to = new Element("to");
	      to.setText(str);
	      mail.addContent(to);
	     
	}
	
	public static void setFrom(String str) {
	      from = new Element("from");
	     from.setText(str);
	      mail.addContent(from);  
	}
	
	public static void setSubject(String str) {
	      subject= new Element("subject");  
	      subject.setText(str);
	      mail.addContent(subject);     
	}
	public static void setMessage(String str) {
	      message= new Element("message");  
	      message.setText(str);
	      mail.addContent(message);     
	}
	
	
	public static void speichern(String file) {
		
	   try {
	      //Normal Anzeige mit getPrettyFormat()
	      XMLOutputter outputFile = new XMLOutputter(Format.getPrettyFormat());
	     
	      outputFile.output(doc, new FileOutputStream(file));
	   }
	   catch (java.io.IOException e){
		   e.printStackTrace();
	   }
	}


}


