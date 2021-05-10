package com.example.iwen.factory.net;

import com.example.iwen.factory.model.api.RspModel;
import com.example.iwen.factory.model.api.account.AccountRspModel;
import com.example.iwen.factory.model.api.account.ChangePwdModel;
import com.example.iwen.factory.model.api.account.LoginModel;
import com.example.iwen.factory.model.api.account.LogoutModel;
import com.example.iwen.factory.model.api.account.LogoutRspModel;
import com.example.iwen.factory.model.api.account.RegisterModel;
import com.example.iwen.factory.model.api.group.GroupCreateModel;
import com.example.iwen.factory.model.api.group.GroupMemberAddModel;
import com.example.iwen.factory.model.api.message.MsgCreateModel;
import com.example.iwen.factory.model.api.user.UserUpdateModel;
import com.example.iwen.factory.model.card.GroupCard;
import com.example.iwen.factory.model.card.GroupMemberCard;
import com.example.iwen.factory.model.card.MessageCard;
import com.example.iwen.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 网络请求的所有接口
 *
 * @author : iwen大大怪
 * create : 12-7 007 18:27
 */
public interface RemoteService {

    /**
     * 网络请求一个注册接口
     *
     * @param model RegisterModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 网络请求一个登录接口
     *
     * @param model LoginModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 网络请求一个退出登录接口
     *
     * @param model LogoutModel
     * @return RspModel<LogoutRspModel>
     */
    @POST("account/logout")
    Call<RspModel<LogoutRspModel>> accountLogout(@Body LogoutModel model);

    /**
     * 网络请求一个修改密码接口
     *
     * @param model ChangePwdModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/changepwd")
    Call<RspModel<AccountRspModel>> changePassword(@Body ChangePwdModel model);

    /**
     * 绑定设备id
     *
     * @param pushId 设备id
     * @return RspModel<AccountRspModel>
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    /**
     * 用户更新的接口
     *
     * @param model UserUpdateModel
     * @return User
     */
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    /**
     * 用户搜索的接口
     *
     * @param name 名字
     * @return List<UserCard>
     */
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);

    /**
     * 用户关注的接口
     *
     * @param userId 用户id
     * @return List<UserCard>
     */
    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId") String userId);

    /**
     * 获取联系人列表的接口
     *
     * @return List<UserCard>
     */
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();

    /**
     * 查询某人的信息
     *
     * @return UserCard
     */
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    /**
     * 发送消息的接口
     *
     * @param model 要发送的消息的model
     * @return <RspModel<MessageCard>>
     */
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    /**
     * 创建群的接口
     *
     * @param model 创建群的model
     * @return <RspModel<GroupCard>>
     */
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    /**
     * 获取一个群信息的接口
     *
     * @return <RspModel<GroupCard>>
     */
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);

    /**
     * 群搜索的接口
     *
     * @param name 名字
     * @return List<GroupCard>
     */
    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    /**
     * 我的群列表的接口
     *
     * @param date 时间
     * @return List<GroupCard>
     */
    @GET("group/search/{date}")
    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);

    /**
     * 群成员列表的接口
     *
     * @param groupId 群id
     * @return List<GroupMemberCard>
     */
    @GET("group/{groupId}/members")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);


    /**
     * 给群添加成员的接口
     *
     * @param groupId 群id
     * @return List<GroupMemberCard>
     */
    @POST("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId,
                                                         @Body GroupMemberAddModel memberAddModel);

}
