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
    JScrollPane scrollPane;
    //Integers for location of window
    int x = 50;
    int y = 50;
    public static void main(String[] args){
        TextBox tb = new TextBox();
        tb.newWindow(50,50);
    }
    public void newWindow(int x, int y){
        // Basic frame
        frame = new JFrame("Text Editor");
        frame.setResizable(true);
        // Basic text writing zone
        textArea = new JTextArea();
        //Add scrollbar
        scrollPane = new JScrollPane (textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //Menubar
        JMenuBar menuBar = new JMenuBar();
        //Menu
        JMenu file = new JMenu("File");
        //Menu items
        file.addSeparator();
        JMenu filesubmenu = new JMenu("Save");
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem savetoPDF = new JMenuItem("Save as PDF");
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
        filesubmenu.add(saveFile);
        filesubmenu.add(savetoPDF);
        file.add(printFile);
        file.add(filesubmenu);

        //Edit option
        JMenu edit = new JMenu("Edit");

        //Creating edit menu items
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem paste = new JMenuItem("Paste");
        // submenu to edit
        edit.addSeparator();
        JMenu submenu = new JMenu("Edit Font");
        JMenuItem normal = new JMenuItem("Normal");
        JMenuItem bold = new JMenuItem("Bold");
        JMenuItem italic = new JMenuItem("Italic");
        JMenuItem underline = new JMenuItem("Underline");
        JMenuItem superscript = new JMenuItem("Superscript");
        JMenuItem color = new JMenuItem("Color");
        JMenuItem leftalign = new JMenuItem("Align left");
        JMenuItem rightalign = new JMenuItem("Align right");
        //Adding action listeners
        copy.addActionListener(this);
        cut.addActionListener(this);
        paste.addActionListener(this);
        submenu.addActionListener(this);
        normal.addActionListener(this);
        bold.addActionListener(this);
        italic.addActionListener(this);
        underline.addActionListener(this);
        superscript.addActionListener(this);
        color.addActionListener(this);
        leftalign.addActionListener(this);
        rightalign.addActionListener(this);
        // Add to edit dropdown
        edit.add(copy);
        edit.add(cut);
        edit.add(paste);
        submenu.add(normal);
        submenu.add(bold);
        submenu.add(italic);
        submenu.add(superscript);
        submenu.add(color);
        submenu.add(leftalign);
        submenu.add(rightalign);
        edit.add(submenu);

        //Search option
        JMenuItem search = new JMenuItem("Search");

        search.addActionListener(this);

        //Time/Date option
        JMenuItem timeDate = new JMenuItem();
        //Refresh rate in milliseconds
        int refresh = 1000;
        ActionListener showTime = evt -> {
            String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(System.currentTimeMillis()));
            timeDate.setText(date);
        };
        new Timer(refresh, showTime).start();

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
        frame.add(scrollPane);
        frame.setSize(500,500);
        frame.setLocation(x,y);
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
                File fi = new File(j.getSelectedFile().getAbsolutePath() + ".txt");
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
            //display new window at new coordinates
            x += 15;
            y += 15;
            newWindow(x,y);
        }

        else if (event.equals("About")) {
            JOptionPane.showMessageDialog(null, "Developed by Chris McDonald & Holly Ducoing\nThank you for using our Text Editor!", "About", JOptionPane.INFORMATION_MESSAGE);
        }

        else if (event.equals("Exit")) {
            System.exit(0);
        }

    }
}
