package org.block.util;

import org.block.project.block.Block;

import java.io.File;
import java.util.*;

public interface GeneralUntil {

    static String[] getImport(Block.ValueBlock<?> value){
        Class<?> clazz = null;
        String nested = null;
        do{
            Class<?> clazz2 = (clazz == null) ? value.getExpectedValue() : clazz.getDeclaringClass();
            if(clazz2 == null){
                break;
            }
            if(nested == null){
                nested = clazz2.getSimpleName();
            }else{
                nested = clazz2.getSimpleName() + "." + nested;
            }
            clazz = clazz2;
        }while(true);

        if(clazz == null){
            throw new IllegalStateException("No value block attached");
        }
        return new String[]{clazz.getPackage().getName(), nested};
    }

    static String formatToClassName(String name){
        return formatTypicalName(name, true);
    }

    static String formatToMethodName(String name){
        return formatToLocalVariableName(name);
    }

    static String formatToLocalVariableName(String name){
        return formatTypicalName(name, false);
    }

    static String formatTypicalName(String name, boolean shouldUpper){
        String ret = "";
        for(int A = 0; A < name.length(); A++){
            char at = name.charAt(A);
            if(at == ' '){
                shouldUpper = true;
                continue;
            }
            if(shouldUpper){
                ret += Character.toUpperCase(at);
            }else{
                ret += Character.toLowerCase(at);
            }
        }
        return ret;
    }

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
