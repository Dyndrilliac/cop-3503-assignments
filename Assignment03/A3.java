/*
 * Title: COP 3503 - Assignment 03
 * Author: Matthew Boyette
 * Date: 2/10/2013
 */

package Assignment03;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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

public class A3
{
    public static class Rectangle
    {
        private double height = 2.0;
        private double width  = 1.0;
        
        public Rectangle()
        {
        }
        
        public Rectangle(final double height, final double width)
        {
            this.setHeight(height);
            this.setWidth(width);
        }
        
        public final double getArea()
        {
            return (this.getHeight() * this.getWidth());
        }
        
        public final double getHeight()
        {
            return this.height;
        }
        
        public final double getPerimeter()
        {
            if (this.isItSquare())
            {
                // Squares can take any one of their sides and multiply it by 4 (or raise to the power of 2) to get their perimeter.
                return (this.getHeight() * 4);
            }
            else
            {
                // Rectangles get their perimeter by mutliplying their heights and widths by 2 and adding the products together.
                return ((this.getHeight() * 2) + (this.getWidth() * 2));
            }
        }
        
        public final double getWidth()
        {
            return this.width;
        }
        
        public final boolean isItSquare()
        {
            // A rectangle is a square if its height and its width are equal.
            return (this.getHeight() == this.getWidth());
        }
        
        public final void setHeight(final double height)
        {
            this.height = height;
        }
        
        public final void setWidth(final double width)
        {
            this.width = width;
        }
        
        @Override
        public String toString()
        {
            return "This rectangle's height is " + this.getHeight() + " and its width is " + this.getWidth() + ".";
        }
    }
    
    public final static void main(final String[] args)
    {
        new A3(args);
    }
    
    private JComboBox<String> inputHeight = null;
    private JComboBox<String> inputWidth  = null;
    private boolean           isDebugging = false;
    private RichTextPane      output      = null;
    private ApplicationWindow window      = null;
    
    public A3(final String[] args)
    {
        this.setDebugging(Support.promptDebugMode(this.getWindow()));
        
        // Define a self-contained ActionListener event handler.
        // @formatter:off
        EventHandler<A3> myActionPerformed = new EventHandler<A3>(this)
        {
            private final static long serialVersionUID = 1L;
            
            @Override
            public final void run(final AWTEvent event)
            {
                ActionEvent actionEvent = (ActionEvent)event;
                A3 parent = this.getParent();
                
                if ((parent.getOutput() != null) && (parent.getInputHeight() != null) && (parent.getInputWidth() != null))
                {
                    /*
                     * JDK 7 allows string objects as the expression in a switch statement. This generally produces more efficient byte code compared
                     * to a chain of if statements. http://docs.oracle.com/javase/7/docs/technotes/guides/language/strings-switch.html
                     */
                    switch (actionEvent.getActionCommand())
                    {
                        case "Clear":
                            
                            parent.getOutput().clear();
                            parent.getInputWidth().removeAllItems();
                            parent.getInputWidth().setSelectedIndex(-1);
                            parent.getInputHeight().removeAllItems();
                            parent.getInputHeight().setSelectedIndex(-1);
                            parent.getInputHeight().grabFocus();
                            break;
                        
                        case "Open":
                            
                            parent.getOutput().openOrSaveFile(true);
                            parent.getInputHeight().grabFocus();
                            break;
                        
                        case "Save":
                            
                            parent.getOutput().openOrSaveFile(false);
                            parent.getInputHeight().grabFocus();
                            break;
                        
                        case "Print Rectangle":
                            
                            parent.printRectangle();
                            break;
                        
                        default:
                            
                            break;
                    }
                }
            }
        };
        
        // Define a self-contained interface construction event handler.
        EventHandler<A3> myDrawGUI = new EventHandler<A3>(this)
        {
            private final static long serialVersionUID = 1L;
            
            @Override
            public final void run(final ApplicationWindow window)
            {
                A3 parent = this.getParent();
                Container contentPane = window.getContentPane();
                JMenuBar menuBar = new JMenuBar();
                JMenu fileMenu = new JMenu("File");
                JMenuItem clearOption = new JMenuItem("Clear");
                JMenuItem openOption = new JMenuItem("Open");
                JMenuItem saveOption = new JMenuItem("Save");
                RichTextPane outputBox = new RichTextPane(window, true, window.isDebugging());
                JScrollPane outputPanel = new JScrollPane(outputBox);
                JPanel inputPanel = new JPanel();
                JLabel widthLabel = new JLabel("Enter Width: ");
                JLabel heightLabel = new JLabel("Enter Height: ");
                JComboBox<String> cboHeight = new JComboBox<String>();
                JComboBox<String> cboWidth = new JComboBox<String>();
                JButton btnInput = new JButton("Print Rectangle");
                
                parent.setOutput(outputBox);
                cboHeight.setFont(Support.DEFAULT_TEXT_FONT);
                cboHeight.setEditable(true);
                cboHeight.addActionListener(window);
                parent.setInputHeight(cboHeight);
                cboWidth.setFont(Support.DEFAULT_TEXT_FONT);
                cboWidth.setEditable(true);
                cboWidth.addActionListener(window);
                parent.setInputWidth(cboWidth);
                btnInput.setFont(Support.DEFAULT_TEXT_FONT);
                btnInput.addActionListener(window);
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
                inputPanel.setLayout(new FlowLayout());
                inputPanel.add(heightLabel);
                inputPanel.add(cboHeight);
                inputPanel.add(widthLabel);
                inputPanel.add(cboWidth);
                inputPanel.add(btnInput);
                contentPane.setLayout(new BorderLayout());
                contentPane.add(outputPanel, BorderLayout.CENTER);
                contentPane.add(inputPanel, BorderLayout.NORTH);
            }
        };
        
        this.setWindow(new ApplicationWindow(null,
            "Rectangle Application",
            new Dimension(800, 600),
            this.isDebugging(),
            true,
            myActionPerformed,
            myDrawGUI));
        // @formatter:on
    }
    
