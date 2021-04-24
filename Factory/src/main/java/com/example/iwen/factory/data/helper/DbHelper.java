package com.example.iwen.factory.data.helper;

import com.example.iwen.factory.model.db.AppDatabase;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.model.db.GroupMember;
import com.example.iwen.factory.model.db.Group_Table;
import com.example.iwen.factory.model.db.Message;
import com.example.iwen.factory.model.db.Session;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库的辅助工具类，辅助完成数据库增删改功能
 *
 * @author iwen大大怪
 * @Create to 2021/02/14 22:55
 */
public class DbHelper {

    // 单例模式
    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    private DbHelper() {
    }

    /**
     * 观察者的集合
     * Class<?>:观察的表
     * Set<ChangedListener>: 每一个表对应的观察者有很多
     */
    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    /**
     * 从所有的监听者中，获取某一个表的所有监听者
     *
     * @param clazz   表对应的class信息
     * @param <Model> 泛型
     * @return Set<ChangedListener>
     */
    public <Model extends BaseModel> Set<ChangedListener> getListener(Class<Model> clazz) {
        if (changedListeners.containsKey(clazz)) {
            return changedListeners.get(clazz);
        }
        return null;
    }

    /**
     * 添加一个监听
     *
     * @param clazz    对某个表关注
     * @param listener 监听者
     * @param <Model>  表的泛型
     */
    public static <Model extends BaseModel> void addChangedListener(final Class<Model> clazz,
                                                                    ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListener(clazz);

        if (changedListeners == null) {
            // 初始化某一类型的容器
            changedListeners = new HashSet<>();
            // 添加到总的map
            instance.changedListeners.put(clazz, changedListeners);
        }
        changedListeners.add(listener);
    }

    /**
     * 删除某一个表的某一个监听器
     *
     * @param clazz    表
     * @param listener 监听器
     * @param <Model>  泛型
     */
    public static <Model extends BaseModel> void removeChangedListener(final Class<Model> clazz,
                                                                       ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListener(clazz);

        if (changedListeners == null) {
            // 初容器本身为空，说明从来没有加过
            return;
        }
        changedListeners.remove(listener);
    }

    /**
     * 新增或者修改的统一方法
     *
     * @param clazz   传递一个class信息
     * @param models  这个class类对应的实例数组
     * @param <Model> 泛型，限定条件是继承自 BaseModel
     */
    public static <Model extends BaseModel> void save(final Class<Model> clazz, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        // 当前数据库的管理者
        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
        // 提交一个事务
        databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // 执行
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(clazz);
                // 保存，将数据转换成List进行保存
                adapter.saveAll(Arrays.asList(models));
                // 唤起通知
                instance.notifySave(clazz, models);
            }
        }).build().execute();
    }

    /**
     * 数据库进行删除操作的统一封装
     *
     * @param clazz   传递一个class信息
     * @param models  这个class类对应的实例数组
     * @param <Model> 泛型，限定条件是继承自 BaseModel
     */
    public static <Model extends BaseModel> void delete(final Class<Model> clazz, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        // 当前数据库的管理者
        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
        // 提交一个事务
        databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // 执行
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(clazz);
                // 删除
                adapter.deleteAll(Arrays.asList(models));
                // 唤起通知
                instance.notifyDelete(clazz, models);
            }
        }).build().execute();
    }

    /**
     * 进行保存的通知调用
     *
     * @param clazz   通知的类型
     * @param models  通知的Model数组
     * @param <Model> 泛型，限定条件是继承自baseModel
     */
    @SuppressWarnings("unchecked")
    private final <Model extends BaseModel> void notifySave(final Class<Model> clazz, final Model... models) {
        // 找监听器
        final Set<ChangedListener> listeners = instance.getListener(clazz);
        if (listeners != null && listeners.size() > 0) {
            // 通用通知
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataSave(models);
            }
        }
        // 例外情况
        if (GroupMember.class.equals(clazz)) {
            // 群成员变更，需要通知对应群信息跟新
            updateGroupMember((GroupMember[]) models);
        } else if (Message.class.equals(clazz)) {
            // 消息变化，应该通知会话列表更新
            updateSession((Message[]) models);
        }
    }

    /**
     * 进行删除的通知调用
     *
     * @param clazz   通知的类型
     * @param models  通知的Model数组
     * @param <Model> 泛型，限定条件是继承自baseModel
     */
    @SuppressWarnings("unchecked")
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> clazz, final Model... models) {
        // 找监听器
        final Set<ChangedListener> listeners = instance.getListener(clazz);
        if (listeners != null && listeners.size() > 0) {
            // 通用通知
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataDelete(models);
            }
        }
        // 列外情况
        if (GroupMember.class.equals(clazz)) {
            // 群成员变更，需要通知对应群信息跟新
            updateGroupMember((GroupMember[]) models);
        } else if (Message.class.equals(clazz)) {
            // 消息变化，应该通知会话列表更新
            updateSession((Message[]) models);
        }
    }

    /**
     * 从成员中找出对应的群，并对群进行更新
     *
     * @param members 群成员列表
     */
    private void updateGroupMember(GroupMember... members) {
        // 存储群的id，不重复的集合
        final Set<String> groupIds = new HashSet<>();
        for (GroupMember member : members) {
            // 添加群Id
            groupIds.add(member.getGroup().getId());
        }
        // 异步数据库查询，异步发起二次通知
        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
        // 提交一个事务
        databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // 找到需要通知的群
                List<Group> groups = SQLite.select()
                        .from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();
                // 调用直接进行一次通知分发
                instance.notifySave(Group.class, groups.toArray(new Group[0]));
            }
        }).build().execute();
    }

    /**
     * 从消息列表中，筛选出相应的会话，并对会话进行更新
     *
     * @param messages Message列表
     */
    private void updateSession(Message... messages) {
        // 标识一个Session的唯一性
        final Set<Session.Identify> identifies = new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify = Session.createSessionIdentify(message);
            identifies.add(identify);
        }
        if (identifies.size() == 0) {
            return;
        }
        // 异步数据库查询，异步发起二次通知
        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
        // 提交一个事务
        databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identifies.size()];
                int index = 0;
                for (Session.Identify identify : identifies) {
                    Session session = SessionHelper.findFromLocal(identify.id);
                    if (session == null) {
                        // 第一次聊天，创建一个你和对方的一个会话
                        session = new Session(identify);
                    }
                    // 把会话刷新到当前Message的最新状态
                    session.refreshToNow();
                    // 数据存储
                    adapter.save(session);
                    // 添加到集合
                    sessions[index++] = session;
                }
                // 调用直接进行一次通知分发
                instance.notifySave(Session.class, sessions);
            }
        }).build().execute();
    }

    /**
     * 通知监听器
     */
    public interface ChangedListener<Data extends BaseModel> {
        @SuppressWarnings("unchecked")
        void onDataSave(Data... list);

        @SuppressWarnings("unchecked")
        void onDataDelete(Data... list);
    }
}