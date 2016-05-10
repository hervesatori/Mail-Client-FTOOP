package ftoop.mailclient.daten;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class EmailKontoControl {

	private ArrayList<EmailKonto> Kontos;
  
  public EmailKontoControl(){
	  //Initialisieren der Variablen
	  this.Kontos = new ArrayList<EmailKonto>();
  }
  public void loadKonten(String pathToXML) {
	  SAXBuilder builder = new SAXBuilder();
	  File xmlFile = new File(pathToXML);

	  try {

		Document document = (Document) builder.build(xmlFile);
		Element rootNode = document.getRootElement();
		List<Element> list = rootNode.getChildren("Konto");

		for (Element el:list) {

		  //Laden der einzelnen Kontoattributen und hinzufügen zu einem neuen Konto
			this.newKonto(el.getChildText("Kontoname"), el.getChildText("Benutzername"), el.getChildText("Email"),
					el.getChildText("PopServer"),
					Integer.parseInt(el.getChildText("PopPort")),
					el.getChildText("Benutzername"),
					el.getChildText("Passwort"),
					el.getChildText("SmtpServer"),
					Integer.parseInt(el.getChildText("SmtpPort")),
					el.getChildText("Benutzername"),
					el.getChildText("Passwort"));

		}
	
	  } catch (IOException io) {
		System.out.println(io.getMessage());
	  } catch (JDOMException jdomex) {
		System.out.println(jdomex.getMessage());
	  }
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
		  Element xmlKontoName = new Element("Kontoname");
		  xmlKontoName.setText(konto.getKonto());
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
		  xmlKonto.addContent(xmlKontoName);
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

	public void newKonto(String konto,String name,String email,String pop3Server,int pop3Port,
			  String benutzerNamePop,String passwortPop,String smtpServer,int smtpPort,
			  String benutzerNameSmtp,
			  String passwortSmtp) {
		EmailKonto newKonto = new EmailKonto(konto,name, email, pop3Server, pop3Port,
				   benutzerNamePop, passwortPop, smtpServer, smtpPort,
				   benutzerNameSmtp,
				   passwortSmtp);
		
		this.getKontos().add(newKonto);
	}

  public void removeKonto(int id) {
	  
	  if(id>=0 && id < this.getKontos().size()){
		  this.getKontos().remove(id);
	  }else{
		  System.out.println("Konto ID "+ id + " existiert nicht.");
	  }
  }
  @Override
  public String toString(){
	  String returnStr = "";
	  for(EmailKonto konto:this.getKontos()){
		  //Pro Konto alle einzelnen Daten ausgeben
		  returnStr += "Benutzername: "+konto.getBenutzerNameSmtp() + "\n";
		  returnStr += "Emailadresse: "+konto.getEmail() + "\n";
		  returnStr += "Kontoname: "+konto.getName() + "\n";
		  returnStr += "Passwort: "+konto.getPasswortPop() + "\n";
		  returnStr += "Pop3 Port: "+konto.getPop3Port() + "\n";
		  returnStr += "Pop3 Server: "+konto.getPop3Server() + "\n";
		  returnStr += "SMTP Port: "+konto.getSmtpPort() + "\n";
		  returnStr += "SMTP Server: "+konto.getSmtpServer() + "\n";
	  }
	  return returnStr;
  }
}