package org.util.print;

import java.io.*;

public class ShinyOutputStream extends OutputStream {

    private final File debugFile;
    private final FileWriter debugStream;
    private final File simpleFile;
    private final FileWriter simpleStream;
    private final PrintStream console;
    private String buffer = "";
    private boolean bufferError = false;

    public ShinyOutputStream(File simple, File debug) {
        this(System.out, simple, debug);
    }

    public ShinyOutputStream(PrintStream stream, File simple, File debug) {
        this.debugFile = debug;
        this.simpleFile = simple;
        this.console = stream;
        FileWriter simpleWriter = null;
        try {
            simpleWriter = new FileWriter(this.simpleFile);
        } catch (IOException ignore) {
        }
        FileWriter debugWriter = null;
        try {
            debugWriter = new FileWriter(this.debugFile);
        } catch (IOException ignore) {
        }
        this.simpleStream = simpleWriter;
        this.debugStream = debugWriter;
    }

    @Override
    public void write(int b) throws IOException {
        write(new int[]{b}, 0, 1);
    }

    public void write(int[] bytes, int offset, int length){
        String character = new String(bytes, offset, length);
        this.buffer = this.buffer + character;
        if(this.console != null) {
            this.console.print(character);
        }
        try {
            if(this.debugStream != null) {
                this.debugStream.write(character);
            }
        } catch (IOException ignore) {
        }
        if(character.charAt(0) == '\n'){
            boolean isNumberAtEnd = false;
            if(this.buffer.length() >= 4) {
                try {
                    Integer.parseInt(this.buffer.charAt(this.buffer.length() - 4) + "");
                    isNumberAtEnd = true;
                } catch (NumberFormatException ignored) {
                }
            }

            if(!(this.buffer.startsWith("\t") && isNumberAtEnd)){
                this.bufferError = false;
            }
            try {
                if(this.debugStream != null) {
                    this.debugStream.flush();
                }
            } catch (IOException ignore) {
            }
            if(this.simpleStream != null && !this.bufferError) {
                try {
                    this.simpleStream.write(this.buffer);
                    this.simpleStream.flush();
                } catch (IOException e) {
                }
            }
            if(this.buffer.contains("Exception") || this.buffer.contains("Error")){
                this.bufferError = true;
            }
            this.buffer = "";
        }
    }

    public static void createDefault(){
        createDefault( new File("logs/Simple.txt"), new File("logs/Debug.txt"));
    }

    public static void createDefault(File simple, File debug) {
        try {
            simple.getParentFile().mkdirs();
            simple.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            debug.getParentFile().mkdirs();
            debug.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintStream stream = new PrintStream(new ShinyOutputStream(simple, debug));
        System.setErr(stream);
        System.setOut(stream);
    }
}
