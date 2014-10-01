/*
	Title:	COP 3503 - Assignment 07
	Author: Matthew Boyette
	Date:   4/13/2013
*/

package Assignment07;

import api.gui.ApplicationWindow;
import api.util.EventHandler;
import api.util.Support;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;

public class A7
{	
	public static class Car extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		public static final int VELOCITY_SLOW   = 1;
		public static final int VELOCITY_MEDIUM = 2;
		public static final int VELOCITY_FAST   = 3;
		public static final int DIRECTION_LEFT  = 1;
		public static final int DIRECTION_RIGHT = 2;
		
		protected final static class CarListener implements ActionListener
		{
			private Car parent = null;
			
			public CarListener(final Car car)
			{
				this.setParent(car);
			}
			
			public final void actionPerformed(final ActionEvent event)
			{
				// Move the car.
				this.getParent().move();
			}
			
			public final Car getParent()
			{
				return this.parent;
			}
			
			protected final void setParent(final Car parent)
			{
				this.parent = parent;
			}
		}
		
		private Component parent    = null;
		private Point2D   origin    = null;
		private Color     bodyColor = Color.RED;
		private int       direction = DIRECTION_RIGHT;
		private int       velocity  = VELOCITY_SLOW;
		private Timer     animation = new Timer(333, new CarListener(this));
		
		public Car(final Component parent)
		{
			this.calculateOrigin();
			this.parent = parent;
		}
		
		public final void animationStart()
		{
			Support.playAudioClipFromURL(this.parent, "http://static1.grsites.com/archive/sounds/cars/cars005.wav");
			this.animation.start();
		}
		
		public final void animationStop()
		{
			Support.playAudioClipFromURL(this.parent, "http://static1.grsites.com/archive/sounds/cars/cars024.wav");
			this.animation.stop();
		}
		
		protected final void calculateOrigin()
		{	
			double xCenter = (this.getWidth() / 2.0);
			double yCenter = (this.getHeight() / 2.0);
			this.setOrigin(new Point2D.Double(xCenter, yCenter));
		}
		
		public final Color getBodyColor()
		{
			return this.bodyColor;
		}
		
		public final int getDirection()
		{
			return this.direction;
		}
		
		public final Dimension getPreferredSize()
		{
			return (new Dimension(this.parent.getWidth(), (this.parent.getHeight() / 4)));
		}
		
		public final Point2D getOrigin()
		{
			return this.origin;
		}
		
		public final int getVelocity()
		{
			return this.velocity;
		}

		public final void honkHorn()
		{
			Support.playAudioClipFromURL(this.parent, "http://static1.grsites.com/archive/sounds/cars/cars014.wav");
		}
		
		public final void move()
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
		
		protected final void paintComponent(final Graphics g)
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

		public final void setBodyColor(final Color bodyColor)
		{
			this.bodyColor = bodyColor;
			this.repaint();
		}
		
		public final void setDirection(final int direction)
		{
			this.direction = direction;
		}

		private final void setOrigin(final Point2D origin)
		{
			this.origin = origin;
		}
		
		public final void setVelocity(final int velocity)
		{
			this.velocity = velocity;
		}
		
		public final void startRadio()
		{
			Support.openWebPageInDefaultBrowser(this.parent, "https://www.youtube.com/watch?v=Hva5KS8TFEQ&list=PLLsuQeGbt7lp94HjetXZPXfyVDMxNAcxV");
		}
		
		public final void stopRadio()
		{
			try
			{
				final String os = System.getProperty("os.name");
				
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
			catch (final Exception exception)
			{
				Support.displayException(this.parent, exception, false);
			}
		}
	}
	
	public final static class CarGUI extends JPanel implements ActionListener
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
				
		protected final static class GUIListener implements ActionListener
		{
			private CarGUI parent = null;
			
			public GUIListener(final CarGUI gui)
			{
				this.parent = gui;
			}
			
			public final void actionPerformed(final ActionEvent event)
			{
				// Update speed.
				JSlider velocity = this.parent.getVelocitySlider();
				Car     car      = this.parent.getCar();
				
				car.setVelocity(velocity.getValue());
			}
		}
		
		public CarGUI(final Car car)
		{
			this.setCar(car);
			this.drawGUI();
			this.guiTimer.start();
		}
		
		public final void actionPerformed(final ActionEvent event)
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
		
