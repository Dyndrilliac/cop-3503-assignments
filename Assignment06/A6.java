/*
 * Title: COP 3503 - Assignment 06
 * Author: Matthew Boyette
 * Date: 3/25/2013
 */

package Assignment06;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;

import api.gui.draw.Clock;
import api.gui.draw.Fans;
import api.gui.draw.Parabola;
import api.gui.swing.ApplicationWindow;
import api.util.EventHandler;
import api.util.Support;

public class A6
{
    public static final void main(final String[] args)
    {
        new A6(args);
    }
    
    private boolean           isDebugging;
    private ApplicationWindow window;
    
    public A6(final String[] args)
    {
        this.setDebugging(Support.promptDebugMode(this.getWindow()));
        
        // Define a self-contained interface construction event handler.
        // @formatter:off
        EventHandler<A6> myDrawGUI = new EventHandler<A6>(this)
        {
            private static final long serialVersionUID = 1L;
            
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
        
        this.setWindow(new ApplicationWindow(null, "Graphics Application", new Dimension(800, 300), this.isDebugging(), false, null, myDrawGUI));
        // @formatter:on
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