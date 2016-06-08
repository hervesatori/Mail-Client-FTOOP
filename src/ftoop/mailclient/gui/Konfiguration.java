package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	private JButton konfigLoeschen;
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
	//Buttons Mainview
	private JButton send;
	private JButton neue;
	private JButton antworten;
	private JButton weiterleiten;
	private JButton loeschen;
	private JButton ordnerSync;
	private boolean neuAktiv = false;
	
	public Konfiguration(JButton send,JButton neue,JButton antworten,
			JButton weiterleiten,JButton loeschen,JButton ordnerSync)  {
		 
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
		 konfigLoeschen = new JButton("Löschen");
		 konfigLoeschen.setEnabled(false);
		 speichern = new JButton("Speichern");
		 speichern.setEnabled(false);
		
		 buttonPane.add(neu);
		 buttonPane.add(konfigLoeschen);
		 buttonPane.add(speichern);
		 //Button von MainView
		 this.send = send;
		 this.neue = neue;
		 this.antworten = antworten;
		 this.weiterleiten = weiterleiten;
		 this.loeschen = loeschen;
		 this.ordnerSync = ordnerSync;
		
	}	 
		 
	@SuppressWarnings("static-access")
	public void initKonfig() {	 
			//**********Laden der Konti
			EmailKontoControl kontoControl = new EmailKontoControl();
			kontoControl.loadKonten("kontos.xml");
		    //*******************************************
		    //Model ListModel wird erstellt
			 for(EmailKonto konto : kontoControl.getKontos()) {
	  		    	listModel.addElement(konto);
	  		    }
			 form.disableField();
		    //Listener für JList
			 if(kontoControl.getKontos().size() == 0) { 	 
				 speichern.setEnabled(false);
				 konfigLoeschen.setEnabled(false);
				 JOptionPane warn = new JOptionPane();
				 warn.showMessageDialog(null, "Bitte Button Neu drücken um ein neues Konto hinzufügen",
						 "Achtung", JOptionPane.WARNING_MESSAGE);
			 }
			
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
		              form.enableField();
		              konfigLoeschen.setEnabled(true);
		              speichern.setEnabled(true);
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
		    	  form.enableField();
		    	  speichern.setEnabled(true);
		    	  neuAktiv = true;
		        }
		    });
		    speichern.addActionListener(new ActionListener() {
				@SuppressWarnings("static-access")
				public void actionPerformed(ActionEvent e) {
					int pop, smtp, imap;;
				 try {
					    pop = Integer.valueOf(form.getText(4));
						smtp = Integer.valueOf(form.getText(6));
						imap = Integer.valueOf(form.getText(8));
				
				   if(neuAktiv) {	
					  EmailKonto konto = new EmailKonto(form.getText(0),form.getText(2),form.getText(1),form.getText(5),Integer.valueOf(form.getText(4))
		      			  ,form.getText(2),form.getText(3),form.getText(7),Integer.valueOf(form.getText(6)),form.getText(0),form.getText(0),
		      			  form.getText(9),Integer.valueOf(form.getText(8)));
					  kontoControl.addKonto(konto);
					  listModel.addElement(konto);
				   }else {
					  konto = list.getSelectedValue();
					  konto.setKonto(form.getText(0));
					  konto.setEmail(form.getText(1));
					  konto.setBenutzerNamePop(form.getText(2));
					  konto.setPasswortPop(form.getText(3));
					  konto.setPop3Port(pop);
					  konto.setPop3Server(form.getText(5));
					  konto.setSmtpPort(smtp);
					  konto.setSmtpServer(form.getText(7));
					  konto.setImapPort(imap);
					  konto.setImapServer(form.getText(9));		 
				   }
		        /*  //Test ob der Konto schon vorhanden ist
		          for(int i = 0; i<kontoControl.getKontos().size(); i++) {
		            if(kontoControl.getKontos().get(i).getEmail().equals(konto.getEmail())) { 	  
		        	  JOptionPane err = new JOptionPane();
		   		      err.showMessageDialog(null, "Konto existiert schon", "Fehler", JOptionPane.ERROR_MESSAGE);
		   		      return;
		            }
		          }*/
		         
		          kontoControl.saveKonten("kontos.xml");
		          System.out.println(listModel.getSize()); 
		          neuAktiv = false;
		          
		          if(kontoControl.getKontos().size() > 0) { 	  
		        	 
		        	  enableButton();
		          }
			   } catch (NumberFormatException nfe){
					 JOptionPane err = new JOptionPane();
		   		      err.showMessageDialog(null, "Port.Nr sind falsch", "Fehler", JOptionPane.ERROR_MESSAGE);
		   		      return;
			   } 
		          
				}
		      });
		    konfigLoeschen.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		          int index = list.getSelectedIndex();
		          kontoControl.removeKonto(index);
		          kontoControl.saveKonten("kontos.xml");
		          for(int i =0; i < labels.length; i++) {
		        	  form.setText(i, "");
		          }  
		          System.out.println(index);
		      //    System.out.println(listModel.getElementAt(index));
		          status = false;
		          if(index >= 0 && index < listModel.size()) { 
		          	listModel.removeElement(listModel.getElementAt(index));
		          }else {
		        	System.out.println("ListModel, Konto.Id "+index+" existiert nicht");
		          }
		          status = true;
		          if(kontoControl.getKontos().size() == 0) { 	  
		        	  speichern.setEnabled(false);
		        	  disableButton();
		        	  form.disableField();
		          }
		          
		        }
		      });	    
		    
	 } 
	public void disableButton() {
		konfigLoeschen.setEnabled(false);
		send.setEnabled(false);
		neue.setEnabled(false);
		antworten.setEnabled(false);
		weiterleiten.setEnabled(false);
		loeschen.setEnabled(false);
		ordnerSync.setEnabled(false);
	}
	public void enableButton() {
		konfigLoeschen.setEnabled(true);
		send.setEnabled(true);
		neue.setEnabled(true);
		antworten.setEnabled(true);
		weiterleiten.setEnabled(true);
		loeschen.setEnabled(true);
		ordnerSync.setEnabled(true);
	}
	public JPanel getPanelUnten() {
		initKonfig();
		this.add(scrollPane, BorderLayout.WEST);
		this.add(form,BorderLayout.CENTER);
		 this.add(buttonPane, BorderLayout.SOUTH);
		return this;
	}
	public DefaultListModel<EmailKonto> getListModel()  {
		return listModel;
	}
}