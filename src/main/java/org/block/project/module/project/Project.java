package org.block.project.module.project;

import javafx.scene.shape.Rectangle;
import org.block.project.module.Module;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.FixedTitle;
import org.block.serialization.json.JSONConfigNode;
import org.block.serialization.parse.Parser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface Project {

    FixedTitle<Module> CONFIG_MODULE = new FixedTitle<>("id", Parser.MODULE, "Meta", "Module");
    FixedTitle<String> CONFIG_MODULE_VERSION = new FixedTitle<>("version", Parser.STRING, "Meta", "Module");
    FixedTitle<String> CONFIG_PROJECT_NAME = new FixedTitle<>("name", Parser.STRING, "Meta");
    FixedTitle.Listable<String> CONFIG_PROJECT_VERSION = new FixedTitle.Listable<>("versions", Parser.STRING, "Meta");
    FixedTitle<Rectangle> CONFIG_PROJECT_WINDOW_LOCATION = new FixedTitle<>("Location", Parser.RECTANGLE, "Meta", "Graphics");
    FixedTitle<Integer> CONFIG_PROJECT_WINDOW_DISPLAY = new FixedTitle<>("display", Parser.INTEGER, "Meta", "Graphics");

    /**
     * Gets the folder that the project is located in
     *
     * @return The folder for all things related to this project
     */
    File getDirectory();

    /**
     * Gets the name of the module
     *
     * @return The name of the module
     * @throws IOException           If the file cannot be opened
     * @throws IllegalStateException If the file doesn't contain a name
     */
    default String getDisplayName() throws IOException {
        JSONConfigNode json = ConfigImplementation.JSON.load(getFile().toPath());
        Optional<String> opTitle = CONFIG_PROJECT_NAME.deserialize(json);
        if (opTitle.isPresent()) {
            return opTitle.get();
        }
        throw new IllegalStateException("Could not find title");
    }

    /**
     * Gets the default position and resolution of the window
     *
     * @return The Rectangle of the window
     */
    default Rectangle getPreferredSize() {
        try {
            JSONConfigNode json = ConfigImplementation.JSON.load(getFile().toPath());
            return CONFIG_PROJECT_WINDOW_LOCATION.deserialize(json).orElse(new Rectangle(0, 0, 600, 800));
        } catch (IOException e) {
            return new Rectangle(0, 0, 600, 800);
        }
    }

    /**
     * Gets the expected module for the project
     *
     * @return The expected module for the project
     * @throws IOException           If the file cannot be opened
     * @throws IllegalStateException If the module cannot be found
     */
    default Module getExpectedModule() throws IOException {
        JSONConfigNode json = ConfigImplementation.JSON.load(getFile().toPath());
        Optional<Module> opModule = CONFIG_MODULE.deserialize(json);
        if (opModule.isPresent()) {
            return opModule.get();
        }
        throw new IllegalStateException("Could not find module");
    }

    /**
     * Gets the OpenBlocks file that holds the basic information about the project
     *
     * @return The file location to the OpenBlocks config file for this project
     */
    default File getFile() {
        return new File(getDirectory(), "OpenBlocks.json");
    }

    interface Loaded extends Project {

        /**
         * Gets the module that has the project loaded, if the project is not loaded then this should return {@link Project#getExpectedModule()}
         *
         * @return The module of the project
         */
        Module getModule();
    }

}
