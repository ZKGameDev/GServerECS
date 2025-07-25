package top.kgame.lib.ecs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.kgame.lib.ecs.command.EntityCommandBuffer;
import top.kgame.lib.ecs.core.*;
import top.kgame.lib.ecs.extensions.component.DestroyingComponent;
import top.kgame.lib.ecs.command.EcsCommand;
import top.kgame.lib.ecs.extensions.entity.EntityFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 非线程安全，只能在单线程使用
 */
public class EcsWorld{
    private static final Logger logger = LogManager.getLogger(EcsWorld.class);
    private static final int INIT_CURRENT_TIME = -1;

    private State state = State.INIT;
    private long currentTime = INIT_CURRENT_TIME;
    private EcsClassScanner ecsClassScanner;

    private final EcsEntityManager entityManager = new EcsEntityManager(this);
    private final List<Entity> waitDestroyEntity = new ArrayList<>();

    private final EcsSystemManager systemManager = new EcsSystemManager(this);

    private EntityCommandBuffer waitUpdateCommand;

    private Object context;

    EntityGroup getOrCreateEntityGroup(ComponentTypeQuery componentTypes) {
        return this.entityManager.getOrCreateEntityGroup(componentTypes);
    }

    public void addDelayCommand(EcsCommand command) {
        waitUpdateCommand.addCommand(command);
    }

    private enum State {
        INIT,
        WAIT_RUNNING,
        RUNNING,
        WAIT_DESTROY,
        DESTROYING,
        DESTROYED,
    }

    EcsWorld() {}

    /**
     * 生成EcsWorld实例
     * @param packageName 需要扫描的包名
     * @return EcsWorld实例
     */
    public static EcsWorld generateInstance(String packageName) {
        EcsWorld ecsWorld = new EcsWorld();
        ecsWorld.init(packageName);
        return ecsWorld;
    }

    void init(String packageName) {
        ecsClassScanner = EcsClassScanner.getInstance(packageName);
        entityManager.init(ecsClassScanner);
        systemManager.init(ecsClassScanner);
        state = State.WAIT_RUNNING;
        waitUpdateCommand = new EntityCommandBuffer();
    }

    /**
     * 设置自定义上下文
     * @param context 上下文对象
     */
    public void setContext(Object context) {
        this.context = context;
    }

    /**
     * 获取自定义上下文，调用之前需要先调用setContext设置上下文
     * @param <T> 上下文类型
     * @return 上下文对象
     * @throws ClassCastException 当上下文对象无法转换为指定类型时抛出异常
     */
    @SuppressWarnings({"unchecked"})
    public <T> T getContext() {
        return (T) context;
    }
    /**
     * 关闭World。
     * 如果在update期间调用，会等本次所有System update完成之后才执行关闭逻辑。
     */
    public void close() {
        if (state == State.INIT || state == State.DESTROYED) {
            return;
        }
        if (state == State.RUNNING) {
            state = State.WAIT_DESTROY;
            return;
        }
        logger.info("Disposing ecs world at time {}...", currentTime);
        state = State.DESTROYING;
        currentTime = INIT_CURRENT_TIME;
        waitDestroyEntity.clear();
        systemManager.clean();
        entityManager.clean();
        waitUpdateCommand.clear();
        state = State.DESTROYED;
    }

    public boolean isClosed() {
        return state == State.DESTROYED;
    }

    // 通过EntityFactory类型ID创建实体
    public Entity createEntity(int factoryTypeId) {
        EntityFactory entityFactory = entityManager.getEntityFactory(factoryTypeId);
        if (entityFactory == null) {
            throw new IllegalArgumentException("No entity factory found for type id " + factoryTypeId);
        }
        return entityFactory.create(this.entityManager);
    }

    // 通过工厂类创建实体
    public Entity createEntity(Class<? extends EntityFactory> klass) {
        EntityFactory entityFactory = entityManager.getEntityFactory(klass);
        return entityFactory == null ? null : entityFactory.create(this.entityManager);
    }

    public void requestDestroyEntity(int entityIndex) {
        Entity entity = getEntity(entityIndex);
        if (entity != null) {
            requestDestroyEntity(entity);
        }
    }

    public void requestDestroyEntity(Entity entity) {
        entity.addComponent(DestroyingComponent.generate());
        this.waitDestroyEntity.add(entity);
    }

    public Entity getEntity(int entityIndex) {
        return entityManager.getEntity(entityIndex);
    }

    public Collection<Entity> getAllEntity() {
        return entityManager.getAllEntity();
    }

    /**
     * 执行ECS世界更新循环
     * <p>执行所有系统更新，处理实体销毁，执行EcsCommandScope.WORLD级的EcsCommand。</p>
     * <p>时间戳必须严格递增。</p>
     *
     * @param now 当前时间戳（毫秒），必须大于上次传入的时间
     * @throws IllegalArgumentException 当时间戳无效时抛出异常
     */
    public void update(long now) {
        if (currentTime >= now) {
            throw new IllegalArgumentException(String.format(
                "EcsWorld try update failed! reason: currentTime >= nowTime. currentTime: %d, now: %d", 
                currentTime, now));
        }
        if (state != State.WAIT_RUNNING) {
            logger.warn("EcsWorld request update failed! reason: EcsWorld has disposed");
            return;
        }
        state = State.RUNNING;
        this.currentTime = now;
        systemManager.update();
        for (Entity waitDestroyEntity : this.waitDestroyEntity) {
            entityManager.destroyEntity(waitDestroyEntity);
        }
        this.waitDestroyEntity.clear();
        waitUpdateCommand.playBack();
        if (state == State.WAIT_DESTROY) {
            close();
        } else {
            state = State.WAIT_RUNNING;
        }
    }

    public int getComponentTypeIndex(Class<?> type) {
        return ecsClassScanner.getComponentTypeIndex(type);
    }

    public long getCurrentTime() {
        return currentTime;
    }

    /**
     * 获取当前正在执行的SystemGroup
     *
     * @return 正在执行的SystemGroup的class
     */
    public EcsSystem getCurrentSystemGroupClass() {
        return systemManager.getCurrentSystemGroup();
    }
}
