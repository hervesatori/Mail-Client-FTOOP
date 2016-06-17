/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package ftoop.mailclient.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import ftoop.mailclient.daten.EmailKonto;

/**
 * TextInputDemo.java uses these additional files:
 *   SpringUtilities.java
 *   ...
 */
public class ConfigurationFieldsPanel extends JPanel implements ActionListener,
                                                     FocusListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8432912549117328877L;
	JTextField  kontoName, email, userName,
    password, popServer,smtpServer,imapServer,popPort;
    JFormattedTextField  smtpPort, imapPort;
    JSpinner stateSpinner;
    Font regularFont, italicFont;
    JLabel configDisplay;
    final static int GAP = 10;

    public ConfigurationFieldsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel fieldsPane = new JPanel(){
            //Don't allow us to stretch vertically.
        	@Override
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE,
                                     pref.height);
            }
        };
        fieldsPane.setLayout(new BoxLayout(fieldsPane,
                                         BoxLayout.PAGE_AXIS));
        fieldsPane.add(createEntryFields());
        fieldsPane.add(createButtons());

        add(fieldsPane);
        this.setFieldsEnabled(false);
    }

    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

 
        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                                                GAP-5, GAP-5));
        return panel;
    }

    /**
     * Called when the user clicks the button or presses
     * Enter in a text field.
     */
    public void actionPerformed(ActionEvent e) {
          updateDisplays();
    }

    protected void updateDisplays() {
            configDisplay.setFont(regularFont);
    }

    protected JComponent createAddressDisplay() {
        JPanel panel = new JPanel(new BorderLayout());
        configDisplay = new JLabel();
        configDisplay.setHorizontalAlignment(JLabel.CENTER);
        regularFont = configDisplay.getFont().deriveFont(Font.PLAIN,
                                                            16.0f);
        italicFont = regularFont.deriveFont(Font.ITALIC);
        updateDisplays();

        //Lay out the panel.
        panel.setBorder(BorderFactory.createEmptyBorder(
                                GAP/2, //top
                                0,     //left
                                GAP/2, //bottom
                                0));   //right
        panel.add(new JSeparator(JSeparator.VERTICAL),
                  BorderLayout.LINE_START);
        panel.add(configDisplay,
                  BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(200, 150));

        return panel;
    }


    /**
     * Called when one of the fields gets the focus so that
     * we can select the focused field.
     */
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selectItLater(c);
        } else if (c instanceof JTextField) {
            ((JTextField)c).selectAll();
        }
    }

    //Workaround for formatted text field focus side effects.
    protected void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField)c;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }

    //Needed for FocusListener interface.
    public void focusLost(FocusEvent e) { } //ignore
    public void setFieldTexts(EmailKonto konto){
    	this.kontoName.setText(konto.getKonto());
    	this.email.setText(konto.getEmail());
    	this.userName.setText(konto.getBenutzerNamePop());
    	this.password.setText(konto.getPasswortPop());
    	this.popServer.setText(konto.getPop3Server());
    	this.smtpServer.setText(konto.getSmtpServer());
    	this.imapServer.setText(konto.getImapServer());
    	
    	this.popPort.setText(String.valueOf(konto.getPop3Port()));
    	this.smtpPort.setText(String.valueOf(konto.getSmtpPort()));
    	this.imapPort.setText(String.valueOf(konto.getImapPort()));
    }
    public EmailKonto getFieldTexts() {
    	
    	return new EmailKonto(this.kontoName.getText(),this.userName.getText(),this.email.getText(),this.popServer.getText(),Integer.parseInt(this.popPort.getText()),this.userName.getText(),this.password.getText(),this.smtpServer.getText(),Integer.parseInt(this.smtpPort.getText()),this.userName.getText(),this.password.getText(),this.imapServer.getText(),Integer.parseInt(this.imapPort.getText()));
    }
    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
            "Kontoname: ",
            "Emailadresse: ",
            "Benutzername: ",
            "Passwort: ",
            "Pop3 Port: ",
            "Pop3 Server: ",
            "SMTP Port: ",
            "SMTP Server: ",
            "IMAP Port: ",
            "IMAP Server: "
        };

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        this.kontoName  = new JTextField();
        this.kontoName.setColumns(20);
        fields[fieldNum++] = this.kontoName;

        this.email  = new JTextField();
        this.email.setColumns(20);
        fields[fieldNum++] = email;

        this.userName  = new JTextField();
        this.userName.setColumns(20);
        fields[fieldNum++] = this.userName;

        this.password  = new JTextField();
        this.password.setColumns(20);
        fields[fieldNum++] = this.password;

        NumberFormatter nf = new NumberFormatter();
        nf.setMinimum(new Integer(1));
        nf.setMaximum(new Integer(50000));
        this.popPort = new JFormattedTextField(nf);
        fields[fieldNum++] = this.popPort;

        this.popServer  = new JTextField();
        this.popServer.setColumns(20);
        fields[fieldNum++] = this.popServer;

        this.smtpPort = new JFormattedTextField(nf);
        fields[fieldNum++] = this.smtpPort;

        this.smtpServer  = new JTextField();
        this.smtpServer.setColumns(20);
        fields[fieldNum++] = this.smtpServer;

        this.imapPort = new JFormattedTextField(nf);
        fields[fieldNum++] = this.imapPort;

        this.imapServer  = new JTextField();
        this.imapServer.setColumns(20);
        fields[fieldNum++] = this.imapServer;

        //Associate label/field pairs, add everything,
        //and lay it out.
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i],
                                   JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);

            //Add listeners to each field.
            JTextField tf = null;
            if (fields[i] instanceof JSpinner) {
                tf = getTextField((JSpinner)fields[i]);
            } else {
                tf = (JTextField)fields[i];
            }
            tf.addActionListener(this);
            tf.addFocusListener(this);
        }
        SpringUtilities.makeCompactGrid(panel,
                                        labelStrings.length, 2,
                                        GAP, GAP, //init x,y
                                        GAP, GAP/2);//xpad, ypad
        return panel;
    }

        private JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: "
                               + spinner.getEditor().getClass()
                               + " isn't a descendant of DefaultEditor");
            return null;
        }
    }

		public void setFieldsEnabled(boolean enabled) {
	    	this.kontoName.setEnabled(enabled);
	    	this.email.setEnabled(enabled);
	    	this.userName.setEnabled(enabled);
	    	this.password.setEnabled(enabled);
	    	this.popServer.setEnabled(enabled);
	    	this.smtpServer.setEnabled(enabled);
	    	this.imapServer.setEnabled(enabled);
	    	
	    	this.popPort.setEnabled(enabled);
	    	this.smtpPort.setEnabled(enabled);
	    	this.imapPort.setEnabled(enabled);
			
		}

		public void clearFields() {
	    	this.kontoName.setText("");
	    	this.email.setText("");
	    	this.userName.setText("");
	    	this.password.setText("");
	    	this.popServer.setText("");
	    	this.smtpServer.setText("");
	    	this.imapServer.setText("");
	    	
	    	this.popPort.setText("");
	    	this.smtpPort.setText("");
	    	this.imapPort.setText("");
		}


}