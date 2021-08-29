import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.EditableTextExtractor;
import org.yaml.snakeyaml.Yaml;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
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
    public void newWindow(int x, int y) {

        // Import config file parameters
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.yml");
        Yaml yaml = new Yaml();
        Map<String,Object> data = yaml.load(is);

        // Set background and foreground colours
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // Retrieve background colour from config file
        String backColStr = data.get("background_color").toString();
        String[] backColArray = backColStr.split(",");
        int[] backIntArray = new int[3];
        int count1 = 0;
        for (String num1 : backColArray) {
            int final1Num = Integer.parseInt(num1);
            backIntArray[count1] = final1Num;
            count1 += 1;
        }
        // Retrieve foreground colour from config file
        String foreColStr = data.get("foreground_color").toString();
        String[] foreColArray = foreColStr.split(",");
        int[] foreIntArray = new int[3];
        int count2 = 0;
        for (String num2 : foreColArray) {
            int final2Num = Integer.parseInt(num2);
            foreIntArray[count2] = final2Num;
            count2 += 1;
        }
        // Set colours for menus
        Color background = new Color(backIntArray[0],backIntArray[1],backIntArray[2]);
        Color foreground = new Color(foreIntArray[0],foreIntArray[1],foreIntArray[2]);
        UIManager.put("MenuBar.foreground",foreground);
        UIManager.put("Menu.foreground",foreground);
        UIManager.put("MenuItem.foreground",foreground);
        // Basic frame
        frame = new JFrame(data.get("title").toString());
        frame.setResizable(true);
        // Basic text writing zone
        //textArea = new JTextArea();
        textArea = new RSyntaxTextArea();
        sp = new RTextScrollPane(textArea);
        // Set font type,style and size
        Font font = new Font(data.get("font").toString(),Font.PLAIN,(int) data.get("size"));
        // Set color scheme for text editor window
        textArea.setFont(font);
        textArea.setBackground(background);
        textArea.setForeground(foreground);
        textArea.setCurrentLineHighlightColor(background);
        textArea.setCaretColor(foreground);
        //Add scrollbar
        //scrollPane = new JScrollPane (textArea,
                //JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //Menubar
        JMenuBar menuBar = new JMenuBar();
        //Menu
        JMenu file = new JMenu("File");
        //Menu items
        JMenu filesubmenu = new JMenu("Save");
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem savetoPDF = new JMenuItem("Save as PDF");
        JMenuItem saveasRTF = new JMenuItem("Save as RTF");
        JMenuItem printFile = new JMenuItem("Print");

        //Action listeners
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        printFile.addActionListener(this);
        savetoPDF.addActionListener(this);
        saveasRTF.addActionListener(this);

        // Adding menu items to menu
        file.add(newFile);
        file.add(openFile);
        filesubmenu.add(saveFile);
        filesubmenu.add(savetoPDF);
        filesubmenu.add(saveasRTF);
        file.add(filesubmenu);
        file.add(printFile);

        //Edit option
        JMenu edit = new JMenu("Edit");

        //Creating edit menu items
        JMenuItem select = new JMenuItem("Select All");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem paste = new JMenuItem("Paste");
        // submenu to edit
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
        select.addActionListener(this);
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
        edit.add(select);
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
        int width = (int) data.get("width");
        int height = (int) data.get("height");
        frame.setSize(width,height);
        frame.setLocation(x,y);
        frame.show();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String event = e.getActionCommand();
        // Select all text in textArea
        if (event.equals("Select All")) {
            textArea.selectAll();
        }
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
                } if (end.equals("odt")) {
                    TextDocument textD;
                    try {
                        // Extract text content from file
                        textD = TextDocument.loadDocument(fileName);
                        EditableTextExtractor ete = EditableTextExtractor.newOdfEditableTextExtractor(textD);
                        ete.getText();
                        String output = ete.getText();
                        // Remove metadata from string output
                        String cutoff = "MicrosoftOffice";
                        String finalOutput = output.substring(0,output.indexOf(cutoff));
                        // Add text to textArea
                        textArea.setText(finalOutput);
                    } catch (Exception ex) {
                        ex.printStackTrace();
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
                    JOptionPane.showMessageDialog(frame, "Save successful.");
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
//        } else if (event.equals("Save as RTF")) {
//            Path filePath = Paths.get(String.valueOf(fi));
//            byte[] content = new byte[0];
//            try {
//                content = Files.readAllBytes(filePath);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            //Set up RTF reader
//            String rtf = new String(content, StandardCharsets.ISO_8859_1);
//            StringReader in = new StringReader(rtf);
//            RTFEditorKit kit = new RTFEditorKit();
//            Document doc = kit.createDefaultDocument();
//            //Read RTF file
//            kit.write(in, doc, 0, doc.getLength());
//            String text = doc.getText(0, doc.getLength());
//            //Display
//            textArea.setText(text);
        }
//            ByteArrayInputStream input= new ByteArrayInputStream(textArea.getText().getBytes());
//            String rtf = new String(input, StandardCharsets.ISO_8859_1);
//            StringWriter in = new StringWriter(rtf);
//            try {
//                RTFEditorKit rtfEditorKit = new RTFEditorKit();
////                HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
////                Document htmlDoc = htmlEditorKit.createDefaultDocument();
//                Document rtfDoc = rtfEditorKit.createDefaultDocument();
//                rtfEditorKit.read(input, rtfDoc,0);
////                htmlEditorKit.read(input, htmlDoc, 0);
//                rtfEditorKit.write(writer, rtfDoc, 0, rtfDoc.getLength());
//            } catch (Exception ex) {
//                System.out.println("Error"+ex);
//            }
//            System.out.println(writer.toString());
//        }



//            final StringWriter out = new StringWriter();
//            ByteArrayOutputStream b = new ByteArrayOutputStream();
//            Document doc = textArea.getDocument();
//            String x = null;
//            try {
//                x = doc.getText(0,doc.getLength());
//            } catch (BadLocationException ex) {
//                ex.printStackTrace();
//            }
//            System.out.println(x);
//            RTFEditorKit kit = new RTFEditorKit();
//            try {
//                kit.write(b, doc, 0, doc.getLength());
//            } catch (IOException | BadLocationException ex) {
//                ex.printStackTrace();
//            }
//            try {
//                out.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//
//            String rtfContent = b.toString();
//            System.out.println(rtfContent);
//            {
//                // replace "Monospaced" by a well-known monospace font
//                rtfContent = rtfContent.replaceAll("Monospaced", "Courier New");
//                final StringBuffer rtfContentBuffer = new StringBuffer(rtfContent);
//                final int endProlog = rtfContentBuffer.indexOf("\n\n");
//                // set a good Line Space and no Space Before or Space After each paragraph
////                rtfContentBuffer.insert(endProlog, "\n\\sl240");
////                rtfContentBuffer.insert(endProlog, "\n\\sb0\\sa0");
////                rtfContent = rtfContentBuffer.toString();
//            }
//
//            //final File file = new File("c:\\temp\\test.rtf");
//            FileOutputStream fos = null;
//            try {
//                JFileChooser j = new JFileChooser("f:");
//                // Simple SaveDialog function
//                int r = j.showSaveDialog(null);
//                if (r == JFileChooser.APPROVE_OPTION) {
//                    File fi = new File(j.getSelectedFile().getAbsolutePath());
//                    fos = new FileOutputStream(fi);
//                }
//            } catch (FileNotFoundException ex) {
//                ex.printStackTrace();
//            }
//            try {
//                assert fos != null;
//                fos.write(rtfContent.getBytes());
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            try {
//                fos.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }

//            JFileChooser j = new JFileChooser("f:");
//            // Simple SaveDialog function
//            int r = j.showSaveDialog(null);
//            if (r == JFileChooser.APPROVE_OPTION) {
//                File fi = new File(j.getSelectedFile().getAbsolutePath());
//                Path path = Paths.get(String.valueOf(fi));
//                byte[] content = new byte[0];
//                content = textArea.getText().getBytes();
//                String str = new String(content, StandardCharsets.ISO_8859_1);
//                content = str.getBytes(StandardCharsets.ISO_8859_1);
//                try {
//                    Files.write(path, content);
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }

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
