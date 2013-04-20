/*
	Author: Matthew Boyette
	Date:   1/12/2013
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
	/*
		I wrote this class in a previous Java course because I was not satisfied with JTextPane,
		and lusted after a text box object with much richer features like the RichTextBox control
		available in Visual Basic.NET and C#. Thus, RichTextPane was born.
	*/
	private static class RichTextPane extends JTextPane
	{
		private Component parent      = null;
		private boolean   isDebugging = false;
		
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
				handleException(this.parent, new Exception("Variable append method received incorrect number of arguments."));
			}
			
			// Call the append() helper method for each set of arguments.
			for (int i = 0; i < oArgs.length; i += 3) { this.append((Color)oArgs[i], (Color)oArgs[i+1], oArgs[i+2].toString()); }
		}
		
		private void append(Color fgc, Color bgc, String s)
		{
			// Call setFormatting to set the foreground and background colors for the text.
			SimpleAttributeSet aset = setFormatting(fgc, bgc, false, false, false); 
			
			try
			{
				// Append a string to the current document using the desired attribute set.
				this.getDocument().insertString(this.getDocument().getLength(), s, aset);
			}
			catch (Exception e)
			{
				handleException(this.parent, e);
			}
		}
		
		public void clear()
		{
			this.setDocument(new DefaultStyledDocument());
			this.setFont(new Font("Lucida Console", Font.PLAIN, 14));
		}
		
		private String getFilePath(boolean isOpen)
		{
			String  filePath = null;
			
			do
			{
				try
				{
					filePath = JOptionPane.showInputDialog(this.parent, 
							"Please enter the absolute file path:", 
							"Input File Path", 
							JOptionPane.QUESTION_MESSAGE);
				}
				catch (Exception e)
				{
					filePath = null;
				}
			} while ((filePath == null) || filePath.isEmpty());
			
			/* Loop while stopFlag equals false, post-test.
			boolean stopFlag = false;
			
			do
			{
				JFileChooser fileDialog = new JFileChooser();
				int intChoice = 0;
				
				if (isOpen)
				{
					intChoice = fileDialog.showOpenDialog(this.parent);
				}
				else
				{
					intChoice = fileDialog.showSaveDialog(this.parent);
				}
				
				switch (intChoice)
				{
					case JFileChooser.APPROVE_OPTION:
						
						try
						{
							filePath = fileDialog.getSelectedFile().getCanonicalPath();
							stopFlag = true;
						}
						catch (Exception  e)
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
			while (stopFlag == false);*/
			
			if (this.isDebugging)
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
				inputStream.close();
			}
			catch(Exception e)
			{
				handleException(this.parent, e);
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
				outputStream.close();
			}
			catch(Exception e)
			{
				handleException(this.parent, e);
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
		private boolean      isDebugging = false;
		private JButton      btnInput    = null;
		private RichTextPane rtpOutput   = null;
		
		public MainApplicationWindow(String strAppTitle, boolean isDebugging, int intWidth, int intHeight)
		{
			super(strAppTitle);
			this.setDebugMode(isDebugging);
			this.setSize(new Dimension(intWidth, intHeight));
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			this.drawGUI();
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if (this.getDebugMode())
			{
				JOptionPane.showMessageDialog(this, 
											"What: " + e.getActionCommand() + 
											"\nWhere: " + e.getSource().getClass().getSimpleName() + " (" + e.getSource().getClass().getCanonicalName() + ")" + 
											"\nWhen: " + getDateTimeStamp(), 
											"Event Occurred", 
											JOptionPane.INFORMATION_MESSAGE);
			}
			
			/*
				JDK 7 allows string objects as the expression in a switch statement.
				This generally produces more efficient byte code compared to a chain of if statements.
				http://docs.oracle.com/javase/7/docs/technotes/guides/language/strings-switch.html
			*/
			switch (e.getActionCommand())
			{
				case "Clear":
					this.rtpOutput.clear();
					break;
				
				case "Open":
					this.rtpOutput.openFile();
					break;
				
				case "Save":
					this.rtpOutput.saveFile();
					break;
					
				case "Input":
					showInput(this, this.rtpOutput);
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
			
			this.btnInput = new JButton("Input");
			this.btnInput.setPreferredSize(new Dimension(100, 20));
			this.btnInput.addActionListener(this);
			inputPanel.add(this.btnInput);
			
			contentPane.add(outputPanel, BorderLayout.CENTER);
			contentPane.add(inputPanel, BorderLayout.SOUTH);
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
	
	private static void handleException(Component parent, Exception e)
	{
		/*
			Report error message, complete with some useful debug info.
			Source file is where the error chain ended, which could be null in the case of a function in the Java API.
			Cause file is where the error chain began, which is the bottom of the stack and where the bad method is likely to be.
		*/
		JOptionPane.showMessageDialog(parent,
									e.toString() + "\n" +
									"\nSource file: " + e.getStackTrace()[0].getFileName() +
									"\nLine number: " + e.getStackTrace()[0].getLineNumber() +
									"\nCause file: " + e.getStackTrace()[e.getStackTrace().length-1].getFileName() +
									"\nLine number: " + e.getStackTrace()[e.getStackTrace().length-1].getLineNumber() +
									"\nWhen: " + getDateTimeStamp(),
									"Unhandled Exception",
									JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
		System.exit(-1);
	}
	
	public static void main(String[] args)
	{
		/*
			I am particularly fond of "debug modes" to easily turn diagnostic responses on and off in a simple
			and elegant manner.
		*/
		MainApplicationWindow myMainWindow = null;
		int intChoice = JOptionPane.showConfirmDialog(null, 
														"Do you wish to activate debug mode?\n\nClosing this message without choosing will close the application.", 
														"Debug Mode", 
														JOptionPane.YES_NO_OPTION);
		
		if (intChoice == JOptionPane.YES_OPTION)
		{
			myMainWindow = new MainApplicationWindow("Payroll Application", true, 640, 480);
		}
		else if (intChoice == JOptionPane.NO_OPTION)
		{
			myMainWindow = new MainApplicationWindow("Payroll Application", false, 640, 480);
		}
		else
		{
			return;
		}
		
		myMainWindow.setVisible(true);
	}
	
	private static void showInput(Component parent, RichTextPane rtpOutput)
	{
		String employeeName = null;
		String tempBuffer   = null;
		double hoursWorked  = 0;
		double payRate      = 0;
		double fedTaxRate   = 0;
		double stateTaxRate = 0;
		
		do
		{
			employeeName = JOptionPane.showInputDialog(parent, 
					"Please enter this employee's name:", 
					"Input Employee Name", 
					JOptionPane.QUESTION_MESSAGE);
		} while ((employeeName == null) || employeeName.isEmpty());
		
		do
		{
			tempBuffer = JOptionPane.showInputDialog(parent, 
					"Please enter the number of hours this employee worked:", 
					"Input Hours Worked", 
					JOptionPane.QUESTION_MESSAGE);
			if ((tempBuffer != null) && (tempBuffer.isEmpty() == false))
			{
				hoursWorked = Double.parseDouble(tempBuffer);
			}
		} while (hoursWorked == 0);
		
		do
		{
			tempBuffer = JOptionPane.showInputDialog(parent, 
					"Please enter this employee's hourly pay rate:", 
					"Input Pay Rate", 
					JOptionPane.QUESTION_MESSAGE);
			if ((tempBuffer != null) && (tempBuffer.isEmpty() == false))
			{
				payRate = Double.parseDouble(tempBuffer);
			}
		} while (payRate == 0);
		
		do
		{
			tempBuffer = JOptionPane.showInputDialog(parent, 
					"Please enter the Federal income tax rate:", 
					"Input Federal Tax Rate", 
					JOptionPane.QUESTION_MESSAGE);
			if ((tempBuffer != null) && (tempBuffer.isEmpty() == false))
			{
				fedTaxRate = Double.parseDouble(tempBuffer);
			}
		} while (fedTaxRate == 0);
		
		do
		{
			tempBuffer = JOptionPane.showInputDialog(parent, 
					"Please enter the State income tax rate:", 
					"Input State Tax Rate", 
					JOptionPane.QUESTION_MESSAGE);
			if ((tempBuffer != null) && (tempBuffer.isEmpty() == false))
			{
				stateTaxRate = Double.parseDouble(tempBuffer);
			}
		} while (stateTaxRate == 0);
		
		showOutput(rtpOutput, employeeName, hoursWorked, payRate, fedTaxRate, stateTaxRate);
	}
	
	private static void showOutput(RichTextPane rtpOutput, String employeeName, double hoursWorked, double payRate, double fedTaxRate, double stateTaxRate)
	{
		DecimalFormat twoDecimalPlaces = new DecimalFormat("##.##");
		
		double grossPay       = payRate * hoursWorked;
		double totalDeduction = (fedTaxRate * grossPay) + (stateTaxRate *  grossPay);
		double netPay         = grossPay - totalDeduction;
		
		rtpOutput.append(Color.BLACK, Color.WHITE, "Employee Name:  " + employeeName + "\n");
		rtpOutput.append(Color.BLACK, Color.WHITE, "Hours Worked:  " + Double.valueOf(twoDecimalPlaces.format(hoursWorked)) + "\n");
		rtpOutput.append(Color.BLACK, Color.WHITE, "Pay Rate:  $" + Double.valueOf(twoDecimalPlaces.format(payRate)) + "\n");
		rtpOutput.append(Color.BLACK, Color.WHITE, "Gross Pay:  $" + Double.valueOf(twoDecimalPlaces.format(grossPay)) + "\n");
		rtpOutput.append(Color.RED, Color.WHITE, "Deductions:\n");
		rtpOutput.append(Color.RED, Color.WHITE, "\tFederal Withholding (" + Double.valueOf(twoDecimalPlaces.format((fedTaxRate * 100))) + "%):  $" + Double.valueOf(twoDecimalPlaces.format((fedTaxRate * grossPay))) + "\n");
		rtpOutput.append(Color.RED, Color.WHITE, "\tState Withholding (" + Double.valueOf(twoDecimalPlaces.format((stateTaxRate * 100))) + "%):  $" + Double.valueOf(twoDecimalPlaces.format((stateTaxRate *  grossPay))) + "\n");
		rtpOutput.append(Color.RED, Color.WHITE, "\tTotal Deduction:  $" + Double.valueOf(twoDecimalPlaces.format(totalDeduction)) + "\n");
		rtpOutput.append(Color.BLACK, Color.WHITE, "Net Pay:  $" + Double.valueOf(twoDecimalPlaces.format(netPay)) + "\n");
	}
}