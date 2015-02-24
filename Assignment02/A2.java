/*
 * Title: COP 3503 - Assignment 02
 * Author: Matthew Boyette
 * Date: 1/23/2013
 */

/*
 * Problem 4.23 Loop Pseudocode
 *
 * initialize an integer variable called 'x' with a value of one.
 * while 'x' is less than or equal to the variable 'problemArg', do this:
 * set the variable 'ltrSum' equal to the sum of the total and the quotient of one divided by 'x'.
 * then, increment the variable 'x' by one and repeat.
 *
 * initialize an integer variable called 'j' with a value of 'problemArg'.
 * while 'j' is greater than or equal to one, do this:
 * set the variable 'rtlSum' equal to the sum of the total and the quotient of one divided by 'j'.
 * then, decrement the variable 'j' by one and repeat.
 *
 * Problem 4.23 Loop Code
 *
 * // Get the left-to-right sum.
 * for (int x = 1; x <= problemArg; x++)
 * {
 * ltrSum += (1/((double)x));
 * }
 *
 * // Get the right-to-left sum.
 * for (int j = problemArg; j >= 1; j--)
 * {
 * rtlSum += (1/((double)j));
 * }
 */

/*
 * Problem 4.25 Loop Pseudocode
 *
 * initialize an integer variable called 'i' with a value of one.
 * while 'i' is less than or equal to the sum of the variable 'problemArg' and one, do this:
 * initialize a double variable called 'numerator' with the value of negative one raised exponentially to the sum of 'i' and one.
 * initialize a double variable called 'denominator' with the sum of the product of two times 'i' and negative one.
 * set the variable 'sum' equal to the sum of the total and the quotient of 'numerator' divided by 'denominator'.
 * then, increment 'i' by one and repeat.
 *
 * Problem 4.25 Loop Code
 *
 * for (int i = 1; i <= (problemArg+1); i++)
 * {
 * double numerator = Math.pow(-1.0, (double)(i+1)); // numerator = ((-1)^(i+1))
 * double denominator = ((2.0 * (double)i) - 1.0); // denominator = ((2i)-1)
 * sum += (numerator / denominator);
 * }
 */

package Assignment02;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import api.gui.swing.ApplicationWindow;
import api.gui.swing.RichTextPane;
import api.util.EventHandler;
import api.util.Support;

public class A2
{
	public final static void main(final String[] args)
	{
		new A2(args);
	}

	protected static void problem23(final RichTextPane rtpOutput, final int problemArg)
	{
		double ltrSum = 0;
		double rtlSum = 0;

		// Get the left-to-right sum.
		for (int x = 1; x <= problemArg; x++)
		{
			ltrSum += (1 / ((double)x));

			if (rtpOutput.isDebugging())
			{
				rtpOutput.append(Color.BLUE, Color.WHITE, "\tLTR Sum(" + x + "): " + ltrSum + "\n");
			}
		}

		// This is just to make the debug mode output easier to read.
		if (rtpOutput.isDebugging())
		{
			rtpOutput.append(Color.BLUE, Color.WHITE, "\n");
		}

		// Get the right-to-left sum.
		for (int j = problemArg; j >= 1; j--)
		{
			rtlSum += (1 / ((double)j));

			if (rtpOutput.isDebugging())
			{
				rtpOutput.append(Color.BLUE, Color.WHITE, "\tRTL Sum(" + j + "): " + rtlSum + "\n");
			}
		}

		// This is just to make the debug mode output easier to read.
		if (rtpOutput.isDebugging())
		{
			rtpOutput.append(Color.BLUE, Color.WHITE, "\n");
		}

		rtpOutput.append(Color.BLACK, Color.WHITE, "\tAnswer (Left-to-Right): " + ltrSum + "\n",
			Color.BLACK, Color.WHITE, "\tAnswer (Right-to-Left): " + rtlSum + "\n\n");
	}

	protected static void problem25(final RichTextPane rtpOutput, final int problemArg)
	{
		// Approximate pi using the Leibniz series given an arbitrary number of iterations.
		double sum = 0;

		for (int i = 1; i <= (problemArg + 1); i++)
		{
			double numerator = Math.pow(-1.0, i + 1); // numerator = ((-1)^(i+1))
			double denominator = ((2.0 * i) - 1.0);     // denominator = ((2i)-1)
			sum += (numerator / denominator);

			if (rtpOutput.isDebugging())
			{
				rtpOutput.append(Color.BLUE, Color.WHITE, "\tNumerator(" + i + "): " + numerator + "\n",
					Color.BLUE, Color.WHITE, "\tDenominator(" + i + "): " + denominator + "\n",
					Color.BLUE, Color.WHITE, "\tSum(" + i + "): " + sum + "\n",
					Color.BLUE, Color.WHITE, "\tAnswer(" + i + "): " + (sum * 4) + "\n\n");
			}
		}

		/*
		 * Summing the Leibniz series yields the same value as arctan(1), which is pi/4. Therefore to get pi we have to multiply the sum by 4.
		 * This series converges very slowly. You can only accurately calculate pi to many digits with a very large number of iterations.
		 * As the number of iterations approaches positive infinity, the answer becomes more accurate.
		 */
		rtpOutput.append(Color.BLACK, Color.WHITE, "\tAnswer: " + (sum * 4) + "\n\n");
	}

	boolean				isDebugging	= false;
	RichTextPane		output		= null;
	ApplicationWindow	window		= null;

