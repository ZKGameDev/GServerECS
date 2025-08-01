package top.kgame.lib.ecstest.system.mixed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.kgame.lib.ecs.EcsWorld;
import top.kgame.lib.ecs.Entity;
import top.kgame.lib.ecstest.system.mixed.component.ComponentA2;

/**
 * System默认执行顺序测试用例
 */
class EcsMixSystemTest {
    private EcsWorld ecsWorld;
    public static String data = "";

    @BeforeEach
    void setUp() {
        System.out.println("Setting up EcsDefaultOrderTest...");
        String packageName = EcsMixSystemTest.class.getPackage().getName();
        ecsWorld = EcsWorld.generateInstance(packageName);
    }

    @Test
    void updateWorld()  {
        Entity entity = ecsWorld.createEntity(1);

        // 记录开始时间
        final int interval = 33;
        long startTime = 0;
        long endTime = startTime + interval * 100;
        boolean inited = false;
        boolean destroy = false;

        while (startTime < endTime) {
            System.out.println("=====Updating world in " + startTime + "=====");
            // 更新ECS世界
            ecsWorld.update(startTime);
            ComponentA2 a2 = entity.getComponent(ComponentA2.class);
            if (!destroy) {
                if (!inited) {
                    inited = true;
                    System.out.println("update result: " + a2.data);
                    assert a2.data.equals("o1o2o3o4");
                } else {
                    System.out.println("update result: " + a2.data);
                    assert a2.data.equals("a1a2a3a4");
                }
            }
            if (startTime >= endTime - interval * 10 && !destroy) {
                destroy = true;
                ecsWorld.requestDestroyEntity(entity);
                System.out.println("destroy entity");
            }
            startTime += interval;
        }

        // 清理资源
        ecsWorld.close();
    }
}