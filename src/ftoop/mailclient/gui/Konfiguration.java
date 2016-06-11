package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

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
    private final static String[] type ={ "kein","kein","kein", "pass","kein" ,
		"kein","kein","kein","kein","kein"};
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
		
		 // Formul�r wird instanziert
		 form = new TextForm(labels, widths, descs,type);
		
		// Buttons
		 buttonPane = new JPanel();
		 neu = new JButton("Neu");
		 konfigLoeschen = new JButton("L�schen");
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
		    //Listener f�r JList
			 if(kontoControl.getKontos().size() == 0) { 	 
				 speichern.setEnabled(false);
				 konfigLoeschen.setEnabled(false);
				 JOptionPane warn = new JOptionPane();
				 warn.showMessageDialog(null, "Bitte Button Neu dr�cken um ein neues Konto hinzuf�gen",
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
		        	form.enableField();
		    	  for(int i =0; i < labels.length; i++) {
		        	  form.setText(i, "");
		        	 
		          }  
		    	  
		    	  speichern.setEnabled(true);
		    	  neuAktiv = true;  	  
		        }
		    });
		    speichern.addActionListener(new ActionListener() {
				@SuppressWarnings("static-access")
				public void actionPerformed(ActionEvent e) {
					int pop, smtp, imap;;
				 try {
					 
					    pop =  Integer.valueOf(form.getText(4));
						smtp = Integer.valueOf(form.getText(6));
						imap = Integer.valueOf(form.getText(8));
				
				   if(neuAktiv) {	
					  EmailKonto konto = new EmailKonto((String) form.getText(0),(
							  String) form.getText(2),(String) form.getText(1),(String) form.getText(5),pop
		      			  ,(String) form.getText(2),(String) form.getText(3),(String) form.getText(7),
		      			  smtp,(String) form.getText(0),(String) form.getText(0),(String) form.getText(9),imap);
					  kontoControl.addKonto(konto);
					  listModel.addElement(konto);
				   }else {
					  konto = list.getSelectedValue();
					  konto.setKonto((String) form.getText(0));
					  konto.setEmail((String) form.getText(1));
					  konto.setBenutzerNamePop((String) form.getText(2));
					  konto.setPasswortPop((String) form.getText(3));
					  konto.setPop3Port(pop);
					  konto.setPop3Server((String) form.getText(5));
					  konto.setSmtpPort(smtp);
					  konto.setSmtpServer((String) form.getText(7));
					  konto.setImapPort(imap);
					  konto.setImapServer((String) form.getText(9));		 
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
		          //Ordner synchronisieren
		          MainView.init(false);
		          
			   } catch (NumberFormatException nfe){
					 JOptionPane err = new JOptionPane();
		   		      err.showMessageDialog(null, "Als Port Nummer sind nur Zahlen erlaubt!", "Fehler", JOptionPane.ERROR_MESSAGE);
		   		      return;
			   } 
		          
				}
		      });
		    konfigLoeschen.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        	   // Rezising von splitpane / panelcenter
			          ButtonListener.resizing();
		          int index = list.getSelectedIndex();
		          System.out.println(index);
		      //    System.out.println(listModel.getElementAt(index));
		          status = false;
		          if(index >= 0 && index < listModel.size()) { 
		         // Jtree (Folder) wird gel�scht
		          	  Object root =  	MainView.getCurrentTree().getModel().getRoot();
			    	  DefaultTreeModel treeModel = (DefaultTreeModel) MainView.getCurrentTree().getModel();
			    	  for(int i = 0; i < treeModel.getChildCount(root);i++) {
			    		  if(treeModel.getChild(root, i).toString().equals(kontoControl.getKontos().get(index).getEmail())) {
			    			  treeModel.removeNodeFromParent((MutableTreeNode) treeModel.getChild(root, i));
			    		  }
			    	  }   	  
		        	  kontoControl.removeKonto(index);
			          kontoControl.saveKonten("kontos.xml");
			      
			          for(int i =0; i < labels.length; i++) {
			        	  form.setText(i, "");
			          }  
			       // ModelList wird gel�scht
		          	  listModel.removeElement(listModel.getElementAt(index));
		          	  ButtonListener.resizing();
		         
		          }else {
		        	  System.out.println("ListModel, Konto.Id "+index+" existiert nicht");
		          }
		          status = true;
		       
		          
		          //button deaktiviert + textfelder
		          if(kontoControl.getKontos().size() == 0) { 	  
		        	  speichern.setEnabled(false);
		        	  disableButton();
		        	  form.disableField();
		          }
		          
		        }
		      });	 
		    
		// Port d�rfen NUR int sein !!
		    KeyListener keyListener= new KeyAdapter() {
		        public void keyTyped(KeyEvent e) {
		          char c = e.getKeyChar();
		          if (!((c >= '0') && (c <= '9') ||
		             (c == KeyEvent.VK_BACK_SPACE) ||
		             (c == KeyEvent.VK_DELETE))) {
		            getToolkit().beep();
		            e.consume();
		          }
		        }
		      };
		      form.getField(4).addKeyListener(keyListener);
		      form.getField(6).addKeyListener(keyListener);
		      form.getField(8).addKeyListener(keyListener);
		      
		     
		    
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