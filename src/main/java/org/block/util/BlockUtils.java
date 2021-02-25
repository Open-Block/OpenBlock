package org.block.util;

import org.block.Blocks;
import org.block.panel.main.FXMainDisplay;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.Project;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.ConfigNode;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class BlockUtils {

    public static Set<Block> load(Project.Loaded project, ConfigImplementation<? extends ConfigNode> impl, Consumer<Block> loaded) {
        return load(project, project.getDirectory(), impl, loaded);
    }

    public static Set<Block> load(Project.Loaded project, File folder, ConfigImplementation<? extends ConfigNode> impl, Consumer<Block> loaded) {
        FXMainDisplay panel = (FXMainDisplay) Blocks.getInstance().getWindow();
        List<BlockType<? extends Block>> blockTypes = /*project.getPanel().getChooserPanel().getBlockTypes();*/ new ArrayList<>();
        Map<File, BlockType<? extends Block>> depends = new HashMap<>();
        Set<Block> load = new HashSet<>();
        blockTypes.forEach(t -> {
            File typeFolder = new File(folder, t.saveLocation().getPath());
            if (!typeFolder.exists()) {
                return;
            }
            File[] files = typeFolder.listFiles(pathname -> pathname.getName().endsWith(".json"));
            if (files == null) {
                return;
            }
            for (File file : files) {
                ConfigNode node;
                try {
                    node = impl.load(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                List<UUID> dependsOn = BlockType.TITLE_DEPENDS.deserialize(node).get();
                if (!dependsOn.stream().allMatch(d -> load.stream().anyMatch(b -> b.getUniqueId().equals(d)))) {
                    depends.put(file, t);
                    continue;
                }
                String clazzName = BlockType.TITLE_CLASS.deserialize(node).orElse("Global");
                if (clazzName.length() == 0) {
                    clazzName = "Global";
                }
                final String finalClassName = clazzName;
                FXMainDisplay bdPanel = (FXMainDisplay) Blocks.getInstance().getWindow();


                try {
                    Block block = t.build(node);
                    if (block == null) {
                        System.err.println("Could not load '" + file.getName() + "' as a '" + t.getName() + "'. Failed to build. Block build returned null in '" + t.getClass().getName() + "'. This is a generic message, as a developer you should provide more checks for what is going wrong.");
                        continue;
                    }
                    handle(block, bdPanel, project, load, loaded);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        Set<File> dependsRemove = new HashSet<>();
        while (!depends.isEmpty()) {
            for (Map.Entry<File, BlockType<? extends Block>> entry : depends.entrySet()) {
                ConfigNode node;
                try {
                    node = impl.load(entry.getKey().toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                List<UUID> dependsOn = BlockType.TITLE_DEPENDS.deserialize(node).get();
                if (!dependsOn.stream().allMatch(d -> load.stream().anyMatch(b -> b.getUniqueId().equals(d)))) {
                    continue;
                }
                String clazzName = BlockType.TITLE_CLASS.deserialize(node).orElse("Global");
                if (clazzName.length() == 0) {
                    clazzName = "Global";
                }
                final String finalClassName = clazzName;
                FXMainDisplay bdPanel = (FXMainDisplay) Blocks.getInstance().getWindow();

                try {
                    Block block = entry.getValue().build(node);
                    handle(block, bdPanel, project, load, loaded);
                    dependsRemove.add(entry.getKey());
                } catch (IllegalStateException e) {
                    bdPanel.getDisplayingBlocks().forEach(b -> {
                        System.out.println("Block: " + b.getType().getName() + " UUID: " + b.getUniqueId());
                    });
                    e.printStackTrace();
                }
            }
            dependsRemove.forEach(depends::remove);
            dependsRemove.clear();
        }
        return load;
    }

    private static void handle(Block block, FXMainDisplay panel, Project.Loaded project, Set<Block> load, Consumer<Block> loaded) {
        load.add(block);

        while (panel.getDisplayingBlocks().contains(block)) {
            block.setLayer(block.getLayer() + 1);
        }
        //TODO handle all

        panel.getDisplayingBlocks().add(block);
        loaded.accept(block);
    }
}
