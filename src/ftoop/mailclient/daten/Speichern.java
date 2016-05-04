package ftoop.mailclient.daten;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.*;

public class Speichern {
	 private  org.jdom2.Document doc;
	 private  Element root;
	 private  Element mail;
	 private Element mailDate;
	 private  Element to;
	 private  Element from;
	 private  Element subject;
	 private Element message;
	 private  SAXBuilder sxb;
	 private  SimpleDateFormat sdf;
	
	 
	 
	// erstellt der XML File 
	public  void  createFile() {
	  //Würzel erstellen
	   root = new Element("mails");
	  //neu Document 
	   doc = new Document(root);    
	}
	
	public  void xmlParser(String file) {
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
	public  void setMail() {
		 mail = new Element("mail");
	     root.addContent(mail);
	}
	
	public  org.jdom2.Document getDoc() {
		return doc;
	}
	
	public  void setDocNull() {
		doc=null;
	}
	public  void setDate(Date date) {
		  sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
	      mailDate = new Element("datum");
	      mailDate.setText(sdf.format(date));
	      mail.addContent(mailDate);
	     
	}
	
	public  void setTo(String str) {
	      to = new Element("to");
	      to.setText(str);
	      mail.addContent(to);
	     
	}
	
	public  void setFrom(String str) {
	      from = new Element("from");
	     from.setText(str);
	      mail.addContent(from);  
	}
	
	public  void setSubject(String str) {
	      subject= new Element("subject");  
	      subject.setText(str);
	      mail.addContent(subject);     
	}
	public  void setMessage(String str) {
	      message= new Element("message");  
	      message.setText(str);
	      mail.addContent(message);     
	}
	
	
	public  void speichern(String file) {
		
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


