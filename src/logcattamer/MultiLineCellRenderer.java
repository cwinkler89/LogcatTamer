package logcattamer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

  public MultiLineCellRenderer() {
    setLineWrap(true);
    setWrapStyleWord(true);
    setOpaque(true);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    
    try {
    	Object levelObject = table.getValueAt(row, 0);    
    	
    	if (levelObject != null) {
    		String levelString = levelObject.toString();
    		
    		Color rowColor;
    		
    		if (levelString.equals("V")) rowColor = new Color(0x000000);
    		else if (levelString.equals("D")) rowColor = new Color(0x0000FF);
    		else if (levelString.equals("I")) rowColor = new Color(0x008800);
    		else if (levelString.equals("W")) rowColor = new Color(0xFF8800);
    		else if (levelString.equals("E")) rowColor = new Color(0xDD0000);
    		else rowColor = new Color(0xAA00AA);
    		
    		if (isSelected) {
    			setForeground(rowColor);
    		    setBackground(table.getSelectionBackground());      
		    } else {
	    		setBackground(rowColor);
	    		setForeground(Color.WHITE);
		    }
    	}
        
        if (column == 4 && value != null) {
    		int cellWidth = table.getCellRect(row, column, false).width;
    		int charsInCell = value.toString().length();
    		int linesNeeded = charsInCell * 6 / cellWidth;
    		
    		int hightNeeded = Math.max(18, 36 * linesNeeded);
    		if (table.getRowHeight(row) != hightNeeded) table.setRowHeight(row, hightNeeded);
    	}
        
        
    } catch(Exception e) {
    	System.out.println(e.toString());
    }
    
    
    setFont(table.getFont());
    if (hasFocus) {
      setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
      if (table.isCellEditable(row, column)) {
        setForeground(UIManager.getColor("Table.focusCellForeground"));
        setBackground(UIManager.getColor("Table.focusCellBackground"));
      }
    } else {
      setBorder(new EmptyBorder(1, 2, 1, 2));
    }
    setText((value == null) ? "" : value.toString());
    return this;
  }
}