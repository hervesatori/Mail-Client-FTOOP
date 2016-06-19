package ftoop.mailclient.daten;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
/**
 * Steuerung von Emailkontos
 * @author Dominique Borer & Herve Satori
 *
 */
public class EmailKontoControl {
/**
 * @param Kontos
 */
	private ArrayList<EmailKonto> Kontos;
  /**
   * 
   */
  public EmailKontoControl(){
	  //Initialisieren der Variablen
	  this.Kontos = new ArrayList<EmailKonto>();
  }
  /**
   * 
   * @param pathToXML  
   * XML File
   */
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
					el.getChildText("Passwort"),
					el.getChildText("ImapServer"),
					Integer.parseInt(el.getChildText("ImapPort")));

		}
	
	  } catch (IOException io) {
		System.out.println(io.getMessage());
	  } catch (JDOMException jdomex) {
		System.out.println(jdomex.getMessage());
	  }
	}
/**
 * 
 * @param path
 */
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
		  Element xmlImapServer = new Element("ImapServer");
		  xmlImapServer.setText(konto.getImapServer());
		  Element xmlImapPort = new Element("ImapPort");
		  xmlImapPort.setText(Integer.toString(konto.getImapPort()));
		  
		  //Hinzufügen der Children zum Konto
		  xmlKonto.addContent(xmlKontoName);
		  xmlKonto.addContent(xmlBenutzerName);
		  xmlKonto.addContent(xmlEmail);
		  xmlKonto.addContent(xmlPasswort);
		  xmlKonto.addContent(xmlPopServer);
		  xmlKonto.addContent(xmlPopPort);
		  xmlKonto.addContent(xmlSmtpServer);
		  xmlKonto.addContent(xmlSmtpPort);
		  xmlKonto.addContent(xmlImapServer);
		  xmlKonto.addContent(xmlImapPort);
		  
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
/**
 * Getter
 * @return Variable Kontos
 */
  public ArrayList<EmailKonto> getKontos() {
	return Kontos;
  }
/**
 * Setter
 * @param kontos Variable Kontos
 */
  public void setKontos(ArrayList<EmailKonto> kontos) {
	Kontos = kontos;
  }

	public void newKonto(String konto,String name,String email,String pop3Server,int pop3Port,
			  String benutzerNamePop,String passwortPop,String smtpServer,int smtpPort,
			  String benutzerNameSmtp,
			  String passwortSmtp,String imapServer,
			  int imapPort) {
		EmailKonto newKonto = new EmailKonto(konto,name, email, pop3Server, pop3Port,
				   benutzerNamePop, passwortPop, smtpServer, smtpPort,
				   benutzerNameSmtp,
				   passwortSmtp, imapServer, imapPort);
		
		this.getKontos().add(newKonto);
	}

	public synchronized void removeKonto(String emailName){
		Iterator<EmailKonto> it = this.getKontos().iterator();
		while(it.hasNext()){
			EmailKonto konto = it.next();
			if(konto.getEmail().equals(emailName)){
				it.remove();
			}
			
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
		  returnStr += "IMAP Port: "+konto.getImapPort() + "\n";
		  returnStr += "IMAP Server: "+konto.getImapServer() + "\n";
	  }
	  return returnStr;
  }
	public void addKonto(EmailKonto konto) {
		this.getKontos().add(konto);
		
	}
	/**
	 * 
	 * @param emailName EmailName zum durchsuchen
	 * @return returns null, falls kein Konto gefunden wurde
	 */
	public EmailKonto getKontoByEmail(String emailName) {
		if(this.getKontos().size()>0){
			Iterator<EmailKonto> it = this.getKontos().iterator();
			while(it.hasNext()){
				EmailKonto konto = it.next();
				if(konto.getEmail().equals(emailName)){
					return konto;
				}
			}
		}
		
		return null;
	}
}