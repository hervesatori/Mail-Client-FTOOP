package ftoop.mailclient.daten;

import java.util.ArrayList;
/**
 * MailContainer, speichert alle Mails eines Mail Ordners
 * @author Herve Satori & Dominique Borer
 *
 */
public class MailContainer{
	private String folderFullPath;
	private ArrayList<Mail> containingMails;
	
	
	protected MailContainer(String folderFullPath) {
		this.folderFullPath = folderFullPath;
		this.containingMails = new ArrayList<Mail>();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the folderFullPath
	 */
	public String getFolderFullPath() {
		return folderFullPath;
	}

	/**
	 * @param folderFullPath the folderFullPath to set
	 */
	public void setFolderFullPath(String folderFullPath) {
		this.folderFullPath = folderFullPath;
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<Mail> getContainingMails(){
		
		return this.containingMails;
	}
	/**
	 * 
	 * @param mail
	 */
	public void addMailToContainer(Mail mail){
		this.getContainingMails().add(mail);
		
	}
	/**
	 * 
	 * @param mID
	 * @return
	 */
	public boolean existsMail(String mID){
		boolean exists = false;
		for(Mail mail:this.getContainingMails()){	
			  if(mail.getMessageID().equals(mID)){
				  exists = true;
			  }
		}
		return exists;
	}
	/**
	 * 
	 * @param mID
	 */
	public void deleteMailFromContainer(String mID){
		 for(Mail mail:this.getContainingMails()){
			  if(mail.getMessageID().equals(mID)){
				  this.getContainingMails().remove(mail);
			  }
		  }
	}
}