    public final JComboBox<String> getInputHeight()
    {
        return this.inputHeight;
    }
    
    public final JComboBox<String> getInputWidth()
    {
        return this.inputWidth;
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
    
    public void printRectangle()
    {
        final JComboBox<String> cboHeight = this.getInputHeight();
        final JComboBox<String> cboWidth = this.getInputWidth();
        
        // Determine whether or not the height and width fields both contain data.
        boolean isHeightEmpty = ((String)cboHeight.getSelectedItem() == null);
        boolean isWidthEmpty = ((String)cboWidth.getSelectedItem() == null);
        
        if ((!isHeightEmpty) && (!isWidthEmpty))
        {
            // If the height and width fields both contain data, then determine whether or not they can be parsed as doubles.
            if (Support.isStringParsedAsDouble((String)cboHeight.getSelectedItem()) && Support.isStringParsedAsDouble((String)cboWidth.getSelectedItem()))
            {
                double height = Double.parseDouble((String)cboHeight.getSelectedItem());
                double width = Double.parseDouble((String)cboWidth.getSelectedItem());
                Rectangle r = new Rectangle(height, width);
                
                this.getOutput().append(Color.GREEN,
                    Color.WHITE,
                    "[" + Support.getDateTimeStamp() + "] ",
                    Color.BLACK,
                    Color.WHITE,
                    "The value of toString() is ",
                    Color.BLUE,
                    Color.WHITE,
                    "\"" + r.toString() + "\"\n");
                this.getOutput().append(Color.GREEN,
                    Color.WHITE,
                    "[" + Support.getDateTimeStamp() + "] ",
                    Color.BLACK,
                    Color.WHITE,
                    "The area is ",
                    Color.RED,
                    Color.WHITE,
                    r.getArea() + "\n");
                this.getOutput().append(Color.GREEN,
                    Color.WHITE,
                    "[" + Support.getDateTimeStamp() + "] ",
                    Color.BLACK,
                    Color.WHITE,
                    "The perimeter is ",
                    Color.RED,
                    Color.WHITE,
                    r.getPerimeter() + "\n");
                
                if (r.isItSquare())
                {
                    this.getOutput().append(Color.GREEN,
                        Color.WHITE,
                        "[" + Support.getDateTimeStamp() + "] ",
                        Color.MAGENTA,
                        Color.WHITE,
                        "This rectangle is a square.\n\n");
                }
                else
                {
                    this.getOutput().append(Color.GREEN,
                        Color.WHITE,
                        "[" + Support.getDateTimeStamp() + "] ",
                        Color.MAGENTA,
                        Color.WHITE,
                        "This rectangle is NOT a square.\n\n");
                }
                
                // Add the most recently used height and width values to their respective dropdown lists.
                if (cboHeight.getSelectedIndex() == -1)
                {
                    cboHeight.addItem((String)cboHeight.getSelectedItem());
                }
                
                if (cboWidth.getSelectedIndex() == -1)
                {
                    cboWidth.addItem((String)cboWidth.getSelectedItem());
                }
                
                // Reset the input fields.
                cboWidth.setSelectedIndex(-1);
                cboHeight.setSelectedIndex(-1);
                cboHeight.grabFocus();
            }
            else
            {
                // If the height and width fields can't be parsed as doubles, inform the user that the application needs numerical data.
                Support.displayException(this.getWindow(),
                    new Exception("Data Entry Exception: Both of the data entry fields (height and width) are required to have numerical data."),
                    false);
            }
        }
        else if (isHeightEmpty || isWidthEmpty)
        {
            // If either the height or the width field is empty, then inform the user that both fields are required.
            Support.displayException(this.getWindow(),
                new Exception("Data Entry Exception: One of the data entry fields (height or width) has been left blank.\n" + "Both fields are required to have numerical data."),
                false);
        }
    }
    
    protected final void setDebugging(final boolean debugMode)
    {
        this.isDebugging = debugMode;
    }
    
    public final void setInputHeight(final JComboBox<String> inputHeight)
    {
        this.inputHeight = inputHeight;
    }
    
    public final void setInputWidth(final JComboBox<String> inputWidth)
    {
        this.inputWidth = inputWidth;
    }
    
    public final void setOutput(final RichTextPane output)
    {
        this.output = output;
    }
    
    protected final void setWindow(final ApplicationWindow window)
    {
        this.window = window;
    }
}