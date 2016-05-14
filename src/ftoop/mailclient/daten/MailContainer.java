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

public class MailContainer extends Folder {
	private Folder folder;
	private ArrayList<Mail> containingMails;
	private ArrayList<MailContainer> subContainers;
	
	
	protected MailContainer(Store store ,Folder folder) {
		super(store);
		this.folder = folder;
		// TODO Auto-generated constructor stub
	}

@Override
public void appendMessages(Message[] arg0) throws MessagingException {
	// TODO Auto-generated method stub
	
}

@Override
public void close(boolean arg0) throws MessagingException {
	// TODO Auto-generated method stub
	
}

@Override
public boolean create(int arg0) throws MessagingException {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean delete(boolean arg0) throws MessagingException {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean exists() throws MessagingException {
	// TODO Auto-generated method stub
	return false;
}

@Override
public Message[] expunge() throws MessagingException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Folder getFolder(String arg0) throws MessagingException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getFullName() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Message getMessage(int arg0) throws MessagingException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public int getMessageCount() throws MessagingException {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public String getName() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Folder getParent() throws MessagingException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Flags getPermanentFlags() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public char getSeparator() throws MessagingException {
	// TODO Auto-generated method stub
	return 0;
}

public int getType() throws MessagingException {
	// TODO Auto-generated method stub
	return 0;
}

public boolean hasNewMessages() throws MessagingException {
	// TODO Auto-generated method stub
	return false;
}

public boolean isOpen() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public Folder[] list(String arg0) throws MessagingException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void open(int arg0) throws MessagingException {
	// TODO Auto-generated method stub
	
}

@Override
public boolean renameTo(Folder arg0) throws MessagingException {
	// TODO Auto-generated method stub
	return false;
}

}
