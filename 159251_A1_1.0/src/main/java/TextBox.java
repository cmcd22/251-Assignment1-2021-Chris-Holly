import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextBox extends JFrame implements ActionListener{
    JFrame frame;
    JTextArea textArea;
    public static void main(String[] args){
        TextBox tb = new TextBox();
        tb.newWindow();
    }
    public void newWindow(){
        // Basic frame
        frame = new JFrame("Text Editor");
        // Basic text writing zone
        textArea = new JTextArea();
        // Menu bar containing important buttons
        /*
New function: to create a new (fresh) window.
Open function: to read other text files (just standard .txt files). This should allow users to navigate the file
system to search for a file.
The ability to read OpenDocument Text (.odt) files. This is part of the Open function.

Save function: save text output into .txt file format. This should allow users to navigate the file system
to save the file in a selected drive/location.

Search: search for text within the screen (this will be tested based on a single word)

Exit: to quit the program – close all windows.

Select text, Copy, Paste and Cut (SCPC) capabilities.

Time and Date (T&D): retrieve the current time and data from the OS and place it at the
top of the page of the editor.

About:  display the names of both team members and a brief message in a popup message box.

Print function: allow your editor to print text by connecting it to the local printer in your machine (
similar to any other text editor that you have used).

harder: include a PDF conversation function to your editor so the file can be also saved
into PDF format (for standard text files). Use an external library for this such as Apache PDFBox  or OpenPDF.
         */
        //Menubar
        JMenuBar menuBar = new JMenuBar();
        //Menu
        JMenu menu = new JMenu("File");
        //Menu items
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem searchText = new JMenuItem("Search");
        JMenuItem savetoPDF = new JMenuItem("Save to PDF");
        JMenuItem printFile = new JMenuItem("Print");

        //Action listeners
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        printFile.addActionListener(this);
        searchText.addActionListener(this);
        savetoPDF.addActionListener(this);
        // Adding menu items to menu
        menu.add(newFile);
        menu.add(openFile);
        menu.add(saveFile);
        menu.add(savetoPDF);
        menu.add(printFile);
        // Exit option
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        //Date and time

        
        // Add everything to the menu bar
        menuBar.add(menu);
        menuBar.add(searchText);
        menuBar.add(exit);
        // Display window
        frame.setJMenuBar(menuBar);
        frame.add(textArea);
        frame.setSize(500,500);
        frame.show();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
