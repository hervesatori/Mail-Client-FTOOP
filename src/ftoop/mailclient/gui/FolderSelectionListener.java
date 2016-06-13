package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreePath;

import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailContainer;
import ftoop.mailclient.daten.MailControl;


public final class FolderSelectionListener implements TreeSelectionListener {
	
	private JScrollPane scrollPane;
	private JSplitPane splitPane;
	private JPanel panelCenter;

	private MainView mailClient;
	private Mail mail;
	private MailControl mailControl;
	private JTable currentTable;

	
	
	public FolderSelectionListener(JSplitPane splitPane,JPanel panelCenter,MainView mailClient) {
		this.splitPane = splitPane;
		this.panelCenter = panelCenter;	
		this.mailClient = mailClient;
		
	}
    @Override
    public void valueChanged(final TreeSelectionEvent event) {
    	System.out.println("Im Mailtree wurde die Selektion geändert.");
    	this.mailClient.setMailSelection(event.getPath());
    	Object[] obj = null;
        final TreePath treePath = event.getPath();
        final Object pathComponent = treePath.getLastPathComponent();
        final String type = pathComponent.getClass().getSimpleName();
        final HashMap<String,MailControl> mailControlContainer = this.mailClient.getMailControlContainer();
        
        //Neue Mail Button wird aktiviert
        this.mailClient.setButtonNeueOn();
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
        System.out.println("Current selektierte JTREE Pfad: " +currentPath);
     	if(mailcontainers.get(currentPath)!=null) {
            //  Init von List containingsMails (Alle Mails von einen Ordner)
     		//  TableModel wird auch instanziert
            List<Mail> containingMails = mailControlContainer.get(obj[1].toString()).getMailContainers()
        		  .get(currentPath).getContainingMails();
            mailControl = mailControlContainer.get(obj[1].toString());
	        final MailTableModel tableModel = new MailTableModel(containingMails);
			final JTable table = new JTable(tableModel);
			// JTable wird nach Datum sortiert
			table.setAutoCreateRowSorter(true);
            table.getRowSorter().toggleSortOrder(3);
            table.setDefaultRenderer(Object.class, new BoldRenderer());
            table.getRowSorter().toggleSortOrder(3);
 //         table.getColumnModel().getColumn(3).setCellRenderer(new DateRenderer());
			currentTable = table;   
		    table.addMouseListener ( new MouseAdapter () {
		         public void mouseClicked ( MouseEvent e ) {
//		        	 FolderSelectionListener.this.mailClient.setButtonLoeschenAntWeiterOn();
		             if  (e.getClickCount () == 1) {
//		            	 clickRefresh(scrollPane,panelCenter,splitPane,true,e,table,containingMails);
		            	 mail.setIsRead(true);
		             }else if(e.getClickCount () == 2) {
//		            	 clickRefresh(scrollPane,panelCenter,splitPane,false,e,table,containingMails);
		            	 mail.setIsRead(true);
		             }
		         }
		       });
//		        //JTABLE wird via ein jScrollPane abgebildet
//		        // ***********************************************
//			    // sog. Resizing mit splitpane und Divider
//		        final int pos = splitPane.getDividerLocation();
//		        System.out.println("Divider Location: " + pos);
//		        System.out.println("Last Location: " + splitPane.getLastDividerLocation());
//		     	scrollPane = new JScrollPane(table);
//	     		panelCenter = new JPanel(new GridLayout(2,1,5,5));
//	     		panelCenter.add(scrollPane);
//	     		splitPane.setRightComponent(panelCenter);
//		        splitPane.setLastDividerLocation(500);
//				splitPane.setDividerLocation(500);
////	     		splitPane.revalidate(); 
     	}else{
     		//Es wurde nur der Mail Account, aber keine Ordner selektiert..
     		this.mailControl = mailControlContainer.get(currentPath);
     	}
   }
//    public void clickRefresh(JScrollPane scrollPane,JPanel panelCenter,
//    		JSplitPane splitPane,Boolean oneClick,MouseEvent e,JTable table,List<Mail> containingMails) {
//
//        Mail mailLokal = containingMails.get(table.convertRowIndexToModel(table.getSelectedRow()));//containingMails.get(row);
//        mail = mailLokal;
//        System.out.println("MAIL:  "+mail.getFrom()+"Menge:  "+mailLokal.getAttachments().size());
//        scrollPane = new JScrollPane(table);
//        panelCenter = new JPanel(new BorderLayout(5,5));
//        if(oneClick) {
//          panelCenter.add(scrollPane,BorderLayout.NORTH);
//        }
//        panelCenter.add(new MailPane(mailLokal,MailPane.MailWindowType.READ,this.getSelectedMailControl()),BorderLayout.CENTER);
////        // sog. Resizing mit splitpane und Divider
////        int pos = splitPane.getLastDividerLocation(); //.getDividerLocation();
////        System.out.println("Last Location: " + splitPane.getLastDividerLocation());
////        splitPane.setRightComponent(panelCenter);
////		splitPane.setDividerLocation(500);
//////       
////        splitPane.revalidate();  
////        splitPane.repaint();
////        
//////        splitPane.setLastDividerLocation(500);
////		splitPane.setDividerLocation(500);
//	}      
    public Mail getSelectedMail() {
    	return mail;
    }
    public MailControl getSelectedMailControl() {
    	return mailControl;
    }
    public JTable getCurrentTable() {
	   return currentTable;
    }
}

