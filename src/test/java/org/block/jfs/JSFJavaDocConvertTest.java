package org.block.jfs;

import org.block.jsf.JavaStructureDocReader;
import org.block.jsf.data.JSFPart;
import org.block.jsf.data.jsfclass.JSFClass;
import org.block.serialization.json.JSONConfigImplementation;
import org.block.serialization.json.JSONConfigNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class JSFJavaDocConvertTest {

    @Test
    public void docReadTest(){
        InputStream stream = JSFJavaDocConvertTest.class.getClassLoader().getResourceAsStream("File.html");
        if(stream == null){
            Assertions.fail("Files.html could not be found");
            return;
        }
        JavaStructureDocReader reader;
        try {
            reader = JavaStructureDocReader.valueOf(stream);
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Could not ready HTML file");
            return;
        }
        JSFClass read = reader.read();

        JSONConfigNode node = JSONConfigImplementation.JSON.createEmptyNode();
        JSFPart.TITLE_CLASSES.serialize(node, Collections.singletonList(read));
        System.out.println(JSONConfigImplementation.JSON.write(node));
        Assertions.assertNotEquals(null, read);
    }
}
