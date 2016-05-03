package ftoop.mailclient.daten;
/**
 * 
 * @author Herve Satori & Domique Borer
 *
 */
public class Mail {
	
	 private String to;
	 private String from;
	 private String subject;
	 private String message;
	 
   public Mail (String to, String from, String subject,String message) {
	   this.to = to;
	   this.from = from;
	   this.subject = subject;
	   this.message = message;
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
