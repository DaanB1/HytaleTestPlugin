package com.daanb.testplugin.commands.firebarrage;

import com.daanb.testplugin.ProjectileEmitter;
import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

public class StopFireBarrage extends AbstractPlayerCommand {

    public StopFireBarrage() {
        String name = "stop";
        String description = "Removes all projectile emitter components";
        super(name, description);
    }

    @Override
    protected void execute(@NonNull CommandContext commandContext, @NonNull Store<EntityStore> store, @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {
        Query<EntityStore> query = Archetype.of(ProjectileEmitter.getComponentType());
        store.forEachEntityParallel(query, (index, archetypeChunk, commandBuffer) -> {
            Ref<EntityStore> entityRef = archetypeChunk.getReferenceTo(index);
            commandBuffer.removeComponent(entityRef, ProjectileEmitter.getComponentType());
        });
    }
}
