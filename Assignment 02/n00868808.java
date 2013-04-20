/*
	Author: Matthew Boyette
	Date:   1/23/2013
*/

/*
	Problem 4.23 Loop Pseudocode
		
		initialize an integer variable called 'x' with a value of one.
		
		while 'x' is less than or equal to the variable 'problemArg', do this:
			
			set the variable 'ltrSum' equal to the sum of the total and the quotient of one divided by 'x'.
			
		then, increment the variable 'x' by one and repeat.
		
		
		
		initialize an integer variable called 'j' with a value of 'problemArg'.
		
		while 'j' is greater than or equal to one, do this:
			
			set the variable 'rtlSum' equal to the sum of the total and the quotient of one divided by 'j'.
			
		then, decrement the variable 'j' by one and repeat.
		
	Problem 4.23 Loop Code
		
		// Get the left-to-right sum.
		for (int x = 1; x <= problemArg; x++)
		{
			ltrSum += (1/((double)x));
		}
		
		// Get the right-to-left sum.
		for (int j = problemArg; j >= 1; j--)
		{
			rtlSum += (1/((double)j));
		}
*/

/*
	Problem 4.25 Loop Pseudocode
	
		initialize an integer variable called 'i' with a value of one.
		
		while 'i' is less than or equal to the sum of the variable 'problemArg' and one, do this:
			
			initialize a double variable called 'numerator' with the value of negative one raised exponentially to the sum of 'i' and one.
			
			initialize a double variable called 'denominator' with the sum of the product of two times 'i' and negative one.
			
			set the variable 'sum' equal to the sum of the total and the quotient of 'numerator' divided by 'denominator'.
			
		then, increment 'i' by one and repeat.
	
	Problem 4.25 Loop Code
	
		for (int i = 1; i <= (problemArg+1); i++)
		{
			double numerator   = Math.pow(-1.0, (double)(i+1)); // numerator   = ((-1)^(i+1))
			double denominator = ((2.0 * (double)i) - 1.0);     // denominator = ((2i)-1) 
			sum += (numerator / denominator);
		}
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
		
		public boolean getDebugMode()
		{
			return this.isDebugging;
		}
		
		private String getFilePath(boolean isOpen)
		{
			String  filePath = null;
			
			// Loop while stopFlag equals false, post-test.
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
			while (stopFlag == false);
			
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
					
				case "Click here to input data!!":
					
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
			
			this.btnInput = new JButton("Click here to input data!!");
			this.btnInput.setPreferredSize(new Dimension(175, 20));
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
			e.toString() + 
			"\n\nSource file: " + e.getStackTrace()[0].getFileName() +
			"\nLine number: " + e.getStackTrace()[0].getLineNumber() +
			"\n\nCause file: " + e.getStackTrace()[e.getStackTrace().length-1].getFileName() +
			"\nLine number: " + e.getStackTrace()[e.getStackTrace().length-1].getLineNumber() +
			"\n\nWhen: " + getDateTimeStamp(),
			"Unhandled Exception",
			JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
		System.exit(-1);
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
			myMainWindow = new MainApplicationWindow("Summation Application", true, 640, 480);
		}
		else if (intChoice == JOptionPane.NO_OPTION)
		{
			myMainWindow = new MainApplicationWindow("Summation Application", false, 640, 480);
		}
		else
		{
			return;
		}
		
		myMainWindow.setVisible(true);
	}
	
	private static void problem23(RichTextPane rtpOutput, int problemArg)
	{
		double ltrSum = 0;
		double rtlSum = 0;
		
		// Get the left-to-right sum.
		for (int x = 1; x <= problemArg; x++)
		{
			ltrSum += (1/((double)x));
			if (rtpOutput.getDebugMode())
			{
				rtpOutput.append(Color.BLUE, Color.WHITE, "\tLTR Sum("+ x +"): " + ltrSum + "\n");
			}
		}
		
		// This is just to make the debug mode output easier to read.
		if (rtpOutput.getDebugMode())
		{
			rtpOutput.append(Color.BLUE, Color.WHITE, "\n");
		}
		
		// Get the right-to-left sum.
		for (int j = problemArg; j >= 1; j--)
		{
			rtlSum += (1/((double)j));
			if (rtpOutput.getDebugMode())
			{
				rtpOutput.append(Color.BLUE, Color.WHITE, "\tRTL Sum("+ j +"): " + rtlSum + "\n");
			}
		}
		
		// This is just to make the debug mode output easier to read.
		if (rtpOutput.getDebugMode())
		{
			rtpOutput.append(Color.BLUE, Color.WHITE, "\n");
		}
		
		rtpOutput.append(Color.BLACK, Color.WHITE, "\tAnswer (Left-to-Right): " + ltrSum + "\n");
		rtpOutput.append(Color.BLACK, Color.WHITE, "\tAnswer (Right-to-Left): " + rtlSum + "\n\n");
	}
	
	private static void problem25(RichTextPane rtpOutput, int problemArg)
	{
		// Approximate pi using the Leibniz series given an arbitrary number of iterations.
		double sum = 0;
		
		for (int i = 1; i <= (problemArg+1); i++)
		{
			double numerator   = Math.pow(-1.0, (double)(i+1)); // numerator   = ((-1)^(i+1))
			double denominator = ((2.0 * (double)i) - 1.0);     // denominator = ((2i)-1) 
			sum += (numerator / denominator);
			if (rtpOutput.getDebugMode())
			{
				rtpOutput.append(Color.BLUE, Color.WHITE, "\tNumerator("+ i +"): " + numerator + "\n");
				rtpOutput.append(Color.BLUE, Color.WHITE, "\tDenominator("+ i +"): " + denominator + "\n");
				rtpOutput.append(Color.BLUE, Color.WHITE, "\tSum("+ i +"): " + sum + "\n");
				rtpOutput.append(Color.BLUE, Color.WHITE, "\tAnswer("+ i +"): " + (sum * 4) + "\n\n");
			}
		}
		
		/*
			Summing the Leibniz series yields the same value as arctan(1), which is pi/4. Therefore to get pi we have to multiply the sum by 4.
			This series converges very slowly. You can only accurately calculate pi to many digits with a very large number of iterations.
			As the number of iterations approaches positive infinity, the answer becomes more accurate.
		*/
		
		rtpOutput.append(Color.BLACK, Color.WHITE, "\tAnswer: " + (sum * 4) + "\n\n");
	}
	
	private static void showInput(Component parent, RichTextPane rtpOutput)
	{
		String inputString = null;
		int    problemNum  = 0;
		int    problemArg  = 0;
		
		do
		{
			// Take input from the user. Repeat this process until the user gives valid data.
			do
			{
				inputString = JOptionPane.showInputDialog(parent, 
					"Note: Commands that generate output must be entered as \"number1 number2\" where number1 is the problem number you wish to solve (23 or 25), and\n" + 
					"number2 is the parameter for that problem. You may enter the word \"stop\" at any time to halt program input. The stop command is not case sensitive.\n\n" + 
					"Example #1: 23 5000\nExample #2: 25 10000\nExample #3: stop\n\n" + 
					"Please enter the desired command:", 
					"Input Command", 
					JOptionPane.QUESTION_MESSAGE);
			} while (validateInput(inputString) == false);
			
			if (inputString.equalsIgnoreCase("stop") == false)
			{
				// Split the command string into two substrings.
				String substrings[] = inputString.split(" ", 2);
				
				// Convert the substrings into integers.
				problemNum = Integer.parseInt(substrings[0]);
				problemArg = Integer.parseInt(substrings[1]);
				
				// Pass the data to the output routine.
				showOutput(parent, rtpOutput, problemNum, problemArg);
			}
		} while (inputString.equalsIgnoreCase("stop") == false);
	}
	
	private static void showOutput(Component parent, RichTextPane rtpOutput, int problemNum, int problemArg)
	{
		// First, echo the given command back to the user.
		rtpOutput.append(Color.BLACK, Color.WHITE, getDateTimeStamp() + ": ", Color.BLACK, Color.WHITE, problemNum + " " + problemArg + "\n\n");
		
		// Decide which problem to solve.
		switch (problemNum)
		{
			case 23:
				
				// Solve problem 23.
				problem23(rtpOutput, problemArg);
				break;
				
			case 25:
				
				// Solve problem 25.
				problem25(rtpOutput, problemArg);
				break;
				
			default:
				
				handleException(parent, new Exception("The given problem number is invalid. The only valid problem numbers are 23 and 25. The application will now exit."));
				break;
		}
	}
	
	private static boolean validateInput(String inputString)
	{
		if ((inputString == null) || inputString.isEmpty())
		{
			return false;
		}
		
		if (inputString.equalsIgnoreCase("stop"))
		{
			return true;
		}
		
		// Use a regular expression to pattern match the input string and validate it for further processing.
		return inputString.matches("[0-9]+ [0-9]+");
	}
}