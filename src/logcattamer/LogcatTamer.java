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

import javax.security.auth.callback.Callback;
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
import java.awt.Adjustable;
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
	
	public final static boolean DEBUG = false;
	
    private JScrollPane mFilterPane;
    private JScrollPane mLoggerPane;
    
	private boolean mAutoScroll = true;

	private LogcatLogger mLogger;
	private LogcatFilter mFilter;
	
	private Thread mLoggingThread;
	
	
	public LogcatTamer() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        initFilter();
        intiLogger();
        
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        		mFilterPane, mLoggerPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(250);
		
		//Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		mFilterPane.setMinimumSize(minimumSize);
		mLoggerPane.setMinimumSize(minimumSize);
		
		add(splitPane);
		
		mLoggingThread = new Thread(new LoggingRunnable());
		mLoggingThread.start();
    }
  
	private void intiLogger() {
        mLogger = new LogcatLogger();
        mLoggerPane = new JScrollPane(mLogger);
        
        mLoggerPane.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				mAutoScroll = false;
			}
		});
        
        mLoggerPane.setAutoscrolls(true);
		
		mLoggerPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			public void adjustmentValueChanged(AdjustmentEvent e) {
    			if (mAutoScroll) e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
    		}});
		
		mLoggerPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mAutoScroll = false;
			}
		});
		
		mLogger.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				if (arg0.getKeyCode() == 35) {
					mAutoScroll = true;
					System.out.println("autoscroll ON");
				} else {
					mAutoScroll = false;
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
        
	}

	private void initFilter() {
		mFilter = new LogcatFilter();
		mFilterPane = new JScrollPane(mFilter);
		
		mFilter.addMouseListener(new MouseListener() {
			
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
				System.out.println("GOTCHA");
				
				mLogger.setFilter(mFilter.getFilter());
				
			}
		});
		
	}
	
    public void actionPerformed(ActionEvent event) {
    	
    	System.out.println(event.getActionCommand()); 
    	
    }
    
    private class LoggingRunnable implements Runnable {

		@Override
		public void run() {
			try {
	            String command = "adb logcat -v time";
	            Process child = Runtime.getRuntime().exec(command);

	            // Get the input stream and read from it
	            InputStream in = child.getInputStream();
	            byte[] buffer = new byte[2048];
	            String stringBuffer = new String();
	            
	            int c;
	            while ((c = in.read(buffer)) != -1) {
	            	
	            	stringBuffer += new String(buffer);
	            	buffer = new byte[128];
	            	
	            	stringBuffer = processStringBuffer(stringBuffer);
	            	
	            }
	            in.close();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
			
			System.out.println("run is done");
		}

		private String processStringBuffer(String stringBuffer) {
			while(stringBuffer.contains("\n"))
        	{
        		
				try {
					
					char level = 'D';
					String pid = "0";
					String timeStampString = null;
					String tag = "asdf";
					String message = "someting";
					Boolean parsingError = false;
					
					int levelPos = stringBuffer.indexOf('/')-1;
					
					if (LogcatTamer.DEBUG) {
						System.out.println("levelPos: " + levelPos);
					}
					
					if (stringBuffer.contains("--------- beginning of /dev/log/main") ||
							stringBuffer.contains("--------- beginning of /dev/log/system")) {
						stringBuffer = stringBuffer.substring(stringBuffer.indexOf("\n")+1);
					}
					
					if (levelPos < 0 || levelPos > stringBuffer.length()) {
						parsingError = true;
						level = '?';
					} else {
						if (levelPos > 18) {
							timeStampString = stringBuffer.substring(levelPos-19, levelPos-1);
						} 
						
						level = stringBuffer.charAt(levelPos);
					}
					
					int tagStart = levelPos+2;
					int tagEnd = stringBuffer.indexOf("(");
					
					if (LogcatTamer.DEBUG) {
						System.out.println("tagStart: " + tagStart);
						System.out.println("tagEnd: " + tagEnd);
					}
					
					if (parsingError || tagStart < 0 || tagEnd < 0) {
						parsingError = true;
						tag = "<unknown>";
					} else {
						tag = stringBuffer.substring(tagStart, tagEnd);
					}
					
					
					int pidEnd = stringBuffer.indexOf(")", tagEnd);
					int pidStart = stringBuffer.lastIndexOf("(", pidEnd)+1;
					
					if (LogcatTamer.DEBUG) {
						System.out.println("pidStart: " + pidStart);
						System.out.println("pidEnd: " + pidEnd);
					}
					
					if (parsingError || pidStart < 0 || pidEnd < 0) {
						parsingError = true;
						pid = "0";
						pidEnd = 0;
					} else {
						pid = stringBuffer.substring(pidStart, pidEnd).trim();			            			
					}
					
					int messageStart = stringBuffer.indexOf(":", pidEnd)+1;
					int messageEnd = stringBuffer.indexOf("\n",messageStart);
					
					if (LogcatTamer.DEBUG) {
						System.out.println("messageStart:" +messageStart);
						System.out.println("messageEnd:" +messageEnd);
					}
					
					if (messageStart < 0 || messageEnd < 0 || messageEnd < messageStart) {
						message = "PARSING ERROR";
					} else {
						message = stringBuffer.substring(messageStart, messageEnd);
					}
					
					stringBuffer = stringBuffer.substring(messageEnd+1);
					
					
					if (!parsingError) {
						
						try {
							mLogger.addRow(new Object[]{level, timeStampString, pid, tag, message});
							mFilter.handleNewMessage(pid, tag);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("---UNABLE TO LOG---");
							System.out.println("level: " + level);
							System.out.println("time: " + timeStampString);
							System.out.println("pid: " + pid);
							System.out.println("tag: " + tag);
							System.out.println("message: " + message);
							System.out.println("--- CURRENT STRINGBUFFER START ---");
							System.out.println(stringBuffer);
							System.out.println("--- CURRENT STRINGBUFFER END---");
						}
					} else {
						System.out.println("--- CURRENT STRINGBUFFER START ---");
						System.out.println(stringBuffer);
						System.out.println("--- CURRENT STRINGBUFFER END---");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					
					System.out.println("--- CURRENT STRINGBUFFER START ---");
					System.out.println(stringBuffer);
					System.out.println("--- CURRENT STRINGBUFFER END---");
				}
        	}
			
			return stringBuffer;
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

