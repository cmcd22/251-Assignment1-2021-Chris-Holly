import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextBox extends JFrame implements ActionListener{
    JFrame frame;
    //JTextArea textArea;
    //JScrollPane scrollPane;
    RSyntaxTextArea textArea;
    RTextScrollPane sp;
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
        //textArea = new JTextArea();
        textArea = new RSyntaxTextArea();
        sp = new RTextScrollPane(textArea);
        //Add scrollbar
        //scrollPane = new JScrollPane (textArea,
                //JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
        //frame.add(scrollPane);
        frame.add(sp);
        frame.setSize(500,500);
        frame.setLocation(x,y);
        frame.show();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String event = e.getActionCommand();
        // Paste clipboard
        if (event.equals("Paste")) {
            textArea.paste();
        }
        // Copy to clipboard
        else if (event.equals("Copy")) {
            textArea.copy();
        }
        // Cuts text
        else if (event.equals("Cut")) {
            textArea.cut();
        } else if (event.equals("Open")) {

            JFileChooser j = new JFileChooser("f:");
            // simple OpenDialog function
            int r = j.showOpenDialog(null);
            //Reset syntax style to none
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
            // If a file is selected
            if (r == JFileChooser.APPROVE_OPTION) {
                // Set the label to the path of the selected directory
                File fi = new File(j.getSelectedFile().getAbsolutePath());
                //Get file type
                String fileName = fi.toString();
                int dotIndex = fileName.lastIndexOf('.');
                String end = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
                //Set correct syntax style for type of file
                if (end.equals("java")) {
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                } else if (end.equals("py")) {
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
                } else if (end.equals("cpp")) {
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
                } else if (end.equals("html")) {
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
                } else if (end.equals("xml")) {
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
                }
                if (end.equals("rtf")) {
                    try {
                        //Get file path
                        Path filePath = Paths.get(String.valueOf(fi));
                        byte[] content = Files.readAllBytes(filePath);
                        //Set up RTF reader
                        String rtf = new String(content, StandardCharsets.ISO_8859_1);
                        StringReader in = new StringReader(rtf);
                        RTFEditorKit kit = new RTFEditorKit();
                        Document doc = kit.createDefaultDocument();
                        //Read RTF file
                        kit.read(in, doc, 0);
                        String text = doc.getText(0, doc.getLength());
                        //Display
                        textArea.setText(text);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage());
                    }
                } else {
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
            }
            // If open is cancelled
            else
                JOptionPane.showMessageDialog(frame, "Open cancelled.");

        } else if (event.equals("Save")) {
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
                    wr.close();
                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(frame, evt.getMessage());
                }
            }
            // If save is cancelled
            else {
                JOptionPane.showMessageDialog(frame, "Save cancelled.");
            }
            // PDFbox save
        } else if (event.equals("Save as PDF")) {
            PDDocument document = null;
            try {
                document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                PDFont pdfFont = PDType1Font.COURIER;
                float fontSize = 15;
                float leading = 1.5f * fontSize;

                PDRectangle mb = page.getMediaBox();
                float margin = 70;
                float width = mb.getWidth() - 2 * margin;
                float x = mb.getLowerLeftX() + margin;
                float y = mb.getUpperRightY() - margin;

                String textAll = textArea.getText();
                List<String> lines = new ArrayList<String>();
                for (String text : textAll.split("\n")) {


                int lastSpace = -1;
                while (text.length() > 0) {
                    int spaceIndex = text.indexOf(' ', lastSpace + 1);
                    if (spaceIndex < 0)
                        spaceIndex = text.length();
                    String subString = text.substring(0, spaceIndex);
                    float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
                    if (size > width) {
                        if (lastSpace < 0)
                            lastSpace = spaceIndex;
                        subString = text.substring(0, lastSpace);
                        lines.add(subString);
                        text = text.substring(lastSpace).trim();
                        lastSpace = -1;
                    } else if (spaceIndex == text.length()) {
                        lines.add(text);
                        text = "";
                    } else {
                        lastSpace = spaceIndex;
                    }
                }
            }

                contentStream.beginText();
                contentStream.setFont(pdfFont, fontSize);
                contentStream.newLineAtOffset(x, y);
                for (String line: lines)
                {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -leading);
                }
                contentStream.endText();
                contentStream.close();

                document.save(new File( "My_PDF.pdf"));
                JOptionPane.showMessageDialog(frame, "Saved.");
            } catch (IOException evt) {
                JOptionPane.showMessageDialog(frame, evt.getMessage());
            } finally
            {
                if (document != null)
                {
                    try {
                        document.close();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage());
                    }
                }
            }
//            try (PDDocument doc = new PDDocument()) {
//                PDPage myPage = new PDPage();
//                doc.addPage(myPage);
//                try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {
//                    cont.beginText();
//                    cont.setFont(PDType1Font.COURIER, 10);
//                    cont.setLeading(14.5f);
//                    cont.newLineAtOffset(25, 700);
//                    String line1 = textArea.getText().replace("\n","<br>");
//                    cont.showText(line1);
//                    cont.endText();
//                    JOptionPane.showMessageDialog(frame, "Saved.");
//                }
//                catch(Exception evt){
//                    JOptionPane.showMessageDialog(frame, evt.getMessage());
//                }
//                // saves to /src
//                doc.save("your_PDF.pdf");
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
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

        else if (event.equals("Search")) {

            //Removes existing highlights
            Highlighter h = textArea.getHighlighter();
            h.removeAllHighlights();
            //Show popup window asking user to input a word
            String word = JOptionPane.showInputDialog("Enter search word: \nSingle word searches only");
            //If the 'okay' button is selected
            if (word != null) {
                //Display message if input left blank
                if (word.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a search word");
                }
                //Allow only single-word searches
                else if (word.contains(" ")) {
                    JOptionPane.showMessageDialog(null, "Single word searches only");
                } else {
                    //Create search criteria
                    Pattern p = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(textArea.getText());
                    //Tracks the number of times the search term appears
                    int cycles = 0;
                    //while there are undiscovered instances of the search word
                    while (m.find()) {
                        cycles += 1;
                        //search terms gets highlighted
                        try {
                            h.addHighlight(m.start(), m.end(), DefaultHighlighter.DefaultPainter);
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                    //if no instances of the word are found
                    if (cycles == 0) {
                        JOptionPane.showMessageDialog(null, "Search term not found");
                    }
                }
                //If the window is closed or 'cancel' is clicked
            } else {
                JOptionPane.showMessageDialog(null, "Search cancelled");
            }
        }

        else if (event.equals("About")) {
            JOptionPane.showMessageDialog(null, "Developed by Chris McDonald & Holly Ducoing\nThank you for using our Text Editor!", "About", JOptionPane.INFORMATION_MESSAGE);
        }

        else if (event.equals("Exit")) {
            System.exit(0);
        }

    }
}
