/*
 * Title: COP 3503 - Assignment 06
 * Author: Matthew Boyette
 * Date: 3/25/2013
 */

package Assignment06;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.geom.QuadCurve2D;

import javax.swing.JPanel;

import api.gui.ApplicationWindow;
import api.gui.Clock;
import api.util.EventHandler;
import api.util.Support;

public class A6
{
	public static final class Fan extends JPanel
	{
		private static final long	serialVersionUID	= 1L;

		public Fan()
		{
			this.setForeground(Color.BLACK);
		}

		public Fan(final Color foregroundColor)
		{
			this.setForeground(foregroundColor);
		}

		@Override
		public Dimension getPreferredSize()
		{
			return (new Dimension((250 / 4), (250 / 4)));
		}

		@Override
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
		private static final long	serialVersionUID	= 1L;

		public Fans()
		{
			this.setLayout(new GridLayout(2, 2));
			this.add(new Fan());
			this.add(new Fan());
			this.add(new Fan());
			this.add(new Fan());
		}

		public Fans(final Color foregroundColor)
		{
			this.setLayout(new GridLayout(2, 2));
			this.add(new Fan(foregroundColor));
			this.add(new Fan(foregroundColor));
			this.add(new Fan(foregroundColor));
			this.add(new Fan(foregroundColor));
		}

		@Override
		public Dimension getPreferredSize()
		{
			return (new Dimension(250, 250));
		}
	}

	public static final class Parabola extends JPanel
	{
		private static final long	serialVersionUID	= 1L;

		private Point				beginPoint;
		private Point				endPoint;
		private Color				fgcGraph			= Color.BLUE;
		private Color				fgcPlane			= Color.BLACK;
		private Point				focalPoint;

		public Parabola()
		{
		}

		public Parabola(final Point focalPoint, final Point beginPoint, final Point endPoint)
		{
			this.setPoints(focalPoint, beginPoint, endPoint);
		}

		public Point getBeginPoint()
		{
			return this.beginPoint;
		}

		public Point getEndPoint()
		{
			return this.endPoint;
		}

		public Point getFocalPoint()
		{
			return this.focalPoint;
		}

		@Override
		public Dimension getPreferredSize()
		{
			return (new Dimension(250, 250));
		}

		@Override
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
			Double x1 = xCenter + this.getBeginPoint().getX();
			Double x2 = xCenter + this.getEndPoint().getX();
			Double y1 = yCenter + this.getBeginPoint().getY();
			Double y2 = yCenter + this.getEndPoint().getY();
			Double ctrlX = xCenter + this.getFocalPoint().getX();
			Double ctrlY = yCenter + ((radius / 3) * 1.5) + this.getFocalPoint().getY();

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
		new A6(args);
	}

	private boolean				isDebugging;
	private ApplicationWindow	window;

	public A6(final String[] args)
	{
		this.setDebugging(Support.promptDebugMode(this.getWindow()));

		// Define a self-contained interface construction event handler.
		EventHandler<A6> myDrawGUI = new EventHandler<A6>(this)
			{
			private static final long	serialVersionUID	= 1L;

			@Override
			public final void run(final ApplicationWindow window)
			{
				this.getParent();
				Container contentPane = window.getContentPane();
				Fans fans = new Fans();
				Parabola parabola = new Parabola(new Point(0, 0), new Point(-20, -50), new Point(20, -50));
				Clock clock = new Clock(false);

				contentPane.setLayout(new FlowLayout());
				contentPane.add(fans);
				contentPane.add(parabola);
				contentPane.add(clock);
			}
			};

			this.setWindow(new ApplicationWindow(null, "Graphics Application", new Dimension(800, 300), this.isDebugging(),
				false, null, myDrawGUI));
	}

	public final ApplicationWindow getWindow()
	{
		return this.window;
	}

	public final boolean isDebugging()
	{
		return this.isDebugging;
	}

	protected final void setDebugging(final boolean isDebugging)
	{
		this.isDebugging = isDebugging;
	}

	protected final void setWindow(final ApplicationWindow window)
	{
		this.window = window;
	}
}