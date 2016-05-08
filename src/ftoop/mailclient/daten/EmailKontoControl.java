package ftoop.mailclient.daten;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class EmailKontoControl {
//	private  org.jdom2.Document doc;
//	private  Element root;
//	private  Element mail;
//	private Element mailDate;
//	private  Element to;
//	private  Element from;
//	private  Element subject;
//	private Element message;
//	private  SAXBuilder sxb;
//	private  SimpleDateFormat sdf;
	private ArrayList<EmailKonto> Kontos;
  
  public EmailKontoControl(){
	  //Initialisieren der Variablen
	  this.Kontos = new ArrayList<EmailKonto>();
  }
  public void loadKonten() {
	  
  }

  public void saveKonten(String path) {
	  //Root Element Erstellen
	  Element root = new Element("Kontos");
	  //neues Dokument 
	  org.jdom2.Document doc = new Document(root);    
	  //Pro Konto eigenen XML Zweig hinzufügen
	  for(EmailKonto konto:this.getKontos()){
		  Element xmlKonto = new Element("Konto");
		  
		  //Erstellen der Konto Children und zuweisen des Inhaltes
		  Element xmlBenutzerName = new Element("Benutzername");
		  xmlBenutzerName.setText(konto.getBenutzerNamePop());
		  Element xmlEmail = new Element("Email");
		  xmlEmail.setText(konto.getEmail());
		  Element xmlPasswort = new Element("Passwort");
		  xmlPasswort.setText(konto.getPasswortPop());
		  Element xmlPopServer = new Element("PopServer");
		  xmlPopServer.setText(konto.getPop3Server());
		  Element xmlPopPort = new Element("PopPort");
		  xmlPopPort.setText(Integer.toString(konto.getPop3Port()));
		  Element xmlSmtpServer = new Element("SmtpServer");
		  xmlSmtpServer.setText(konto.getSmtpServer());
		  Element xmlSmtpPort = new Element("SmtpPort");
		  xmlSmtpPort.setText(Integer.toString(konto.getSmtpPort()));
		  
		  //Hinzufügen der Children zum Konto
		  xmlKonto.addContent(xmlBenutzerName);
		  xmlKonto.addContent(xmlEmail);
		  xmlKonto.addContent(xmlPasswort);
		  xmlKonto.addContent(xmlPopServer);
		  xmlKonto.addContent(xmlPopPort);
		  xmlKonto.addContent(xmlSmtpServer);
		  xmlKonto.addContent(xmlSmtpPort);
		  
		  //Hinzufügen des Kontos zum root Zweig
		  root.addContent(xmlKonto);
	  }
	  
	  try {
	      //Normal Anzeige mit getPrettyFormat()
	      XMLOutputter outputFile = new XMLOutputter(Format.getPrettyFormat());
	     
	      outputFile.output(doc, new FileOutputStream(path));
	   }
	   catch (java.io.IOException e){
		   System.out.println("Konnte die Konten nicht im XML Format speichern.");
		   e.printStackTrace();
	   }
  }

  public ArrayList<EmailKonto> getKontos() {
	return Kontos;
  }

  public void setKontos(ArrayList<EmailKonto> kontos) {
	Kontos = kontos;
  }

	public void newKonto() {
		EmailKonto newKonto = new EmailKonto(null, null, null, null, 0, null, null, null, 0, null, null);
		
		this.getKontos().add(newKonto);
	}

  public void removeKonto() {
  }

}