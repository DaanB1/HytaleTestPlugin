package com.daanb.testplugin.commands.firebarrage;

import com.daanb.testplugin.ProjectileEmitter;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.spatial.SpatialResource;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.projectile.config.ProjectileConfig;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jspecify.annotations.NonNull;

public class Doom extends AbstractPlayerCommand {

    private final int RADIUS = 50;
    private final int HEIGHT = 100;

    public Doom() {
        String name = "doom";
        String description = "Every entity will start shooting fireballs!";
        super(name, description);
    }

    @Override
    protected void execute(@NonNull CommandContext commandContext, @NonNull Store<EntityStore> store, @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {
        ProjectileConfig config = ProjectileConfig.getAssetMap().getAsset("Projectile_Config_Custom");
        if (config == null) { return; }

//        ProjectileEmitter emitter = new ProjectileEmitter(config, true, 10,5, 0.1f);
//        Query<EntityStore> query = Query.and(TransformComponent.getComponentType(), UUIDComponent.getComponentType());
//        store.forEachEntityParallel(query, (index, archetypeChunk, commandBuffer) -> {
//            commandBuffer.addComponent(archetypeChunk.getReferenceTo(index), ProjectileEmitter.getComponentType(), emitter);
//        });

        //gets all entities in radius. took a while to figure this out
        ObjectList<Ref<EntityStore>> results = SpatialResource.getThreadLocalReferenceList();
        SpatialResource<Ref<EntityStore>, EntityStore> entitySpatialResource = store.getResource(EntityModule.get().getEntitySpatialResourceType());
        entitySpatialResource.getSpatialStructure().collectCylinder(playerRef.getTransform().getPosition(), RADIUS, HEIGHT, results);

        ObjectList<Ref<EntityStore>> entitiesCopy = new ObjectArrayList<>(results);

        world.execute(() -> {
            for (Ref<EntityStore> entityRef : entitiesCopy) {
                TransformComponent transform = store.getComponent(entityRef, TransformComponent.getComponentType());
                if (transform == null) continue;

                UUIDComponent uuidComponent = store.getComponent(entityRef, UUIDComponent.getComponentType());
                if (uuidComponent == null) continue;

                ProjectileEmitter emitter = new ProjectileEmitter(config, true, 10,5, 0.1f);
                store.putComponent(entityRef, ProjectileEmitter.getComponentType(), emitter);
            }
        });

    }
}
