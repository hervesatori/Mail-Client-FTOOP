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
	 private Boolean isRead;
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
   /**
    * 
    * @return
    */
   public String getMessageID() {
	return messageID;
   }
   /**
    * 
    * @param messageID
    */
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	/**
	 * 
	 * @return
	 */
	public Date getReceived() {
		return received;
	}
	/**
	 * 
	 * @param received
	 */
	public void setReceived(Date received) {
		this.received = received;
	}
	/**
	 * 
	 * @return
	 */
	public String getCc() {
		return cc;
	}
	/**
	 * 
	 * @param cc
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}
	/**
	 * 
	 * @return
	 */
	public String getBcc() {
		return bcc;
	}
	/**
	 * 
	 * @param bcc
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<File>  getAttachments() {
		return attachments;
	}
	/**
	 * 
	 * @param attachments
	 */
	public void setAttachments(ArrayList<File>  attachments) {
		this.attachments = attachments;
	}
	/**
	 * 
	 * @return
	 */
	public String getTo() {
		   return to;
   }
	/**
	 * 
	 * @return
	 */
   public String getFrom() {
	   return from;
   }
   /**
    * 
    * @return
    */
   public String getSubject() {
	   return subject;
   }
   /**
    * 
    * @return
    */
   public String getMessage() {
	   return message;
   }
   /**
    * 
    * @param to
    */
   public void setTo(String to) {
	   this.to = to;
   }
   /**
    * 
    * @param from
    */
   public void setFrom(String from) {
	   this.from = from;
   }
   /**
    * 
    * @param subject
    */
   public void setSubject(String subject) {
	   this.subject = subject;
   }
   /**
    * 
    * @param message
    */
   public void setMessage(String message) {
	   this.message = message;
   }
   /**
    * 
    * @param isRead
    */
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	/**
	 * /**
	 * @return
	 */
	public Boolean getisRead() {
		return isRead;
	}

}
