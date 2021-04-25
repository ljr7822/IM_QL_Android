package com.example.iwen.factory.presenter.group;

import com.example.iwen.common.factory.model.Author;
import com.example.iwen.common.factory.presenter.BaseContract;

/**
 * 群创建的契约
 *
 * @author iwen大大怪
 * @Create to 2021/04/24 20:55
 */
public interface GroupCreateContract {
    interface Presenter extends BaseContract.Presenter {
        // 创建群的提交操作
        void create(String name, String desc, String picture);
        // 更改选中状态
        void changeSelect(ViewModel model, boolean isSelected);
    }

    interface View extends BaseContract.RecyclerView<ViewModel,Presenter>{
        // 创建成功
        void onCreateSuccess();
    }

    class ViewModel{
        public Author author;
        // 是否选中
        public boolean isSelected;
    }
}
