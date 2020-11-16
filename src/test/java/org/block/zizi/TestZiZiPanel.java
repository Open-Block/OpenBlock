package org.block.zizi;

import org.comp.draw.Draw;
import org.comp.draw.styles.LineStyle;
import org.comp.panel.AbstractPanel;

public class TestZiZiPanel extends AbstractPanel {

    @Override
    public int getPreferredWidth() {
        return 500;
    }

    @Override
    public int getPreferredHeight() {
        return 500;
    }

    @Override
    public void paint(Draw draw) {
        draw.drawLine(new LineStyle(), 5, 10, this.getWidth() - 10, this.getPreferredHeight() - 20);
        super.paint(draw);
    }
}
