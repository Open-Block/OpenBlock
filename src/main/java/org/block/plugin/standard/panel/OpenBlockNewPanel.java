package org.block.plugin.standard.panel;

import org.block.plugin.PluginContainer;
import org.block.project.module.project.UnloadedProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class OpenBlockNewPanel extends JPanel {

    public class OnClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String path = OpenBlockNewPanel.this.pathField.getText();
            UnloadedProject project = new UnloadedProject(new File(path.substring(0, path.length() - "OpenBlocks.json".length())));
            project.setTempModule(PluginContainer.OPEN_BLOCK_MODULE);
            project.setTempName(OpenBlockNewPanel.this.nameField.getText());
            project.addTempVersions(OpenBlockNewPanel.this.versionField.getText());
            try {
                project.saveTempData();
                SwingUtilities.getWindowAncestor(OpenBlockNewPanel.this).dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return;
            }
        }
    }

    public class OnKey implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            new Thread(() -> {
                if(OpenBlockNewPanel.this.nameField.getText().length() == 0){
                    OpenBlockNewPanel.this.pathField.setText(new File("Projects/OpenBlock.json").getAbsolutePath());
                    OpenBlockNewPanel.this.acceptButton.setEnabled(false);
                }else{
                    OpenBlockNewPanel.this.pathField.setText(new File("Projects/" + OpenBlockNewPanel.this.nameField.getText() + "/OpenBlock.json").getAbsolutePath());
                    OpenBlockNewPanel.this.acceptButton.setEnabled(true);
                }
            }).start();
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private JTextField nameField = new JTextField();
    private JTextField versionField = new JTextField("1.0.0");
    private JTextField pathField = new JTextField(new File("Projects/OpenBlock.json").getAbsolutePath());
    private JButton cancelButton = new JButton("Cancel");
    private JButton acceptButton = new JButton("Create");

    public OpenBlockNewPanel(){
        init();
    }

    private void init(){
        this.pathField.setEditable(false);
        this.acceptButton.addActionListener(new OnClick());
        this.nameField.addKeyListener(new OnKey());
        this.acceptButton.setEnabled(false);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        this.add(new JLabel("Name"), c);
        c.gridy = 1;
        this.add(new JLabel("Version"), c);
        c.gridy = 2;
        this.add(new JLabel("Location"), c);
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 1.0;
        c.gridwidth = 2;
        this.add(this.nameField, c);
        c.gridy = 1;
        this.add(this.versionField, c);
        c.gridy = 2;
        this.add(this.pathField, c);
        c.gridx = 0;
        c.gridy = 3;
        this.add(this.cancelButton, c);
        c.gridx = 2;
        this.add(this.acceptButton, c);
    }
}
