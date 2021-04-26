package com.example.iwen.factory.presenter.group;

import androidx.recyclerview.widget.DiffUtil;

import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.factory.data.group.GroupsDataSource;
import com.example.iwen.factory.data.group.GroupsRepository;
import com.example.iwen.factory.data.helper.GroupHelper;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.presenter.BaseSourcePresenter;
import com.example.iwen.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 我的群组Presenter
 *
 * @author iwen大大怪
 * @Create to 2021/04/25 23:34
 */
public class GroupsPresenter extends BaseSourcePresenter<Group,Group, GroupsDataSource,GroupsContract.View>
        implements GroupsContract.Presenter{

    public GroupsPresenter(GroupsContract.View mView) {
        super(new GroupsRepository(), mView);
    }

    @Override
    public void start() {
        super.start();
        // TODO 加载网络数据,以后可以优化到下拉刷新
        GroupHelper.refreshGroups();

    }

    @Override
    public void onDataLoad(List<Group> groups) {
        // 无论怎么操作，数据变更，都会通知到这里
        final GroupsContract.View view = getView();
        if (view==null){
            return;
        }
        RecyclerAdapter<Group> adapter = view.getRecyclerAdapter();
        List<Group> old = adapter.getItems();

        // 进行数据对比
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old,groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 调用基类方法进行界面更新
        refreshData(result,groups);
    }
}
