package com.daanb.testplugin.commands.firebarrage;

import com.daanb.testplugin.ProjectileEmitter;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.projectile.config.ProjectileConfig;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

public class StartFireBarrage extends AbstractPlayerCommand {

    public StartFireBarrage() {
        String name = "start";
        String description = "Starts an infinite barrage of fireballs";
        super(name, description);
    }

    @Override
    protected void execute(@NonNull CommandContext commandContext, @NonNull Store<EntityStore> store, @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {

        ProjectileConfig config = ProjectileConfig.getAssetMap().getAsset("Projectile_Config_Custom");
        if (config == null) { return; }

        ProjectileEmitter component = new ProjectileEmitter(config, true, 10,5, 0.1f);
        store.putComponent(ref, ProjectileEmitter.getComponentType(), component);

    }

}
