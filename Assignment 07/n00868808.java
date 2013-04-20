/*
	Author: Matthew Boyette
	Date:   4/13/2013
*/

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.InputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

public class n00868808
{
	public static class EventHandler implements Runnable
	{
		public void run() {}
		public void run(final Object... arguments) {}
	}
	
	public static class Support
	{
		// This method displays special debugging messages to be used for diagnostic purposes.
		public static void displayDebugMessage(final Component parent, final Object object)
		{
			String message = "";
			
			if (object == null) displayException(parent, new NullPointerException("displayDebugMessage(): 'object' is null."), true);
			
			if (object instanceof AWTEvent)
			{
				AWTEvent event = (AWTEvent)object;
				message += "What: " + event.paramString() + "\n";
				message += "Where: " + event.getSource().getClass().getSimpleName() + 
					" (" + event.getSource().getClass().getCanonicalName() + ")\n";
			}
			else if (object instanceof String)
			{
				message = (String)object;
			}
			
			message += "When: " + getDateTimeStamp() + "\n";
			JOptionPane.showMessageDialog(parent, message, "Debugging Event", JOptionPane.INFORMATION_MESSAGE);
		}
		
		public static void displayException(final Component parent, final Exception exception, final boolean isFatal)
		{
			/*
				Display error message along with some useful debugging information.
				Source file is where the error chain ended, which could be null in the case of a function in the Java API.
				Cause file is where the error chain began, which is the bottom of the stack and where the bad method is likely to be.
			*/
			String dialogTitle     = null;
			String recoveryMessage = null;
			
			if (isFatal)
			{
				dialogTitle = "Fatal Exception Occurred";
				recoveryMessage = "This error is fatal. The program cannot recover from the problem, and will be terminated following this message.";
			}
			else
			{
				dialogTitle = "Exception Occurred";
				recoveryMessage = "This error is not fatal. The program has recovered from the problem, and you may continue operating it.";
			}
			
			JOptionPane.showMessageDialog(parent,
				exception.toString() + 
				"\n\nSource file: " + exception.getStackTrace()[0].getFileName() +
				"\nLine number: " + exception.getStackTrace()[0].getLineNumber() +
				"\n\nCause file: " + exception.getStackTrace()[exception.getStackTrace().length-1].getFileName() +
				"\nLine number: " + exception.getStackTrace()[exception.getStackTrace().length-1].getLineNumber() +
				"\n\nWhen: " + getDateTimeStamp() +
				"\n\nRecovery: " + recoveryMessage,
				dialogTitle,
				JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
			
			if (isFatal)
			{
				System.exit(-1);
			}
		}
		
		// Get Date/Time stamp in the default format.
		public static String getDateTimeStamp()
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
		public static String getFilePath(final Component parent, final boolean isOpen, final boolean isDebugging)
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
		
		public static InputStream getResourceByName(final String resourceName)
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream(resourceName);
			return input;
		}
		
