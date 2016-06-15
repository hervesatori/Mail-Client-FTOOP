package ftoop.mailclient.gui;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ftoop.mailclient.daten.EmailKonto;
import ftoop.mailclient.daten.Mail;
import ftoop.mailclient.daten.MailControl;

public class MailPane extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static enum MailWindowType{READ, NEW, RESPOND, FORWARD};
	private Mail mail;
	private  JPanel headerPane;
	private  JEditorPane msgPane;
	private JScrollPane scrollEditor;
	// Labeltitel für Mails werden definiert
	private final String von = "Von:";
	private final String an = "An:";
	private final String betreff = "Betreff:";
	private final String datum = "Datum:";
	private  JLabel labelVon ;
	private  JLabel labelAn;
	private JLabel labelCc;
	private  JLabel labelBetreff;
	private  JLabel labelDate;
	private JTextField txtAn;
	private JTextField txtVon;
	/**
	 * @return the currentMC
	 */
	public MailControl getCurrentMC() {
		return currentMC;
	}
	final private MailControl currentMC;

	private JTextField txtBetreff;
	
	
	
	public MailPane(Mail mail,MailWindowType type, MailControl currentMC) {
		  super(new BorderLayout(5,5));
		  this.mail = mail;
		  this.currentMC = currentMC;
		  
		  this.initPanelGUI();
		  
		  
		  
		  this.createEditorLabel(type);
	}
	private void initPanelGUI(){
		this.txtBetreff = new JTextField();	
		this.txtAn = new JTextField();
		this.txtVon = new JTextField();
		this.headerPane = new JPanel();
		headerPane.setLayout(new BoxLayout(headerPane,BoxLayout.Y_AXIS));
		  headerPane.setBorder(
	              BorderFactory.createCompoundBorder(
	                              BorderFactory.createTitledBorder("E-Mail Header"),
	                              BorderFactory.createEmptyBorder(5,5,5,5)));
		  headerPane.setBackground(new Color(255,255,204));
		  msgPane= new JEditorPane();
		  msgPane.setContentType("text/html");
		  msgPane.setEditable(true);
		  
		  headerPane.setLayout(new GridLayout(4,2,7,7));
		  
		  //Setze die Mailheaderlabels und deren Textfields
		  this.labelAn = new JLabel(an);
		  this.labelVon = new JLabel(von);
		  this.labelBetreff = new JLabel(betreff);
		  this.headerPane.add(labelAn);
		  this.headerPane.add(txtAn);
		  this.headerPane.add(new JLabel(" "));
		  this.headerPane.add(labelVon);
		  this.headerPane.add(txtVon);
		  this.headerPane.add(new JLabel(" "));
		  this.headerPane.add(labelBetreff);
		  this.headerPane.add(txtBetreff);
		  
		  this.add(headerPane,BorderLayout.NORTH);
		  this.add(msgPane,BorderLayout.CENTER);
	}
	private void addDateToPanel(){
		if(mail != null){
		  labelDate = new JLabel(datum+" "+mail.getReceived());
		  headerPane.add(new JLabel(" "));
		  headerPane.add(labelDate);
		}
	}
	private void createEditorLabel(MailWindowType type) {
	  switch (type) {
	   case READ:
		  msgPane.setEditable(false);
		  msgPane.setText(mail.getMessage());
		  this.txtBetreff.setText(mail.getSubject());
		  this.txtBetreff.setEditable(false);
		  this.addDateToPanel();
		  break;
	   case RESPOND:
		   this.loadMsgContentToRespond();
		  this.txtAn.setText(mail.getFrom());
		  this.txtBetreff.setText("Re: " + mail.getSubject());
		  break;
	   case FORWARD:
		   
		   this.loadMsgContentToRespond();

		  this.txtBetreff.setText("Fw: " + mail.getSubject());
		  break;
	   case NEW: 
		  this.txtVon.setText(currentMC.getCurrentKonto().getEmail());
		  this.txtVon.setEnabled(false);
		  break;
	}
		
}
	
	private void loadMsgContentToRespond(){
		  String prevMailText = mail.getMessage();
		  
		  Pattern htmlPattern = Pattern.compile(".*\\<[^>]+>.*", Pattern.DOTALL);
		  boolean isHTML = htmlPattern.matcher(prevMailText).matches();
		  if(this.msgPane.getContentType().equals("text/html") && isHTML){
			  //Einfügen einer neuen Mailsektion 
			  Document doc = Jsoup.parse(prevMailText);
			  String bodyElements = doc.body().getAllElements().toString();
			  doc.body().children().remove();
			  doc.body().append("<div><p><br/><br/><br/><br/></p></div><hr/>");
			  doc.body().append("<div>"+bodyElements+"</div>");
			  
			  this.msgPane.setText(doc.toString());
		  }else{
			  //Ist kein HTML
			  this.msgPane.setText("\n\n\n\n"+prevMailText);
		  }
	}
	public String getMessageText(){
		return this.msgPane.getText();		
	}
	public String getBetreff(){
		return this.txtBetreff.getText();
	}
	public String getAn(){
		return this.txtAn.getText();
	}
		
}
	
	
	