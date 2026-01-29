package com.daanb.testplugin;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.projectile.ProjectileModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Random;

public class ProjectileEmitterSystem extends EntityTickingSystem<EntityStore> {

    private Random random = new Random();

    @Override
    public void tick(float dt, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk, @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        ProjectileEmitter emitter = archetypeChunk.getComponent(index, ProjectileEmitter.getComponentType());
        TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        HeadRotation headRotation = archetypeChunk.getComponent(index, HeadRotation.getComponentType());
        ModelComponent model = archetypeChunk.getComponent(index, ModelComponent.getComponentType());
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        if (!ref.isValid()) {
            System.err.println("Invalid entity ref: " + ref);
        }

        Vector3d position = getPosition(transform, model);
        Vector3d direction = getDirection(transform, headRotation, emitter);

        if (emitter.isRepeatingForever() || emitter.getRepetitions() > 1) {
            boolean spawnProjectile = emitter.tick();
            if(spawnProjectile) {
                ProjectileModule.get().spawnProjectile(ref, commandBuffer, emitter.getConfig(), position, direction);
            }
        } else {
            ProjectileModule.get().spawnProjectile(ref, commandBuffer, emitter.getConfig(), position, direction);
            commandBuffer.removeComponent(ref, ProjectileEmitter.getComponentType());
        }
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(ProjectileEmitter.getComponentType(), TransformComponent.getComponentType());
    }

    private Vector3d getPosition(TransformComponent transform, ModelComponent model) {
        if (model == null) {
            return transform.getPosition();
        }
        return transform.getPosition().clone().add(0, model.getModel().getEyeHeight(), 0);
    }

    private Vector3d getDirection(TransformComponent transform, HeadRotation headRotation, ProjectileEmitter emitter) {
        Vector3d direction;
        if (headRotation == null) {
            direction = transform.getTransform().getDirection();
        } else {
            direction = headRotation.getDirection();
        }

        float offsetx = random.nextFloat() * emitter.getDirectionalOffset();
        float offsety = random.nextFloat() * emitter.getDirectionalOffset();
        float offsetz = random.nextFloat() * emitter.getDirectionalOffset();
        direction.rotateX(offsetx);
        direction.rotateY(offsety);
        direction.rotateZ(offsetz);
        return direction;
    }

}
