package com.daanb.testplugin.commands.firebarrage;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class FireBarrage extends AbstractCommandCollection {
    public FireBarrage() {
        String name = "fire";
        String description = "A command to test out the custom ProjectileEmitter";
        super(name, description);
        addSubCommand(new StartFireBarrage());
        addSubCommand(new StopFireBarrage());
        addSubCommand(new Doom());
    }

}
