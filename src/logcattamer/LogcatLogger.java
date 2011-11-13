package logcattamer;

import java.awt.Panel;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class LogcatLogger extends JTable {
	private DefaultTableModel mModel;
	private TableRowSorter<DefaultTableModel> mSorter;
	
	LogcatLogger() {
		
	    mModel = new DefaultTableModel() {
	      public Class getColumnClass(int columnIndex) {
	        return String.class;
	      }
	    };
	    
	    mModel.setColumnIdentifiers(new Object[] {
	    	    "level", "time", "pid", "tag", "message" });
	    
	    setModel(mModel);
	    
	    mSorter = new TableRowSorter<DefaultTableModel>(mModel);
	    setRowSorter(mSorter);
	    
	    getColumnModel().getColumn(0).setMaxWidth(12);
	    
	    getColumnModel().getColumn(1).setMinWidth(120);
	    getColumnModel().getColumn(1).setMaxWidth(120);

	    getColumnModel().getColumn(2).setMinWidth(50);
	    getColumnModel().getColumn(2).setMaxWidth(50);
	    
	    getColumnModel().getColumn(3).setMinWidth(120);
	    
	    setDefaultRenderer(String.class, new MultiLineCellRenderer());
	    
	    setVisible(true);
	    
	}

	public void addRow(Object[] objects) {
		mModel.addRow(objects);
		
		if (mModel.getRowCount() > 5000) mModel.removeRow(0);
	} 
	
	
	public void setFilter(RowFilter<DefaultTableModel, Object> newFilter) {
		mSorter.setRowFilter(newFilter);
		
	}
	

}
