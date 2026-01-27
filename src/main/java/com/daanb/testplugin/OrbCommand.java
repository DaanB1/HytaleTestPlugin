package com.daanb.testplugin;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.ProjectileComponent;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.modules.projectile.ProjectileModule;
import com.hypixel.hytale.server.core.modules.projectile.config.ProjectileConfig;
import com.hypixel.hytale.server.core.modules.time.TimeResource;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

public class OrbCommand extends AbstractPlayerCommand {

    public OrbCommand() {
        String name = "orb";
        String description = "Spawns a glowing orb";
        super(name, description);
    }

    @Override
    protected void execute(@NonNull CommandContext commandContext, @NonNull Store<EntityStore> store, @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {
        HeadRotation rotation = store.getComponent(ref, HeadRotation.getComponentType());
        if (rotation == null) return;

        //System.out.println(ProjectileConfig.getAssetMap().getAssetMap().keySet());
        Vector3d position = playerRef.getTransform().getPosition().add(rotation.getDirection().normalize());
        ProjectileConfig config = ProjectileConfig.getAssetMap().getAsset("Projectile_Config_Fireball_Charged_3");
        if (config == null) return;

        SpawnProjectileComponent component = new SpawnProjectileComponent(config, position, rotation.getDirection());
        store.putComponent(ref, SpawnProjectileComponent.getComponentType(), component);
    }

}
