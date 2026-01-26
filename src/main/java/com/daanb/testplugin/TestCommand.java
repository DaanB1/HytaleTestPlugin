package com.daanb.testplugin;

import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.DefaultArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.TimeUnit;

public class TestCommand extends AbstractPlayerCommand {

    private final DefaultArg<Integer> sizeArg;

    private static final int DEFAULT_SIZE = 12;
    private static final int MAX_SIZE = 30;
    private static final String[] PATTERN = {
            "Cloth_Block_Wool_Red_Light",
            "Cloth_Block_Wool_Red",
            "Cloth_Block_Wool_Orange",
            "Cloth_Block_Wool_Yellow",
            "Cloth_Block_Wool_Yellow_Light",
            "Cloth_Block_Wool_Green_Light",
            "Cloth_Block_Wool_Green",
            "Cloth_Block_Wool_Blue_Light",
            "Cloth_Block_Wool_Blue",
            "Cloth_Block_Wool_Purple",
            "Cloth_Block_Wool_Pink",
    };

    public TestCommand() {
        String name = "bridge";
        String description = "Creates a rainbow bridge";
        super(name, description);

        this.sizeArg = withDefaultArg("size", "Size of the bridge", ArgTypes.INTEGER, DEFAULT_SIZE, String.valueOf(DEFAULT_SIZE))
                .addValidator(Validators.greaterThan(0))
                .addValidator(Validators.max(MAX_SIZE));
        requirePermission(HytalePermissions.fromCommand("bridge"));
    }

    @Override
    protected void execute(@NonNull CommandContext commandContext, @NonNull Store<EntityStore> store, @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {
        TransformComponent pos = store.getComponent(ref, TransformComponent.getComponentType());
        HeadRotation rot = store.getComponent(ref, HeadRotation.getComponentType());
        if (pos == null || rot == null) return;

        Integer size = sizeArg.get(commandContext);

        Vector3d p = pos.getPosition().clone();
        Vector3d d = rot.getDirection().clone();
        d.setY(0);
        d.normalize();

        //TODO: Confirm if this is the right way to add delay
        HytaleServer.SCHEDULED_EXECUTOR.schedule(() -> {
            for (int i = 0; i < size; i++) {
                int x = (int) (p.getX() + i * d.getX());
                int y = (int) p.getY() - 1;
                int z = (int) (p.getZ() + i * d.getZ());
                String block = PATTERN[i % PATTERN.length];

                world.setBlock(x, y, z, block);
                world.execute(() -> { //SFX must be played on the world thread
                    int index = SoundEvent.getAssetMap().getIndex("SFX_Cactus_Large_Hit");
                    SoundUtil.playSoundEvent3d(index, SoundCategory.SFX, x, y, z, store);
                });

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, TimeUnit.SECONDS);
    }
}
