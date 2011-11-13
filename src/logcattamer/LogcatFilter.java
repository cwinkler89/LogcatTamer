package logcattamer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.TableView.TableRow;

public class LogcatFilter extends JTable {
	private DefaultTableModel mModel;
	private String mFilterString;
	
//	HashMap<String, Integer[]> mTagMap;
	
	public LogcatFilter() {
		mModel = new DefaultTableModel() {
			public Class getColumnClass(int columnIndex) {
				return String.class;
			}
		};
	    
		mModel.setColumnIdentifiers(new Object[] {
	    	    "x", "pid", "tag", "#" });
		
		setModel(mModel);
		
	    getColumnModel().getColumn(0).setMaxWidth(12);
	    
	    getColumnModel().getColumn(1).setMinWidth(50);
	    getColumnModel().getColumn(1).setMaxWidth(50);

	    getColumnModel().getColumn(3).setMaxWidth(50);
	    
	    JCheckBox checkBox = new JCheckBox();
    	checkBox.setHorizontalAlignment(JLabel.CENTER);
    	DefaultCellEditor checkBoxEditor = new DefaultCellEditor(checkBox);
    	
    	getColumn("x").setCellRenderer(new CheckBoxRenderer());
    	getColumn("x").setCellEditor(checkBoxEditor);
    	
//    	mTagMap = new HashMap<String, Integer[]>();
    	
    	TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(mModel);
    	
//    	Comparator<Boolean> boolComparator = new Comparator<Boolean>() {
//			@Override
//			public int compare(Boolean arg0, Boolean arg1) {
//				
//				if (arg0 && arg1)
//					return 3;
//				else if (arg0 && !arg1)
//					return 2;
//				else if (!arg0 && arg1)
//					return 1;
//				else
//					return 0;
//			}
//    	};
    	
    	List <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
	    sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
	    sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
//	    sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
	    
	    sorter.setSortKeys(sortKeys);
//	    sorter.setComparator(0, boolComparator);
	    
	    setRowSorter(sorter);
	    
	    setVisible(true);
	    
	    mModel.addRow(new Object[]{true, "", "", 0});
	    
	    
	}

	public void handleNewMessage(String pid, String tag) {
		
//		Integer[] values = mTagMap.get(tag);
//		if (values == null) {
//			mModel.addRow(new Object[]{false, "" + pid, tag, 1});
//			mTagMap.put(tag, new Integer[]{1, mModel.getRowCount()});
//		} else {
//			mModel.setValueAt(++values[0], values[1], 3);
//		}
		
//		int row = findRowByTagOnly(tag);
		int row = findRow(pid, tag);
		if (tag.equals("com.test.loggingtest.LoggingTestActivity")) {
			System.out.println(tag + " --> " + row);
		}
		
		if (row == -1) {
			mModel.addRow(new Object[]{false, pid, tag, 1});
		} else {
			
//			if (!getValueAt(row, 1).equals(pid)){
//				setValueAt("", row, 1);
//			}
			
			setValueAt((Integer) getValueAt(row, 3) + 1, row, 3);
		}
		
		setValueAt((Integer) getValueAt(0, 3) + 1, 0, 3);
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		
		
		super.tableChanged(e);
	}
	
	
	public RowFilter<DefaultTableModel, Object> getFilter() {
		
		RowFilter<DefaultTableModel, Object> rf = null;
        ArrayList<RowFilter<DefaultTableModel, Object>> fullList = new ArrayList<RowFilter<DefaultTableModel,Object>>();
        ArrayList<RowFilter<DefaultTableModel, Object>> tempList;
        
		for (int i = 0; i < getRowCount(); i++) {
			if ((Boolean) getValueAt(i, 0)) {
				String pid = (String) getValueAt(i, 1);
				String tag = (String) getValueAt(i, 2);
				
				tempList = new ArrayList<RowFilter<DefaultTableModel,Object>>();
				
				rf = RowFilter.regexFilter(pid, 2);
				tempList.add(rf);
				
				rf = RowFilter.regexFilter(tag, 3);
				tempList.add(rf);
				
	            fullList.add(RowFilter.andFilter(tempList));    
			}
		}
		
		return RowFilter.orFilter(fullList);
	}
	
	private int findRowByTagOnly(String tag) {
		
		for (int i = 0; i < getRowCount(); i++) {
			if (getValueAt(i, 2).equals(tag)) {
				return i;
			}
		}
		
		return -1;
	}
	
	private int findRow(String pid, String tag) {
		for (int i = 0; i < getRowCount(); i++) {
			if (getValueAt(i, 1).equals(pid) &&
				getValueAt(i, 2).equals(tag)) {
				return i;
			}
		}
		
		return -1;
	}
}