		public static boolean isPrime(final long n)
		{
			// Every prime number is an integer greater than one. If 'n' is less than or equal to one, mark it as composite (not prime).
			if ( n > 1 )
			{	// Check to make sure 'n' is not two or three. If it is either, than we can go ahead and mark it as prime.
				if ( (n != 2) && (n != 3) )
				{	// Since two and three have been handled, we want to know if 'n' is evenly divisible by two or three and mark it as composite.
					if ( ((n % 2) != 0) || ((n % 3) != 0) )
					{
						// Every prime number can be represented by the form 6k+1 or 6k-1. If 'n' cannot be represented this way, 
						// then we mark it as composite.
						if ( (((n+1) % 6) == 0) || (((n-1) % 6) == 0) )
						{
							// If a number can be factored into two numbers, at least one of them should be less than or equal to its square root.
							long limit = (long)Math.ceil(Math.sqrt((double)n));
							// Since we have eliminated all primes less than five, and two is the only even prime, 
							// we only need to check odd numbers from here on out.
							for (long i = 5; i <= limit; i += 2)
							{
								// Every prime number is only evenly divisible by itself and one. 
								// 'i' will never equal 'n' and 'i' will never equal one.
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
		public static boolean isStringParsedAsBoolean(final String s)
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
		public static boolean isStringParsedAsFloat(final String s)
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
		public static boolean isStringParsedAsInteger(final String s)
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
		
		public static void openWebPageInDefaultBrowser(final String s)
		{
			if (Desktop.isDesktopSupported())
			{
				try
				{
					Desktop.getDesktop().browse(new URI(s));
				}
				catch (Exception exception)
				{
					displayException(null, exception, false);
				}
			}
		}
		
		public static void playAudioClipFromURL(final String s)
		{
			try
			{
				URL url = new URL(s);
				AudioClip ac = Applet.newAudioClip(url);
				ac.play();
		    }
			catch (Exception exception)
			{
				displayException(null, exception, false);
			}
		}
		
		// This method is a wrapper for a specific invocation of JOptionPane.showConfirmDialog that I use frequently to prompt test 
		// users for debugging modes.
		public static int promptDebugMode(final Component parent)
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
	
	public static class ApplicationWindow extends JFrame implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		private List<Component>   elements         = new ArrayList<Component>();
		private EventHandler      actionPerformed  = null;
		private EventHandler      drawGUI          = null;
		private boolean           isDebugging      = false;
		
		public ApplicationWindow(final Component parent, final String applicationTitle, final Dimension size, final boolean isDebugging, 
			final boolean isResizable, final EventHandler actionPerformed, final EventHandler drawGUI)
		{
			super(applicationTitle);
			
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception exception) {}
			
			this.setDebugging(isDebugging);
			this.setSize(size);
			this.setResizable(isResizable);
			
			if (parent == null)
			{
				this.setLocationByPlatform(true);
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			else
			{
				this.setLocationRelativeTo(parent);
				this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
			
			this.setActionPerformed(actionPerformed);
			this.setDrawGUI(drawGUI);
			this.drawGUI();
			this.setVisible(true);
		}
		
		public void actionPerformed(final ActionEvent event)
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
		
		public void drawGUI()
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
		
		public EventHandler getActionPerformed()
		{
			return this.actionPerformed;
		}
		
		public EventHandler getDrawGUI()
		{
			return this.drawGUI;
		}
		
		public List<Component> getElements()
		{
			return this.elements;
		}
		
		public boolean isDebugging()
		{
			return this.isDebugging;
		}
		
		public void setActionPerformed(final EventHandler actionPerformed)
		{
			this.actionPerformed = actionPerformed;
		}
		
		public void setDebugging(final boolean isDebugging)
		{
			this.isDebugging = isDebugging;
		}
		
		public void setDrawGUI(final EventHandler drawGUI)
		{
			this.drawGUI = drawGUI;
		}
		
		public void setElements(final List<Component> elements)
		{
			this.elements = elements;
		}
		
		public void setIconImageByResourceName(final String resourceName)
		{
			InputStream input = Support.getResourceByName(resourceName);
			Image icon = null;
			
			try
			{
				icon = ImageIO.read(input);
			}
			catch (Exception exception)
			{
				Support.displayException(this, exception, true);
			}
			
			this.setIconImage(icon);
		}
	}
	
	public static class Car extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		public static final int VELOCITY_SLOW   = 1;
		public static final int VELOCITY_MEDIUM = 2;
		public static final int VELOCITY_FAST   = 3;
		public static final int DIRECTION_LEFT  = 1;
		public static final int DIRECTION_RIGHT = 2;
		
		private static class CarListener implements ActionListener
		{
			Car parent = null;
			
			CarListener(final Car car)
			{
				this.parent = car;
			}
			
			public void actionPerformed(final ActionEvent event)
			{
				// Move the car.
				parent.move();
			}
		}
		
		private Component parent    = null;
		private Point2D   origin    = null;
		private Color     bodyColor = Color.RED;
		private int       direction = DIRECTION_RIGHT;
		private int       velocity  = VELOCITY_SLOW;
		private Timer     animation = new Timer(333, new CarListener(this));
		
		Car(final Component parent)
		{
			this.calculateOrigin();
			this.parent = parent;
		}
		
		public void animationStart()
		{
			Support.playAudioClipFromURL("http://www.autospeak.com/grpsnda/crankit.wav");
			animation.start();
		}
		
		public void animationStop()
		{
			animation.stop();
		}
		
		protected void calculateOrigin()
		{	
			double xCenter = (this.getWidth() / 2.0);
			double yCenter = (this.getHeight() / 2.0);
			this.setOrigin(new Point2D.Double(xCenter, yCenter));
		}
		
		public Color getBodyColor()
		{
			return this.bodyColor;
		}
		
		public int getDirection()
		{
			return this.direction;
		}
		
		public Dimension getPreferredSize()
		{
			return (new Dimension(this.parent.getWidth(), (this.parent.getHeight() / 4)));
		}
		
		public Point2D getOrigin()
		{
			return this.origin;
		}
		
		public int getVelocity()
		{
			return this.velocity;
		}

		public void honkHorn()
		{
			Support.playAudioClipFromURL("http://www.autospeak.com/grpsndb/beepbeep.wav");
		}
		
		public void move()
		{
			Point2D oldOrigin = new Point2D.Double(this.getOrigin().getX(), this.getOrigin().getY());
			double  updatedX  = 0;
			double  updatedY  = 0;
			double  rate      = ((double)this.getVelocity() * 20.0);
			
			if (this.getDirection() == Car.DIRECTION_LEFT)
			{
				// Move to the left.
				
				updatedX = oldOrigin.getX() - rate;
				updatedY = oldOrigin.getY();
				
				if (updatedX < 0)
				{
					updatedX = this.getWidth();
				}
			}
			else
			{
				// Move to the right.
				
				updatedX = oldOrigin.getX() + rate;
				updatedY = oldOrigin.getY();
				
				if (updatedX > this.getWidth())
				{
					updatedX = 0;
				}
			}
			
			this.setOrigin(new Point2D.Double(updatedX, updatedY));
			this.repaint();
		}
		
		protected void paintComponent(final Graphics g)
		{
			super.paintComponent(g);
			
			Graphics2D g2D    = (Graphics2D)g;
			int        radius = (int)(Math.min(this.getWidth(), this.getHeight()) * .5);
			
			// Calculate lengths and widths.
			double bodyLength  = ((double)radius / 2.0);
			double bodyWidth   = ((double)radius / 4.0);
			double wheelLength = ((double)radius / 10.0);
			double wheelWidth  = ((double)radius / 10.0);
			double wheel2diff  = ((double)radius / 10.0);
			
			// Draw rectangular body.
			double bodyX = this.getOrigin().getX(); 
			double bodyY = this.getOrigin().getY();
			RoundRectangle2D carBody = new RoundRectangle2D.Double(bodyX, bodyY, bodyLength, bodyWidth, 5, 5);
			GradientPaint solidColor = new GradientPaint(0, 0, this.getBodyColor(), 100, 0, this.getBodyColor());
			g2D.setPaint(solidColor);
			g2D.fill(carBody);
			
			// Draw the left circular wheel.
			double wheel1X = this.getOrigin().getX();
			double wheel1Y = this.getOrigin().getY() + (bodyWidth - (wheel2diff / 2));
			Ellipse2D wheel1 = new Ellipse2D.Double(wheel1X, wheel1Y, wheelLength, wheelWidth);
			solidColor = new GradientPaint(0, 0, Color.BLACK, 100, 0, Color.BLACK);
			g2D.setPaint(solidColor);
			g2D.fill(wheel1);
			
			// Draw the right circular wheel.
			double wheel2X = this.getOrigin().getX() + (bodyLength - wheel2diff);
			double wheel2Y = this.getOrigin().getY() + (bodyWidth - (wheel2diff / 2));
			Ellipse2D wheel2 = new Ellipse2D.Double(wheel2X, wheel2Y, wheelLength, wheelWidth);
			g2D.setPaint(solidColor);
			g2D.fill(wheel2);
		}

		public void setBodyColor(final Color bodyColor)
		{
			this.bodyColor = bodyColor;
			this.repaint();
		}
		
		public void setDirection(final int direction)
		{
			this.direction = direction;
		}

		private void setOrigin(final Point2D origin)
		{
			this.origin = origin;
		}
		
		public void setVelocity(final int velocity)
		{
			this.velocity = velocity;
		}
		
		public void startRadio()
		{
			Support.openWebPageInDefaultBrowser("http://www.youtube.com/watch?v=Hva5KS8TFEQ&list=PLLsuQeGbt7lp94HjetXZPXfyVDMxNAcxV");
		}
		
		public void stopRadio()
		{
			try
			{
				String os = System.getProperty("os.name");
				
				// Since I chose YouTube as my way of implementing the "radio" functionality, the only way I could turn it off is by closing the browser window.
				// Unfortunately, I couldn't figure out a more elegant way to do this than closing all open browser windows.
				// If you had a more user friendly strategy in mind, I'd love to hear about it.
				if (os.contains("Windows"))
				{
					Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
					Runtime.getRuntime().exec("taskkill /F /IM iexplorer.exe");
					Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
					Runtime.getRuntime().exec("taskkill /F /IM safari.exe");
					Runtime.getRuntime().exec("taskkill /F /IM opera.exe");
				}
				else
				{
					// Assuming the OS will be some version of Unix, Linux, or Mac.
					Runtime.getRuntime().exec("kill `ps -ef | grep -i firefox | grep -v grep | awk '{print $2}'`");
					Runtime.getRuntime().exec("kill `ps -ef | grep -i chrome | grep -v grep | awk '{print $2}'`");
					Runtime.getRuntime().exec("kill `ps -ef | grep -i safari | grep -v grep | awk '{print $2}'`");
				}
			}
			catch (Exception exception)
			{
				Support.displayException(null, exception, false);
			}
		}
	}
	
	public static class CarGUI extends JPanel implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		
		private Car     car              = null;
		private JButton startCarButton   = null;
		private JButton stopCarButton    = null;
		private JButton directionButton  = null;
		private JSlider velocitySlider   = null;
		private JButton hornButton       = null;
		private JButton startRadioButton = null;
		private JButton stopRadioButton  = null;
		private JButton redBodyButton    = null;
		private JButton blueBodyButton   = null;
		private JButton greenBodyButton  = null;
		private JButton yellowBodyButton = null;
		private Timer   guiTimer         = new Timer(333, new GUIListener(this));
				
		private static class GUIListener implements ActionListener
		{
			private CarGUI parent = null;
			
			GUIListener(final CarGUI gui)
			{
				this.parent = gui;
			}
			
			public void actionPerformed(final ActionEvent event)
			{
				// Update speed.
				JSlider velocity = parent.getVelocitySlider();
				Car     car      = parent.getCar();
				
				car.setVelocity(velocity.getValue());
			}
		}
		
		CarGUI(Car car)
		{
			this.setCar(car);
			this.drawGUI();
			this.guiTimer.start();
		}
		
		public void actionPerformed(ActionEvent event)
		{
			switch (event.getActionCommand())
			{
				case "Start Car":
					
					this.car.animationStart();
					break;
					
				case "Stop Car":
					
					this.car.animationStop();
					break;
					
				case "Change Direction":
					
					if (this.car.getDirection() == Car.DIRECTION_LEFT)
					{
						this.car.setDirection(Car.DIRECTION_RIGHT);
					}
					else
					{
						this.car.setDirection(Car.DIRECTION_LEFT);
					}
					
					break;
					
				case "Honk Horn":
					
					this.car.honkHorn();
					break;
					
				case "Start Radio":
					
					this.car.startRadio();
					break;
					
				case "Stop Radio":
					
					this.car.stopRadio();
					break;
					
				case "Red":
					
					this.car.setBodyColor(Color.RED);
					break;
					
				case "Blue":
					
					this.car.setBodyColor(Color.BLUE);
					break;
					
				case "Green":
					
					this.car.setBodyColor(Color.GREEN);
					break;
					
				case "Yellow":
					
					this.car.setBodyColor(Color.YELLOW);
					break;
					
				default:
					
					break;
			}
		}
		
		public void drawGUI()
		{
			JPanel motionControls = new JPanel();
			JPanel soundControls  = new JPanel();
			JPanel colorControls  = new JPanel();
			
			this.setLayout(new BorderLayout());
			motionControls.setLayout(new BorderLayout());
			soundControls.setLayout(new FlowLayout());
			colorControls.setLayout(new FlowLayout());
			this.add(motionControls, BorderLayout.NORTH);
			this.add(soundControls, BorderLayout.CENTER);
			this.add(colorControls, BorderLayout.SOUTH);
			
			startCarButton   = new JButton("Start Car");
			startCarButton.addActionListener(this);
			stopCarButton    = new JButton("Stop Car");
			stopCarButton.addActionListener(this);
			directionButton  = new JButton("Change Direction");
			directionButton.addActionListener(this);
			velocitySlider   = new JSlider(JSlider.HORIZONTAL, 1, 3, 1);
			hornButton       = new JButton("Honk Horn");
			hornButton.addActionListener(this);
			startRadioButton = new JButton("Start Radio");
			startRadioButton.addActionListener(this);
			stopRadioButton  = new JButton("Stop Radio");
			stopRadioButton.addActionListener(this);
			redBodyButton    = new JButton("Red");
			redBodyButton.addActionListener(this);
			blueBodyButton   = new JButton("Blue");
			blueBodyButton.addActionListener(this);
			greenBodyButton  = new JButton("Green");
			greenBodyButton.addActionListener(this);
			yellowBodyButton = new JButton("Yellow");
			yellowBodyButton.addActionListener(this);
			
			motionControls.add(startCarButton, BorderLayout.EAST);
			motionControls.add(stopCarButton, BorderLayout.WEST);
			motionControls.add(directionButton, BorderLayout.CENTER);
			motionControls.add(velocitySlider, BorderLayout.SOUTH);
			
			soundControls.add(stopRadioButton);
			soundControls.add(hornButton);
			soundControls.add(startRadioButton);
			
			colorControls.add(redBodyButton);
			colorControls.add(blueBodyButton);
			colorControls.add(greenBodyButton);
			colorControls.add(yellowBodyButton);
		}
		
		public Car getCar()
		{
			return this.car;
		}
		
		public JSlider getVelocitySlider()
		{
			return this.velocitySlider;
		}

		public void setCar(Car car)
		{
			this.car = car;
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
				JPanel            carsPane    = new JPanel();
				JPanel            car1Pane    = new JPanel();
				JPanel            car2Pane    = new JPanel();
				JPanel            guisPane    = new JPanel();
				JPanel            gui1Pane    = new JPanel();
				JPanel            gui2Pane    = new JPanel();
				Car               car1        = new Car(window);
				Car               car2        = new Car(window);
				CarGUI            gui1        = new CarGUI(car1);
				CarGUI            gui2        = new CarGUI(car2);
				
				carsPane.setLayout(new BorderLayout());
				car1Pane.setLayout(new FlowLayout());
				car2Pane.setLayout(new FlowLayout());
				carsPane.add(car1Pane, BorderLayout.NORTH);
				carsPane.add(car2Pane, BorderLayout.SOUTH);
				
				guisPane.setLayout(new BorderLayout());
				gui1Pane.setLayout(new FlowLayout());
				gui2Pane.setLayout(new FlowLayout());
				guisPane.add(gui1Pane, BorderLayout.WEST);
				guisPane.add(gui2Pane, BorderLayout.EAST);
				
				contentPane.setLayout(new BorderLayout());
				contentPane.add(carsPane, BorderLayout.NORTH);
				contentPane.add(guisPane, BorderLayout.SOUTH);
				
				car1Pane.add(car1);
				car2Pane.add(car2);
				gui1Pane.add(gui1);
				gui2Pane.add(gui2);
			}
		};
		
		if (choice == JOptionPane.YES_OPTION)
		{
			mainWindow = new ApplicationWindow(null, "Car Application", new Dimension(800, 600), true, true, 
				null, myDrawGUI);
		}
		else if (choice == JOptionPane.NO_OPTION)
		{
			mainWindow = new ApplicationWindow(null, "Car Application", new Dimension(800, 600), false, true, 
				null, myDrawGUI);
		}
		else
		{
			return;
		}
	}
}