	public A2(final String[] args)
	{
		this.setDebugging(Support.promptDebugMode(this.getWindow()));

		// Define a self-contained ActionListener event handler.
		EventHandler<A2> myActionPerformed = new EventHandler<A2>(this)
			{
			private final static long	serialVersionUID	= 1L;

			@Override
			public final void run(final AWTEvent event)
			{
				ActionEvent actionEvent = (ActionEvent)event;
				A2 parent = this.getParent();

				if (parent.getOutput() != null)
				{
					/*
					 * JDK 7 allows string objects as the expression in a switch statement.
					 * This generally produces more efficient byte code compared to a chain of if statements.
					 * http://docs.oracle.com/javase/7/docs/technotes/guides/language/strings-switch.html
					 */
					switch (actionEvent.getActionCommand())
					{
						case "Clear":

							parent.getOutput().clear();
							break;

						case "Open":

							parent.getOutput().openOrSaveFile(true);
							break;

						case "Save":

							parent.getOutput().openOrSaveFile(false);
							break;

						case "Input":

							parent.showInput();
							break;
					}
				}
			}
			};

			// Define a self-contained interface construction event handler.
			EventHandler<A2> myDrawGUI = new EventHandler<A2>(this)
				{
				private final static long	serialVersionUID	= 1L;

				@Override
				public final void run(final ApplicationWindow window)
				{
					A2 parent = this.getParent();
					Container contentPane = window.getContentPane();
					JMenuBar menuBar = new JMenuBar();
					JMenu fileMenu = new JMenu("File");
					JMenuItem clearOption = new JMenuItem("Clear");
					JMenuItem openOption = new JMenuItem("Open");
					JMenuItem saveOption = new JMenuItem("Save");
					RichTextPane outputBox = new RichTextPane(window, true, parent.isDebugging());
					JScrollPane outputPanel = new JScrollPane(outputBox);
					JPanel inputPanel = new JPanel();
					JButton btnInput = new JButton("Input");

					contentPane.setLayout(new BorderLayout());
					clearOption.setFont(Support.DEFAULT_TEXT_FONT);
					clearOption.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.Event.CTRL_MASK));
					clearOption.setMnemonic('C');
					clearOption.addActionListener(window);
					openOption.setFont(Support.DEFAULT_TEXT_FONT);
					openOption.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
					openOption.setMnemonic('O');
					openOption.addActionListener(window);
					saveOption.setFont(Support.DEFAULT_TEXT_FONT);
					saveOption.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
					saveOption.setMnemonic('S');
					saveOption.addActionListener(window);
					fileMenu.setFont(Support.DEFAULT_TEXT_FONT);
					fileMenu.setMnemonic('F');
					fileMenu.add(clearOption);
					fileMenu.add(openOption);
					fileMenu.add(saveOption);
					menuBar.setFont(Support.DEFAULT_TEXT_FONT);
					menuBar.add(fileMenu);
					window.setJMenuBar(menuBar);
					parent.setOutput(outputBox);
					inputPanel.setLayout(new FlowLayout());
					btnInput.addActionListener(window);
					inputPanel.add(btnInput);
					contentPane.add(outputPanel, BorderLayout.CENTER);
					contentPane.add(inputPanel, BorderLayout.SOUTH);
				}
				};

				this.setWindow(new ApplicationWindow(null, "Summation Application", new Dimension(800, 600),
					this.isDebugging(), true, myActionPerformed, myDrawGUI));
	}

	public final RichTextPane getOutput()
	{
		return this.output;
	}

	public final ApplicationWindow getWindow()
	{
		return this.window;
	}

	public final boolean isDebugging()
	{
		return this.isDebugging;
	}

	protected final void setDebugging(final boolean debugMode)
	{
		this.isDebugging = debugMode;
	}

	protected final void setOutput(final RichTextPane output)
	{
		this.output = output;
	}

	protected final void setWindow(final ApplicationWindow window)
	{
		this.window = window;
	}

	public void showInput()
	{
		String inputString = null;
		int problemNum = 0;
		int problemArg = 0;

		do
		{
			// Take input from the user. Repeat this process until the user gives valid data.
			do
			{
				inputString =
					Support
					.getInputString(
						this.getWindow(),
						"Note: Commands that generate output must be entered as \"number1 number2\" where number1 is the problem number you wish to solve (23 or 25), and\n"
							+
							"number2 is the parameter for that problem. You may enter the word \"stop\" at any time to halt program input. The stop command is not case sensitive.\n\n"
							+
							"Example #1: 23 5000\nExample #2: 25 10000\nExample #3: stop\n\n"
							+
							"Please enter the desired command:",
						"Input Command");
			}
			while (this.validateInput(inputString) == false);

			if (inputString.equalsIgnoreCase("stop") == false)
			{
				// Split the command string into two substrings.
				String substrings[] = inputString.split(" ", 2);

				// Convert the substrings into integers.
				problemNum = Integer.parseInt(substrings[0]);
				problemArg = Integer.parseInt(substrings[1]);

				// Pass the data to the output routine.
				this.showOutput(problemNum, problemArg);
			}
		}
		while (inputString.equalsIgnoreCase("stop") == false);
	}

	protected void showOutput(final int problemNum, final int problemArg)
	{
		// First, echo the given command back to the user.
		this.getOutput().append(Color.BLACK, Color.WHITE, "[" + Support.getDateTimeStamp() + "]: ",
			Color.BLACK, Color.WHITE, problemNum + " " + problemArg + "\n\n");

		// Decide which problem to solve.
		switch (problemNum)
		{
			case 23:

				// Solve problem 23.
				A2.problem23(this.getOutput(), problemArg);
				break;

			case 25:

				// Solve problem 25.
				A2.problem25(this.getOutput(), problemArg);
				break;

			default:

				Support.displayException(this.getWindow(),
					new Exception("The given problem number is invalid. The only valid problem numbers are 23 and 25."),
					true);
				break;
		}
	}

	protected boolean validateInput(final String inputString)
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