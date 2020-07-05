package org.block.project.panel;

import org.block.Blocks;
import org.block.plugin.standard.panel.OpenBlockNewPanel;
import org.block.project.module.Module;
import org.block.project.module.project.Project;
import org.block.project.module.project.UnloadedProject;
import org.block.util.GeneralUntil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

public class ProjectsPanel extends JPanel {

    public class ClickProject implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            Optional<UnloadedProject> opProject = ProjectsPanel.this.projects.stream().filter(p -> {
                try {
                    return p.getDisplayName().equals(label.getText());
                } catch (IOException ioException) {
                    return ("Corrupt (" + p.getDirectory().getName() + ")").equals(label.getText());
                }
            }).findFirst();
            if(!opProject.isPresent()){
                System.err.println("Clicked element of '" + label.getText() + "' does not have a project linked to it????");
            }else {
                ProjectsPanel.this.selected = opProject.get();
            }
            ProjectsPanel.this.updateProjects();
            ProjectsPanel.this.updateSettings();
            ProjectsPanel.this.repaint();
            ProjectsPanel.this.revalidate();


        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public class Search implements Runnable {

        @Override
        public void run() {
            File file = ProjectsPanel.this.projectsDirectory;
            while(true) {
                File[] files = file.listFiles(File::isDirectory);
                if (files == null) {
                    continue;
                }
                for(File folder : files){
                    File openBlockFile = new File(folder, "OpenBlocks.json");
                    if(!openBlockFile.exists()){
                        System.err.println("Invalid folder found: " + openBlockFile.getAbsolutePath());
                        continue;
                    }
                    if (ProjectsPanel.this.projects.stream().anyMatch(p -> p.getDirectory().equals(folder))){
                        continue;
                    }
                    ProjectsPanel.this.projects.add(new UnloadedProject(folder));
                    ProjectsPanel.this.updateProjects();
                    ProjectsPanel.this.repaint();
                    ProjectsPanel.this.revalidate();
                }
            }
        }
    }

    public static class Bar extends JMenuBar {

        public Bar(){
            init();
        }

        private void init(){
            this.add(createNew());
        }

        private JMenu createNew(){
            JMenu menu = new JMenu("+");
            Blocks.getInstance().getAllEnabledPlugins().getAll(p -> p.getModules()).parallelStream().forEach(m -> {
                JMenuItem item = new JMenuItem(m.getDisplayName());
                item.addActionListener((a) -> {
                    JDialog dialog = new JDialog();
                    dialog.setContentPane(m.createProjectCreator());
                    dialog.pack();
                    dialog.setVisible(true);
                });
                menu.add(item);
            });
            return menu;
        }
    }

    private final File projectsDirectory;
    private final TreeSet<UnloadedProject> projects = new TreeSet<>(Comparator.comparing(p -> {
        try {
            return p.getDisplayName();
        } catch (IOException e) {
            //PUTS TO BOTTOM OF LIST - This shouldn't be needed but just incase
            return "ZZZZZZZ";
        }
    }));
    private final JPanel projectsPanel = new JPanel();
    private final JPanel projectSettings = new JPanel();
    private UnloadedProject selected = null;
    private final Thread searchingThread = new Thread(new Search());


    private final JButton loadButton = new JButton("Load");
    private final JButton deleteButton = new JButton("Delete");
    private final JLabel displayNameLabel = new JLabel("", SwingUtilities.RIGHT);
    private final JLabel displayNameTagLabel = new JLabel("Name:");

    public ProjectsPanel(){
        this(new File("Projects"));
    }

    public ProjectsPanel(File projectsDirectory){
        this.projectsDirectory = projectsDirectory;
        init();
    }

    private void init(){
        this.deleteButton.addActionListener((a) -> {
            GeneralUntil.getFiles(this.selected.getDirectory()).parallelStream().forEach(f -> f.delete());
            ProjectsPanel.this.updateProjects();
            ProjectsPanel.this.updateSettings();
        });
        this.loadButton.addActionListener((a) -> {
            new Thread(() -> ProjectsPanel.this.loadProject()).start();
        });
        this.setBackground(Color.GRAY);
        this.setOpaque(true);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 1);
        this.add(this.projectsPanel, c);
        c.gridx = 1;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 0, 0);
        this.add(this.projectSettings, c);
        updateProjects();
        updateSettings();
        this.searchingThread.start();
    }

    public void updateProjects(){
        this.projectsPanel.removeAll();
        this.projectsPanel.setLayout(new GridLayout(1, this.projects.size()));
        this.projects.forEach(p -> {
            JLabel label;
            try {
                label = new JLabel(p.getDisplayName());
            } catch (IOException e) {
                label = new JLabel("Corrupt (" + p.getDirectory().getName() + ")");
            }
            label.addMouseListener(new ClickProject());
            if(this.selected != null && this.selected.equals(p)){
                label.setBackground(Color.BLACK);
                label.setForeground(Color.WHITE);
                label.setOpaque(true);
            }else{
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
                label.setOpaque(false);
            }
            this.projectsPanel.add(label);
        });
    }

    public void updateSettings(){
        this.projectSettings.removeAll();
        this.projectSettings.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        if(this.selected != null){
            this.loadButton.setEnabled(true);
            try {
                this.displayNameLabel.setText(this.selected.getDisplayName());
            } catch (IOException e) {
                this.displayNameLabel.setText("Corrupted");
                this.loadButton.setEnabled(false);
            }
            this.projectSettings.add(this.displayNameTagLabel, c);
            c.gridwidth = 3;
            c.gridy++;
            c.weighty = 1.0;
            this.projectSettings.add(new JPanel(), c);
            c.weighty = 0;
            c.weightx = 1.0;
            c.gridwidth = 2;
            c.gridy++;
            this.projectSettings.add(this.deleteButton, c);
            c.gridy = 0;
            c.gridx = 2;
            c.gridwidth = 1;
            this.projectSettings.add(this.displayNameLabel, c);
            c.gridy++;
            c.gridy++;
            this.projectSettings.add(this.loadButton, c);
            return;
        }
        c.weighty = 1.0;
        c.weightx = 1.0;
        JTextArea area = new JTextArea("No project selected. Select one of your loaded projects on the left or create a new one using the '+' sign at the top left");
        this.projectSettings.add(area, c);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setBackground(null);
        area.setEditable(false);
    }

    public void loadProject(){
        if(this.selected == null){
            throw new IllegalStateException("Unable to load a unknown project");
        }
        this.loadProject(this.selected);
    }

    public void loadProject(UnloadedProject project) {
        Project.Loaded loaded = project.load();
        Module module = loaded.getModule();
        JFrame frame;
        try {
            frame = new JFrame("Open Block: " + module.getDisplayName() + ": " + loaded.getDisplayName());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        frame.setContentPane(module.createPanel(loaded));
        frame.setJMenuBar(module.createToolbar(project));
        Rectangle size = loaded.getPreferredSize();
        frame.setSize(size.width, size.height);
        frame.setLocation(size.x, size.y);
        Blocks.getInstance().setWindow(frame);
        //last thing to do
        SwingUtilities.getWindowAncestor(this).dispose();
        frame.setVisible(true);
    }

}
