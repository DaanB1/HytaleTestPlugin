package com.daanb.testplugin;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.modules.projectile.ProjectileModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * The system that spawns the projectile.
 * todo: spawn from entity head position instead of feet position
 * todo: fix weird bug that causes server to freeze (possibly due to ref being wrong)
 */
public class SpawnProjectileSystem extends EntityTickingSystem<EntityStore> {

    @Override
    public void tick(float dt, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk, @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        SpawnProjectileComponent proj = archetypeChunk.getComponent(index, SpawnProjectileComponent.getComponentType());
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        ProjectileModule.get().spawnProjectile(ref, commandBuffer, proj.getConfig(), proj.getPosition(), proj.getRotation());
        commandBuffer.removeComponent(ref, SpawnProjectileComponent.getComponentType());
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(SpawnProjectileComponent.getComponentType());
    }

}
