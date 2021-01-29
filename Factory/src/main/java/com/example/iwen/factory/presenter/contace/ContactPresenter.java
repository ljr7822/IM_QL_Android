package com.example.iwen.factory.presenter.contace;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BasePresenter;
import com.example.iwen.factory.data.helper.UserHelper;
import com.example.iwen.factory.model.card.UserCard;
import com.example.iwen.factory.model.db.AppDatabase;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.model.db.User_Table;
import com.example.iwen.factory.persistence.Account;
import com.example.iwen.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
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
        UserHelper.refreshContacts(new DataSource.Callback<List<UserCard>>() {
            @Override
            public void onDataNotAvailable(int strRes) {
                // 网络失败，因为本地有数据，不理会网络错误
            }

            @Override
            public void onDataLoad(final List<UserCard> userCards) {
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
                diff(getView().getRecyclerAdapter().getItems(),users);
            }
        });
    }

    private void diff(List<User> newList,List<User> oldList){
        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldList,newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        result.dispatchUpdatesTo(getView().getRecyclerAdapter());
        getView().onAdapterDataChanged();
    }
}
