package com.example.iwen.factory.presenter.contace;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.iwen.common.factory.presenter.BasePresenter;
import com.example.iwen.factory.data.helper.UserHelper;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.model.db.User_Table;
import com.example.iwen.factory.persistence.Account;
import com.example.iwen.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.List;

/**
 * 联系人的presenter实现
 *
 * @author : iwen大大怪
 * create : 2021/01/29 1:58
 */
public class ContactPresenter extends BasePresenter<ContactContract.View>
        implements ContactContract.Presenter {
    public ContactPresenter(ContactContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        super.start();
        // 加载本地数据库数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        getView().getRecyclerAdapter().replace(tResult);
                        getView().onAdapterDataChanged();
                    }
                }).execute();

        // 加载网络数据
        UserHelper.refreshContacts();

        /*
        // 转换成user
        final List<User> users = new ArrayList<>();
        for (UserCard userCard : userCards) {
            users.add(userCard.build());
        }
        // 丢到事务中进行保存到数据库中
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                FlowManager.getModelAdapter(User.class)
                        .saveAll(users);
            }
        }).build().execute();
        // 网络数据一般是新的，直接刷新界面
        List<User> old= getView().getRecyclerAdapter().getItems();
        diff(old,users);
        */


    }

    private void diff(List<User> oldList,List<User> newList){
        // 进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldList,newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        // 在对比完成后进行数据的赋值
        getView().getRecyclerAdapter().replace(newList);
        // 尝试刷新界面
        result.dispatchUpdatesTo(getView().getRecyclerAdapter());
        getView().onAdapterDataChanged();
    }
}
