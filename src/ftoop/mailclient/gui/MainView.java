package ftoop.mailclient.gui;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import ftoop.mailclient.daten.EmailKonto;
import ftoop.mailclient.daten.EmailKontoControl;
import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailControl;

/**
 * Hauptlayout von Mail Client 
 * 
 * @author Dominique Borer & Herve Satori
 *
 */

public final class MainView extends JFrame{
	
	private static final long serialVersionUID = -8066584911865251505L;
	
	private EmailKontoControl kontoControl;
	private HashMap<String,MailControl> mailControlContainer;
	private JSplitPane splitPane;
	// Labeltitel für Mails werden definiert
	private String xmlPath = "kontos.xml";
	protected String von = "Von";
	protected String an = "An";
	protected String betreff = "Betreff";
	protected String datum = "Datum";
	private JTree currentTree;
	private JButton neueMail;
	private JButton loeschen;
	private JButton antworten;
	private JButton weiterLeiten;
	private JButton schliessen;
	private JButton ordnerSynchro;
	private JButton konfiguration;
	private MailWorker mailWorker;
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel mailTreeModel;	
	private FolderSelectionListener folderSelectionListener;
	private JSplitPane horizontalMailPane;
	
	/**
	 * Create the frame.
	 * @param string 
	 */
	public MainView(String name) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.initGUI(name);
		
		this.init(true);
	   
	}
	
	private JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
	    neueMail = new JButton("Neue E-Mail");
		loeschen = new JButton("Löschen");
	    antworten = new JButton("Antworten");
	    weiterLeiten = new JButton("Weiterleiten");
	    schliessen = new JButton("schliessen");
	    ordnerSynchro = new JButton("Ordner synchronisieren");
	    konfiguration = new JButton("Konfiguration");
		neueMail.setEnabled(false);
		antworten.setEnabled(false);
		weiterLeiten.setEnabled(false);
		loeschen.setEnabled(false);
		buttonDisable();
		neueMail.addActionListener(MailButtonListenerTool.getNewMailActionListener(this.folderSelectionListener));
		loeschen.addActionListener(MailButtonListenerTool.getDeleteMailActionListener(this.folderSelectionListener));
		antworten.addActionListener(MailButtonListenerTool.getRespondToMailActionListener(this.folderSelectionListener));
		weiterLeiten.addActionListener(MailButtonListenerTool.getForwardToMailActionListener(this.folderSelectionListener));
		schliessen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
					//Ersetze das Panel in der Mitte durch ein leeres und zeige somit ein Schliessen an
					replaceRightComponentWithNewPanel(new JPanel());	
				   
				   loeschen.setEnabled(false);
				   weiterLeiten.setEnabled(false);
				   antworten.setEnabled(false);
			}
			
		});
