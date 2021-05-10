package com.example.iwen.factory.model.api.account;

/**
 * 修改密码的model
 *
 * @author iwen大大怪
 * @Create 2021/05/10 1:49
 */
public class ChangePwdModel {
    private String userId;

    private String oldPwd;

    private String newPwd;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
