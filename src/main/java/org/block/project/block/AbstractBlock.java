package org.block.project.block;

import org.block.project.block.type.called.CodeStartBlock;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractBlock implements Block {

    protected UUID id;
    protected CodeStartBlock codeStartBlock;

    public AbstractBlock() {
        this.id = UUID.randomUUID();
    }

    @Override
    public Optional<CodeStartBlock> getParent() {
        return Optional.ofNullable(this.codeStartBlock);
    }

    @Override
    public void setParent(CodeStartBlock block) {
        this.codeStartBlock = block;
    }

    @Override
    public UUID getUniqueId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Block)) {
            return false;
        }
        Block block = (Block) obj;
        return this.getUniqueId().equals(block.getUniqueId());
    }
}
