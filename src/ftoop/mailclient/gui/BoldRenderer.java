package ftoop.mailclient.gui;

import java.awt.Component;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class BoldRenderer extends DefaultTableCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    
    
    public Component getTableCellRendererComponent(JTable tblData,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(
                tblData, value, isSelected, hasFocus, row, column);

        if (tblData.getValueAt(row, 5).equals(true)) {
            cellComponent.setFont(cellComponent.getFont().deriveFont(
                    Font.BOLD));
        } else {
            cellComponent.setFont(cellComponent.getFont().deriveFont(
                    Font.PLAIN));
        }

        return cellComponent;
    }
}