package com.example.iwen.factory.presenter.contace;

import androidx.recyclerview.widget.DiffUtil;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BaseRecyclerPresenter;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.factory.data.helper.UserHelper;
import com.example.iwen.factory.data.user.ContactDataSource;
import com.example.iwen.factory.data.user.ContactRepository;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 联系人的presenter实现
 *
 * @author : iwen大大怪
 * create : 2021/01/29 1:58
 */
public class ContactPresenter extends BaseRecyclerPresenter<ContactContract.View,User>
        implements ContactContract.Presenter, DataSource.SuccessCallback<List<User>> {
    private ContactDataSource mSource;

    public ContactPresenter(ContactContract.View mView) {
        super(mView);
        // 初始化数据仓库
        mSource = new ContactRepository();
    }

    @Override
    public void start() {
        super.start();
        // 进行本地数据加载，并添加监听
        mSource.load(this);

        // 加载网络数据
        UserHelper.refreshContacts();

    }

    // 要保证运行到这里是子线程
    @Override
    public void onDataLoad(List<User> users) {
        // 无论怎么操作，数据变更，都会通知到这里
        ContactContract.View view = getView();
        if (view==null){
            return;
        }
        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();

        // 进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(old,users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 调用基类方法进行界面更新
        refreshData(result,users);
    }

    @Override
    public void destroy() {
        super.destroy();
        // 界面销毁时，将数据销毁
        mSource.dispose();
    }
}
