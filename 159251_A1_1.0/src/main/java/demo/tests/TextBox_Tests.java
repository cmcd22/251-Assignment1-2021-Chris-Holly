package demo.tests;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.odftoolkit.simple.form.TextBox;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;

public class TextBox_Tests{
    TextArea textArea;
    TextBox tb;
    //Unit testing class
    @Before
    public void setUp() throws Exception{

    }
    @After
    public void tearDown() throws Exception{

    }
    // Test that text area matches expected result.
    @Test
    public void testTextArea(){
        textArea.setText("Test");
        String expResult = "Test";
        assertEquals(expResult, textArea.getText());
    }
    @Test
    public void test2(){

    }
}
