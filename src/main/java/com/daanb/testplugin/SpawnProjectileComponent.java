package com.daanb.testplugin;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.projectile.config.ProjectileConfig;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Custom component that can be used to spawn projectiles. I couldn't find an easier way to spawn projectiles.
 */
public class SpawnProjectileComponent implements Component<EntityStore> {

    public static final BuilderCodec<SpawnProjectileComponent> CODEC; 
    private ProjectileConfig config;
    private Vector3d position;
    private Vector3d rotation;


    public SpawnProjectileComponent(@NonNull ProjectileConfig config, @NonNull Vector3d position, @NonNull Vector3d rotation) {
        this.position = position;
        this.rotation = rotation;
        this.config = config;
    }

    public SpawnProjectileComponent(SpawnProjectileComponent other) {
        this.position = other.position;
        this.rotation = other.rotation;
        this.config = other.config;
    }

    public SpawnProjectileComponent() {
    }

    public static ComponentType<EntityStore, SpawnProjectileComponent> getComponentType() {
        return TestPlugin.get().getSpawnProjectileComponentComponentType();
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new SpawnProjectileComponent(this);
    }

    public ProjectileConfig getConfig() {
        return config;
    }

    public Vector3d getPosition() {
        return position;
    }

    public Vector3d getRotation() {
        return rotation;
    }

    static {
        CODEC = BuilderCodec.builder(SpawnProjectileComponent.class, SpawnProjectileComponent::new)

                .append(new KeyedCodec<>("Config", ProjectileConfig.CODEC),
                        (data, value) -> data.config = value,
                        SpawnProjectileComponent::getConfig)
                .addValidator(Validators.nonNull())
                .add()

                .append(new KeyedCodec<>("Position", Vector3d.CODEC),
                        (data, value) -> data.position = value,
                        SpawnProjectileComponent::getPosition)
                .addValidator(Validators.nonNull())
                .add()

                .append(new KeyedCodec<>("Rotation", Vector3d.CODEC),
                        (data, value) -> data.rotation = value,
                        SpawnProjectileComponent::getRotation)
                .addValidator(Validators.nonNull())
                .add()

                .build();
    }
}
