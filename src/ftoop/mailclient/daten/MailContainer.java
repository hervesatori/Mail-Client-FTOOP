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
//@Override
//public void appendMessages(Message[] arg0) throws MessagingException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void close(boolean arg0) throws MessagingException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public boolean create(int arg0) throws MessagingException {
//	// TODO Auto-generated method stub
//	return false;
//}
//
//@Override
//public boolean delete(boolean arg0) throws MessagingException {
//	// TODO Auto-generated method stub
//	return false;
//}
//
//@Override
//public boolean exists() throws MessagingException {
//	// TODO Auto-generated method stub
//	return false;
//}
//
//@Override
//public Message[] expunge() throws MessagingException {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public Folder getFolder(String arg0) throws MessagingException {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public String getFullName() {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public Message getMessage(int arg0) throws MessagingException {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public int getMessageCount() throws MessagingException {
//	// TODO Auto-generated method stub
//	return 0;
//}
//
//@Override
//public String getName() {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public Folder getParent() throws MessagingException {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public Flags getPermanentFlags() {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public char getSeparator() throws MessagingException {
//	// TODO Auto-generated method stub
//	return 0;
//}
//
//public int getType() throws MessagingException {
//	// TODO Auto-generated method stub
//	return 0;
//}
//
//public boolean hasNewMessages() throws MessagingException {
//	// TODO Auto-generated method stub
//	return false;
//}
//
//public boolean isOpen() {
//	// TODO Auto-generated method stub
//	return false;
//}
//
//@Override
//public Folder[] list(String arg0) throws MessagingException {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public void open(int arg0) throws MessagingException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public boolean renameTo(Folder arg0) throws MessagingException {
//	// TODO Auto-generated method stub
//	return false;
//}

}
