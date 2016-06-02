package ftoop.mailclient.gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ftoop.mailclient.daten.Mail;

public class MailWindows extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Mail mail;
	protected  JPanel labelPane;
	protected  JEditorPane msgPane;
	protected JScrollPane scrollEditor;
	// Labeltitel für Mails werden definiert
	protected static final String von = "Von";
	protected static final String an = "An";
	protected static final String betreff = "Betreff";
	protected static final String datum = "Datum";
	protected  JLabel labelVon ;
	protected  JLabel labelAn;
	protected JLabel labelCc;
	protected  JLabel labelBetreff;
	protected  JLabel labelDate;
	protected JTextField txtAn;
	protected JTextField txtDate;

	protected JTextField txtBetreff;
	private String aktion;
	
	
	
	public MailWindows(Mail mail,String aktion) {
		  super(new BorderLayout(5,5));
		  this.mail = mail;
		  this.aktion = aktion;
		  
		  txtBetreff = new JTextField();
		  txtAn = new JTextField();
		  txtDate = new JTextField();
		  labelPane = new JPanel();
		  labelPane.setLayout(new BoxLayout(labelPane,BoxLayout.Y_AXIS));
		  labelPane.setBorder(
	              BorderFactory.createCompoundBorder(
	                              BorderFactory.createTitledBorder("E-Mail Header"),
	                              BorderFactory.createEmptyBorder(5,5,5,5)));
		  labelPane.setBackground(new Color(255,255,204));
		  msgPane= new JEditorPane();
		  msgPane.setContentType("text/html");
	}
	public void createEditorLabel() {
	  switch (aktion) {
	   case "lesen":
		  msgPane.setEditable(false);
		  msgPane.setText(mail.getMessage());
		  labelVon = new JLabel(von+": "+mail.getFrom());
		  labelAn = new JLabel (an+": "+mail.getTo());
		  labelBetreff = new JLabel(betreff+": "+mail.getSubject());
		  labelDate = new JLabel(datum+": "+mail.getReceived());
		  labelPane.add(labelVon);
		  labelPane.add(new JLabel(" "));
		  labelPane.add(labelAn);
		  labelPane.add(new JLabel(" "));
		  labelPane.add(labelBetreff);
		  labelPane.add(new JLabel(" "));
		  labelPane.add(labelDate);
		  break;
	   case "antworten":
		  msgPane.setEditable(true);
		  msgPane.setText("\n"+mail.getMessage());
		  labelAn = new JLabel (an+": "+mail.getFrom());
		  labelBetreff = new JLabel(betreff+": "+mail.getSubject());
		  labelDate = new JLabel(datum+": "+mail.getReceived());
		  labelPane.add(labelAn);
		  labelPane.add(new JLabel(" "));
		  labelPane.add(labelBetreff);
		  labelPane.add(new JLabel(" "));
		  labelPane.add(labelDate);
		  break;
	   case "weiterleiten":
		  msgPane.setEditable(true);
		  msgPane.setText("\n \n \n \n"+mail.getMessage());
		  labelPane.setLayout(new GridLayout(4,2,7,7));
		  msgPane.setEditable(true);
		  labelAn = new JLabel(an);
		  labelBetreff = new JLabel(betreff);
		  labelDate = new JLabel(datum);
		  labelPane.add(labelAn);
		  labelPane.add(txtAn);
	      labelPane.add(new JLabel(" "));
	      labelPane.add(new JLabel(" "));
		  labelPane.add(labelBetreff);
		  txtBetreff.setText(mail.getSubject());
		  txtBetreff.setEnabled(false);
		  labelPane.add(txtBetreff);
		  labelPane.add(labelDate);
		  txtDate.setText(mail.getReceived().toString());
		  txtDate.setEnabled(false);
		  labelPane.add(txtDate);
		  break;
	   case "neueMail":
		  labelPane.setLayout(new GridLayout(3,2,7,7));
		  msgPane.setEditable(true);
		  labelAn = new JLabel(an);
		  labelBetreff = new JLabel(betreff);
		  labelPane.add(labelAn);
		  labelPane.add(txtAn);
	      labelPane.add(new JLabel(" "));
	      labelPane.add(new JLabel(" "));
		  labelPane.add(labelBetreff);
		  labelPane.add(txtBetreff);
		  break;
	}
		
}

	public JPanel getPanelUnten() {
		  
		  scrollEditor = new JScrollPane(msgPane);		  
		  createEditorLabel();
		 
		  this.add(labelPane,BorderLayout.NORTH);
		  this.add(scrollEditor,BorderLayout.CENTER);
		  return this;
	}
	public JTextField getTxtAn () {
		return txtAn;
	}
	public JTextField getTxtBetreff () {
		return txtBetreff;
	}
	public JEditorPane getMsgPane() {
		return msgPane;
	}
		
}
	
	
	