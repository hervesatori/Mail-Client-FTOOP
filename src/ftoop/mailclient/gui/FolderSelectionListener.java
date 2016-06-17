package ftoop.mailclient.gui;

import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailContainer;
import ftoop.mailclient.daten.MailControl;


public final class FolderSelectionListener implements TreeSelectionListener {
	
	private MainView mailClient;
	private MailControl mailControl;
	private JTable currentTable;
	private MailTableModel currentMailsInTable;
	private List<Mail> currentContainingMails;

	
	
	public FolderSelectionListener(MainView mailClient) {
		this.mailClient = mailClient;
		
	}
    @Override
    public void valueChanged(final TreeSelectionEvent event) {
    	System.out.println("Im Mailtree wurde die Selektion geändert.");
    	
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
     		this.currentContainingMails = mailControlContainer.get(obj[1].toString()).getMailContainers()
        		  .get(currentPath).getContainingMails();
            mailControl = mailControlContainer.get(obj[1].toString());
	        this.currentMailsInTable = new MailTableModel(this.currentContainingMails);
			this.currentTable = new JTable(this.currentMailsInTable);
			//Verstecke die Gelesen Columne von der View
			TableColumn gelesenColumn = this.currentTable.getColumnModel().getColumn(MailTableModel.GELESEN_COLUMN);
			gelesenColumn.setMinWidth(0);
			gelesenColumn.setMaxWidth(0);
			gelesenColumn.setPreferredWidth(0);
//			// JTable wird nach Datum sortiert
			this.currentTable .setAutoCreateRowSorter(true);
			this.currentTable .getRowSorter().toggleSortOrder(3);
			this.currentTable .setDefaultRenderer(Object.class, new BoldRenderer());
			this.currentTable .getRowSorter().toggleSortOrder(3);
			this.mailClient.setMailSelection();
     	}else{
     		//Es wurde nur der Mail Account, aber keine Ordner selektiert..
     		this.mailControl = mailControlContainer.get(currentPath);
     	}
   }  
    public Mail getSelectedMail() {
    	final int tRow = this.currentTable.getSelectedRow();
    	final int modelRow =  currentTable.convertRowIndexToModel(tRow);
    	return this.currentMailsInTable.getListEntry(modelRow);
    }
    public MailControl getSelectedMailControl() {
    	return mailControl;
    }
    public JTable getCurrentTable() {
	   return currentTable;
    }
    public List<Mail> getCurrentContainingMails() {
    	
    	return this.currentContainingMails;
    }
}

