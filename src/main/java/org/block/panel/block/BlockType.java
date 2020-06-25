package org.block.panel.block;

public interface BlockType<B extends Block> {

    B build(int x, int y);

    default B buildDefault(int x, int y){
        return build(x, y);
    }
}
