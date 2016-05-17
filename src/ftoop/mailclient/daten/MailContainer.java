package ftoop.mailclient.daten;

import java.util.ArrayList;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
/**
 * 
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

	public ArrayList<Mail> getContainingMails(){
		
		return this.containingMails;
	}
	public void addMailToContainer(Mail mail){
		this.getContainingMails().add(mail);
		
	}
	public void deleteMailFromContainer(String mID){
		 for(Mail mail:this.getContainingMails()){
			  if(mail.getMessageID().equals(mID)){
				  this.getContainingMails().remove(mail);
			  }
		  }
	}
}
