package org.block.network.common.packets.project;

import org.block.network.common.Connection;
import org.block.network.common.packets.Packet;
import org.block.plugin.Plugin;
import org.block.project.Project;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Optional;

public class SendProjectPacketBuilder implements Packet.PacketBuilder {

    public static final String PLUGIN_ID = "ModuleID";
    public static final String PLUGIN_VERSION = "ModuleVer";
    private Project project;
    private LocalTime time = LocalTime.now();

    public LocalTime getTime() {
        return this.time;
    }

    public SendProjectPacketBuilder setTime(LocalTime time) {
        this.time = time;
        return this;
    }

    public Optional<Project> getProject() {
        return Optional.ofNullable(this.project);
    }

    public Optional<Plugin> getPlugin() {
        if (this.project == null) {
            return Optional.empty();
        }
        if (this.project instanceof Project.Loaded) {
            return Optional.of(((Project.Loaded) this.project).getPlugin());
        }
        try {
            return Optional.of(this.project.getExpectedPlugin());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public SendProjectPacketBuilder setProject(Project project) {
        this.project = project;
        return this;
    }

    @Override
    public String build(Connection connection) {
        if (this.project == null) {
            throw new IllegalStateException("Project not set");
        }
        var plugin = this.getPlugin().orElseThrow();
        return this.time + "\t" + PLUGIN_ID + ": " + plugin.getId() + " | " + PLUGIN_VERSION + ": " + plugin.getVersion();
    }

    @Override
    public Packet getPacket() {
        return Packet.SEND_PROJECT_PACKET;
    }
}
