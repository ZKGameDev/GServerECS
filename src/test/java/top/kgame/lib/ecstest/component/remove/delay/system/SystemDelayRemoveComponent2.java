package top.kgame.lib.ecstest.component.remove.delay.system;

import top.kgame.lib.ecs.Entity;
import top.kgame.lib.ecs.annotation.UpdateInGroup;
import top.kgame.lib.ecs.extensions.system.EcsUpdateSystemOne;
import top.kgame.lib.ecstest.component.remove.delay.component.ComponentDelayRemove1;
import top.kgame.lib.ecstest.component.remove.delay.component.ComponentDelayRemove3;
import top.kgame.lib.ecstest.component.remove.delay.group.SysGroupDelayRemoveComponentA;

@UpdateInGroup(SysGroupDelayRemoveComponentA.class)
public class SystemDelayRemoveComponent2 extends EcsUpdateSystemOne<ComponentDelayRemove3> {

    @Override
    protected void update(Entity entity, ComponentDelayRemove3 component) {
        System.out.println(this.getClass().getSimpleName() +" update at: " + getWorld().getCurrentTime());
        ComponentDelayRemove1 r1 = entity.getComponent(ComponentDelayRemove1.class);
        r1.data += "r2";
    }
} 