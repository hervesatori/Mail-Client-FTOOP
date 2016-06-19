package ftoop.mailclient.gui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ftoop.mailclient.daten.Mail;
/**
 * Tablemodel, hier werden die Spalten der Emailliste definiert, um danach Mails mit Hauptattributen in der Liste darzustellen
 * 
 * @author Herve Satori & Dominique Borer
 *
 */
public class MailTableModel extends AbstractTableModel {
	public final static int VON_COLUMN = 0;
	public final static int AN_COLUMN = 1;
	public final static int BETREFF_COLUMN = 2;
	public final static int DATUM_COLUMN = 3;
	public final static int ATTACHMENTS_COLUMN = 4;
	public final static int GELESEN_COLUMN = 5;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String[] COLUMN_NAMES = { "Von","An","Betreff", "Datum","Attachements","Gelesen"};
  
    private final List<Mail> containingMails;
    /**
     * 
     * @param containingMails
     */
    public MailTableModel(final List<Mail> containingMails) 
    {
        // Kopie von containingMails
        this.containingMails = new ArrayList<>(containingMails);
    }

    @Override
    public int getRowCount() {
        return containingMails.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex){
        final Mail mail = containingMails.get(rowIndex);
        Format sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
        if (columnIndex == MailTableModel.VON_COLUMN)
            return mail.getFrom();
        else if (columnIndex == MailTableModel.AN_COLUMN)
            return mail.getTo();
        else if (columnIndex == MailTableModel.BETREFF_COLUMN)
            return mail.getSubject();
        else if (columnIndex == MailTableModel.DATUM_COLUMN)
            return sdf.format(mail.getReceived());
        else if (columnIndex == MailTableModel.ATTACHMENTS_COLUMN)
            return mail.getAttachments().size();
        else if (columnIndex == MailTableModel.GELESEN_COLUMN)
            return mail.getisRead();

        throw new IllegalArgumentException("Invalid columIndex " + columnIndex);
    }
    @Override
    public String getColumnName(final int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }
    
    public void removeRow(int row) {
     if(row >= 0 && row < containingMails.size()) {
    	containingMails.remove(row);
        fireTableRowsDeleted(row, row);
     }
    }
    public Mail getListEntry(final int rowIndex){
    	return containingMails.get(rowIndex);
    }
}

