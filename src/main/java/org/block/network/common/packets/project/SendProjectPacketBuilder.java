package org.block.network.common.packets.project;

import org.block.network.common.Connection;
import org.block.network.common.packets.Packet;
import org.block.project.module.Module;
import org.block.project.module.project.Project;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Optional;

public class SendProjectPacketBuilder implements Packet.PacketBuilder {

    private Project project;
    private LocalTime time = LocalTime.now();

    public static final String MODULE_ID = "ModuleID";
    public static final String MODULE_VERSION = "ModuleVer";

    public LocalTime getTime(){
        return this.time;
    }

    public SendProjectPacketBuilder setTime(LocalTime time){
        this.time = time;
        return this;
    }

    public Optional<Project> getProject(){
        return Optional.ofNullable(this.project);
    }

    public Optional<Module> getModule(){
        if(this.project == null){
            return Optional.empty();
        }
        if(this.project instanceof Project.Loaded){
            return Optional.of(((Project.Loaded)this.project).getModule());
        }
        try {
            return Optional.of(this.project.getExpectedModule());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public SendProjectPacketBuilder setProject(Project project){
        this.project = project;
        return this;
    }

    @Override
    public String build(Connection connection) {
        if(this.project == null){
            throw new IllegalStateException("Project not set");
        }
        Module module = this.getModule().get();
        return this.time + "\t" + MODULE_ID + ": " + module.getId() + " | " + MODULE_VERSION + ": " + module.getVersion();
    }

    @Override
    public Packet getPacket() {
        return Packet.SEND_PROJECT_PACKET;
    }
}
