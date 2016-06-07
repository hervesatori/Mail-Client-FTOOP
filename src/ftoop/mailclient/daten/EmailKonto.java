package ftoop.mailclient.daten;
/**
 * 
 * @author Herve Satori & Dominique Borer
 *
 */
public class EmailKonto {
	
	
	private String konto;
	private String name;
	private String email;
	private String pop3Server;
	private int pop3Port;
	private String benutzerNamePop;
	private String passwortPop;
	private String smtpServer;
	private int smtpPort;
	private String benutzerNameSmtp;
	private String passwortSmtp;
	private String imapServer;
	private int imapPort;
	
  public EmailKonto(String konto,String name,String email,String pop3Server,int pop3Port,
		  String benutzerNamePop,String passwortPop,String smtpServer,int smtpPort,
		  String benutzerNameSmtp,
		  String passwortSmtp,
		  String imapServer,
		  int imapPort) {
    this.konto = konto;
    this.name = name;
    this.email = email;
    this.pop3Server = pop3Server;
    this.pop3Port = pop3Port;
    this.benutzerNamePop = benutzerNamePop;
    this.passwortPop = passwortPop;
    this.smtpServer = smtpServer;
    this.smtpPort = smtpPort;
    this.benutzerNameSmtp = benutzerNameSmtp;
    this.passwortSmtp = passwortSmtp;
    this.imapServer = imapServer;
    this.imapPort = imapPort;    
	  
  }
  
  public String toString() {
	  return this.email;
  }
  
  /**
 * @return the imapPort
 */
public int getImapPort() {
	return imapPort;
}

/**
 * @param imapPort the imapPort to set
 */
public void setImapPort(int imapPort) {
	this.imapPort = imapPort;
}

/**
 * @return the imapServer
 */
public String getImapServer() {
	return imapServer;
}

/**
 * @param imapServer the imapServer to set
 */
public void setImapServer(String imapServer) {
	this.imapServer = imapServer;
}

/**
 * @param smtpPort the smtpPort to set
 */
public void setSmtpPort(int smtpPort) {
	this.smtpPort = smtpPort;
}

/**
	 * @return the konto
	 */
	public String getKonto() {
		return konto;
	}

	/**
	 * @param konto the konto to set
	 */
	public void setKonto(String konto) {
		this.konto = konto;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the pop3Server
	 */
	public String getPop3Server() {
		return pop3Server;
	}

	/**
	 * @param pop3Server the pop3Server to set
	 */
	public void setPop3Server(String pop3Server) {
		this.pop3Server = pop3Server;
	}
	/**
	 * 
	 * @return Pop3 Server Port
	 */
	public int getPop3Port() {
		return pop3Port;
	}

	/**
	 * @param pop3Server the pop3Server to set
	 */
	public void setPop3Port(int pop3Port) {
		this.pop3Port = pop3Port;
	}

	/**
	 * @return the benutzerNamePop
	 */
	public String getBenutzerNamePop() {
		return benutzerNamePop;
	}

	/**
	 * @param benutzerNamePop the benutzerNamePop to set
	 */
	public void setBenutzerNamePop(String benutzerNamePop) {
		this.benutzerNamePop = benutzerNamePop;
	}

	/**
	 * @return the passwortPop
	 */
	public String getPasswortPop() {
		return passwortPop;
	}

	/**
	 * @param passwortPop the passwortPop to set
	 */
	public void setPasswortPop(String passwortPop) {
		this.passwortPop = passwortPop;
	}

	/**
	 * @return the smtpServer
	 */
	public String getSmtpServer() {
		return smtpServer;
	}
	
	public int getSmtpPort() {
		return smtpPort;
	}

	/**
	 * @param smtpServer the smtpServer to set
	 */
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	/**
	 * @return the benutzerNameSmtp
	 */
	public String getBenutzerNameSmtp() {
		return benutzerNameSmtp;
	}

	/**
	 * @param benutzerNameSmtp the benutzerNameSmtp to set
	 */
	public void setBenutzerNameSmtp(String benutzerNameSmtp) {
		this.benutzerNameSmtp = benutzerNameSmtp;
	}

	/**
	 * @return the passwortSmtp
	 */
	public String getPasswortSmtp() {
		return passwortSmtp;
	}

	/**
	 * @param passwortSmtp the passwortSmtp to set
	 */
	public void setPasswortSmtp(String passwortSmtp) {
		this.passwortSmtp = passwortSmtp;
	}

}
