package com.daanb.testplugin;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.entity.teleport.TeleportRecord;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class TestPlugin extends JavaPlugin {

    private static TestPlugin instance;
    private ComponentType<EntityStore, SpawnProjectileComponent> spawnProjectileComponentComponentType;

    public TestPlugin(JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new TestCommand());
        this.getCommandRegistry().registerCommand(new OrbCommand());

        //important: initalize component type before initializing the system that manipulates it
        this.spawnProjectileComponentComponentType = this.getEntityStoreRegistry().registerComponent(SpawnProjectileComponent.class, "SpawnProjectile", SpawnProjectileComponent.CODEC);
        this.getEntityStoreRegistry().registerSystem(new SpawnProjectileSystem());
    }

    public static TestPlugin get() {
        return instance;
    }

    public ComponentType<EntityStore, SpawnProjectileComponent> getSpawnProjectileComponentComponentType() {
        return spawnProjectileComponentComponentType;
    }
}
