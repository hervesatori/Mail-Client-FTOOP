package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import ftoop.mailclient.daten.EmailKonto;
import ftoop.mailclient.daten.EmailKontoControl;
import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailControl;
/**
 * Die abstrakte Klasse SwingWorker  wird implementiert
 * Mailtree wird hier aktualisiert
 * 
 * @author Dominique Borer & Herve Satori
 *
 */
public final class MailWorker extends SwingWorker<Integer, Mail> {
	private final EmailKontoControl kontoControl;
	private final HashMap<String,MailControl> mailControlContainer;
	private final MainView mailClient;
	private JFrame frameWaiting;
	/**
	 * 
	 * @param kontoControl
	 * @param mailControlContainer
	 * @param mailClient
	 */
	public MailWorker(EmailKontoControl kontoControl,HashMap<String,MailControl> mailControlContainer, MainView mailClient){
		this.kontoControl = kontoControl;
		this.mailControlContainer = mailControlContainer;
		this.mailClient = mailClient;
		
		this.displayWaitingBar();
	}
	  private void displayWaitingBar(){
		  this.frameWaiting = new JFrame();
		  frameWaiting.setLayout(new BorderLayout());
		  frameWaiting.setSize(800,50);
		  frameWaiting.setLocationRelativeTo(null);
		  frameWaiting.setUndecorated(true); 
		  frameWaiting.setAlwaysOnTop(true);
		  JProgressBar bar = new JProgressBar();
		  bar.setIndeterminate(true);
		  bar.setStringPainted(true);
		  bar.setFont(new Font("Arial",Font.BOLD,20));
		  bar.setString("Bitte warten, die Kontos und Folders werden synchronisiert");
		  frameWaiting.add(bar,BorderLayout.CENTER);
		  frameWaiting.setVisible(true);
	  }
	private void disposeframeWaiting(){
		this.frameWaiting.dispose();
	}
	@Override
	protected Integer doInBackground() throws Exception {
			
			
			Iterator<EmailKonto> kontoIterator = kontoControl.getKontos().iterator();
			//Kontos die in kontosToAdd sind, sind neue und müssen intiliaisert und zum MailControlCnotainer hinzugefügt werden werden
			ArrayList<EmailKonto> kontosToAdd = new ArrayList<EmailKonto>(); 
			while(kontoIterator.hasNext()){
				EmailKonto konto = kontoIterator.next();
				if(mailControlContainer.get(konto.getEmail())== null){
					kontosToAdd.add(konto);
				}
			}
			//Aktualisieren der bestehenden Konti
			for(MailControl currentMC:mailControlContainer.values()){
				currentMC.mailReceive();
				currentMC.saveMailContainers();
				closeFolderConnections(currentMC);				
			}			
		    //Die neuen Konti werden zum Container hinzugefügt
	        for(int i = 0; i < kontosToAdd.size(); i++){
	        	//********** Verwenden eines Kontos mit MailControl 
             MailControl mailControl = new MailControl(kontosToAdd.get(i));
             mailControl.mailReceive();
             mailControl.saveMailContainers();
             mailControlContainer.put(kontoControl.getKontos().get(i).getName(),mailControl);
             closeFolderConnections(mailControl);
	        }
      
		return null;
	}
	private void closeFolderConnections(MailControl mailControl){
        //********** Schliessen aller offenen Verbindungen
        try {
     	   mailControl.closeAllFolderConnections();
        } catch (MessagingException e) {
     	   System.out.println("Fehler beim Schliessen der offenen Verbindungen");
     	   e.printStackTrace();
        }
	}
	
	@Override
	protected void done(){
		System.out.println("SwingWorker ist fertig, aktualisiere MailTree");
		this.mailClient.buildTree();
		this.disposeframeWaiting();
	}
}
