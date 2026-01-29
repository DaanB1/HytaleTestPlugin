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

    //persistent
    private String configName;
    private boolean repeatingForever = false;
    private int repetitions = 0;
    private int remainingTickDelay = 0;
    private int tickDelay = 0;
    private float directionalOffset = 0f;

    //non persistent
    private ProjectileConfig config;

    //For when you want to launch a projectile repeatedly
    public ProjectileEmitter(String configName, boolean repeatingForever, int repetitions, int tickDelay, float directionalOffset) {

        this.configName = configName;
        this.repeatingForever = repeatingForever;
        this.repetitions = repetitions;
        this.tickDelay = tickDelay;
        this.remainingTickDelay =  0;
        this.directionalOffset = directionalOffset;

        this.config = ProjectileEmitter.loadConfig(configName);
    }

    //For when you want to launch a single projectile
    public ProjectileEmitter(String configName) {
        this.configName = configName;
        this.config = ProjectileEmitter.loadConfig(configName);
    }

    //Copy constructor
    public ProjectileEmitter(ProjectileEmitter other) {
        this.configName = other.configName;
        this.config = other.config;
        this.repeatingForever = other.repeatingForever;
        this.repetitions = other.repetitions;
        this.remainingTickDelay = other.remainingTickDelay;
        this.tickDelay = other.tickDelay;
        this.directionalOffset = other.directionalOffset;
    }

    public ProjectileEmitter() {
    }

    public static ComponentType<EntityStore, ProjectileEmitter> getComponentType() {
        return TestPlugin.get().getProjectileEmitterComponentType();
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new ProjectileEmitter(this);
    }

    public String getConfigName() {
        return configName;
    }

    public boolean isRepeatingForever() {
        return repeatingForever;
    }

    public int getRepetitions() {
        return repetitions;
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
        if(repetitions > 1)
            repetitions -= 1;
        return true;
    }

    public ProjectileConfig getConfig() {
        return config;
    }

    private static ProjectileConfig loadConfig(String configName) {
        return ProjectileConfig.getAssetMap().getAsset(configName);
    }

    static {
        CODEC = BuilderCodec.builder(ProjectileEmitter.class, ProjectileEmitter::new)

                //Note: Instead of storing the full ProjectileEmitter, we only store the name
                //and load it immediately after the name gets read.
                .append(new KeyedCodec<>("ConfigName", Codec.STRING),
                        (data, value) -> {
                            data.configName = value;
                            data.config = ProjectileEmitter.loadConfig(value);},
                        ProjectileEmitter::getConfigName)
                .add()

                .append(new KeyedCodec<>("RepeatingForever", Codec.BOOLEAN),
                        (data, value) -> data.repeatingForever = value,
                        (data -> data.repeatingForever))
                .add()

                .append(new KeyedCodec<>("RemainingTickDelay", Codec.INTEGER),
                        (data, value) -> data.remainingTickDelay = value,
                        (data -> data.remainingTickDelay))
                .add()

                .append(new KeyedCodec<>("TickDelay", Codec.INTEGER),
                        (data, value) -> data.tickDelay = value,
                        (data -> data.tickDelay))
                .add()

                .append(new KeyedCodec<>("Repetitions", Codec.INTEGER),
                        (data, value) -> data.repetitions = value,
                        (data -> data.repetitions))
                .add()

                .append(new KeyedCodec<>("DirectionalOffset", Codec.FLOAT),
                        (data, value) -> data.directionalOffset = value,
                        (data -> data.directionalOffset))
                .add()

                .build();
    }
}
