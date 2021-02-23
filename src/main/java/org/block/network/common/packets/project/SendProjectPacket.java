package org.block.network.common.packets.project;

import org.block.Blocks;
import org.block.network.common.Connection;
import org.block.network.common.packets.Packet;
import org.block.panel.main.FXMainDisplay;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.module.project.Project;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.network.NetworkConfigNode;

import java.util.List;

public class SendProjectPacket implements Packet {
    @Override
    public String getId() {
        return "Project";
    }

    @Override
    public void onReceive(Connection.Direct direct) {
        System.out.println("onReceive SendProjectPacket");
        direct.onReceive(s -> {
            System.out.println("OnBlockRec: " + s);
        });
    }

    @Override
    public void onSend(Connection.Direct direct, PacketBuilder builder) {
        if (!(builder instanceof SendProjectPacketBuilder)) {
            throw new IllegalArgumentException("PacketBuilder needs to be SendProjectPacketBuilder");
        }
        SendProjectPacketBuilder projectBuilder = (SendProjectPacketBuilder) builder;
        Project project = projectBuilder.getProject().get();
        if (!(project instanceof Project.Loaded)) {
            throw new IllegalArgumentException("The project must be loaded for sending");
        }
        Project.Loaded loadedProject = (Project.Loaded) project;
        //TODO - all classes
        FXMainDisplay panel = (FXMainDisplay) Blocks.getInstance().getSceneSource();
        List<Block> blocks = panel.getDisplayingBlocks();
        String first = builder.build(direct);
        direct.sendMessage(first);
        for (Block block : blocks) {
            NetworkConfigNode node = ConfigImplementation.NETWORK.createEmptyNode();
            writeBlock(node, block);
            String text = ConfigImplementation.NETWORK.write(node);
            direct.sendMessage(text);
        }
        direct.sendMessage("End of project");
    }

    private <T extends Block> void writeBlock(NetworkConfigNode node, Block block) {
        BlockType<T> blockType = (BlockType<T>) block.getType();
        blockType.write(node, (T) block);
    }
}
