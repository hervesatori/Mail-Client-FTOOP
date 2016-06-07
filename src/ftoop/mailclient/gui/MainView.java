package ftoop.mailclient.gui;


import java.awt.BorderLayout;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import ftoop.mailclient.daten.EmailKontoControl;
import ftoop.mailclient.daten.MailControl;



public class MainView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static EmailKontoControl kontoControl;
	private static HashMap<String,MailControl> mailControlContainer;
	private JPanel panelCenter;
	private JSplitPane splitPane;
	// Labeltitel für Mails werden definiert
	protected static final String von = "Von";
	protected static final String an = "An";
	protected static final String betreff = "Betreff";
	protected static final String datum = "Datum";
	private static JFrame fr;
	private JButton neueMail;
	private JButton loeschen;
	private JButton antworten;
	private JButton weiterLeiten;
	private JButton schliessen;
	private JButton senden;
	private JButton ordnerSynchro;
	private JButton konfiguration;
	/**
	 * Create the frame.
	 * @param string 
	 */
	public MainView(String name) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(name);
		this.setSize(1100,700);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout(5,5));
		this.getContentPane().add(createCenterPanel(),BorderLayout.CENTER);
		this.getContentPane().add(createMenuBar(),BorderLayout.NORTH);
	    fr = this.getFrame();
	}
	
	private JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
	    neueMail = new JButton("Neue E-Mail");
		loeschen = new JButton("Löschen");
	    antworten = new JButton("Antworten");
	    weiterLeiten = new JButton("Weiterleiten");
	    schliessen = new JButton("schliessen");
		senden = new JButton("senden");
	    ordnerSynchro = new JButton("Ordner synchronisieren");
	    konfiguration = new JButton("Konfiguration");
		senden.setEnabled(false);
		if(kontoControl.getKontos().size() <= 0) {
			buttonDisable();
		}
		//ButtonListener****************************************************
		neueMail.addActionListener(new ButtonListener(splitPane,panelCenter,senden,neueMail,antworten,
				weiterLeiten,loeschen,ordnerSynchro));
		loeschen.addActionListener(new ButtonListener(splitPane,panelCenter,senden,neueMail,antworten,
				weiterLeiten,loeschen,ordnerSynchro));
		antworten.addActionListener(new ButtonListener(splitPane,panelCenter,senden,neueMail,antworten,
				weiterLeiten,loeschen,ordnerSynchro));
		schliessen.addActionListener(new ButtonListener(splitPane,panelCenter,senden,neueMail,antworten,
				weiterLeiten,loeschen,ordnerSynchro));
		weiterLeiten.addActionListener(new ButtonListener(splitPane,panelCenter,senden,neueMail,antworten,
				weiterLeiten,loeschen,ordnerSynchro));
		senden.addActionListener(new ButtonListener(splitPane,panelCenter,senden,neueMail,antworten,
				weiterLeiten,loeschen,ordnerSynchro));
		konfiguration.addActionListener(new ButtonListener(splitPane,panelCenter,senden,neueMail,antworten,
				weiterLeiten,loeschen,ordnerSynchro));
		ordnerSynchro.addActionListener(new ButtonListener(splitPane,panelCenter,senden,neueMail,antworten,
				weiterLeiten,loeschen,ordnerSynchro));
		//Die Buttons werden an JMenuBar angehängt
		menuBar.add(neueMail);
		menuBar.add(loeschen);
		menuBar.add(antworten);
		menuBar.add(weiterLeiten);
		menuBar.add(schliessen);
		//2 Buttons werden jetzt Rechtsbüntig plaziert
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(senden);
		menuBar.add(ordnerSynchro);
		menuBar.add(konfiguration);
		return menuBar;
	}
	 private JComponent createCenterPanel() {
		 	//Init der panelCenter
		 	panelCenter = new JPanel();
		 	panelCenter.add(new JTextArea("MAIL CLIENT"));
	        splitPane = new JSplitPane();
	        splitPane.setDividerLocation(180);
	        //JTREE  ****************************************
	        // Root wird via Method buildTree erzeugt
	        final DefaultMutableTreeNode rootNode = buildTree();
	        final TreeModel treeModel = new DefaultTreeModel(rootNode);
	        final JTree tree = new JTree(treeModel);
	        tree.setRootVisible(false); 
	        tree.expandRow(0);
	        //  Treelistener
	        tree.addTreeSelectionListener(new FolderSelectionListener(mailControlContainer,splitPane,panelCenter));
	        splitPane.setLeftComponent(new JScrollPane(tree));
	        splitPane.setRightComponent(panelCenter);
	        return splitPane;
	  }
	 public void buttonDisable() {
		    neueMail.setEnabled(false);
			loeschen.setEnabled(false);
			antworten.setEnabled(false);
			weiterLeiten.setEnabled(false);
			ordnerSynchro.setEnabled(false);
	 }
	 /* public static JPanel createPanelMail(Mail mail) {
		  // Definition der Panels
		  final JPanel panelUnten = new JPanel(new BorderLayout(5,5));
		  final JPanel labelPane = new JPanel();
		  labelPane.setLayout(new BoxLayout(labelPane,BoxLayout.Y_AXIS));
		  labelPane.setBorder(
	                BorderFactory.createCompoundBorder(
	                                BorderFactory.createTitledBorder("E-Mail Header"),
	                                BorderFactory.createEmptyBorder(5,5,5,5)));
		  labelPane.setBackground(new Color(255,255,204));
		  //*****************************************************************
		  // Definition der JEditorPane um die Mail anzuzeigen mit JScrollPane 
		  JEditorPane msgPane= new JEditorPane();
		  msgPane.setEditable(false);
		  msgPane.setContentType("text/html");
		  msgPane.setText(mail.getMessage());
		  JScrollPane scrollEditor = new JScrollPane(msgPane);
		  //*****************************************************************
		  //Label Von, An, Betreff und Datum werden definiert
		  final JLabel labelVon = new JLabel(von+": "+mail.getFrom());
		  final JLabel labelAn = new JLabel (an+": "+mail.getTo());
		  final JLabel labelBetreff = new JLabel(betreff+": "+mail.getSubject());
		  final JLabel labelDate = new JLabel(datum+": "+mail.getReceived());
		  //die JLabels an labelPane hinzugefügt
		  labelPane.add(labelVon);
		  labelPane.add(new JLabel(" "));
		  labelPane.add(labelAn);
		  labelPane.add(new JLabel(" "));
		  labelPane.add(labelBetreff);
		  labelPane.add(new JLabel(" "));
		  labelPane.add(labelDate);
		  //labelPane und msgPane werden an panelUnten hinzugefügt
		  panelUnten.add(labelPane,BorderLayout.NORTH);
		  panelUnten.add(scrollEditor,BorderLayout.CENTER);
		  //*****************************************************************
		  return panelUnten;  
	  }*/
	 
	 
	  private DefaultMutableTreeNode buildTree(){ 
	    // Root wird erstellt
	        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Racine");
	    if(kontoControl.getKontos()!=null && mailControlContainer!=null) {
            System.out.println(kontoControl.getKontos().size());
			//Kontos werden an Root hinzugefügt
	         for(int i = 0; i < kontoControl.getKontos().size(); i++){
	        	  DefaultMutableTreeNode konto = new DefaultMutableTreeNode(kontoControl.getKontos().get(i).getName());  	
            //Folders werden an Konto hinzugefügt  
	        //    for(int j = 0; j < mailControlContainer.get(kontoControl.getKontos().get(i).
	        //    		getName()).getServerMailFolders().size();j++){  
	        	 
	        	  
	        	  Set<String> keyParent = mailControlContainer.get(kontoControl.getKontos().get(i).getName()).
	        			  getParentContainer().keySet(); 
	        	  System.out.println(keyParent.size());
	        	for(String folderFullPath : keyParent ){
	            	
	        		DefaultMutableTreeNode folder = new DefaultMutableTreeNode(folderFullPath);
	        	    ArrayList<String> str =	mailControlContainer.get(kontoControl.getKontos().get(i).getName()).
	        	    		getParentContainer().get(folderFullPath);
	                for(String folder1 : str) {
	                	DefaultMutableTreeNode folder2 = new DefaultMutableTreeNode(folder1);
	                	folder.add(folder2);
	                }
	               konto.add(folder);
	        	}   
	               for(String str2 : mailControlContainer.get(kontoControl.getKontos().get(i).getName()).getFolderWithoutParent()) {
	            	   DefaultMutableTreeNode folder3 = new DefaultMutableTreeNode(str2);
	            	   konto.add(folder3);
	               }
	        	
	        	/* Set<String> key = mailControlContainer.get(kontoControl.getKontos().get(i).getName()).getMailContainers().keySet(); 
	        	  System.out.println(key.size());
	        	for(String folderFullPath : key ){
	            	DefaultMutableTreeNode folder = new DefaultMutableTreeNode(folderFullPath);			
	                konto.add(folder);
	                System.out.println(folderFullPath);
	                
	               
	        	}*/
	       //     }
	            //Konto wird an root hinzugefügt
	            root.add(konto);  
	         }
	    }   
	       return root;
	  }
	  public static void init(Boolean start){
		
		  JFrame frameWait = new JFrame();
		  frameWait.setLayout(new BorderLayout());
		  frameWait .setSize(800,70);
		  frameWait .setLocationRelativeTo(null);
		  frameWait.setUndecorated(true); 
		//  frameWait.setAlwaysOnTop(true);
		  JProgressBar bar = new JProgressBar();
		  bar.setIndeterminate(true);
		  bar.setStringPainted(true);
		  bar.setFont(new Font("Arial",Font.BOLD,20));
		  bar.setString("Bitte warten, die Kontos und Folders werden synchronisiert");
		  frameWait.add(bar,BorderLayout.CENTER);
		  SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>() {
		      @Override
		      protected Void doInBackground()
		      {
		      	  //**********erstellen der MailControlContainer
		  		   mailControlContainer = new HashMap<String,MailControl>();
		  		   //**********Laden der Konti
		  			kontoControl = new EmailKontoControl();
		  			kontoControl.loadKonten("kontos.xml");
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
		   
		      @Override
		      protected void done() {
		    	  if(start) {
		      		JFrame frame = new MainView("Mail Client");
		      		frame.setVisible(true);
		    	  }else {
		    		 fr.dispose();
		    		 JFrame frame = new MainView("Mail Client");
			      	 frame.setVisible(true);
		    	  }
		          frameWait.dispose();
		      }
		  };
		  worker.execute();
		  frameWait.setVisible(true); 

		  
	  }
	  
   public JFrame getFrame() {
	   return this;
   }
   /**
    * Launch the application.
    * @throws UnsupportedLookAndFeelException 
    * @throws IllegalAccessException 
    * @throws InstantiationException 
    * @throws ClassNotFoundException 
    */
   public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
   IllegalAccessException, UnsupportedLookAndFeelException {
   	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
   	
  		init(true);
   	
   	
  /*//**********erstellen der MailControlContainer
	mailControlContainer = new HashMap<String,MailControl>();
	   //**********Laden der Konti
	kontoControl = new EmailKontoControl();
	kontoControl.loadKonten("kontos.xml");
	//Konti werden gesucht
	for(int i = 0; i < kontoControl.getKontos().size(); i++){
	//********** Verwenden eines Kontos mit MailControl 
        MailControl mailControl = new MailControl(kontoControl.getKontos().get(i));
        // Überprüfen, ob bereits eine Konto Mailbox XML vorhanden ist und falls nicht,
        //erstellen, plus herunterladen aller Mails vom Account. Andernfalls Aktualisieren der Mailbox
        if(mailControl.existsMailboxXML()){
         		 System.out.println("Mailbox "+mailControl.getMailboxName()+" zum Konto "+mailControl.getCurrentKonto().getKonto()+" wurde gefunden");
         		
         		 mailControl.loadMailFolders(mailControl.getMailboxName());
         		
         }else{
         		  //********** Erstellen der Mailbox
         		 System.out.println("Neu Initialisierung der Mailbox "+mailControl.getMailboxName()+" zum Konto "+mailControl.getCurrentKonto().getKonto()+".");
         		 mailControl.initializeMailbox();
         }
         	
                 mailControlContainer.put(kontoControl.getKontos().get(i).getName(),mailControl);
            
	}
	 EventQueue.invokeLater(new Runnable()
     {
        public void run()
        {
           JFrame frame = new MainView("Mail Client");
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.setVisible(true);
        }
     });
 
  }*/
}

}


