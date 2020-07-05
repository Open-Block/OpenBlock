package org.block.project.exception;

import org.block.project.block.Block;

import java.io.IOException;

public class InvalidBlockException extends IOException {

    private final Block block;
    private final String reason;

    public InvalidBlockException(Block block, String reason){
        super("Invalid block (X: " + block.getX() + " Y: " + block.getY() + " Type: " + block.getType().getName() + "): " + reason);
        this.block = block;
        this.reason = reason;
    }

    public Block getBlock(){
        return this.block;
    }

    public String getReason(){
        return this.reason;
    }
}
