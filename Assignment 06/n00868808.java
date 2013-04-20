/*
	Author: Matthew Boyette
	Date:   3/25/2013
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.*;

public class n00868808
{
	public static final class ApplicationWindow extends JFrame implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		private List<Component>   elements         = new ArrayList<Component>();
		private EventHandler      actionPerformed  = null;
		private EventHandler      drawGUI          = null;
		private boolean           isDebugging      = false;
		
		public ApplicationWindow(final Component parent, final String applicationTitle, final Dimension size, final boolean isDebugging, 
			final boolean isResizable, final boolean isPrimary, final EventHandler actionPerformed, final EventHandler drawGUI)
		{
			super(applicationTitle);
			this.setDebugging(isDebugging);
			this.setSize(size);
			this.setResizable(isResizable);
			this.setLocationRelativeTo(parent);
			
			if (isPrimary)
			{
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			else
			{
				this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
			
			this.setActionPerformed(actionPerformed);
			this.setDrawGUI(drawGUI);
		}
		
		public final void actionPerformed(final ActionEvent event)
		{
			if (this.isDebugging())
			{
				Support.displayDebugMessage(this, event);
			}
			
			if (this.getActionPerformed() != null)
			{
				try // This could be code supplied by a third-party in another module.
				{   // Execute it in a try/catch block to be safe.
					this.getActionPerformed().run(event, this);
				}
				catch (Exception exception)
				{
					Support.displayException(this, exception, true);
				}
			}
		}
		
		public final void drawGUI()
		{
			if (this.getDrawGUI() != null)
			{
				try // This could be code supplied by a third-party in another module.
				{   // Execute it in a try/catch block to be safe.
					this.getDrawGUI().run(this);
				}
				catch (Exception exception)
				{
					Support.displayException(this, exception, true);
				}
			}
		}
		
		public final EventHandler getActionPerformed()
		{
			return this.actionPerformed;
		}
		
		public final EventHandler getDrawGUI()
		{
			return this.drawGUI;
		}
		
		public final List<Component> getElements()
		{
			return this.elements;
		}
		
		public final boolean isDebugging()
		{
			return this.isDebugging;
		}
		
		public final void setActionPerformed(final EventHandler actionPerformed)
		{
			this.actionPerformed = actionPerformed;
		}
		
		public final void setDebugging(final boolean isDebugging)
		{
			this.isDebugging = isDebugging;
		}
		
		public final void setDrawGUI(final EventHandler drawGUI)
		{
			this.drawGUI = drawGUI;
		}
		
		public final void setElements(final List<Component> elements)
		{
			this.elements = elements;
		}
	}
	
	public static final class RichTextPane extends JTextPane
	{
		private static final long serialVersionUID = 1L;
		private Component         parent           = null;
		private boolean           isDebugging      = false;
		
		public RichTextPane(final Component parent, final boolean isReadOnly, final boolean isDebugging, final Font defaultFont)
		{
			this.setFocusable(!isReadOnly);
			this.setEditable(!isReadOnly);
			this.setFont(defaultFont);
			this.isDebugging = isDebugging;
			this.parent = parent;
			this.clear();
		}
		
		public final void append(final Object... arguments) throws IllegalArgumentException
		{
			/*
				The append() helper method takes three arguments. In order for variable number of arguments
				to work, the number of arguments must be evenly divisible by three and they must also be in
				the correct order: Color, Color, String
			*/
			if ((arguments.length % 3) != 0)
			{
				throw new IllegalArgumentException("The variable argument append method received a number of arguments not evenly divisible by three.");
			}

			// Call the append() helper method for each set of arguments.
			for (int i = 0; i < arguments.length; i += 3)
			{
				this.append((Color)arguments[i], (Color)arguments[i+1], arguments[i+2].toString());
			}
		}
		
		public final void append(final Color fgc, final Color bgc, final String string)
		{
			// Call getAttributeSet to generate the formatting settings for the text.
			SimpleAttributeSet attributeSet = getAttributeSet(fgc, bgc, false, false, false);

			try
			{
				// Append a string to the current document using the desired attribute set.
				this.getDocument().insertString(this.getDocument().getLength(), string, attributeSet);
			}
			catch (Exception exception)
			{
				Support.displayException(this.parent, exception, true);
			}
		}
		
		public final void clear()
		{
			this.setDocument(new DefaultStyledDocument());
		}
		
		public static final SimpleAttributeSet getAttributeSet(final Color fgc, final Color bgc, final boolean isBolded, 
			final boolean isItalicized, final boolean isUnderlined)
		{
			// Create an attribute set.
			SimpleAttributeSet attributeSet = new SimpleAttributeSet();

			// Set styles.
			attributeSet.addAttribute(StyleConstants.CharacterConstants.Bold, isBolded);
			attributeSet.addAttribute(StyleConstants.CharacterConstants.Italic, isItalicized);
			attributeSet.addAttribute(StyleConstants.CharacterConstants.Underline, isUnderlined);

			// Set colors.
			attributeSet.addAttribute(StyleConstants.CharacterConstants.Foreground, fgc);
			attributeSet.addAttribute(StyleConstants.CharacterConstants.Background, bgc);

			// Return the attribute set for use in text formatting.
			return attributeSet;
		}
		
		public final void openFile()
		{
			String filePath = Support.getFilePath(this.parent, true, this.isDebugging);

			if ((filePath == null) || filePath.isEmpty())
			{
				return;
			}

			ObjectInputStream inputStream = null;

			try
			{
				// Use binary file manipulation to import a file containing a Document object.
				inputStream = new ObjectInputStream(new FileInputStream(filePath));
				this.setDocument((StyledDocument)inputStream.readObject());
			}
			catch (Exception exception)
			{
				Support.displayException(this.parent, exception, false);
			}
			finally
			{
				try
				{
					inputStream.close();
				}
				catch (Exception exception)
				{
					Support.displayException(this.parent, exception, false);
				}
			}
		}
		
		public final void saveFile()
		{
			String filePath = Support.getFilePath(this.parent, false, this.isDebugging);

			if ((filePath == null) || filePath.isEmpty())
			{
				return;
			}

			ObjectOutputStream outputStream = null;

			try
			{
				// Use binary file manipulation to export a file containing a Document object.
				outputStream = new ObjectOutputStream(new FileOutputStream(filePath));
				outputStream.writeObject(this.getDocument());
			}
			catch (Exception exception)
			{
				Support.displayException(this.parent, exception, false);
			}
			finally
			{
				try
				{
					outputStream.close();
				}
				catch (Exception exception)
				{
					Support.displayException(this.parent, exception, false);
				}
			}
		}
	}
	
	public static class EventHandler implements Runnable
	{
		public void run() {}
		public void run(final Object... arguments) {}
	}
	
	public static final class Support
	{
		// This method displays special debugging messages to be used for diagnostic purposes.
		public static final void displayDebugMessage(final Component parent, final Object object)
		{
			String message = "";
			
			if (object == null) displayException(parent, new NullPointerException("displayDebugMessage(): 'object' is null."), true);
			
			if (object instanceof AWTEvent)
			{
				AWTEvent event = (AWTEvent)object;
				message += "What: " + event.paramString() + "\n";
				message += "Where: " + event.getSource().getClass().getSimpleName() + " (" + event.getSource().getClass().getCanonicalName() + ")\n";
			}
			else if (object instanceof String)
			{
				message = (String)object;
			}
			
			message += "When: " + getDateTimeStamp() + "\n";
			JOptionPane.showMessageDialog(parent, message, "Debugging Event", JOptionPane.INFORMATION_MESSAGE);
		}
		
		public static final void displayException(final Component parent, final Exception exception, final boolean isFatal)
		{
			/*
				Display error message along with some useful debugging information.
				Source file is where the error chain ended, which could be null in the case of a function in the Java API.
				Cause file is where the error chain began, which is the bottom of the stack and where the bad method is likely to be.
			*/
			String dialogTitle = null;
			
			if (isFatal)
			{
				dialogTitle = "Fatal Exception Occurred";
			}
			else
			{
				dialogTitle = "Exception Occurred";
			}
			
			JOptionPane.showMessageDialog(parent,
				exception.toString() + 
				"\n\nSource file: " + exception.getStackTrace()[0].getFileName() +
				"\nLine number: " + exception.getStackTrace()[0].getLineNumber() +
				"\n\nCause file: " + exception.getStackTrace()[exception.getStackTrace().length-1].getFileName() +
				"\nLine number: " + exception.getStackTrace()[exception.getStackTrace().length-1].getLineNumber() +
				"\n\nWhen: " + getDateTimeStamp(),
				dialogTitle,
				JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
			
			if (isFatal)
			{
				System.exit(-1);
			}
		}
		
		// Get Date/Time stamp in the default format.
		public static final String getDateTimeStamp()
		{
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss a z");
			return dateFormatter.format(new Date());
		}
		
		// Get Date/Time stamp in a custom format.
		public static final String getDateTimeStamp(final String dateFormat)
		{
			SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
			return dateFormatter.format(new Date());
		}
		
		// This method prompts the user to either open or save a file using a generic dialog box and returns the path to the selected file.
		public static final String getFilePath(final Component parent, final boolean isOpen, final boolean isDebugging)
		{
			JFileChooser fileDialog = new JFileChooser();
			String       filePath   = null;
			boolean      isDone     = false;
			int          choice     = 0;

			do // Loop while 'isDone' equals false, post-test.
			{
				if (isOpen)
				{
					choice = fileDialog.showOpenDialog(parent);
				}
				else
				{
					choice = fileDialog.showSaveDialog(parent);
				}

				switch (choice)
				{
					case JFileChooser.APPROVE_OPTION:

						try
						{
							filePath = fileDialog.getSelectedFile().getCanonicalPath();
							isDone = true;
						}
						catch (Exception exception)
						{
							filePath = null;
							isDone = false;
						}
						break;

					case JFileChooser.CANCEL_OPTION:

						filePath = null;
						isDone = true;
						break;

					default:

						filePath = null;
						isDone = false;
						break;
				}
			}
			while (!isDone);

			if (isDebugging)
			{
				displayDebugMessage(null, "File Path: " + filePath + "\n");
			}

			return filePath;
		}
		
		public static final boolean isPrime(final long n)
		{
			// Every prime number is an integer greater than one. If 'n' is less than or equal to one, mark it as composite (not prime).
			if ( n > 1 )
			{	// Check to make sure 'n' is not two or three. If it is either, than we can go ahead and mark it as prime.
				if ( (n != 2) && (n != 3) )
				{	// Since two and three have been handled, we want to know if 'n' is evenly divisible by two or three and mark it as composite.
					if ( ((n % 2) != 0) || ((n % 3) != 0) )
					{
						// Every prime number can be represented by the form 6k+1 or 6k-1. If 'n' cannot be represented this way, then we mark it as composite.
						if ( (((n+1) % 6) == 0) || (((n-1) % 6) == 0) )
						{
							// If a number can be factored into two numbers, at least one of them should be less than or equal to its square root.
							long limit = (long)Math.ceil(Math.sqrt((double)n));
							// Since we have eliminated all primes less than five, and two is the only even prime, we only need to check odd numbers from here on out.
							for (long i = 5; i <= limit; i += 2)
							{
								// Every prime number is only evenly divisible by itself and one. 'i' will never equal 'n' and 'i' will never equal one.
								// Thus, if 'n' is evenly divisible by 'i' then it cannot be prime.
								if ( (n % i) == 0 )
								{
									return false;
								}
							}

							return true;
						}
					}
				}
				else
				{
					return true;
				}
			}

			return false;
		}
		
		// This method takes a string and determines if it can be safely parsed as a boolean.
		// Return value of true indicates that the string is safe to parse, and false means that the string is not safe to parse.
		public static final boolean isStringParsedAsBoolean(final String s)
		{
			try
			{
				// parseBoolean throws an exception if the string can't be parsed.
				Boolean.parseBoolean(s);
			}
			catch (Exception exception)
			{
				// If we catch an exception, then we return false.
				return false;
			}

			// Base case; return true if the string was parsed without an exception being thrown.
			return true;
		}

		// This method takes a string and determines if it can be safely parsed as a float.
		// Return value of true indicates that the string is safe to parse, and false means that the string is not safe to parse.
		public static final boolean isStringParsedAsFloat(final String s)
		{
			try
			{
				// parseFloat throws an exception if the string can't be parsed.
				Float.parseFloat(s);
			}
			catch (Exception exception)
			{
				// If we catch an exception, then we return false.
				return false;
			}

			// Base case; return true if the string was parsed without an exception being thrown.
			return true;
		}

		// This method takes a string and determines if it can be safely parsed as an integer.
		// Return value of true indicates that the string is safe to parse, and false means that the string is not safe to parse.
		public static final boolean isStringParsedAsInteger(final String s)
		{
			try
			{
				// parseInt throws an exception if the string can't be parsed.
				Integer.parseInt(s);
			}
			catch (Exception exception)
			{
				// If we catch an exception, then we return false.
				return false;
			}

			// Base case; return true if the string was parsed without an exception being thrown.
			return true;
		}
		
		// This method is a wrapper for a specific invocation of JOptionPane.showConfirmDialog that I use frequently to prompt test users for debugging modes.
		public static final int promptDebugMode(final Component parent)
		{
			return JOptionPane.showConfirmDialog(parent, 
				"Do you wish to activate the debugging mode?\n\n" + 
				"Turning on the debugging mode will cause diagnostic messages to be displayed during special events that are helpful when testing this application.\n\n" + 
				"Note: This choice has no effect on error messages. Error messages will always be displayed!\n\n" + 
				"Closing this message without choosing \"Yes\" or \"No\" will close this application.", 
				"Debugging Mode", 
				JOptionPane.YES_NO_OPTION);
		}
	}
	
	public static final class Games
	{
		// Pretty self-explanatory. Returns a random integer between 'min' and 'max'.
		// The user tells the method whether the desired maximum is inclusive or exclusive.
		// Exclusive range in interval notation: [min, max)
		// Inclusive range in interval notation: [min, max]
		public static final int getRandomInteger(final int min, final int max, final boolean isMaxInclusive)
		{
			Random randomGenerator = new Random(System.nanoTime());

			if (isMaxInclusive)
			{
				return (randomGenerator.nextInt(max - min + 1) + min);
			}
			else
			{
				return (randomGenerator.nextInt(max - min) + min);
			}
		}
		
		// Pretty self-explanatory. Takes an array of integers, and returns the sum.
		public static final int getSumFromIntegerArray(final int[] arrayOfIntegers)
		{
			int sum = 0;

			for (int i = 0; i < arrayOfIntegers.length; i++)
			{
				sum += arrayOfIntegers[i];
			}

			return sum;
		}
		
		/*
			This method is useful in games like Dungeons and Dragons. The user provides the number of dice and the number of sides each die has.
			The resulting array contains the result of each die separately and the final element in the array is the sum of all the dice.
		*/
		public static final int[] throwDice(final int numberOfDice, final int numberOfSides)
		{
			int[] resultsArray = new int[numberOfDice+1];

			for (int i = 0; i < numberOfDice; i++)
			{
				resultsArray[i] = getRandomInteger(1, numberOfSides, true);
			}

			resultsArray[numberOfDice] = getSumFromIntegerArray(resultsArray);

			return resultsArray;
		}
	}
	
	public static final class Clock extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		private int hour;
		private int minute;
		private int second;
		
		private boolean isAnimated = false;
		
		private Color fgcCircle = Color.BLACK;
		private Color fgcHour   = Color.RED;
		private Color fgcMinute = Color.BLUE;
		private Color fgcSecond = Color.GREEN;
		
		private Timer animationTimer = new Timer(500, new TimerListener(this));
		
		private class TimerListener implements ActionListener
		{
			Clock parent = null;
			
			public TimerListener(final Clock clock)
			{
				super();
				this.parent = clock;
			}
			
			public void actionPerformed(final ActionEvent event)
			{
				// Set the time.
				parent.setCurrentTime();
			}
		}
		
		public Clock(final boolean isCurrent)
		{
			if (isCurrent)
			{
				this.setCurrentTime();
			}
			else
			{
				this.setRandomTime();
			}
		}
		
		public Clock(final int hour, final int minute, final int second)
		{
			this.setHour(hour);
			this.setMinute(minute);
			this.setSecond(second);
		}
		
		public final int getHour()
		{
			return this.hour;
		}
		
		public final int getMinute()
		{
			return this.minute;
		}
		
		public Dimension getPreferredSize()
		{
			return (new Dimension(250, 250));
		}
		
		public final int getSecond()
		{
			return this.second;
		}
		
		public final boolean isAnimated()
		{
			return this.isAnimated;
		}
		
		protected void paintComponent(final Graphics g)
		{
			super.paintComponent(g);
			
			// Initialize clock parameters.
			int xCenter = this.getWidth() / 2;
			int yCenter = this.getHeight() / 2;
			int clockRadius = (int)(Math.min(this.getWidth(), this.getHeight()) * 0.8 * 0.5);
			
			// Draw circle.
			g.setColor(this.fgcCircle);
			g.drawOval(xCenter - clockRadius, yCenter - clockRadius, 2 * clockRadius, 2 * clockRadius);
			g.drawString("12", xCenter - 5, yCenter - clockRadius + 12);
			g.drawString("9", xCenter - clockRadius + 3, yCenter + 5);
			g.drawString("3", xCenter + clockRadius - 10, yCenter + 3);
			g.drawString("6", xCenter - 3, yCenter + clockRadius - 3);
			
			// Draw exact time below the clock.
			g.drawString(this.toString(), (xCenter - 30), (yCenter + clockRadius + 20));
			// Draw my name horizontally between the '9' and the '3'.
			g.drawString("Matthew", (xCenter - 20), (yCenter + 3));
			
			// Draw the second hand.
			int sLength = (int)(clockRadius * 0.8);
			int xSecond = (int)(xCenter + sLength * Math.sin(this.getSecond() * (2 * Math.PI / 60)));
			int ySecond = (int)(yCenter - sLength * Math.cos(this.getSecond() * (2 * Math.PI / 60)));
			g.setColor(this.fgcSecond);
			g.drawLine(xCenter, yCenter, xSecond, ySecond);
			
			// Draw the minute hand.
			int mLength = (int)(clockRadius * 0.65);
			int xMinute = (int)(xCenter + mLength * Math.sin(this.getMinute() * (2 * Math.PI / 60)));
			int yMinute = (int)(yCenter - mLength * Math.cos(this.getMinute() * (2 * Math.PI / 60)));
			g.setColor(this.fgcMinute);
			g.drawLine(xCenter, yCenter, xMinute, yMinute);
			
			// Draw the hour hand.
			int hLength = (int)(clockRadius * 0.5);
			int xHour = (int)(xCenter + hLength * Math.sin((this.getHour() % 12 + this.getMinute() / 60.0) * (2 * Math.PI / 12)));
			int yHour = (int)(yCenter - hLength * Math.cos((this.getHour() % 12 + this.getMinute() / 60.0) * (2 * Math.PI / 12)));
			g.setColor(this.fgcHour);
			g.drawLine(xCenter, yCenter, xHour, yHour);
		}
		
		public final void setColors(final Color fgcCircle, final Color fgcHour, final Color fgcMinute, 
			final Color fgcSecond)
		{
			this.fgcCircle = fgcCircle;
			this.fgcHour   = fgcHour;
			this.fgcMinute = fgcMinute;
			this.fgcSecond = fgcSecond;
		}
		
		public final void setCurrentTime()
		{
			Calendar calendar = new GregorianCalendar();
			
			this.setHour(calendar.get(Calendar.HOUR_OF_DAY));
			this.setMinute(calendar.get(Calendar.MINUTE));
			this.setSecond(calendar.get(Calendar.SECOND));
		}
		
		public final void setHour(final int hour)
		{
			this.hour = hour;
			this.repaint();
		}
		
		public final void setMinute(final int minute)
		{
			this.minute = minute;
			this.repaint();
		}
		
		public final void setRandomTime()
		{
			this.setHour(Games.getRandomInteger(0, 24, false));
			this.setMinute(Games.getRandomInteger(0, 60, false));
			this.setSecond(Games.getRandomInteger(0, 60, false));
		}
		
		public final void setSecond(final int second)
		{
			this.second = second;
			this.repaint();
		}
		
		public final void startAnimation()
		{
			this.isAnimated = true;
			this.animationTimer.start();
		}
		
		public final void stopAnimation()
		{
			this.isAnimated = false;
			this.animationTimer.stop();
		}
		
		public final String toString()
		{
			String hour   = "";
			String minute = "";
			String second = "";
			String ampm   = "";
			
			if (this.getHour() < 12)
			{
				ampm = "AM";
			}
			else
			{
				ampm = "PM";
				int h = (this.getHour() - 12);
				
				if (h < 10)
				{
					if (h == 0)
					{
						hour = "12";
					}
					else
					{
						hour = ("0" + h);
					}
				}
				else
				{
					hour = ("" + h);
				}
			}
			
			if (this.getHour() < 10)
			{
				if (this.getHour() == 0)
				{
					hour = "12";
				}
				else
				{
					hour = ("0" + this.getHour());
				}
			}
			else
			{
				if (this.getHour() < 12)
				{
					hour = ("" + this.getHour());
				}
			}
			
			if (this.getMinute() < 10)
			{
				minute = ("0" + this.getMinute());
			}
			else
			{
				minute = ("" + this.getMinute());
			}
			
			if (this.getSecond() < 10)
			{
				second = ("0" + this.getSecond());
			}
			else
			{
				second = ("" + this.getSecond());
			}
			
			return (hour + ":" + minute + ":" + second + " " + ampm);
		}
	}
	
	public static final class Fan extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public Fan()
		{
			this.setForeground(Color.BLACK);
		}
		
		public Fan(final Color foregroundColor)
		{
			this.setForeground(foregroundColor);
		}
		
		public Dimension getPreferredSize()
		{
			return (new Dimension((250 / 4), (250 / 4)));
		}
		
		protected void paintComponent(final Graphics g)
		{
			super.paintComponent(g);

			int xCenter = this.getWidth() / 2;
			int yCenter = this.getHeight() / 2;
			int radius = (int)(Math.min(this.getWidth(), this.getHeight()) * 0.4);

			int x = xCenter - radius;
			int y = yCenter - radius;

			g.setColor(this.getForeground());
			g.drawOval(x, y, 2 * radius, 2 * radius);
			g.fillArc(x, y, 2 * radius, 2 * radius, 0, 30);
			g.fillArc(x, y, 2 * radius, 2 * radius, 90, 30);
			g.fillArc(x, y, 2 * radius, 2 * radius, 180, 30);
			g.fillArc(x, y, 2 * radius, 2 * radius, 270, 30);
		}
	}
	
	public static final class Fans extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		public Fans()
		{
			this.setLayout(new GridLayout(2,2));
			this.add(new Fan());
			this.add(new Fan());
			this.add(new Fan());
			this.add(new Fan());
		}
		
		public Fans(final Color foregroundColor)
		{
			this.setLayout(new GridLayout(2,2));
			this.add(new Fan(foregroundColor));
			this.add(new Fan(foregroundColor));
			this.add(new Fan(foregroundColor));
			this.add(new Fan(foregroundColor));
		}
		
		public Dimension getPreferredSize()
		{
			return (new Dimension(250, 250));
		}
	}
	
	public static final class Parabola extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		private Point focalPoint;
		private Point beginPoint;
		private Point endPoint;
		
		private Color fgcPlane = Color.BLACK;
		private Color fgcGraph = Color.BLUE;
		
		public Parabola() {}
		
		public Parabola(final Point focalPoint, final Point beginPoint, final Point endPoint)
		{
			this.setPoints(focalPoint, beginPoint, endPoint);
		}
		
		public Point getFocalPoint()
		{
			return this.focalPoint;
		}

		public Point getBeginPoint()
		{
			return this.beginPoint;
		}

		public Point getEndPoint()
		{
			return this.endPoint;
		}

		public Dimension getPreferredSize()
		{
			return (new Dimension(250, 250));
		}
		
		protected void paintComponent(final Graphics g)
		{
			super.paintComponent(g);
			
			Graphics2D g2D = (Graphics2D)g;
			
			int xCenter = this.getWidth() / 2;
			int yCenter = this.getHeight() / 2;
			int radius = (int)(Math.min(this.getWidth(), this.getHeight()) * 0.4);
			
			// Draw Axes
			g2D.setColor(this.fgcPlane);
			
			g2D.drawLine((xCenter - radius), yCenter, (xCenter + radius), yCenter);
			g2D.drawLine((xCenter - radius), yCenter, ((xCenter - radius) + 5), (yCenter + 5));
			g2D.drawLine((xCenter - radius), yCenter, ((xCenter - radius) + 5), (yCenter - 5));
			g2D.drawLine((xCenter + radius), yCenter, ((xCenter + radius) - 5), (yCenter + 5));
			g2D.drawLine((xCenter + radius), yCenter, ((xCenter + radius) - 5), (yCenter - 5));
			g2D.drawString("Y", (xCenter + 8), (yCenter - radius - 8));
			
			g2D.drawLine(xCenter, (yCenter - radius), xCenter, (yCenter + radius));
			g2D.drawLine(xCenter, (yCenter - radius), (xCenter + 5), ((yCenter - radius) + 5));
			g2D.drawLine(xCenter, (yCenter - radius), (xCenter - 5), ((yCenter - radius) + 5));
			g2D.drawLine(xCenter, (yCenter + radius), (xCenter + 5), ((yCenter + radius) - 5));
			g2D.drawLine(xCenter, (yCenter + radius), (xCenter - 5), ((yCenter + radius) - 5));
			g2D.drawString("X", (xCenter + radius + 8), (yCenter + 8));
			
			// Draw Parabola
			g2D.setColor(this.fgcGraph);
			
			QuadCurve2D quadratic = new QuadCurve2D.Double();
			Double      x1        = (Double)(xCenter + this.getBeginPoint().getX());
			Double      x2        = (Double)(xCenter + this.getEndPoint().getX());
			Double      y1        = (Double)(yCenter + this.getBeginPoint().getY());
			Double      y2        = (Double)(yCenter + this.getEndPoint().getY());
			Double      ctrlX     = (Double)(xCenter + this.getFocalPoint().getX());
			Double      ctrlY     = (Double)(yCenter + ((radius / 3) * 1.5) + this.getFocalPoint().getY());
			
			quadratic.setCurve(x1, y1, ctrlX, ctrlY, x2, y2);
			g2D.draw(quadratic);
		}
		
		public void setBeginPoint(final Point beginPoint)
		{
			this.beginPoint = beginPoint;
		}
		
		public void setColors(final Color fgcPlane, final Color fgcGraph)
		{
			this.fgcPlane = fgcPlane;
			this.fgcGraph = fgcGraph;
		}
		
		public void setEndPoint(final Point endPoint)
		{
			this.endPoint = endPoint;
		}
		
		public void setFocalPoint(final Point focalPoint)
		{
			this.focalPoint = focalPoint;
		}
		
		public void setPoints(final Point focalPoint, final Point beginPoint, final Point endPoint)
		{
			this.setFocalPoint(focalPoint);
			this.setBeginPoint(beginPoint);
			this.setEndPoint(endPoint);
		}
	}
	
	public static final void main(final String[] args)
	{
		ApplicationWindow mainWindow = null;
		int choice = Support.promptDebugMode(mainWindow);
		
		// Define a self-contained interface construction event handler.
		EventHandler myDrawGUI = new EventHandler()
		{
			public final void run(final Object... arguments) throws IllegalArgumentException
			{
				if (arguments.length <= 0)
				{
					throw new IllegalArgumentException("myDrawGUI Error : incorrect number of arguments.");
				}
				else if (!(arguments[0] instanceof ApplicationWindow))
				{
					throw new IllegalArgumentException("myDrawGUI Error : argument[0] is of incorrect type.");
				}
				
				ApplicationWindow window      = (ApplicationWindow)arguments[0];
				Container         contentPane = window.getContentPane();
				Fans              fans        = new Fans();
				Parabola          parabola    = new Parabola(new Point(0,0), new Point(-20,-50), new Point(20,-50));
				Clock             clock       = new Clock(false);
				
				contentPane.setLayout(new FlowLayout());
				contentPane.add(fans);
				contentPane.add(parabola);
				contentPane.add(clock);
			}
		};
		
		if (choice == JOptionPane.YES_OPTION)
		{
			mainWindow = new ApplicationWindow(null, "Graphics Application", new Dimension(800, 300), true, false, 
				true, null, myDrawGUI);
		}
		else if (choice == JOptionPane.NO_OPTION)
		{
			mainWindow = new ApplicationWindow(null, "Graphics Application", new Dimension(800, 300), false, false, 
				true, null, myDrawGUI);
		}
		else
		{
			return;
		}
		
		mainWindow.drawGUI();
		mainWindow.setVisible(true);
	}
}