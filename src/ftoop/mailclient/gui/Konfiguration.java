package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ftoop.mailclient.daten.EmailKonto;
import ftoop.mailclient.daten.EmailKontoControl;

public class Konfiguration extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JPanel buttonPane;
	private TextForm form;
	private JButton neu;
	private JButton loeschen;
	private JButton speichern;
	// Init der Labels
    private final static String[] labels = { "Kontoname","Emailadresse","Benutzername", "Passwort","Pop3 Port" ,
    		"Pop3 Server","SMTP Port","SMTP Server","IMAP Port","IMAP Server"};
  
    private final int[] widths = { 70,70,70,70,70,70,70,70,70,70 };
    private final static String[] descs = { "Kontoname","Emailadresse","Benutzername", "Passwort","Pop3 Port" ,
    		"Pop3 Server","SMTP Port","SMTP Server","IMAP Port","IMAP Server"};
	private DefaultListModel<EmailKonto> listModel;
	private JList<EmailKonto> list;
	private EmailKonto konto;
	private Boolean status = true;
	
	public Konfiguration()  {
		 
		 super(new BorderLayout());
		 listModel = new DefaultListModel<EmailKonto>();
		 list = new JList<EmailKonto>(listModel);
		 list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 scrollPane = new JScrollPane(list);
		
		 // Formulär wird instanziert
		 form = new TextForm(labels, widths, descs);
		
		// Buttons
		 buttonPane = new JPanel();
		 neu = new JButton("Neu");
		 loeschen = new JButton("Löschen");
		 speichern = new JButton("Speichern");
		 speichern.setEnabled(false);
		 buttonPane.add(neu);
		 buttonPane.add(loeschen);
		 buttonPane.add(speichern);
		
	}	 
		 
	public void initKonfig() {	 
			//**********Laden der Konti
			EmailKontoControl kontoControl = new EmailKontoControl();
			kontoControl.loadKonten("kontos.xml");
		    //*******************************************
		    //Model ListModel wird erstellt
			 for(EmailKonto konto : kontoControl.getKontos()) {
	  		    	listModel.addElement(konto);
	  		    }
		    //Listener für JList
		    list.addListSelectionListener(new ListSelectionListener() {
		        @Override
		        public void valueChanged(final ListSelectionEvent e) {
		        	
		            if (!e.getValueIsAdjusting() && status == true) {
		              //Selected Konto wird bearbeitet
		            	System.out.println(kontoControl.getKontos().get(list.getSelectedIndex()));
		              konto = kontoControl.getKontos().get(list.getSelectedIndex());
		             int index = list.getSelectedIndex(); 
		              System.out.println(konto.getBenutzerNamePop()+index);
		              String[] fieldsGet = { konto.getKonto(), konto.getEmail(), konto.getBenutzerNamePop(), 
		            		  konto.getPasswortPop(),String.valueOf(konto.getPop3Port()),
		                		konto.getPop3Server(),String.valueOf(konto.getSmtpPort()),
		                		konto.getSmtpServer(),String.valueOf(konto.getImapPort()),konto.getImapServer()};
		                
		              for(int i =0;i<fieldsGet.length;i++) {
		            	  form.setText(i, fieldsGet[i]);
		              } 
		            } 
		        }
		    });
		    
		    //**********************************************
		    //Buttons Listener NEU  LOESCHEN SPEICHERN
		    neu.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		    	  for(int i =0; i < labels.length; i++) {
		        	  form.setText(i, "");
		          }  
		    	  speichern.setEnabled(true);
		        }
		    });
		    speichern.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
		          EmailKonto  konto = new EmailKonto(form.getText(0),form.getText(2),form.getText(1),form.getText(5),Integer.valueOf(form.getText(4))
		      			  ,form.getText(2),form.getText(3),form.getText(7),Integer.valueOf(form.getText(6)),form.getText(0),form.getText(0),
		      			  form.getText(9),Integer.valueOf(form.getText(8)));
		          kontoControl.addKonto(konto);
		          kontoControl.saveKonten("kontos.xml");
		          System.out.println(listModel.getSize()); 
		         
		          listModel.addElement(konto);
		          speichern.setEnabled(false);
				}
		      });
		    loeschen.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		          int index = list.getSelectedIndex();
		          kontoControl.removeKonto(index);
		          kontoControl.saveKonten("kontos.xml");
		          System.out.println(index);
		      //    System.out.println(listModel.getElementAt(index));
		          status = false;
		          if(index >= 0 && index < listModel.size()) { 
		          	listModel.removeElement(listModel.getElementAt(index));
		          }else {
		        	System.out.println("ListModel, Konto.Id "+index+" existiert nicht");
		          }
		          status = true;
		        }
		      });	    
		    
	 } 
	public JPanel getPanelUnten() {
		initKonfig();
		this.add(scrollPane, BorderLayout.WEST);
		this.add(form,BorderLayout.CENTER);
		 this.add(buttonPane, BorderLayout.SOUTH);
		return this;
	}
}