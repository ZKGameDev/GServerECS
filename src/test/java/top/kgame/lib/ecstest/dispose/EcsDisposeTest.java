package top.kgame.lib.ecstest.dispose;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.kgame.lib.ecs.EcsWorld;
import top.kgame.lib.ecs.Entity;
import top.kgame.lib.ecstest.dispose.component.ComponentDispose1;
import top.kgame.lib.ecstest.dispose.component.ComponentDispose2;
import top.kgame.lib.ecstest.dispose.component.ComponentDispose3;

class EcsDisposeTest {
    private EcsWorld ecsWorld;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前都会执行这个方法
        System.out.println("Setting up EcsDisposeTest...");
        String packageName = this.getClass().getPackage().getName();
        ecsWorld = EcsWorld.generateInstance(packageName);
    }

    @Test
    void updateWorld()  {
        Entity entity = ecsWorld.createEntity(2);
        final int tickInterval = 33;
        // 记录开始时间
        long startTime = 0;
        // 设置结束时间（1分钟后）
        long endTime = startTime + 60000; // 60000ms = 1分钟

        ComponentDispose1 componentDispose1 = entity.getComponent(ComponentDispose1.class);
        ComponentDispose2 componentDispose2 = entity.getComponent(ComponentDispose2.class);
        ComponentDispose3 componentDispose3 = entity.getComponent(ComponentDispose3.class);

        boolean disposed = false;
        long disposeTime = 0;
        while (startTime < endTime && !disposed) {
            // 更新ECS世界
            if (startTime > 3000 ) {
                ecsWorld.close();
                disposed = true;
                disposeTime = startTime;
            }
            ecsWorld.update(startTime);
            startTime += tickInterval;
        }
        assert componentDispose1.updateTime == disposeTime - tickInterval;
        assert componentDispose2.updateTime == disposeTime - tickInterval;
        assert componentDispose3.updateTime == disposeTime - tickInterval;

        // 清理资源
        ecsWorld.close();
    }

    @Test
    void updateWorld1()  {
        Entity entity = ecsWorld.createEntity(2);
        final int tickInterval = 33;
        // 记录开始时间
        long startTime = 0;
        // 设置结束时间（1分钟后）
        long endTime = startTime + 60000; // 60000ms = 1分钟

        ComponentDispose1 componentDispose1 = entity.getComponent(ComponentDispose1.class);
        ComponentDispose2 componentDispose2 = entity.getComponent(ComponentDispose2.class);
        ComponentDispose3 componentDispose3 = entity.getComponent(ComponentDispose3.class);

        boolean disposed = false;
        long disposeTime = 0;
        while (startTime < endTime && !disposed) {
            // 更新ECS世界
            ecsWorld.update(startTime);
            if (startTime > 3000 ) {
                ecsWorld.close();
                disposed = true;
                disposeTime = startTime;
            }
            startTime += tickInterval;
        }
        assert componentDispose1.updateTime == disposeTime;
        assert componentDispose2.updateTime == disposeTime;
        assert componentDispose3.updateTime == disposeTime;

        // 清理资源
        ecsWorld.close();
    }

    @Test
    void updateWorld2()  {
        Entity entity = ecsWorld.createEntity(2);
        final int tickInterval = 33;
        // 记录开始时间
        long startTime = 0;
        // 设置结束时间（1分钟后）
        long endTime = startTime + 60000; // 60000ms = 1分钟
        long disposeTime = 22000;

        ComponentDispose1 componentDispose1 = entity.getComponent(ComponentDispose1.class);
        ComponentDispose2 componentDispose2 = entity.getComponent(ComponentDispose2.class);
        ComponentDispose3 componentDispose3 = entity.getComponent(ComponentDispose3.class);

        boolean disposed = false;
        long disposeLogicTime = 0;
        componentDispose1.disposeTime = disposeTime;
        while (startTime < endTime && !ecsWorld.isClosed()) {
            // 更新ECS世界
            ecsWorld.update(startTime);
            if (ecsWorld.isClosed()) {
                disposeLogicTime = startTime;
            }
            startTime += tickInterval;
        }
        assert componentDispose1.updateTime == disposeLogicTime;
        assert componentDispose2.updateTime == disposeLogicTime;
        assert componentDispose3.updateTime == disposeLogicTime;

        // 清理资源
        ecsWorld.close();
    }

    @Test
    void updateWorld3()  {
        Entity entity = ecsWorld.createEntity(2);
        final int tickInterval = 33;
        // 记录开始时间
        long startTime = 0;
        // 设置结束时间（1分钟后）
        long endTime = startTime + 60000; // 60000ms = 1分钟
        long disposeTime = 22000;

        ComponentDispose1 componentDispose1 = entity.getComponent(ComponentDispose1.class);
        ComponentDispose2 componentDispose2 = entity.getComponent(ComponentDispose2.class);
        ComponentDispose3 componentDispose3 = entity.getComponent(ComponentDispose3.class);

        boolean disposed = false;
        long disposeLogicTime = 0;
        componentDispose2.disposeTime = disposeTime;
        while (startTime < endTime && !ecsWorld.isClosed()) {
            // 更新ECS世界
            ecsWorld.update(startTime);
            if (ecsWorld.isClosed()) {
                disposeLogicTime = startTime;
            }
            startTime += tickInterval;
        }
        assert componentDispose1.updateTime == disposeLogicTime;
        assert componentDispose2.updateTime == disposeLogicTime;
        assert componentDispose3.updateTime == disposeLogicTime;

        // 清理资源
        ecsWorld.close();
    }

    @Test
    void updateWorld4()  {
        Entity entity = ecsWorld.createEntity(2);
        final int tickInterval = 33;
        // 记录开始时间
        long startTime = 0;
        // 设置结束时间（1分钟后）
        long endTime = startTime + 60000; // 60000ms = 1分钟
        long disposeTime = 22000;

        ComponentDispose1 componentDispose1 = entity.getComponent(ComponentDispose1.class);
        ComponentDispose2 componentDispose2 = entity.getComponent(ComponentDispose2.class);
        ComponentDispose3 componentDispose3 = entity.getComponent(ComponentDispose3.class);

        boolean disposed = false;
        long disposeLogicTime = 0;
        componentDispose3.disposeTime = disposeTime;
        while (startTime < endTime && !ecsWorld.isClosed()) {
            // 更新ECS世界
            ecsWorld.update(startTime);
            if (ecsWorld.isClosed()) {
                disposeLogicTime = startTime;
            }
            startTime += tickInterval;
        }
        assert componentDispose1.updateTime == disposeLogicTime;
        assert componentDispose2.updateTime == disposeLogicTime;
        assert componentDispose3.updateTime == disposeLogicTime;

        // 清理资源
        ecsWorld.close();
    }
} 