//		senden.addActionListener(this.buttonListener);
		konfiguration.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel panelCenter = new ConfigurationPanel(MainView.this);
				MainView.this.replaceRightComponentWithNewPanel(panelCenter);

			}
			
		});
		ordnerSynchro.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!startFolderSynchronization()){
					JOptionPane.showMessageDialog(MainView.this, "Ordner Synchronization läuft bereits.");
				}
			}
			
		});
		//Die Buttons werden an JMenuBar angehängt
		menuBar.add(neueMail);
		menuBar.add(loeschen);
		menuBar.add(antworten);
		menuBar.add(weiterLeiten);
		menuBar.add(schliessen);
		//2 Buttons werden jetzt Rechtsbündig plaziert
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(ordnerSynchro);
		menuBar.add(konfiguration);
		return menuBar;
	}
	
	/**
	 * 
	 * @return Returns false, if MailWorker (Synchronization) is already running, true for successfully Start
	 */
	private boolean startFolderSynchronization(){
		
		//überprüfe ob doBackground fertig ist oder cancelled
		if(mailWorker == null || mailWorker.isDone() == true ||mailWorker.isCancelled()){
			//Mailworker ist fertig und starte nochmals neu
	  		  this.mailWorker = new MailWorker(kontoControl,mailControlContainer, this );	  			
			  this.mailWorker.execute(); 
			  return true;
		}else{
			//Prozess ist noch am laufen
			return false;
		}
		
	}
	 private JComponent createCenterPanel() {

	    	int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	    	int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		 	//Init der panelCenter
		 	JPanel panelCenter = new JPanel();
		 	this.horizontalMailPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		 	this.horizontalMailPane.setTopComponent(new JLabel("MAIL CLIENT", SwingConstants.CENTER));
		 	this.horizontalMailPane.setDividerLocation((int)(height*0.5));
		 	panelCenter.add(this.horizontalMailPane);
		 	
	        splitPane = new JSplitPane();
			splitPane.setDividerLocation((int)(width*0.2));
	        //JTREE  ****************************************
	        // Root wird via Method buildTree erzeugt
	        this.rootNode = new DefaultMutableTreeNode("Racine");
	        this.mailTreeModel = new DefaultTreeModel(rootNode);
	        //Zuweisung an currentTree
	        currentTree = new JTree(mailTreeModel);
	        currentTree.setRootVisible(false); 
	        currentTree.expandRow(0);
	        //Initialisierung des FolderSelection Listener(Treelistener)
	        this.folderSelectionListener = new FolderSelectionListener(this);
	        currentTree.addTreeSelectionListener(folderSelectionListener);
	        splitPane.setLeftComponent(new JScrollPane(currentTree));
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
	 public void setButtonNeueOn() {
		   neueMail.setEnabled(true);
		
	 }
	 public void setButtonLoeschenAntWeiterOn() {
		    antworten.setEnabled(true);
			weiterLeiten.setEnabled(true);
			loeschen.setEnabled(true);
	 }
    private void clickRefresh(JScrollPane scrollPane,JPanel panelCenter,
	    		JSplitPane splitPane,Boolean oneClick,MouseEvent e,JTable table,List<Mail> containingMails) {
    	
	        Mail mailLocal = this.folderSelectionListener.getSelectedMail();
	        mailLocal.setIsRead(true);
	        System.out.println("MAIL:  "+mailLocal.getFrom()+" mID: "+ mailLocal.getMessageID() +"Menge:  "+mailLocal.getAttachments().size());
	        
	        scrollPane = new JScrollPane(table);
	        panelCenter = new JPanel(new BorderLayout(5,5));

	        //Zeige im MainGui die Mail unterhalb der Selektion an
	        this.replaceHorizontalSplitPaneComponents(scrollPane, new MailPane(mailLocal,MailPane.MailWindowType.READ,this.folderSelectionListener.getSelectedMailControl()));
	        panelCenter.add(this.horizontalMailPane);
	        this.replaceRightComponentWithNewPanel(panelCenter);
	        
	        if(!oneClick) {
	        	//Doppelklick, somit öffne die Mail in einem separaten Fenster
	        	final MailPane mw = new MailPane(mailLocal,MailPane.MailWindowType.READ, this.folderSelectionListener.getSelectedMailControl());
				final PopupMailFrame readingFrame = new PopupMailFrame(mailLocal.getFrom(), mw,PopupMailFrame.READING_MAIL_FRAME,folderSelectionListener );
				readingFrame.setVisible(true);
	        }
    }  
    private void replaceRightComponentWithNewPanel(JPanel newPanel){
        // sog. Resizing mit splitpane und Divider
        int pos = splitPane.getDividerLocation();
        splitPane.setRightComponent(newPanel);
		splitPane.setDividerLocation(pos);
    }
    private void replaceHorizontalSplitPaneComponents(JComponent top, JComponent bottom){
        // sog. Resizing mit splitpane und Divider
        int pos = this.horizontalMailPane.getDividerLocation();
        this.horizontalMailPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.horizontalMailPane.setTopComponent(top);
        this.horizontalMailPane.setBottomComponent(bottom);
        horizontalMailPane.setDividerLocation(pos);
    }
	 public void setMailSelection(){
		        //JTABLE wird via ein jScrollPane abgebildet
		        // ***********************************************
		 		JTable currentMailTable = folderSelectionListener.getCurrentTable();
		    	JScrollPane scrollPane = new JScrollPane(currentMailTable);
		 		this.replaceHorizontalSplitPaneComponents(scrollPane, new JPanel());
		    	JPanel panelCenter = new JPanel(new GridLayout()); //(new GridLayout(2,1,5,5));
	     		panelCenter.add(this.horizontalMailPane);
	     		
	     		
	     		
//			     Setze beim Splitpane den Divider auf die letzte Position, da durch den Tausch des Panels das Splitpane neu gepackt wird.
		        this.replaceRightComponentWithNewPanel(panelCenter);
				
				currentMailTable.addMouseListener ( new MouseAdapter () {
			         public void mouseClicked ( MouseEvent e ) {
			        	 setButtonLoeschenAntWeiterOn();
			        	 List<Mail> containingMails = folderSelectionListener.getCurrentContainingMails();
		            	 if  (e.getClickCount () == 1) {
			            	 clickRefresh(scrollPane,panelCenter,splitPane,true,e,currentMailTable,containingMails);
			             }else if(e.getClickCount () == 2) {
			            	 clickRefresh(scrollPane,panelCenter,splitPane,false,e,currentMailTable,containingMails);
			             }			            
			         }
			       });

	     	       
	 }

	 /**
	  * buildTree wird verwendet um links in der Gui den Mailkontobaum anzuzeigen
	  */
	  public synchronized void buildTree(){ 
		System.out.println("Builde MailTree");
	    this.rootNode.removeAllChildren();
	    if(this.kontoControl.getKontos()!=null && this.mailControlContainer!=null) {
            System.out.println("Anzahl Kontis zum Tree hinzuzufügen: "+ kontoControl.getKontos().size());
			//Kontos werden an Root hinzugefügt
            Iterator<EmailKonto> kontoIterator = kontoControl.getKontos().iterator();
	         while(kontoIterator.hasNext()){
	        	 EmailKonto currentKonto = kontoIterator.next();
	        	final String kontoName= currentKonto.getName();
	     		System.out.println("Füge Konto "+ kontoName  + " hinzu.");
	        	  DefaultMutableTreeNode konto = new DefaultMutableTreeNode(kontoName);  	
	        	  //Folders werden an Konto hinzugefügt  
	        	  MailControl currentMC = mailControlContainer.get(currentKonto.getName());
	        	  	//Falls der MailControlContainer schon geladen wurde
	        	  if(currentMC != null){
		        	  Set<String> keyParent = currentMC.getParentContainer().keySet(); 
		        	  System.out.println(keyParent.size());
		        	for(String folderFullPath : keyParent ){
		            	
		        		DefaultMutableTreeNode folder = new DefaultMutableTreeNode(folderFullPath);
		        	    ArrayList<String> str =	mailControlContainer.get(currentKonto.getName()).
		        	    		getParentContainer().get(folderFullPath);
		                for(String folder1 : str) {
		                	DefaultMutableTreeNode folder2 = new DefaultMutableTreeNode(folder1);
		                	folder.add(folder2);
		                }
		               konto.add(folder);
		        	}   
		               for(String str2 : mailControlContainer.get(currentKonto.getName()).getFolderWithoutParent()) {
		            	   if(!str2.equals(null)){
			            	   DefaultMutableTreeNode folder3 = new DefaultMutableTreeNode(str2);
			            	   konto.add(folder3);
		            	   }
		               }
		        	
		            this.rootNode.add(konto); 
		         }
	         }
	    }
	    this.mailTreeModel.reload(this.rootNode);
	  }

	  public void checkOrdnerSynchro(){
		  if(this.kontoControl.getKontos().size()>0){
			  this.ordnerSynchro.setEnabled(true);
		  }else{
			  this.ordnerSynchro.setEnabled(false);
		  }
	  }
	  private void initGUI(String name){
			this.setTitle(name);
			this.setSize(1100,700);
			this.setLocationRelativeTo(null);
			this.setLayout(new BorderLayout(5,5));
			this.getContentPane().add(createCenterPanel(),BorderLayout.CENTER);
			this.getContentPane().add(createMenuBar(),BorderLayout.NORTH);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	  }

	  private void init(Boolean start){
      	  //**********erstellen der MailControlContainer
  		  mailControlContainer = new HashMap<String,MailControl>();
  		  //**********Laden der Konti
  		  kontoControl = new EmailKontoControl();
  		  kontoControl.loadKonten(this.getXmlPath());
  		  this.startFolderSynchronization();
         //Falls es nun mindestens ein Konto hat, den OrdnerSynchro Button aktivieren
         this.checkOrdnerSynchro();
         
		  this.setVisible(true);		  
	  }
  /**
	 * @return the mailControlContainer
	 */
	protected HashMap<String, MailControl> getMailControlContainer() {
		return mailControlContainer;
	}
   public EmailKontoControl getKontoControl() {	   
	   return kontoControl;
   }
   public void removeKontoFromMailTree(EmailKonto kontoToRemove){
	   Object root = this.currentTree.getModel().getRoot();
 	  DefaultTreeModel treeModel = (DefaultTreeModel) this.currentTree.getModel();
 	  for(int i = 0; i < treeModel.getChildCount(root);i++) {
 		  if(treeModel.getChild(root, i).toString().equals(kontoToRemove.getEmail())) {
 			  treeModel.removeNodeFromParent((MutableTreeNode) treeModel.getChild(root, i));
 		  }
 	  } 

   }
	/**
	 * @return the xmlPath
	 */
	public String getXmlPath() {
		return xmlPath;
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
   	
  		
   	
  		SwingUtilities.invokeLater(new Runnable() {
  	      @Override
  	      public void run() {
  	        @SuppressWarnings("unused")
			final MainView mailClient = new MainView("FTOOP MailClient");
  	      }
  	    });
}

}


