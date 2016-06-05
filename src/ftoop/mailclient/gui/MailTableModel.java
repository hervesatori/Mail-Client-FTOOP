package ftoop.mailclient.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ftoop.mailclient.daten.Mail;
/**
 * 
 * @author Herve Satori & Dominique Borer
 *
 */
public class MailTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String[] COLUMN_NAMES = { "Von","An","Betreff", "Datum","Attachements" };
  
    private final List<Mail> containingMails;

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
        if (columnIndex == 0)
            return mail.getFrom();
        else if (columnIndex == 1)
            return mail.getTo();
        else if (columnIndex == 2)
            return mail.getSubject();
        else if (columnIndex == 3)
            return mail.getReceived();
        else if (columnIndex == 4)
            return mail.getAttachments().size();

        throw new IllegalArgumentException("Invalid columIndex " + columnIndex);
    }
    @Override
    public String getColumnName(final int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }
    
    public void removeRow(int row)
    {
     if(row >= 0 && row < containingMails.size()) {
    	containingMails.remove(row);
        fireTableRowsDeleted(row, row);
     }
    }
}

