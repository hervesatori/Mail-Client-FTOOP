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
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import ftoop.mailclient.daten.EmailKonto;
import ftoop.mailclient.daten.EmailKontoControl;
import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailButtonListenerTool;
import ftoop.mailclient.daten.MailContainer;
import ftoop.mailclient.daten.MailControl;



public final class MainView extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8066584911865251505L;
	/**
	 * 
	 */
	private EmailKontoControl kontoControl;
	private HashMap<String,MailControl> mailControlContainer;
//	private JPanel panelCenter;
	private JSplitPane splitPane;
	// Labeltitel für Mails werden definiert
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
		           splitPane.setRightComponent(new JPanel());
		    	   splitPane.validate();
		    	   
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
		 	//Init der panelCenter
		 	JPanel panelCenter = new JPanel();
		 	panelCenter.add(new JTextArea("MAIL CLIENT"));
	        splitPane = new JSplitPane();
	    	int width = Toolkit.getDefaultToolkit().getScreenSize().width;
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
	 //  	    Point origin = e.getPoint () ;
	 //       int row = table.rowAtPoint( origin ) ; 
	        Mail mailLocal = containingMails.get(table.convertRowIndexToModel(table.getSelectedRow()));//containingMails.get(row);
	        mailLocal.setIsRead(true);
	        System.out.println("MAIL:  "+mailLocal.getFrom()+"Menge:  "+mailLocal.getAttachments().size());
	        
	        
	        
	        
	        scrollPane = new JScrollPane(table);
	        panelCenter = new JPanel(new BorderLayout(5,5));
//	        final MailWindows readingMailWindow = new MailWindows(mailLocal,"lesen", this);

	        //Zeige im MainGui die Mail unterhalb der Selektion an
	        panelCenter.add(scrollPane,BorderLayout.NORTH);
	        panelCenter.add(new MailPane(mailLocal,MailPane.MailWindowType.READ,this.folderSelectionListener.getSelectedMailControl()),BorderLayout.CENTER);
	        this.replaceRightComponentWithNewPanel(panelCenter);
	        
	        if(!oneClick) {
	        	//Doppelklick, somit öffne die Mail in einem separaten Fenster
	        	final MailPane mw = new MailPane(mailLocal,MailPane.MailWindowType.READ, this.folderSelectionListener.getSelectedMailControl());
				final PopupMailFrame readingFrame = new PopupMailFrame(mailLocal.getFrom(), mw,PopupMailFrame.READING_MAIL_FRAME );
	        	
	        }
    }  
    private void replaceRightComponentWithNewPanel(JPanel newPanel){
        // sog. Resizing mit splitpane und Divider
        int pos = splitPane.getDividerLocation();
        splitPane.setRightComponent(newPanel);
		splitPane.setDividerLocation(pos);
    }
	 public void setMailSelection(TreePath treePath){
	    	Object[] obj = null;
	        final Object pathComponent = treePath.getLastPathComponent();
	        final String type = pathComponent.getClass().getSimpleName();
	        //Button neue mail wird aktiviert
	        this.setButtonNeueOn();
	        System.out.println("Path: " + treePath + " / Object: " + pathComponent.toString() + " / Type: " + type+"///////"+pathComponent);
	        //return a array of path
	        obj= treePath.getPath();
	        System.out.println(mailControlContainer.get(obj[1].toString()).toString());  
	        System.out.println(obj[1].toString());
	        HashMap<String,MailContainer> mailcontainers = mailControlContainer.get(obj[1].toString()).getMailContainers();
	        
	        // Ordnerbehandlung, "/" wird getrennt
	        String tempStr = treePath.toString().replaceAll("\\s","");
	        String currentPath;
	        String[]anpassen = tempStr.split(",");
	        int lange = anpassen.length;
	        if(lange>3) {
	        	currentPath = anpassen[lange-2]+"/"+pathComponent.toString();
	        	
	        }else {
	        	currentPath = pathComponent.toString();
	        }
	        System.out.println(currentPath);
	     	if(mailcontainers.get(currentPath)!=null) {
	            //  Init von List containingsMails (Alle Mails von einen Ordner)
	     		//  TableModel wird auch instanziert
	            List<Mail> containingMails = mailControlContainer.get(obj[1].toString()).getMailContainers()
	        		  .get(currentPath).getContainingMails();
//	            final MailControl mailControl = mailControlContainer.get(obj[1].toString());
		        final MailTableModel tableModel = new MailTableModel(containingMails);
				final JTable table = new JTable(tableModel);
				// JTable wird nach Datum sortiert
				table.setAutoCreateRowSorter(true);
	            table.getRowSorter().toggleSortOrder(3);
	            table.setDefaultRenderer(Object.class, new BoldRenderer());
	            table.getRowSorter().toggleSortOrder(3);
	            
		        //JTABLE wird via ein jScrollPane abgebildet
		        // ***********************************************
		    	JScrollPane scrollPane = new JScrollPane(table);
		    	JPanel panelCenter = new JPanel(new GridLayout(2,1,5,5));
	     		panelCenter.add(scrollPane);
	     		
	     		
//			     Setze beim Splitpane den Divider auf die letzte Position, da durch den Tausch des Panels das Splitpane neu gepackt wird.
		        final int pos = splitPane.getDividerLocation();
	     		splitPane.setRightComponent(panelCenter);
				splitPane.setDividerLocation(pos);
				
			    table.addMouseListener ( new MouseAdapter () {
			         public void mouseClicked ( MouseEvent e ) {
			        	 setButtonLoeschenAntWeiterOn();
			             if  (e.getClickCount () == 1) {
			            	 clickRefresh(scrollPane,panelCenter,splitPane,true,e,table,containingMails);			            	
			             }else if(e.getClickCount () == 2) {
			            	 clickRefresh(scrollPane,panelCenter,splitPane,false,e,table,containingMails);
			             }
			         }
			       });

	     	}        
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
  
	        	  Set<String> keyParent = mailControlContainer.get(currentKonto.getName()).
	        			  getParentContainer().keySet(); 
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
	         //Falls es nun mindestens ein Konto hat, den OrdnerSynchro Button aktivieren
	         this.checkOrdnerSynchro();
	    }
	    this.mailTreeModel.reload(this.rootNode);
	  }
//	  public void synchronizeFolders(){
//		  
//	  }
	  private void checkOrdnerSynchro(){
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
  		  kontoControl.loadKonten("kontos.xml");
  		  this.startFolderSynchronization();
  		  
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
 	  //also kontoControl aktualisieren
 	 kontoControl.loadKonten("kontos.xml");
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


