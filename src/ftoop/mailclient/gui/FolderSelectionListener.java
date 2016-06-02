package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailContainer;
import ftoop.mailclient.daten.MailControl;


public final class FolderSelectionListener implements TreeSelectionListener {
	private HashMap<String,MailControl> mailControlContainer;
	
	private JScrollPane scrollPane;
	private JSplitPane splitPane;
	private JPanel panelCenter;
	private static Mail mail;
	private static MailControl mailControl;
	
	
	public FolderSelectionListener(HashMap<String,MailControl> mailControlContainer,JSplitPane splitPane,JPanel panelCenter) {
		this.mailControlContainer = mailControlContainer;
		this.splitPane = splitPane;
		this.panelCenter = panelCenter;	
	}
    @Override
    public void valueChanged(final TreeSelectionEvent event) {
    	Object[] obj = null;
        final TreePath treePath = event.getPath();
        final Object pathComponent = treePath.getLastPathComponent();
        final String type = pathComponent.getClass().getSimpleName();
        
        System.out.println("Path: " + treePath + " / Object: " + pathComponent.toString() + " / Type: " + type);
        
        	  obj= treePath.getPath();
          System.out.println(mailControlContainer.get(obj[1].toString()).toString());  
            System.out.println(obj[1].toString());
        HashMap<String,MailContainer> mailcontainers = mailControlContainer.get(obj[1].toString()).getMailContainers();
        
        /* if ( mailcontainers.get(pathComponent.toString())!=null){
             System.out.println("NOT NULL");
         }
      // Get keys.
     	Set<String> keys = mailcontainers.keySet();

     	// Loop over String keys.
     	for (String key : keys) {
     	    System.out.println(key);
     	}*/
        
     	if(mailcontainers.get(pathComponent.toString())!=null) {
            //  Init von List containingsMails (Alle Mails von einen Ordner)
     		//  TableModel wird auch instanziert
            List<Mail> containingMails = mailControlContainer.get(obj[1].toString()).getMailContainers()
        		  .get(pathComponent.toString()).getContainingMails();
            mailControl = mailControlContainer.get(obj[1].toString());
	        final MailTableModel tableModel = new MailTableModel(containingMails);
			final JTable table = new JTable(tableModel);
         
		     
		     table.addMouseListener ( new MouseAdapter () {
		         public void mouseClicked ( MouseEvent e ) {
		             if  (e.getClickCount () == 1) {
		            	 clickRefresh(scrollPane,panelCenter,splitPane,true,e,table,containingMails);
		 	       
		         //        splitPane.validate();
		             }else if(e.getClickCount () == 2) {
		            	 clickRefresh(scrollPane,panelCenter,splitPane,false,e,table,containingMails);
		             }
		         }
		       });
		        //JTABLE wird via ein jScrollPane abgebildet
		        // ***********************************************
		     	scrollPane = new JScrollPane(table);
	     		panelCenter = new JPanel(new GridLayout(2,1,5,5));
	     		panelCenter.add(scrollPane);
	     		splitPane.setRightComponent(panelCenter);
	       
	     		splitPane.validate();
		  
     	}        
   }
    public void clickRefresh(JScrollPane scrollPane,JPanel panelCenter,
    		JSplitPane splitPane,Boolean oneClick,MouseEvent e,JTable table,List<Mail> containingMails) {
   	    Point origin = e.getPoint () ;
        int row = table.rowAtPoint ( origin ) ;
        
        Mail mailLokal = containingMails.get(row);
        mail = mailLokal;
        System.out.println("MAIL:  "+mail.getFrom());
        scrollPane = new JScrollPane(table);
        panelCenter = new JPanel(new BorderLayout(5,5));
        if(oneClick) {
          panelCenter.add(scrollPane,BorderLayout.NORTH);
        }
        panelCenter.add(new MailWindows(mailLokal,"lesen").getPanelUnten(),BorderLayout.CENTER);
        splitPane.setRightComponent(panelCenter);
        splitPane.revalidate();  
        splitPane.repaint();
    }  
    public static Mail getSelectedMail() {
    	return mail;
    }
    public static MailControl getSelectedMailControl() {
    	return mailControl;
    }
// *******************************************    panelCenter.add(MainView.createPanelMail(mail),BorderLayout.CENTER);
   
}

