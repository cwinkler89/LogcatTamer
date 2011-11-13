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
import javax.swing.table.DefaultTableModel;

public class LogcatViewer extends JTable {
	private DefaultTableModel mLogger;
	private boolean DEBUG = false;

	LogcatViewer() {
		
	    mLogger = new DefaultTableModel() {
	      public Class getColumnClass(int columnIndex) {
	        return String.class;
	      }
	    };
	    
	    

	    mLogger.setColumnIdentifiers(new Object[] {
	    	    "level", "time", "pid", "tag", "message" });
	    
	    setModel(mLogger);
	    
	    getColumnModel().getColumn(0).setMaxWidth(12);
	    
	    getColumnModel().getColumn(1).setMinWidth(120);
	    getColumnModel().getColumn(1).setMaxWidth(120);

	    getColumnModel().getColumn(2).setMinWidth(50);
	    getColumnModel().getColumn(2).setMaxWidth(50);
	    
	    getColumnModel().getColumn(3).setMinWidth(120);
	    
	    setDefaultRenderer(String.class, new MultiLineCellRenderer());
	    
	    setVisible(true);
	    new Thread(new LoggingRunnable()).start();
	}
	
	private class LoggingRunnable implements Runnable {

		@Override
		public void run() {
			try {
	            // Execute command
//	            String command = "adb logcat";
	            String command = "adb logcat -v time";
	            Process child = Runtime.getRuntime().exec(command);

	            // Get the input stream and read from it
	            InputStream in = child.getInputStream();
	            byte[] buffer = new byte[2048];
	            String stringBuffer = new String();
	            
	            int c;
	            while ((c = in.read(buffer)) != -1) {
	            	long millis = Calendar.getInstance().getTimeInMillis();
	            	Date d = new Date(millis);
	            	
	            	stringBuffer += new String(buffer);
	            	buffer = new byte[128];
	            	
	            	
	            	while(stringBuffer.contains("\n"))
	            	{
	            		
	            		try {
	            			
	            			char level = 'D';
	            			int pid = 123;
	            			String timeStampString = null;
	            			String tag = "asdf";
	            			String message = "someting";
	            			Boolean parsingError = false;
	            			
//		            	System.out.println(stringBuffer);
	            			
	            			int levelPos = stringBuffer.indexOf('/')-1;
	            			
	            			if (DEBUG) {
	            				System.out.println("levelPos: " + levelPos);
	            			}
	            			
	            			if (stringBuffer.contains("--------- beginning of /dev/log/main")) {
	            				stringBuffer = stringBuffer.substring(stringBuffer.indexOf("\n")+1);
	            			}
	            			
	            			if (levelPos < 0 || levelPos > stringBuffer.length()) {
	            				parsingError = true;
	            				level = 'F';
	            			} else {
	            				if (levelPos > 18) {
	            					timeStampString = stringBuffer.substring(levelPos-19, levelPos-1);
	            				} 
	            				
	            				level = stringBuffer.charAt(levelPos);
	            			}
	            			
	            			int tagStart = levelPos+2;
	            			int tagEnd = stringBuffer.indexOf("(");
	            			
	            			if (DEBUG) {
	            				System.out.println("tagStart: " + tagStart);
	            				System.out.println("tagEnd: " + tagEnd);
	            			}
	            			
	            			if (parsingError || tagStart < 0 || tagEnd < 0) {
	            				parsingError = true;
	            				tag = "";
	            			} else {
	            				tag = stringBuffer.substring(tagStart, tagEnd);
	            			}
	            			
	            			
	            			int pidEnd = stringBuffer.indexOf(")", tagEnd);
	            			int pidStart = stringBuffer.lastIndexOf("(", pidEnd)+1;
	            			
	            			if (DEBUG) {
	            				System.out.println("pidStart: " + pidStart);
	            				System.out.println("pidEnd: " + pidEnd);
	            			}
	            			
	            			if (parsingError || pidStart < 0 || pidEnd < 0) {
	            				parsingError = true;
	            				pid = 0;
	            				pidEnd = 0;
	            			} else {
	            				try {
	            					pid = new Integer(stringBuffer.substring(pidStart, pidEnd).trim());			            			
	            				} catch (Exception e) {
	            					tag = e.getClass().toString();
	            				}
	            			}
	            			
	            			int messageStart = stringBuffer.indexOf(":", pidEnd)+1;
	            			int messageEnd = stringBuffer.indexOf("\n",messageStart);
	            			
	            			if (DEBUG) {
	            				System.out.println("messageStart:" +messageStart);
	            				System.out.println("messageEnd:" +messageEnd);
	            			}
	            			
	            			if (messageStart < 0 || messageEnd < 0 || messageEnd < messageStart) {
	            				message = "PARSING ERROR";
	            			} else {
	            				message = stringBuffer.substring(messageStart, messageEnd);
	            			}
	            			
	            			if (parsingError || DEBUG) System.out.println(stringBuffer);
	            			
	            			stringBuffer = stringBuffer.substring(messageEnd+1);
	            			
	            			mLogger.addRow(new Object[]{
	            					level,
	            					(timeStampString == null ? d.toLocaleString() : timeStampString),
	            					pid,
	            					tag,
	            					message
	            					
	            			});
	            			
	            			if (mLogger.getRowCount() > 400) mLogger.removeRow(0);
	            		} catch (Exception e) {
	            			e.printStackTrace();
	            		}
		            	
	            	}
	            	 
	            }
	            in.close();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
			
			System.out.println("run is done");
		}
		
	}

}
