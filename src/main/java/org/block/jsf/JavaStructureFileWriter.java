package org.block.jsf;

import java.io.File;
import java.io.InputStream;

public class JavaStructureFileWriter {

    private final InputStream stream;

    public JavaStructureFileWriter(InputStream file){
        this.stream = file;
    }

    public InputStream getStream(){
        return this.stream;
    }


}
