package com.example.iwen.common;

/**
 * @author : Iwen大大怪
 * create : 2020/11/11 14:33
 */
public class Common {
    /**
     * 一些不可变的、永恒的参数
     * 通常用于一些配置
     */
    public interface Constance{
        // 手机号的正则表达式：11位
        String REGEX_MOBILE = "[1][3,4,5,6,7,8,9][0-9]{9}$";
        // 基础的网络请求地址
        // String API_URL = "https://www.easy-mock.com/mock/5fb4acf4bae16b281b2fdc76/";
        String API_URL = "http://10.129.9.177:8080/api/";
        //String API_URL = "http://127.0.0.1:8080/api/";

        String AccessKey_ID ="LTAI4G7qeeWttgB6YC2KGXNf";
        String AccessKey_Secret ="bCJ9ILXJtevcfLrTy5iY9aBE8R40e5";
    }
}
