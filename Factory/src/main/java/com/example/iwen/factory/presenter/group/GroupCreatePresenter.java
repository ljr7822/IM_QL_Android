package com.example.iwen.factory.presenter.group;

import android.text.TextUtils;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BaseRecyclerPresenter;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.R;
import com.example.iwen.factory.data.helper.GroupHelper;
import com.example.iwen.factory.data.helper.UserHelper;
import com.example.iwen.factory.model.api.group.GroupCreateModel;
import com.example.iwen.factory.model.card.GroupCard;
import com.example.iwen.factory.model.db.UserSampleModel;
import com.example.iwen.factory.net.UploadHelper;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 群创建界面的 Presenter
 *
 * @author iwen大大怪
 * @Create to 2021/04/24 21:11
 */
public class GroupCreatePresenter
        extends BaseRecyclerPresenter<GroupCreateContract.View, GroupCreateContract.ViewModel>
        implements GroupCreateContract.Presenter ,DataSource.Callback<GroupCard> {

    private Set<String> users = new HashSet<>();

    public GroupCreatePresenter(GroupCreateContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        super.start();
        // 加载可选联系人数据
        Factory.runOnAsync(loader);
    }


    @Override
    public void create(final String name, final String desc, final String picture) {
        GroupCreateContract.View view = getView();
        view.showLoading();
        // 判断参数
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(picture) || users.size() == 0) {
            view.showError(R.string.label_group_create_invalid);
        }

        // 上传图片
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = uploadPicture(picture);
                if (TextUtils.isEmpty(url)) {
                    return;
                }
                // 开始网络请求
                GroupCreateModel model = new GroupCreateModel(name,desc,url,users);
                GroupHelper.create(model, GroupCreatePresenter.this);
            }
        });
    }


    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {
        if (isSelected) {
            users.add(model.author.getId());
        } else {
            users.remove(model.author.getId());
        }
    }

    /**
     * 上传图片方法
     *
     * @param path 图片地址
     * @return 上传后的地址
     */
    private String uploadPicture(String path) {
        // 同步上传
        String url = UploadHelper.uploadAvatar(path);
        if (TextUtils.isEmpty(url)) {
            // 切换到UI线程，提示信息
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    GroupCreateContract.View view = getView();
                    if (view != null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return url;
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            // 加载本地数据库数据
            List<UserSampleModel> sampleModels = UserHelper.getSampleContact();
            List<GroupCreateContract.ViewModel> models = new ArrayList<>();
            for (UserSampleModel sampleModel : sampleModels) {
                GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                viewModel.author = sampleModel;
                models.add(viewModel);
            }
            refreshData(models);
        }
    };

    @Override
    public void onDataLoad(GroupCard groupCard) {
        // 成功
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.onCreateSuccess();
                }
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 失败
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.showError(strRes);
                }
            }
        });
    }
}
