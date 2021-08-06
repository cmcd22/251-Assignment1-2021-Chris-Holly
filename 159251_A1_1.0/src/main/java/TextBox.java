import javax.swing.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        frame.setResizable(true);
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
        JMenu file = new JMenu("File");
        //Menu items
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem savetoPDF = new JMenuItem("Save to PDF");
        JMenuItem printFile = new JMenuItem("Print");

        //Action listeners
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        printFile.addActionListener(this);
        savetoPDF.addActionListener(this);
        // Adding menu items to menu
        file.add(newFile);
        file.add(openFile);
        file.add(saveFile);
        file.add(savetoPDF);
        file.add(printFile);

        //Edit option
        JMenu edit = new JMenu("Edit");

        //Creating edit menu items
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem paste = new JMenuItem("Paste");

        //Adding action listeners
        copy.addActionListener(this);
        cut.addActionListener(this);
        paste.addActionListener(this);

        edit.add(copy);
        edit.add(cut);
        edit.add(paste);

        //Search option
        JMenuItem search = new JMenuItem("Search");

        search.addActionListener(this);

        //Time/Date option
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        JMenuItem timeDate = new JMenuItem(formatter.format(date));

        timeDate.addActionListener(this);

        //About option
        JMenuItem about = new JMenuItem("About");

        about.addActionListener(this);

        // Exit option
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(this);

        // Add everything to the menu bar
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(search);
        menuBar.add(timeDate);
        menuBar.add(about);
        menuBar.add(exit);
        // Display window
        frame.setJMenuBar(menuBar);
        frame.add(textArea);
        frame.setSize(500,500);
        frame.show();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String event = e.getActionCommand();
        // Paste clipboard
        if (event.equals("Paste")){
            textArea.paste();
        }
        // Copy to clipboard
        else if (event.equals("Copy")){
            textArea.copy();
        }
        // Cuts text
        else if (event.equals("Cut")){
            textArea.cut();
        }
        else if (event.equals("Open")) {
            JFileChooser j = new JFileChooser("f:");
            // simple OpenDialog function
            int r = j.showOpenDialog(null);
            // If a file is selected
            if (r == JFileChooser.APPROVE_OPTION) {
                // Set the label to the path of the selected directory
                File fi = new File(j.getSelectedFile().getAbsolutePath());
                try {
                    String s1 = "",
                            sl = "";
                    // File reader
                    FileReader fr = new FileReader(fi);
                    // Buffered reader
                    BufferedReader br = new BufferedReader(fr);
                    // Initialize sl
                    sl = br.readLine();
                    // Take the input from the file
                    while ((s1 = br.readLine()) != null) {
                        sl = sl + "\n" + s1;
                    }
                    // Set the text
                    textArea.setText(sl);
                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(frame, evt.getMessage());
                }
            }
            // If open is cancelled
            else
                JOptionPane.showMessageDialog(frame, "Open cancelled.");
        }
        else if (event.equals("Save")){
            JFileChooser j = new JFileChooser("f:");
            // Simple SaveDialog function
            int r = j.showSaveDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                // Set directory
                File fi = new File(j.getSelectedFile().getAbsolutePath());
                // Writes the file
                try {
                    // Create a file writer
                    FileWriter wr = new FileWriter(fi, false);
                    // Create buffered writer
                    BufferedWriter w = new BufferedWriter(wr);
                    // Write
                    w.write(textArea.getText());
                    w.flush();
                    w.close();
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(frame, evt.getMessage());
                }
            }
            // If save is cancelled
            else
                JOptionPane.showMessageDialog(frame, "Save cancelled.");

        }
        else if (event.equals("Print")) {
            try {
                // print the file
                textArea.print();
            }
            catch (Exception evt) {
                JOptionPane.showMessageDialog(frame, evt.getMessage());
            }
        }
        else if (event.equals("New")){
            textArea.setText("");
        }

        else if (event.equals("About")) {
            JOptionPane.showMessageDialog(null, "Developed by Chris McDonald & Holly Ducoing\nThank you for using our Text Editor!", "About", JOptionPane.INFORMATION_MESSAGE);
        }

        else if (event.equals("Exit")) {
            System.exit(0);
        }

    }
}