		public final void drawGUI()
		{
			JPanel motionControls	= new JPanel();
			JPanel soundControls	= new JPanel();
			JPanel colorControls	= new JPanel();
			
			this.startCarButton		= new JButton("Start Car");
			this.stopCarButton		= new JButton("Stop Car");
			this.directionButton	= new JButton("Change Direction");
			this.hornButton			= new JButton("Honk Horn");
			this.velocitySlider		= new JSlider(JSlider.HORIZONTAL, 1, 3, 1);
			this.startRadioButton	= new JButton("Start Radio");
			this.stopRadioButton	= new JButton("Stop Radio");
			this.redBodyButton		= new JButton("Red");
			this.blueBodyButton		= new JButton("Blue");
			this.greenBodyButton	= new JButton("Green");
			this.yellowBodyButton 	= new JButton("Yellow");
			
			this.startCarButton.setFont(Support.DEFAULT_TEXT_FONT);
			this.stopCarButton.setFont(Support.DEFAULT_TEXT_FONT);
			this.directionButton.setFont(Support.DEFAULT_TEXT_FONT);
			this.hornButton.setFont(Support.DEFAULT_TEXT_FONT);
			this.velocitySlider.setFont(Support.DEFAULT_TEXT_FONT);
			this.startRadioButton.setFont(Support.DEFAULT_TEXT_FONT);
			this.stopRadioButton.setFont(Support.DEFAULT_TEXT_FONT);
			this.redBodyButton.setFont(Support.DEFAULT_TEXT_FONT);
			this.blueBodyButton.setFont(Support.DEFAULT_TEXT_FONT);
			this.greenBodyButton.setFont(Support.DEFAULT_TEXT_FONT);
			this.yellowBodyButton.setFont(Support.DEFAULT_TEXT_FONT);
			
			this.startCarButton.addActionListener(this);
			this.stopCarButton.addActionListener(this);
			this.directionButton.addActionListener(this);
			this.hornButton.addActionListener(this);
			this.startRadioButton.addActionListener(this);
			this.stopRadioButton.addActionListener(this);
			this.redBodyButton.addActionListener(this);
			this.blueBodyButton.addActionListener(this);
			this.greenBodyButton.addActionListener(this);
			this.yellowBodyButton.addActionListener(this);
			
			this.setLayout(new BorderLayout());
			motionControls.setLayout(new BorderLayout());
			soundControls.setLayout(new FlowLayout());
			colorControls.setLayout(new FlowLayout());
			this.add(motionControls, BorderLayout.NORTH);
			this.add(soundControls, BorderLayout.CENTER);
			this.add(colorControls, BorderLayout.SOUTH);
			motionControls.add(this.startCarButton, BorderLayout.EAST);
			motionControls.add(this.stopCarButton, BorderLayout.WEST);
			motionControls.add(this.directionButton, BorderLayout.CENTER);
			motionControls.add(this.velocitySlider, BorderLayout.SOUTH);
			soundControls.add(this.stopRadioButton);
			soundControls.add(this.hornButton);
			soundControls.add(this.startRadioButton);
			colorControls.add(this.redBodyButton);
			colorControls.add(this.blueBodyButton);
			colorControls.add(this.greenBodyButton);
			colorControls.add(this.yellowBodyButton);
		}
		
		public final Car getCar()
		{
			return this.car;
		}
		
		public final JSlider getVelocitySlider()
		{
			return this.velocitySlider;
		}

		public final void setCar(final Car car)
		{
			this.car = car;
		}
	}
	
	public final static void main(final String[] args)
	{
		new A7(args);
	}
	
	private boolean				isDebugging	= false;
	private ApplicationWindow	window		= null;
	
	public A7(final String[] args)
	{
		this.setDebugging(Support.promptDebugMode(this.getWindow()));
		
		// Define a self-contained interface construction event handler.
		EventHandler<A7> myDrawGUI = new EventHandler<A7>(this)
		{
			private final static long serialVersionUID = 1L;

			@Override
			public final void run(final ApplicationWindow window)
			{
				Container	contentPane	= window.getContentPane();
				
				JPanel	carsPane	= new JPanel();
				JPanel	car1Pane	= new JPanel();
				JPanel	car2Pane	= new JPanel();
				JPanel	guisPane	= new JPanel();
				JPanel	gui1Pane	= new JPanel();
				JPanel	gui2Pane	= new JPanel();
				Car		car1		= new Car(window);
				Car		car2		= new Car(window);
				CarGUI	gui1		= new CarGUI(car1);
				CarGUI	gui2		= new CarGUI(car2);
				
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
		
		this.setWindow(new ApplicationWindow(null, "Animated Car Application", new Dimension(800, 600), this.isDebugging(), true, null,
			myDrawGUI));
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
	
	protected final void setWindow(final ApplicationWindow window)
	{
		this.window = window;
	}
}