/*
 * Title: COP 3503 - Assignment 07
 * Author: Matthew Boyette
 * Date: 4/13/2013
 */

package Assignment07;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import api.gui.draw.Car;
import api.gui.swing.ApplicationWindow;
import api.gui.swing.CarGUI;
import api.util.EventHandler;
import api.util.Support;

public class A7
{
    public final static void main(final String[] args)
    {
        new A7(args);
    }
    
    private boolean           isDebugging = false;
    private ApplicationWindow window      = null;
    
    public A7(final String[] args)
    {
        this.setDebugging(Support.promptDebugMode(this.getWindow()));
        
        // Define a self-contained interface construction event handler.
        // @formatter:off
        EventHandler<A7> myDrawGUI = new EventHandler<A7>(this)
        {
            private final static long serialVersionUID = 1L;
            
            @Override
            public final void run(final ApplicationWindow window)
            {
                Container contentPane = window.getContentPane();
                
                JPanel carsPane = new JPanel();
                JPanel car1Pane = new JPanel();
                JPanel car2Pane = new JPanel();
                JPanel guisPane = new JPanel();
                JPanel gui1Pane = new JPanel();
                JPanel gui2Pane = new JPanel();
                Car car1 = new Car(window);
                Car car2 = new Car(window);
                CarGUI gui1 = new CarGUI(car1);
                CarGUI gui2 = new CarGUI(car2);
                
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
        
        this.setWindow(new ApplicationWindow(null, "Animated Car Application", new Dimension(800, 600), this.isDebugging(), true, null, myDrawGUI));
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
    
    protected final void setDebugging(final boolean debugMode)
    {
        this.isDebugging = debugMode;
    }
    
    protected final void setWindow(final ApplicationWindow window)
    {
        this.window = window;
    }
}