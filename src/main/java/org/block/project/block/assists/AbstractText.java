package org.block.project.block.assists;

import org.block.project.block.AbstractBlock;
import org.block.project.block.Block;

public abstract class AbstractText extends AbstractBlock implements Block.TextBlock {

    private String text;

    public AbstractText(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

}
