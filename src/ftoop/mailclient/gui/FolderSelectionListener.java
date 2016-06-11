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
	private HashMap<String,MailControl> mailControlContainer;
	
	private JScrollPane scrollPane;
	private JSplitPane splitPane;
	private JPanel panelCenter;
	private static Mail mail;
	private static MailControl mailControl;
	private static JTable currentTable;

	
	
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
        //Button neue mail wird aktiviert
        MainView.setButtonNeueOn();
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
		        	 MainView.setButtonLoeschenAntWeiterOn();
		             if  (e.getClickCount () == 1) {
		            	 clickRefresh(scrollPane,panelCenter,splitPane,true,e,table,containingMails);
		            	 mail.setNotRead(false);
		             }else if(e.getClickCount () == 2) {
		            	 clickRefresh(scrollPane,panelCenter,splitPane,false,e,table,containingMails);
		            	 mail.setNotRead(false);
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
 //  	    Point origin = e.getPoint () ;
 //       int row = table.rowAtPoint( origin ) ; 
        Mail mailLokal = containingMails.get(table.convertRowIndexToModel(table.getSelectedRow()));//containingMails.get(row);
        mail = mailLokal;
        System.out.println("MAIL:  "+mail.getFrom()+"Menge:  "+mailLokal.getAttachments().size());
        scrollPane = new JScrollPane(table);
        panelCenter = new JPanel(new BorderLayout(5,5));
        if(oneClick) {
          panelCenter.add(scrollPane,BorderLayout.NORTH);
        }
        panelCenter.add(new MailWindows(mailLokal,"lesen").getPanelUnten(),BorderLayout.CENTER);
        // sog. Resizing mit splitpane und Divider
        int pos = splitPane.getDividerLocation();
        splitPane.setRightComponent(panelCenter);
		splitPane.setDividerLocation(pos);
       
        splitPane.revalidate();  
        splitPane.repaint();
//		}
    }  
    public static Mail getSelectedMail() {
    	return mail;
    }
    public static MailControl getSelectedMailControl() {
    	return mailControl;
    }
    public static JTable getCurrentTable() {
	   return currentTable;
    }
   /* static class DateRenderer extends DefaultTableCellRenderer {
        *//**
		 * 
		 *//*
		private static final long serialVersionUID = 5941895543470503982L;
		DateFormat formatter;
        public DateRenderer() { super(); }

        public void setValue(Object value) {
            if (formatter==null) {
                formatter = DateFormat.getDateInstance();
            }
            setText((value == null) ? "" : formatter.format(value));
        }
    }*/
}

