package org.block.util;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface GeneralUntil {

    static Set<File> getFiles(File folder){
        return getFiles(folder, new HashSet<>());
    }

    static <C extends Collection<File>> C getFiles(File folder, C collection){
        if(folder.isFile()){
            collection.add(folder);
            return collection;
        }
        if(folder.isDirectory()){
            File[] files = folder.listFiles();
            if(files == null){
                return collection;
            }
            for(File file : files){
                getFiles(file, collection);
            }
            return collection;
        }
        System.err.println("No idea what type '" + folder.getAbsolutePath() + "' is");
        return collection;
    }
}
