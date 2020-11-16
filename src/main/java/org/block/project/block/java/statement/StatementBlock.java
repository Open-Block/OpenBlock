package org.block.project.block.java.statement;

import org.block.project.block.Block;
import org.block.project.block.assists.BlockList;

public interface StatementBlock extends Block.AttachableBlock {

    BlockList<Block> getStatementLines();
}
