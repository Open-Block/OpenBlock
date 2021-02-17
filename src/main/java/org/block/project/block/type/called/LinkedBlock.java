package org.block.project.block.type.called;

import org.block.project.block.Block;

import java.util.Optional;

public interface LinkedBlock<B extends Block> {

    Optional<B> getLinkedBlock();
}
