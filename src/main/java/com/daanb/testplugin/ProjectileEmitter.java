package com.daanb.testplugin;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.modules.projectile.config.ProjectileConfig;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Custom component that can be used to spawn projectiles once or repeatedly.
 * The component has to be attached to entities with a TransformComponent.
 * I couldn't find an easier way to spawn projectiles.
 */
public class ProjectileEmitter implements Component<EntityStore> {

    public static final BuilderCodec<ProjectileEmitter> CODEC;

    private ProjectileConfig config;
    private boolean repeatingForever = false;
    private int repetitions = 0;
    private int remainingTickDelay = 0;
    private int tickDelay = 0;
    private float directionalOffset = 0f;

    //For when you want to launch a projectile repeatedly
    public ProjectileEmitter(@NonNull ProjectileConfig config, boolean repeatingForever, int repetitions, int tickDelay, float directionalOffset) {

        this.config = config;
        this.repeatingForever = repeatingForever;
        this.repetitions = repetitions;
        this.tickDelay = tickDelay;
        this.remainingTickDelay =  0;
        this.directionalOffset = directionalOffset;
    }

    //For when you want to launch a single projectile
    public ProjectileEmitter(@NonNull ProjectileConfig config) {
        this.config = config;
    }

    //Copy constructor
    public ProjectileEmitter(ProjectileEmitter other) {
        this.config = other.config;
        this.repeatingForever = other.repeatingForever;
        this.repetitions = other.repetitions;
        this.remainingTickDelay = other.remainingTickDelay;
        this.tickDelay = other.tickDelay;
        this.directionalOffset = other.directionalOffset;
    }

    public ProjectileEmitter() {
        this.repeatingForever = false;
        this.repetitions = 0;
        this.tickDelay = 0;
        this.remainingTickDelay = 0;
        this.directionalOffset = 0f;
    }

    public static ComponentType<EntityStore, ProjectileEmitter> getComponentType() {
        return TestPlugin.get().getProjectileEmitterComponentType();
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new ProjectileEmitter(this);
    }

    public ProjectileConfig getConfig() {
        return config;
    }

    public boolean isRepeatingForever() {
        return repeatingForever;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public int getTickDelay() {
        return tickDelay;
    }

    public int getRemainingTickDelay() {
        return remainingTickDelay;
    }

    public float getDirectionalOffset() {
        return directionalOffset;
    }

    public boolean tick() {
        if(remainingTickDelay > 1) {
            remainingTickDelay--;
            return false;
        }

        remainingTickDelay = tickDelay;
        return true;
    }

    public void decrementRepetitions() {
        repetitions--;
    }

    static {
        CODEC = BuilderCodec.builder(ProjectileEmitter.class, ProjectileEmitter::new)

                .appendInherited(new KeyedCodec<>("Config", ProjectileConfig.CODEC),
                        (data, value) -> data.config = value,
                        ProjectileEmitter::getConfig,
                        (o, p) -> o.config = p.config)
                .add()

                .append(new KeyedCodec<>("RepeatingForever", Codec.BOOLEAN),
                        (data, value) -> data.repeatingForever = value,
                        ProjectileEmitter::isRepeatingForever)
                .add()

                .append(new KeyedCodec<>("RemainingTickDelay", Codec.INTEGER),
                        (data, value) -> data.remainingTickDelay = value,
                        ProjectileEmitter::getRemainingTickDelay)
                .add()

                .append(new KeyedCodec<>("TickDelay", Codec.INTEGER),
                        (data, value) -> data.tickDelay = value,
                        ProjectileEmitter::getTickDelay)
                .addValidator(Validators.min(0))
                .add()

                .append(new KeyedCodec<>("Repetitions", Codec.INTEGER),
                        (data, value) -> data.repetitions = value,
                        ProjectileEmitter::getRepetitions)
                .addValidator(Validators.min(0))
                .add()

                .append(new KeyedCodec<>("DirectionalOffset", Codec.FLOAT),
                        (data, value) -> data.directionalOffset = value,
                        ProjectileEmitter::getDirectionalOffset)
                .add()

                .build();
    }
}
