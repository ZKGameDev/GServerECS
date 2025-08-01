package top.kgame.lib.ecstest.entity.add.immediately.system;

import top.kgame.lib.ecs.Entity;
import top.kgame.lib.ecs.EcsComponent;
import top.kgame.lib.ecs.annotation.UpdateInGroup;
import top.kgame.lib.ecs.extensions.system.EcsInitializeSystem;
import top.kgame.lib.ecstest.entity.add.immediately.component.EntityAdd3;
import top.kgame.lib.ecstest.entity.add.immediately.group.SysGroupEntityAddSpawn;

import java.util.Collection;
import java.util.Collections;

@UpdateInGroup(SysGroupEntityAddSpawn.class)
public class SystemSpawnEntityAdd3 extends EcsInitializeSystem<EntityAdd3> {

    @Override
    public boolean onInitialize(Entity entity, EntityAdd3 component) {
        System.out.println(this.getClass().getSimpleName() + " initialize at: " + getWorld().getCurrentTime());
        component.spawnTime = getWorld().getCurrentTime();
        return true;
    }

    @Override
    protected SystemInitFinishSingle getInitFinishSingle() {
        return new SystemInitFinishSingle() {};
    }

    @Override
    public Collection<Class<? extends EcsComponent>> getExtraRequirementComponent() {
        return Collections.emptyList();
    }

    @Override
    public Collection<Class<? extends EcsComponent>> getExtraExcludeComponent() {
        return Collections.emptyList();
    }
} 