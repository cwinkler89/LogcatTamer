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

package logcattamer;

/*
 * TableSelectionDemo.java requires no other files.
 */

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Calendar;

public class LogcatTamer extends JPanel 
                                implements ActionListener { 
	
	private static boolean DEBUG = false;
	
    private JTable table;
    private JCheckBox rowCheck;
    private JCheckBox columnCheck;
    private JCheckBox cellCheck;
    private ButtonGroup buttonGroup;
    private JTextArea output;

    private JScrollPane mFilterPane;
    private JScrollPane mLoggerPane;
    
    private JTable mFilterTable;
    private JTable mLoggerTable;
    
    private DefaultTableModel mFilter;
    private DefaultTableModel mLogger;
	private JTextField mFilterText;

	private boolean mAutoScroll = true;
	
	
	public LogcatTamer() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        mFilter = new DefaultTableModel() {
        	public boolean isCellEditable(int row, int column) {
        	    if (column == 0) {
        	      return true;
        	    }
        	    return false;
        	}
        };

    	mFilter.setColumnIdentifiers(new Object[] {
    	    "show", "pid", "tag", "count" });
    	

    	JCheckBox checkBox = new JCheckBox();
    	checkBox.setHorizontalAlignment(JLabel.CENTER);
    	DefaultCellEditor checkBoxEditor = new DefaultCellEditor(checkBox);
    	
    	mFilterTable = new JTable(mFilter);
    	mFilterTable.getColumn("show").setCellRenderer(new CheckBoxRenderer());
    	mFilterTable.getColumn("show").setCellEditor(checkBoxEditor);
    	
    	
    	mLogger = new DefaultTableModel() {
        	public boolean isCellEditable(int row, int column) {
        	    return false;
        	}
        };

        mLogger.setColumnIdentifiers(new Object[] {
    	    "level", "time", "pid", "tag", "message" });
        
        mLoggerTable = new JTable(mLogger);
        mLoggerTable.setDefaultRenderer(String.class, new MultiLineCellRenderer());
        
        
        
        mFilterPane = new JScrollPane(mFilterTable);
        mLoggerPane = new JScrollPane(new LogcatViewer());
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        		mFilterPane, mLoggerPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		
		//Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		mFilterPane.setMinimumSize(minimumSize);
		mLoggerPane.setMinimumSize(minimumSize);
		
		add(splitPane);
		
		mLoggerPane.setAutoscrolls(true);
		
		
//		
		mFilter.addRow(new Object[]{true, 1,2,3, 4, 5});
		
		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});
//		mLogger.addRow(new Object[]{true, 1,2,3, 4, 5});

		mLoggerPane.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				mAutoScroll = false;
			}
		});
		
		mLoggerPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			public void adjustmentValueChanged(AdjustmentEvent e) {
				
    			if (mAutoScroll) e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
    		}});
		
//		t.start();
		
		mLoggerPane.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				mAutoScroll = false;
			}
		});
		
		mLoggerPane.
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("keyTyped");
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
				
				if (arg0.getKeyCode() == 35) {
					mAutoScroll = true;
				} else {
					mAutoScroll = false;
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("keyPressed");
			}
		});
		
    }
    
    private JCheckBox addCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.addActionListener(this);
        add(checkBox);
        return checkBox;
    }

    private JRadioButton addRadio(String text) {
        JRadioButton b = new JRadioButton(text);
        b.addActionListener(this);
        buttonGroup.add(b);
        add(b);
        return b;
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        //Cell selection is disabled in Multiple Interval Selection
        //mode. The enabled state of cellCheck is a convenient flag
        //for this status.
        if ("Row Selection" == command) {
            table.setRowSelectionAllowed(rowCheck.isSelected());
            //In MIS mode, column selection allowed must be the
            //opposite of row selection allowed.
            if (!cellCheck.isEnabled()) {
                table.setColumnSelectionAllowed(!rowCheck.isSelected());
            }
        } else if ("Column Selection" == command) {
            table.setColumnSelectionAllowed(columnCheck.isSelected());
            //In MIS mode, row selection allowed must be the
            //opposite of column selection allowed.
            if (!cellCheck.isEnabled()) {
                table.setRowSelectionAllowed(!columnCheck.isSelected());
            }
        } else if ("Cell Selection" == command) {
            table.setCellSelectionEnabled(cellCheck.isSelected());
        } else if ("Multiple Interval Selection" == command) { 
            table.setSelectionMode(
                    ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            //If cell selection is on, turn it off.
            if (cellCheck.isSelected()) {
                cellCheck.setSelected(false);
                table.setCellSelectionEnabled(false);
            }
            //And don't let it be turned back on.
            cellCheck.setEnabled(false);
        } else if ("Single Interval Selection" == command) {
            table.setSelectionMode(
                    ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            //Cell selection is ok in this mode.
            cellCheck.setEnabled(true);
        } else if ("Single Selection" == command) {
            table.setSelectionMode(
                    ListSelectionModel.SINGLE_SELECTION);
            //Cell selection is ok in this mode.
            cellCheck.setEnabled(true);
        }

        //Update checkboxes to reflect selection mode side effects.
        rowCheck.setSelected(table.getRowSelectionAllowed());
        columnCheck.setSelected(table.getColumnSelectionAllowed());
        if (cellCheck.isEnabled()) {
            cellCheck.setSelected(table.getCellSelectionEnabled());
        }
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Disable boldface controls.
        UIManager.put("swing.boldMetal", Boolean.FALSE); 

        //Create and set up the window.
        JFrame frame = new JFrame("Logcat Tamer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        LogcatTamer newContentPane = new LogcatTamer();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        
    }
}

