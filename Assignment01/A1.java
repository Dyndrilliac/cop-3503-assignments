/*
 * Title: COP 3503 - Assignment 01
 * Author: Matthew Boyette
 * Date: 1/12/2013
 */

package Assignment01;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

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

public class A1
{
	public final static void main(final String[] args)
	{
		new A1(args);
	}

	boolean				isDebugging	= false;
	RichTextPane		output		= null;
	ApplicationWindow	window		= null;

	public A1(final String[] args)
	{
		this.setDebugging(Support.promptDebugMode(this.getWindow()));

		// Define a self-contained ActionListener event handler.
		EventHandler<A1> myActionPerformed = new EventHandler<A1>(this)
			{
			private final static long	serialVersionUID	= 1L;

			@Override
			public final void run(final AWTEvent event)
			{
				ActionEvent actionEvent = (ActionEvent)event;
				A1 parent = this.getParent();

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
			EventHandler<A1> myDrawGUI = new EventHandler<A1>(this)
				{
				private final static long	serialVersionUID	= 1L;

				@Override
				public final void run(final ApplicationWindow window)
				{
					A1 parent = this.getParent();
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

				this.setWindow(new ApplicationWindow(null, "Payroll Application", new Dimension(800, 600), this.isDebugging(),
					true, myActionPerformed, myDrawGUI));
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
		String employeeName = Support.getInputString(this.getWindow(),
			"Please enter this employee's name:",
			"Input Employee Name");

		double hoursWorked = 0, payRate = 0, fedTaxRate = 0, stateTaxRate = 0;

		do
		{
			hoursWorked = Support.getDoubleInputString(this.getWindow(),
				"Please enter the number of hours this employee worked:",
				"Input Hours Worked");
		}
		while (hoursWorked == 0);

		do
		{
			payRate = Support.getDoubleInputString(this.getWindow(),
				"Please enter this employee's hourly pay rate:",
				"Input Pay Rate");
		}
		while (payRate == 0);

		do
		{
			fedTaxRate = Support.getDoubleInputString(this.getWindow(),
				"Please enter the Federal income tax rate:",
				"Input Federal Tax Rate");
		}
		while (fedTaxRate == 0);

		do
		{
			stateTaxRate = Support.getDoubleInputString(this.getWindow(),
				"Please enter the State income tax rate:",
				"Input State Tax Rate");
		}
		while (stateTaxRate == 0);

		this.showOutput(employeeName, hoursWorked, payRate, fedTaxRate, stateTaxRate);
	}

	protected void showOutput(final String employeeName, final double hoursWorked, final double payRate, final double fedTaxRate,
		final double stateTaxRate)
	{
		DecimalFormat twoDecimalPlaces = new DecimalFormat("##.##");
		double grossPay = payRate * hoursWorked;
		double totalDeduction = (fedTaxRate * grossPay) + (stateTaxRate * grossPay);
		double netPay = grossPay - totalDeduction;

		this.getOutput().append(Color.BLACK, Color.WHITE, "Employee Name:  " + employeeName + "\n",
			Color.BLACK, Color.WHITE, "Hours Worked:  " + Double.valueOf(twoDecimalPlaces.format(hoursWorked)) + "\n",
			Color.BLACK, Color.WHITE, "Pay Rate:  $" + Double.valueOf(twoDecimalPlaces.format(payRate)) + "\n",
			Color.BLACK, Color.WHITE, "Gross Pay:  $" + Double.valueOf(twoDecimalPlaces.format(grossPay)) + "\n",
			Color.RED, Color.WHITE, "Deductions:\n",
			Color.RED, Color.WHITE, "\tFederal Withholding (" + Double.valueOf(twoDecimalPlaces.format((fedTaxRate * 100))) + "%):  $" +
				Double.valueOf(twoDecimalPlaces.format((fedTaxRate * grossPay))) + "\n",
				Color.RED, Color.WHITE, "\tState Withholding (" + Double.valueOf(twoDecimalPlaces.format((stateTaxRate * 100))) + "%):  $" +
					Double.valueOf(twoDecimalPlaces.format((stateTaxRate * grossPay))) + "\n",
					Color.RED, Color.WHITE, "\tTotal Deduction:  $" + Double.valueOf(twoDecimalPlaces.format(totalDeduction)) + "\n",
					Color.BLACK, Color.WHITE, "Net Pay:  $" + Double.valueOf(twoDecimalPlaces.format(netPay)) + "\n\n");
	}
}