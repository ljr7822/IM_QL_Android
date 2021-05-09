package com.example.iwen.factory.model.api.account;

/**
 * 退出登录的请求model
 *
 * @author iwen大大怪
 * Create to 2021/05/09 21:28
 */
public class LogoutModel {
    private String account;

    public LogoutModel(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
