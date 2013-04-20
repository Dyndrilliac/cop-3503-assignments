/*
	Author: Matthew Boyette
	Date:   2/10/2013
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class n00868808
{
	public static class Rectangle
	{
		private double height = 2.0;
		private double width  = 1.0;
		
		public Rectangle() {}
		
		public Rectangle(double height, double width)
		{
			this.setHeight(height);
			this.setWidth(width);
		}
		
		public double getArea()
		{
			return (this.getHeight() * this.getWidth());
		}
		
		public double getHeight()
		{
			return this.height;
		}
		
		public double getPerimeter()
		{
			if (this.isItSquare())
			{
				// Squares can take any one of their sides and multiply it by 4 to get their perimeter.
				return (this.getHeight() * 4);
			}
			else
			{
				// Rectangles get their perimeter by mutliplying their heights and widths by 2 and adding the products together.
				return ((this.getHeight() * 2) + (this.getWidth() * 2));
			}
		}
		
		public double getWidth()
		{
			return this.width;
		}
		
		public boolean isItSquare()
		{
			// A rectangle is a square if its height and its width are equal.
			return (this.getHeight() == this.getWidth());
		}
		
		public void setHeight(double height)
		{
			this.height = height;
		}
		
		public void setWidth(double width) {
			this.width = width;
		}
		
		public String toString()
		{
			return "This rectangle's height is " + this.getHeight() + " and its width is " + this.getWidth() + ".";
		}
	}
	
	/*
		I wrote this class in a previous Java course because I was not satisfied with JTextPane,
		and lusted after a text box object with much richer features like the RichTextBox control
		available in Visual Basic.NET and C#. Thus, RichTextPane was born.
	*/
	public static class RichTextPane extends JTextPane
	{
		private static final long serialVersionUID = 1L;
		private Component         parent           = null;
		private boolean           isDebugging      = false;
		
		public RichTextPane(boolean isReadOnly, boolean isDebugging, Component parent)
		{
			this.setFocusable(!isReadOnly);
			this.setEditable(!isReadOnly);
			this.isDebugging = isDebugging;
			this.parent = parent;
			this.clear();
		}
		
		public void append(Object... oArgs)
		{
			/*
				The append() helper method takes three arguments. In order for variable number of arguments
				to work, the number of arguments must be evenly divisible by three. I considered writing it
				to take 5 parameters so in addition to the string and the colors one could provide font,
				font size, and font style. But since RichTextPane inherits from JTextPane, and JTextPane
				provides the setFont method, I felt it would be redundant.
			*/
			if ((oArgs.length % 3) != 0)
			{
				handleException(this.parent, new RuntimeException("The variable argument append method received a number of arguments not evenly divisible by three."));
				return;
			}
			
			// Call the append() helper method for each set of arguments.
			for (int i = 0; i < oArgs.length; i += 3) { this.append((Color)oArgs[i], (Color)oArgs[i+1], oArgs[i+2].toString()); }
		}
		
		public void append(Color fgc, Color bgc, String string)
		{
			// Call setFormatting to set the foreground and background colors for the text.
			SimpleAttributeSet aset = setFormatting(fgc, bgc, false, false, false); 
			
			try
			{
				// Append a string to the current document using the desired attribute set.
				this.getDocument().insertString(this.getDocument().getLength(), string, aset);
			}
			catch (Exception exception)
			{
				handleException(this.parent, exception);
			}
		}
		
		public void clear()
		{
			this.setDocument(new DefaultStyledDocument());
			this.setFont(new Font("Lucida Console", Font.PLAIN, 14));
		}
		
		public boolean getDebugMode()
		{
			return this.isDebugging;
		}
		
		private String getFilePath(boolean isOpen)
		{
			JFileChooser fileDialog = new JFileChooser();
			boolean      stopFlag   = false;
			String       filePath   = null;
			int          choice     = 0;
			
			do // Loop while stopFlag equals false, post-test.
			{
				if (isOpen)
				{
					choice = fileDialog.showOpenDialog(this.parent);
				}
				else
				{
					choice = fileDialog.showSaveDialog(this.parent);
				}
				
				switch (choice)
				{
					case JFileChooser.APPROVE_OPTION:
						
						try
						{
							filePath = fileDialog.getSelectedFile().getCanonicalPath();
							stopFlag = true;
						}
						catch (Exception  exception)
						{
							filePath = null;
							stopFlag = false;
						}
						break;
						
					case JFileChooser.CANCEL_OPTION:
						
						filePath = null;
						stopFlag = true;
						break;
						
					default:
						
						filePath = null;
						stopFlag = false;
						break;
				}
			}
			while (stopFlag == false);
			
			if (this.getDebugMode())
			{
				JOptionPane.showMessageDialog(this, 
					"File Path: " + filePath + 
					"\nWhen: " + getDateTimeStamp(), 
					"File Accessed", 
					JOptionPane.INFORMATION_MESSAGE);
			}
			
			return filePath;
		}
		
		public void openFile()
		{
			String filePath = this.getFilePath(true);
			
			if ((filePath == null) || filePath.isEmpty())
			{
				// User has canceled the file operation; abort!
				return;
			}
			
			ObjectInputStream inputStream = null;
			
			try
			{
				// Use binary file manipulation to import a file containing a Document object.
				inputStream = new ObjectInputStream(new FileInputStream(filePath));
				this.setDocument((StyledDocument)inputStream.readObject());
			}
			catch(Exception exception)
			{
				handleException(this.parent, exception);
			}
			finally
			{
				try
				{
					inputStream.close();
				}
				catch (Exception exception)
				{
					handleException(this.parent, exception);
				}
			}
		}
		
		public void saveFile()
		{
			String filePath = this.getFilePath(false);
			
			if ((filePath == null) || filePath.isEmpty())
			{
				// User has canceled the file operation; abort!
				return;
			}
			
			ObjectOutputStream outputStream = null;
			
			try
			{
				// Use binary file manipulation to export a file containing a Document object.
				outputStream = new ObjectOutputStream(new FileOutputStream(filePath));
				outputStream.writeObject(this.getDocument());
			}
			catch(Exception exception)
			{
				handleException(this.parent, exception);
			}
			finally
			{
				try
				{
					outputStream.close();
				}
				catch (Exception exception)
				{
					handleException(this.parent, exception);
				}
			}
		}
		
		private SimpleAttributeSet setFormatting(Color fgc, Color bgc, boolean isBolded, boolean isItalicized, boolean isUnderlined)
		{
			// Create an attribute set.
			SimpleAttributeSet aset = new SimpleAttributeSet();
			
			// Set styles.
			aset.addAttribute(StyleConstants.CharacterConstants.Bold, isBolded);
			aset.addAttribute(StyleConstants.CharacterConstants.Italic, isItalicized);
			aset.addAttribute(StyleConstants.CharacterConstants.Underline, isUnderlined);
			
			// Set colors.
			aset.addAttribute(StyleConstants.CharacterConstants.Foreground, fgc);
			aset.addAttribute(StyleConstants.CharacterConstants.Background, bgc);
			
			// Return the attribute set for use in text formatting.
			return aset;
		}
	}
	
	/*
		Most of these methods, and truly much of this class, was also written in a previous Java course
		to expedite the creation of a basic main application window object.
	*/
	private static class MainApplicationWindow extends JFrame implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		private boolean           isDebugging      = false;
		private JComboBox<String> cboHeight        = null;
		private JComboBox<String> cboWidth         = null;
		private JButton           btnInput         = null;
		private RichTextPane      rtpOutput        = null;
		
		public MainApplicationWindow(String applicationTitle, boolean isDebugging, int width, int height)
		{
			super(applicationTitle);
			this.setDebugMode(isDebugging);
			this.setSize(new Dimension(width, height));
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			this.drawGUI();
		}
		
		public void actionPerformed(ActionEvent event)
		{
			if (this.getDebugMode())
			{
				JOptionPane.showMessageDialog(this, 
					"What: " + event.getActionCommand() + 
					"\nWhere: " + event.getSource().getClass().getSimpleName() + " (" + event.getSource().getClass().getCanonicalName() + ")" + 
					"\nWhen: " + getDateTimeStamp(), 
					"Event Occurred", 
					JOptionPane.INFORMATION_MESSAGE);
			}
			
			/*
				JDK 7 allows string objects as the expression in a switch statement.
				This generally produces more efficient byte code compared to a chain of if statements.
				http://docs.oracle.com/javase/7/docs/technotes/guides/language/strings-switch.html
			*/
			switch (event.getActionCommand())
			{
				case "Clear":
					
					this.rtpOutput.clear();
					// Reset input fields.
					this.cboWidth.removeAllItems();
					this.cboWidth.setSelectedIndex(-1);
					this.cboHeight.removeAllItems();
					this.cboHeight.setSelectedIndex(-1);
					this.cboHeight.grabFocus();
					break;
				
				case "Open":
					
					this.rtpOutput.openFile();
					break;
				
				case "Save":
					
					this.rtpOutput.saveFile();
					break;
					
				case "Print Rectangle":
					
					printRectangle(this, this.rtpOutput, this.cboHeight, this.cboWidth);
					break;
				
				default:
					
					break;
			}
		}
		
		private void drawGUI()
		{
			Container contentPane = this.getContentPane();
			contentPane.setLayout(new BorderLayout());
			
			JMenu fileMenu = new JMenu("File");
			JMenuItem menu;
			
			menu = new JMenuItem("Clear");
			menu.addActionListener(this);
			fileMenu.add(menu);
			
			menu = new JMenuItem("Open");
			menu.addActionListener(this);
			fileMenu.add(menu);
			
			menu = new JMenuItem("Save");
			menu.addActionListener(this);
			fileMenu.add(menu);
			
			JMenuBar menuBar = new JMenuBar();
			menuBar.add(fileMenu);
			this.setJMenuBar(menuBar);
			
			this.rtpOutput = new RichTextPane(true, this.getDebugMode(), this);
			JScrollPane outputPanel = new JScrollPane(this.rtpOutput);
			
			JPanel inputPanel = new JPanel();
			inputPanel.setLayout(new FlowLayout());
			
			JLabel heightLabel = new JLabel("Enter Height: ");
			this.cboHeight = new JComboBox<String>();
			this.cboHeight.setFont(new Font("Lucida Console", Font.PLAIN, 14));
			this.cboHeight.setPreferredSize(new Dimension(100, 20));
			this.cboHeight.setEditable(true);
			this.cboHeight.addActionListener(this);
			inputPanel.add(heightLabel);
			inputPanel.add(this.cboHeight);
			
			JLabel widthLabel = new JLabel("Enter Width: ");
			this.cboWidth = new JComboBox<String>();
			this.cboWidth.setFont(new Font("Lucida Console", Font.PLAIN, 14));
			this.cboWidth.setPreferredSize(new Dimension(100, 20));
			this.cboWidth.setEditable(true);
			this.cboWidth.addActionListener(this);
			inputPanel.add(widthLabel);
			inputPanel.add(this.cboWidth);
			
			this.btnInput = new JButton("Print Rectangle");
			this.btnInput.setPreferredSize(new Dimension(125, 20));
			this.btnInput.addActionListener(this);
			inputPanel.add(this.btnInput);
			
			contentPane.add(outputPanel, BorderLayout.CENTER);
			contentPane.add(inputPanel, BorderLayout.NORTH);
		}
		
		public boolean getDebugMode()
		{
			return this.isDebugging;
		}
		
		private void setDebugMode(boolean isDebugging)
		{
			this.isDebugging = isDebugging;
		}
	}
	
	private static String getDateTimeStamp()
	{
		/*
			This method was constructed after searching for simple custom date/time formatting.
			Its only downside is that the Date class is deprecated, and may become unavailable
			in the future. I am in the process of working on a better alternative.
		*/
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss a z");
		return dateFormat.format(new Date());
	}
	
	private static void handleException(Component parent, Exception exception)
	{
		/*
			Report error message, complete with some useful debug info.
			Source file is where the error chain ended, which could be null in the case of a function in the Java API.
			Cause file is where the error chain began, which is the bottom of the stack and where the bad method is likely to be.
		*/
		JOptionPane.showMessageDialog(parent,
			exception.toString() + 
			"\n\nSource file: " + exception.getStackTrace()[0].getFileName() +
			"\nLine number: " + exception.getStackTrace()[0].getLineNumber() +
			"\n\nCause file: " + exception.getStackTrace()[exception.getStackTrace().length-1].getFileName() +
			"\nLine number: " + exception.getStackTrace()[exception.getStackTrace().length-1].getLineNumber() +
			"\n\nWhen: " + getDateTimeStamp(),
			"Unhandled Exception",
			JOptionPane.ERROR_MESSAGE);
		exception.printStackTrace();
	}
	
	// This method takes a string and determines if it can be safely parsed as a double.
	// Return value of true indicates that the string is safe to parse, and false means that the string is not safe to parse.
	private static boolean isStringParsedAsDouble(String s)
	{
		try
		{
			// parseDouble throws an exception if the string can't be parsed.
			Double.parseDouble(s);
		}
		catch (Exception exception)
		{
			// If we catch aan exception, then we return false.
			return false;
		}
		
		// Base case; return true if the string was parsed without an exception being thrown.
		return true;
	}
	
	public static void main(String[] args)
	{
		// I am particularly fond of "debug modes" to easily turn diagnostic responses on and off in a simple and elegant manner.
		MainApplicationWindow myMainWindow = null;
		int intChoice = JOptionPane.showConfirmDialog(null, 
			"Do you wish to activate debug mode?\n\n" + 
			"Turning on debug mode will cause diagnostic messages to be displayed during special events that are helpful when debugging this application.\n\n" + 
			"Note: Debug mode has no effect on error messages. Error messages will always be displayed!\n\n" + 
			"Closing this message without choosing \"Yes\" or \"No\" will close this application.", 
			"Debug Mode", 
			JOptionPane.YES_NO_OPTION);
		
		if (intChoice == JOptionPane.YES_OPTION)
		{
			myMainWindow = new MainApplicationWindow("Rectangle Application", true, 800, 600);
		}
		else if (intChoice == JOptionPane.NO_OPTION)
		{
			myMainWindow = new MainApplicationWindow("Rectangle Application", false, 800, 600);
		}
		else
		{
			return;
		}
		
		myMainWindow.setVisible(true);
	}
	
	private static void printRectangle(Component parent, RichTextPane rtpOutput, JComboBox<String> cboHeight,  JComboBox<String> cboWidth)
	{
		// Determine whether or not the height and width fields both contain data.
		boolean isHeightEmpty = ((String)cboHeight.getSelectedItem() == null);
		boolean isWidthEmpty = ((String)cboWidth.getSelectedItem() == null);
		
		if ((!isHeightEmpty) && (!isWidthEmpty))
		{
			// If the height and width fields both contain data, then determine whether or not they can be parsed as doubles.
			if (isStringParsedAsDouble((String)cboHeight.getSelectedItem()) && isStringParsedAsDouble((String)cboWidth.getSelectedItem()))
			{
				double height = Double.parseDouble((String)cboHeight.getSelectedItem());
				double width  = Double.parseDouble((String)cboWidth.getSelectedItem());
				
				Rectangle r = new Rectangle(height, width);
				
				rtpOutput.append(Color.GREEN, Color.WHITE, "[" + getDateTimeStamp() + "] ", 
					Color.BLACK, Color.WHITE, "The value of toString() is ", 
					Color.BLUE, Color.WHITE, "\"" + r.toString() + "\"\n");
				rtpOutput.append(Color.GREEN, Color.WHITE, "[" + getDateTimeStamp() + "] ", 
					Color.BLACK, Color.WHITE, "The area is ",  
					Color.RED, Color.WHITE, r.getArea() + "\n");
				rtpOutput.append(Color.GREEN, Color.WHITE, "[" + getDateTimeStamp() + "] ", 
					Color.BLACK, Color.WHITE, "The perimeter is ", 
					Color.RED, Color.WHITE, r.getPerimeter() + "\n");
				
				if (r.isItSquare())
				{
					rtpOutput.append(Color.GREEN, Color.WHITE, "[" + getDateTimeStamp() + "] ", 
						Color.MAGENTA, Color.WHITE, "This rectangle is a square.\n\n");
				}
				else
				{
					rtpOutput.append(Color.GREEN, Color.WHITE, "[" + getDateTimeStamp() + "] ", 
						Color.MAGENTA, Color.WHITE, "This rectangle is NOT a square.\n\n");
				}
				
				// Add the most recently used height and width values to their respective dropdown lists.
				if (cboHeight.getSelectedIndex() == -1)
				{
					cboHeight.addItem((String)cboHeight.getSelectedItem());
				}
				
				if (cboWidth.getSelectedIndex() == -1)
				{
					cboWidth.addItem((String)cboWidth.getSelectedItem());
				}
				
				// Reset the input fields.
				cboWidth.setSelectedIndex(-1);
				cboHeight.setSelectedIndex(-1);
				cboHeight.grabFocus();
			}
			else
			{
				// If the height and width fields can't be parsed as doubles, inform the user that the application needs numerical data.
				JOptionPane.showMessageDialog(parent, 
					"Both of the data entry fields (height and width) are required to have numerical data.", 
					"Data Entry Error", 
					JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (isHeightEmpty || isWidthEmpty)
		{
			// If either the height or the width field is empty, then inform the user that both fields are required.
			JOptionPane.showMessageDialog(parent, 
				"One of the data entry fields (height or width) has been left blank. Both fields are required to have numerical data.", 
				"Data Entry Error", 
				JOptionPane.ERROR_MESSAGE);
		}
	}
}