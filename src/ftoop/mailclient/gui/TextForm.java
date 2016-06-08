package ftoop.mailclient.gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextForm extends JPanel {

  /**
	 * 
	 */

	
	
	
	private static final long serialVersionUID = 1L;
    private JTextField[] fields;


  // Create a form with the specified labels, tooltips, and sizes.
  public TextForm(String[] labels, int[] widths, String[] tips) {
    super(new BorderLayout(5,5));
    JPanel labelPanel = new JPanel(new GridLayout(labels.length, 1));
    JPanel fieldPanel = new JPanel(new GridLayout(labels.length, 1));
    add(labelPanel,BorderLayout.WEST);
    add(fieldPanel,BorderLayout.CENTER);
    
    fields = new JTextField[labels.length];

    for (int i = 0; i < labels.length; i++) {
      fields[i] = new JTextField();
      if (i < tips.length && i < widths.length) {
        fields[i].setToolTipText(tips[i]);
        fields[i].setColumns(widths[i]);
      }

      JLabel lab = new JLabel(labels[i], JLabel.RIGHT);
      lab.setLabelFor(fields[i]);
      labelPanel.add(lab);
      JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p.add(fields[i]);
      fieldPanel.add(p);
    }
  }

  public String getText(int i) {
    return (fields[i].getText());
  }
  public void setText(int i,String field) {
	  fields[i].setText(field);
  }
  public void disableField() {
	  for(int i = 0; i < fields.length; i++) {
		  fields[i].setEnabled(false);
	}
	  
  }
  public void enableField() {
	  for(int i = 0; i < fields.length; i++) {
		  fields[i].setEnabled(true);
	}
  }

}	
 

  