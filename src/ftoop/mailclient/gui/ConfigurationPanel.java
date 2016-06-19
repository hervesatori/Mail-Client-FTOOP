package ftoop.mailclient.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ftoop.mailclient.daten.EmailKonto;
import ftoop.mailclient.daten.EmailKontoControl;
/**
 * JPanel "Konfiguration" wird erstellt und EmailKontos werden hochgeladen
 * 
 * @author Dominique Borer & Herve Satori
 *
 */
public class ConfigurationPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JPanel buttonPane;
	private JButton neu;
	private JButton konfigLoeschen;
	private JButton speichern;
	private ConfigurationFieldsPanel configFieldPanel;
	private DefaultListModel<EmailKonto> listModel;
	private JList<EmailKonto> list;
	private EmailKontoControl kontoControl;
	private boolean neuModus = false;
	private MainView mailClient;

	/**
	 * 
	 * @param mailClient
	 */
	public ConfigurationPanel(MainView mailClient)  {
		this.mailClient = mailClient;
		this.kontoControl = this.mailClient.getKontoControl();
		this.setLayout(new BorderLayout());
		initPanelGUI();
		loadMailKontoConfig();
	}	 
	/**
	 * 
	 */
	public void loadMailKontoConfig() {	 
		    //Model ListModel wird erstellt
			 for(EmailKonto konto : this.kontoControl.getKontos()) {
	  		    	listModel.addElement(konto);
	  		    }
			 this.configFieldPanel.setEnabled(false);
		    //Listener für JList
			 if(kontoControl.getKontos().size() == 0) { 	 
				 speichern.setEnabled(false);
				 konfigLoeschen.setEnabled(false);
         		//Ebenso die Eingabefelder deaktivieren
         		ConfigurationPanel.this.configFieldPanel.setFieldsEnabled(false);
				 
				 JOptionPane.showMessageDialog(this, "Bitte Button Neu drücken um ein neues Konto hinzufügen",
						 "Achtung", JOptionPane.WARNING_MESSAGE);
			 }
			
		    list.addListSelectionListener(new ListSelectionListener() {
		        @SuppressWarnings("rawtypes")
				@Override
				/**
				 * Falls ein anderes Konto selektiert wird, werden entsprechend die Kontofelder befüllt und Speichern/Löschen Buttons de-/aktiviert.
				 */
		        public void valueChanged(final ListSelectionEvent e) {
		        	
		            if (!e.getValueIsAdjusting()&&kontoControl.getKontos().size()>0) {
		              //Selected Konto wird bearbeitet	
		              if(list.getSelectedIndex() != -1){
		            	EmailKonto listKonto = list.getSelectedValue();
		              EmailKonto konto = kontoControl.getKontoByEmail(listKonto.getEmail());
		              int index = list.getSelectedIndex(); 
		              ConfigurationPanel.this.configFieldPanel.setFieldTexts(konto);
		              System.out.println(konto.getBenutzerNamePop()+index);		              
		              speichern.setEnabled(true);
		              }
		             
		            } 
		            //Falls in der Liste kein Item selektiert ist.
		            Object oList = e.getSource();
		            if(oList instanceof JList){
		            	if(((JList)oList).isSelectionEmpty() == true){
			            	//Button Löschen und Speichern deaktivieren
		            		ConfigurationPanel.this.speichern.setEnabled(false);
		            		ConfigurationPanel.this.konfigLoeschen.setEnabled(false);
		            		//Ebenso die Eingabefelder deaktivieren
		            		ConfigurationPanel.this.configFieldPanel.setFieldsEnabled(false);
			            }else{
			            	//Button Löschen und Speichern deaktivieren
		            		ConfigurationPanel.this.speichern.setEnabled(true);
		            		ConfigurationPanel.this.konfigLoeschen.setEnabled(true);
		            		//Ebenso die Eingabefelder deaktivieren
		            		ConfigurationPanel.this.configFieldPanel.setFieldsEnabled(true);
			            }
		            }
		            checkKonfigLoeschenButton();
		        }
		    });
		    
		    //**********************************************
		    //Buttons Listener NEU  LOESCHEN SPEICHERN
		    neu.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		          ConfigurationPanel.this.configFieldPanel.setFieldsEnabled(true);        	
		    	  speichern.setEnabled(true);
		    	  neuModus = true;
		    	  konfigLoeschen.setEnabled(false);
		    	  ConfigurationPanel.this.neu.setEnabled(false);
	        	  ConfigurationPanel.this.configFieldPanel.clearFields();
		        }
		    });
		    speichern.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				 try {					 
					 
				   if(neuModus) {	
					   EmailKonto newKontoToAdd = ConfigurationPanel.this.configFieldPanel.getFieldTexts();
			            if(kontoControl.getKontoByEmail(newKontoToAdd.getEmail())!=null) { 
			        	  JOptionPane.showMessageDialog(null, "Konto existiert schon", "Fehler", JOptionPane.ERROR_MESSAGE);
			   		      return;
			            }		 
					  kontoControl.addKonto(newKontoToAdd);
					  listModel.addElement(newKontoToAdd);
					  //Neues Konto hinzugefügt, Modus neu auf false setzen
					  neuModus = false;
				   }else {
					   
					  EmailKonto kontoInList = list.getSelectedValue();
					  EmailKonto existingKonto = kontoControl.getKontoByEmail(kontoInList.getEmail());
					  kontoControl.removeKonto(existingKonto.getEmail());
					  
					  //Update Konto in der Liste mit neuen Werten					  
					  kontoInList = ConfigurationPanel.this.configFieldPanel.getFieldTexts();					  
					  //Füge das neue Konto wieder zum KontoControl hinzu
					  kontoControl.addKonto(kontoInList);
					  //Liste neu laden
					  reloadKontoToListModel();
				   }

		         
		          kontoControl.saveKonten(mailClient.getXmlPath());
		          //Falls jetzt Anzahl KOnti == 0, dann deaktiviere den Button zur Synchronisation, falls >0 aktivieren
		          mailClient.checkOrdnerSynchro();
		          System.out.println(listModel.getSize()); 
		          ConfigurationPanel.this.speichern.setEnabled(false);
		          ConfigurationPanel.this.neu.setEnabled(true);
		          clearFieldSelectionAndDisableFields();
			   } catch (NumberFormatException nfe){
					 JOptionPane.showMessageDialog(null, "Als Port Nummern sind nur Zahlen erlaubt!", "Fehler", JOptionPane.ERROR_MESSAGE);
		   		      return;
			   } 
		          
				}
		      });
		    konfigLoeschen.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		          int index = list.getSelectedIndex();
		          EmailKonto kontoToRemove = list.getModel().getElementAt(index);
		          if(index >= 0 && index < listModel.size()) { 
		        	  kontoControl.removeKonto(kontoToRemove.getEmail());
			          kontoControl.saveKonten(mailClient.getXmlPath());
		          	  listModel.removeElement(listModel.getElementAt(index));
		        	  ConfigurationPanel.this.mailClient.removeKontoFromMailTree(kontoToRemove);
		              clearFieldSelectionAndDisableFields();
		          }else {
		        	  System.out.println("ListModel, Konto.Id "+index+" existiert nicht");
		          }
		       
		          
		          //button deaktiviert + textfelder
		          if(kontoControl.getKontos().size() == 0) { 	  
		        	  speichern.setEnabled(false);
		        	  checkKonfigLoeschenButton();
		        	  ConfigurationPanel.this.configFieldPanel.setEnabled(false);
		        	  ConfigurationPanel.this.configFieldPanel.clearFields();
		          }
		          
		        }
		      });	
	 } 
	/**
	 * 
	 */
	private void reloadKontoToListModel(){
		listModel.clear();
		for(EmailKonto konto : this.kontoControl.getKontos()) {
		    	listModel.addElement(konto);
		    }
	}
	/**
	 * 
	 */
	private void clearFieldSelectionAndDisableFields(){
      list.clearSelection();
  	  ConfigurationPanel.this.configFieldPanel.setFieldsEnabled(false);
  	  ConfigurationPanel.this.configFieldPanel.clearFields();
	}
	/**
	 * 
	 */
	private void checkKonfigLoeschenButton(){
        if(this.list.getModel().getSize()> 0 && !this.list.isSelectionEmpty()) {       	 
      	  	ConfigurationPanel.this.konfigLoeschen.setEnabled(true);
        }else{
        	 ConfigurationPanel.this.konfigLoeschen.setEnabled(false);
        }
	}
	/**
	 * 
	 */
	private void initPanelGUI(){	
		 JSplitPane splitPane = new JSplitPane(); 	
		 listModel = new DefaultListModel<EmailKonto>();
		 list = new JList<EmailKonto>(listModel);
		 list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 scrollPane = new JScrollPane(list);
		
		 // Formular wird instanziert
		
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
		
		 splitPane.setLeftComponent(scrollPane);
		 this.configFieldPanel = new ConfigurationFieldsPanel();
		 splitPane.setRightComponent(configFieldPanel);
		 this.add(splitPane,BorderLayout.CENTER);
		 this.add(buttonPane, BorderLayout.NORTH);		
		 int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		 splitPane.setDividerLocation((int)(width*0.3));
	}
	/**
	 * 
	 * @return
	 */
	public DefaultListModel<EmailKonto> getListModel()  {
		return listModel;
	}
}