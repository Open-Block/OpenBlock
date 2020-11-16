package org.block.serialization;

import org.block.serialization.parse.Parser;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ConfigTest {

    public static void main(String[] args) throws IOException {
        File file = new File("test/test.json");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        ConfigNode node = ConfigImplementation.JSON.load(file.toPath());
        ConfigNode node2 = node.getNode("array", "another", "test");

        Random random = new Random();
        /*char[] chars = new char[random.nextInt(12)];
        for(int A = 0; A < chars.length; A++){
            int ascii = 97 + random.nextInt(26);
            chars[A] = (char)ascii;
        }*/
        Integer[] array = new Integer[random.nextInt(20)];
        for(int A = 0; A < array.length; A++){
            array[A] = random.nextInt(10000);
        }
        /*String value = String.valueOf(chars);
        node.setValue("Test", value);*/
        node2.setCollection("Numbers", Parser.INTEGER, array);
        ConfigImplementation.JSON.write(node, file.toPath());

    }
}
