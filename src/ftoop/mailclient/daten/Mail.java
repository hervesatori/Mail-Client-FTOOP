package ftoop.mailclient.daten;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author Herve Satori & Domique Borer
 *
 */
public class Mail {
	 private String messageID;
	 private Date received;	 
	 private String to;
	 private String cc;
	 private String bcc;
	 private String from;
	 private String subject;
	 private String message;
	 private ArrayList<File> attachments; 
	/**
	 *  
	 * @param messageID
	 * @param received
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param from
	 * @param subject
	 * @param message
	 * @param attachments
	 */
   public Mail (String messageID, Date received, String to, String cc,String bcc, String from, String subject,String message, ArrayList<File>  attachments) {
	   this.messageID = messageID;
	   this.received = received;
	   this.to = to;
	   this.cc = cc;
	   this.bcc = bcc;
	   this.from = from;
	   this.subject = subject;
	   this.message = message;
	   this.attachments = attachments;
   }
   public String getMessageID() {
	return messageID;
   }

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public Date getReceived() {
		return received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	public String getCc() {
		return cc;
	}
	
	public void setCc(String cc) {
		this.cc = cc;
	}
	
	public String getBcc() {
		return bcc;
	}
	
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	
	public ArrayList<File>  getAttachments() {
		return attachments;
	}
	
	public void setAttachments(ArrayList<File>  attachments) {
		this.attachments = attachments;
	}
	
	public String getTo() {
		   return to;
   }
   
   public String getFrom() {
	   return from;
   }
   
   public String getSubject() {
	   return subject;
   }
   
   public String getMessage() {
	   return message;
   }
   
   public void setTo(String to) {
	   this.to = to;
   }
   
   public void setFrom(String from) {
	   this.from = from;
   }
   
   public void setSubject(String subject) {
	   this.subject = subject;
   }
   
   public void setMessage(String message) {
	   this.message = message;
   }
	

}
