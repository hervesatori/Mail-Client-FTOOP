package ftoop.mailclient.daten;

/**
 * 
 * @author Herve Satori & Dominique Borer
 *
 */

public class MailContainer {
	
	private String folderName;
	
   public MailContainer(String folderName) {
	   this.folderName = folderName;
   }
   
   public String getFolderName() {
	   return folderName;
   }

}
