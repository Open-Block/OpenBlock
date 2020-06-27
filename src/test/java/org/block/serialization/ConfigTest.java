package org.block.serialization;

import org.block.serializtion.ConfigImplementation;
import org.block.serializtion.ConfigNode;
import org.block.serializtion.parse.Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        System.out.println("Loading file: " + file.getAbsolutePath());
        ConfigNode node = ConfigImplementation.JSON.load(file.toPath());
        //System.out.println("String: Test: " + node.getString("Test"));

        ConfigNode node2 = node.getNode("array", "another", "test");
        System.out.println("Array: Numbers: " + node2.getCollection("Numbers", Parser.INTEGER));



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
        System.out.println("Writing: Test: " + value);
        System.out.println("Writing: Array: ");
        node.setValue("Test", value);*/
        System.out.println("Node: " + node2.getClass().getSimpleName());
        node2.setCollection("Numbers", Parser.INTEGER, array);
        System.out.println("Saving");
        ConfigImplementation.JSON.write(node, file.toPath());

    }
}
