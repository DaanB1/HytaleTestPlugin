package com.daanb.testplugin;

import com.daanb.testplugin.commands.firebarrage.FireBarrage;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Bug list:
 *  - The interactions attached to the projectile mess things up (client server desync)
 */
public class TestPlugin extends JavaPlugin {

    private static TestPlugin instance;
    private ComponentType<EntityStore, ProjectileEmitter> projectileEmitterComponentType;

    public TestPlugin(JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new TestCommand());
        this.getCommandRegistry().registerCommand(new FireBarrage());

        //important: initalize component type before initializing the system that manipulates it
        this.projectileEmitterComponentType = this.getEntityStoreRegistry().registerComponent(ProjectileEmitter.class, "ProjectileEmitter", ProjectileEmitter.CODEC);
        this.getEntityStoreRegistry().registerSystem(new ProjectileEmitterSystem());
    }

    public static TestPlugin get() {
        return instance;
    }

    public ComponentType<EntityStore, ProjectileEmitter> getProjectileEmitterComponentType() {
        return projectileEmitterComponentType;
    }
}
