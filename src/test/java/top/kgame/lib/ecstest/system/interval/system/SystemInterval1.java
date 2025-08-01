package top.kgame.lib.ecstest.system.interval.system;

import top.kgame.lib.ecs.Entity;
import top.kgame.lib.ecs.annotation.UpdateInGroup;
import top.kgame.lib.ecs.extensions.system.EcsUpdateSystemOne;
import top.kgame.lib.ecstest.system.interval.component.ComponentInterval1;
import top.kgame.lib.ecstest.system.interval.group.SysGroupInterval;

@UpdateInGroup(SysGroupInterval.class)
public class SystemInterval1 extends EcsUpdateSystemOne<ComponentInterval1> {

    @Override
    protected void update(Entity entity, ComponentInterval1 component) {
        System.out.println(this.getClass().getSimpleName() +" update at: " + getWorld().getCurrentTime());
        component.i1 = "i1";
    }
}