package org.block.util;

import org.array.utils.ArrayUtils;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.module.project.Project;
import org.block.project.panel.inproject.MainDisplayPanel;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.ConfigNode;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class BlockUtils {

    public static Set<Block> load(Project.Loaded project, Consumer<Block> loaded){
        return load(project, project.getDirectory(), loaded);
    }

    public static Set<Block> load(Project.Loaded project, File folder, Consumer<Block> loaded){
        List<BlockType<? extends Block>> blockTypes = project.getPanel().getChooserPanel().getBlockTypes();
        Map<File, BlockType<? extends Block>> depends = new HashMap<>();
        Set<Block> load = new HashSet<>();
        blockTypes.stream().forEach(t -> {
            File typeFolder = new File(folder, t.saveLocation().getPath());
            if(!typeFolder.exists()){
                return;
            }
            File[] files = typeFolder.listFiles(pathname -> pathname.getName().endsWith(".json"));
            if(files == null){
                return;
            }
            for(File file : files){
                ConfigNode node;
                try {
                    node = ConfigImplementation.JSON.load(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                List<UUID> dependsOn = BlockType.TITLE_DEPENDS.deserialize(node).get();
                if(!dependsOn.stream().allMatch(d -> load.stream().anyMatch(b -> b.getUniqueId().equals(d)))){
                    depends.put(file, t);
                    continue;
                }
                try {
                    Block block = t.build(node);
                    if(block == null){
                        System.err.println("Could not load '" + file.getName() + "' as a '" + t.getName() + "'");
                        continue;
                    }
                    handle(block, project, load, loaded);
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        });
        while(!depends.isEmpty()){
            for(Map.Entry<File, BlockType<? extends Block>> entry : depends.entrySet()){
                ConfigNode node;
                try {
                    node = ConfigImplementation.JSON.load(entry.getKey().toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                List<UUID> dependsOn = BlockType.TITLE_DEPENDS.deserialize(node).get();
                if(!dependsOn.stream().allMatch(d -> load.stream().anyMatch(b -> b.getUniqueId().equals(d)))){
                    continue;
                }
                try {
                    Block block = entry.getValue().build(node);
                    handle(block, project, load, loaded);
                    depends.remove(entry.getKey());
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        }
        return load;
    }

    private static void handle(Block block, Project.Loaded project, Set<Block> load, Consumer<Block> loaded){
        load.add(block);
        while(project.getPanel().getBlockPanel().getBlocks().contains(block)){
            block.setLayer(block.getLayer() + 1);
        }
        System.out.println("Registering block with " + block.getLayer());
        project.getPanel().getBlockPanel().register(block);
        loaded.accept(block);
    }
}
