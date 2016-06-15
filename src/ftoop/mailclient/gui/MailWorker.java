package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import ftoop.mailclient.daten.EmailKontoControl;
import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailControl;

public final class MailWorker extends SwingWorker<Integer, Mail> {
	private EmailKontoControl kontoControl;
	private HashMap<String,MailControl> mailControlContainer;
	private MainView mailClient;
	private JFrame frameWaiting;
	
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
		  //Konti werden gesucht
	        for(int i = 0; i < kontoControl.getKontos().size(); i++){
	        	//********** Verwenden eines Kontos mit MailControl 
             MailControl mailControl = new MailControl(kontoControl.getKontos().get(i));
             mailControl.mailReceive();
             mailControl.saveMailContainers();
             //System.out.println(kontoControl.getKontos().get(i).getName());
             mailControlContainer.put(kontoControl.getKontos().get(i).getName(),mailControl);
             //********** Schliessen aller offenen Verbindungen
             try {
          	   mailControl.closeAllFolderConnections();
             } catch (MessagingException e) {
          	   System.out.println("Fehler beim Schliessen der offenen Verbindungen");
          	   e.printStackTrace();
             }
	        }
      
		return null;
	}
	
//	@Override
//	protected void process(final List<Mail> mails){
//		for(final Mail mail:mails ){
//			//TODO Update JTREE
//		}
//	}
	@Override
	protected void done(){
		System.out.println("SwingWorker ist fertig, aktualisiere MailTree");
		this.mailClient.buildTree();
		this.disposeframeWaiting();
	}
}
