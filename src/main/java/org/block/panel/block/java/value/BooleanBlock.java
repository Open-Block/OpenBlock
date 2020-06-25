package org.block.panel.block.java.value;

public class BooleanBlock extends AbstractValue<Boolean>{

    public BooleanBlock(int x, int y) {
        super(x, y, true, m -> m.toString());
    }

    @Override
    public String writeCode() {
        return this.getValue().toString();
    }
}
