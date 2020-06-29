package org.block.panel.block.input;

import javax.swing.*;
import java.awt.*;

public class OpenBlockDialog<T extends Container> extends JDialog {

    public OpenBlockDialog(Window window, T show){
        this(window, show, "");
    }

    public OpenBlockDialog(Window window, T show, String title){
        super(window, title, ModalityType.APPLICATION_MODAL);
        this.setContentPane(show);
        this.setResizable(false);
        this.pack();
        init(window);
    }

    private void init(Window win){
        int winWidth = win.getWidth();
        int winHeight = win.getHeight();
        int winCenX = winWidth / 2;
        int winCenY = winHeight / 2;
        int diaWidth = this.getWidth();
        int diaHeight = this.getHeight();
        int diaCenX = diaWidth / 2;
        int diaCenY = diaHeight / 2;
        int newX = (win.getX() + winCenX) - diaCenX;
        int newY = (win.getY() + winCenY) - diaCenY;
        this.setBounds(newX, newY, diaWidth, diaHeight);
    }

    @Override
    public T getContentPane() {
        return (T)super.getContentPane();
    }

    @Deprecated
    @Override
    public void setContentPane(Container contentPane) {
        super.setContentPane(contentPane);
    }
}
