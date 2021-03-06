# 轻聊(LightChat) APP开发文档（更新中...）

## 说明

## 功能截图

## 技术框架

## 提交记录
- 第一次提交 2020.11.11
    - 初始化工程文件
    - 封装 BaseActivity基类
    - 封装 BaseFragment基类
    - 分包 common负责提取一些常规操作、lang负责存储字符串类型数据，方便多语言切换
    - app\build.gradle、common\build.gradle添加依赖库
    - 添加 图片资源文件、xml资源文件等

- 第二次提交 2020.11.13
    - 封装 MainActivity界面布局
    - 封装 底部导航栏
    - 实现 头像、顶部导航背景、点击效果
    - 实现 导航栏切换效果

- 第三次提交 2020.11.13
    - 修改 文件名
    - 修改 背景、布局等

- 第四次提交 2020.11.13
    - 封装 Fragment工具类，用于解决fragment的切换
    - 实现 浮动按钮的切换动画

- 第五次提交 2020.11.14
    - 封装 图片选择器类GalleyView
    - 修复 Activity基类初始化控件空指针异常Bug

- 第六次提交 2020.11.14
    - 修复 选择图片时右上角显示
    - 添加 Toast提示
    - 实现 图片选择器框架

- 第七次提交 2020.11.14
    - 添加 ucrop 图片剪切库
    - 添加 GalleryFragment类，实现选择图片

- 第八次提交 2020.11.15
    - 初步实现 头像图片的剪裁

- 第九次提交 2020.11.15
    - 完成 剪裁完成后对图片加载

- 第十次提交 2020.11.15
    - 添加 集合工具类 CollectionUtil
    - 添加 对文件流的操作工具类 StreamUtil
    - 添加 对文件或者字符串进行Hash算法，返回MD5值 HashUtil
    - 添加 Bitmap 工具类 BitmapUtil
    - 添加 时间工具类 DateTimeUtil
    - 添加 PicturesCompressor

- 第十一次提交 2020.11.15
    - 添加 动态权限类 PermissionsFragment
    - 完善 申请权限逻辑
    - 添加 启动页 判断是否获得权限；没有权限就弹出申请

- 第十二次提交 2020.11.15
    - 配置 阿里云oss存储
    - 修复  “Android P系统限制了明文流量的网络请求”->在xml文件夹下新建一个 network_security_config.xml，并在Manifest.xml中添加配置

- 第十三次提交 2020.12.7
    - 实现 登录、注册背景图着色

- 第十四次提交 2020.12.7
    - 实现 登录注册fragment在同一个activity切换
    - 实现 注册界面布局
    - 实现 登录界面布局
    - 封装 BasePresenter、BaseContract基类
    - 添加NetWork类
    - 添加RemoteService接口：网络请求的所有接口都在里面
    - 实现注册功能、与接口互调

- 第十五次提交 2020.12.8
    - 实现 启动页动画
    - 实现 登录页面布局
    - 创建 持久化包
    - 实现 启动界面逻辑

- 第十六次提交 2020.12.8
    - 添加全局广播接收器 MessageReceiver
    - 配置个推，配置个推框架进行设备绑定

- 第十七次提交 2020.12.8
    - 添加 数据库
    - 初始化 数据库
    - 创建 DBFlow的数据库过滤字段 DBFlowExclusionStrategy
    - 创建 数据库基本信息类 AppDatabase
    - 保存 用户信息到持久化xml文件中

- 第十八次提交 2020.12.8
    - 绑定设备PushId
    - 完成数据持久化
    - token注入请求头

- 第十九次提交 2020.12.8
    - 用户信息完善的布局
    - 完善用户信息修改

- 第二十次提交 2021.1.28
    - 完成 添加联系人的网络请求

- 第二十一次提交 2021.1.28
    - 改进搜索人列表
    - 改进登录、完善信息逻辑
    - 与后台数据库表字段实现对接
    
- 第二十二次提交 2021.2.9
    - 修改 初次登陆时完善用户信息bug
    - 完善 数据提交至数据库
    
- 第二十三次提交 2021.2.12
    - 定义了一些基本契约
        - app目录：主要的界面实现包
        - common目录：公共的基础类包
        - lang目录：项目语言包
        - factory目录：项目的逻辑实现(契约、实现、网络、逻辑处理、缓存、数据库操作等)
    - 关注一个人时，同时添加他对我的关注（待完善）
    - 进入群聊的方式：新建群、申请加入群聊
    - 主界面搜索规则
        - 点击搜索图标时，除了群列表界面外，其余都进入搜索人界面    
        
- 第二十四次提交 2021.2.12
    - 实现 数据库查询联系人列表
    
- 第二十五次提交 2021.2.12
    - 实现 数据库查询联系人列表、网络获取联系人列表
    - 实现 网络与本地数据对比
    
- 第二十六次提交 2021.2.12
    - 实现 个人信息界面布局
    - 完善 个人头像加载
    - 完善 个人界面跳转
    
- 第二十七次提交 2021.2.13
    - 实现 界面与数据库信息交互
    
- 第二十八次提交 2021.2.14
    - 修改 部分代码
    - 添加 注释
    
- 第二十九次提交 2021.2.14
    - 添加 card目录下的class文件
    - 添加 db目录下的class文件
    - 添加 api下的PushModel文件
    
- 第三十次提交 2021.2.14
    - 封装 数据库文件的统一写入
    
- 第三十一次提交 2021.2.19
    - 封裝 統一的數據流通知
    
- 第三十二次提交 2021.2.19
    - 封裝 統一的消息中心通知

- 第三十三次提交 2021.2.19
    - 封裝 統一的群中心通知
    
- 第三十四次提交 2021.2.19
    - 完善 統一的群中心通知
    - 完善 統一的消息中心通知
    - 添加 群聊的简单辅助工具类
    
- 第三十五次提交 2021.2.21
    - 添加 通知监听器接口
    
- 第三十六次提交 2021.2.21
    - 添加 会话辅助工具类
    - 完善 DbHelper工具类
    
- 第三十七次提交 2021.2.22
    - 对RecyclerView进行简单的Presenter封装
    - 添加 联系人仓库ContactRepository
    - 添加 联系人数据集合ContactDataSource
    
- 第三十八次提交 2021.2.23
    - 修改bug 联系人页面不能展示联系人
    
- 第三十九次提交 2021.2.23
    - 封装 presenter基类 BaseSourcePresenter
    
- 第四十次提交 2021.2.23
    - 处理 推送过来的消息
    
- 第四十一次提交 2021.2.24
    - 封装 聊天界面的fragment布局
    - 实现 聊天界面的布局
    - 解决 底部输入框没有收缩问题
    - 解决 底部输入框收缩后顶部状态栏没有浸透问题(软键盘弹出问题)
    
- 第四十二次提交 2021.2.24
    - 添加 聊天界面的menu菜单
    - 实现 聊天界面按钮点击事件
    - 实现 发送按钮状态变化
    
- 第四十三次提交 2021.2.24
    - 实现 聊天界面的每一个Item
    - 实现 Adapter根布局
    - 实现 文字holder
    - 实现 语音holder
    - 实现 图片holder
        
- 第四十四次提交 2021.2.24
    - 实现 客户端发送消息功能
    - 添加 聊天契约接口ChatContact.java
    - 实现 单聊、群聊的接口对接
    
- 第四十五次提交 2021.2.24
    - 实现 用户聊天 ChatUserPresenter.java
    - 实现 群聊天 ChatGroupPresenter.java
    - 实现 聊天基类 ChatPresenter.java
    - 实现 消息的数据源定义 MessageDataSource
    - 实现 跟某人聊天时的聊天记录列表 MessageRepository
    - 添加 构建一个新消息的model
    
- 第四十六次提交 2021.2.25
    - 实现 消息发送失败重新发送
    - 实现 聊天界面好友信息初始化
    - 添加 聊天界面标题部分虚化
    
- 第四十七次提交 2021.2.26
    - 实现 最近消息列表
    - 添加 消息列表契约 SessionContract.java
    - 添加 最近聊天列表的 SessionPresenter.java
    
- 第四十八次提交 2021.3.2
    - 添加 获取一个简单的时间字符串 方法
    - 添加 最近会话数据源定义接口 SessionDataSource
    - 添加 最近聊天列表仓库，对SessionDataSource的实现 SessionRepository
    
- 第四十九次提交 2021.3.3
    - 解决 Session.java类中的refreshToNow问题
    - 修改 Logo
    
- 第五十次提交 2021.3.3
    - 修改 会话列表时间显示格式为 HH:mm:ss
    
- 第五十一次提交 2021.3.3
    - 修复 用户授权后不能跳转到相关页面的bug
    - 修复 聊天列表始终滚动到最新一条 smoothScrollToPosition
    
- 第五十二次提交 2021.3.4 
    - 重新布局登录注册界面
    - 修复 登录、注册Fragment切换
    
- 第五十三次提交 2021.04.24
    - 创建群的界面布局
    - 添加 GroupCreateContract
    - 添加 GroupCreatePresenter
    - 添加 cell布局文件
    - 添加 头像点击选择图片
    - 添加 软键盘隐藏

- 第五十四次提交 2021.04.25
    - 加载创建群时联系人列表
    - 添加 ProgressDialog
    - 解决了 创建群时创建者id没有绑定ownerId导致客户端闪退问题
    
- 第五十五次提交 2021.04.25
    - 编写 群相关接口
    - 实现 群搜索相关
    - 实现 个人的群列表
    - 添加 MemberUserModel
    - 完善 GroupFragment
    - 完善 GroupHelper里面的 刷新我的群组列表 方法
    - 添加 GroupHelper里面的 关联查询用户和群的表，返回一个 MemberUserModel 表的集合 方法
    - 添加 GroupHelper里面的 从网络去刷新一个群的成员信息 方法
    - 添加 GroupHelper里面的 获取一个群的成员数量 方法
    
- 第五十六次提交 2021.04.26
    - 添加 群聊天的ChatGroupPresenter
    - 添加 MessageGroupRepository
    - 完善 ChatContact接口
    - 添加公共布局 fragment_chat_common.xml
    - 添加 lay_chat_header_user.xml
    - 添加 lay_chat_header_group.xml
    - 使用 替换布局进行替换
     
- 第五十七次提交 2021.04.27
    - 完善 群聊天的ChatGroupPresenter
    - 添加 单独头像布局 lay_chat_group_avatar
    - 完善 添加群成员头像逻辑
    - 完善 显示群成员
    - 修改 群成员跳转详情页无反应 关联查询错误
    - 修改 头像不显示问题
     
- 第五十八次提交 2021.04.28
    - 完善 群聊天的ChatGroupPresenter
    - 完善 群聊天的MessageGroupRepository
    - 完善 群消息过滤器
    - 完善 头像隐藏
    
- 第五十九次提交 2021.04.29
    - 完善 展示群成员列表
    - 完善 添加新成员界面
    - 添加 GroupMemberActivity
    - 添加 GroupMembersPresenter
    - 添加 GroupMembersContract
    - 修改 查找群的主题
    - 修改 查找联系人的主题
    
- 第六十次提交 2021.04.30
    - 添加 自定义表情输入面板 SmoothInputLayout.java
    - 修改 表情、语音、更多点击事件的实现逻辑
    - 实现 面板切换

- 第六十一次提交 2021.05.02
    - 完善 聊天拓展：表情模块实现
    - 表情解决方案
        - Emoji标准方案
        - 内嵌font字体、资源文件
        - 动态下发方案，表情商店
    - 添加 faceres 资源模块model
    - 添加 表情资源
    - 添加 表情面板lay_panel_face
    - 添加 表情工具类Face.java
  
- 第六十二次提交 2021.05.02
    - 完善 输入表情到editable 逻辑
    - 完善 输入表情解析 逻辑
    
- 第六十三次提交 2021.05.02
    - 添加 图片布局面板实现
    - 实现 基类中的发送图片
    - 完善 MessageHelper中发送图片的逻辑
    - 删除 Common.java配置文件
    
- 第六十四次提交 2021.05.02
    - 修改 消息发送后自动定位到最后一行
    - 增加 聊天功能面板弹出时，顶部顶部会缩回
    - 增加 聊天窗口点击其他view时软键盘缩回
    
- 第六十五次提交 2021.05.06
    - 实现 语音聊天
    - 流程 开始进行录制 --> 压缩转码 --> 上传oss
    
- 第六十六次提交 2021.05.09
    - 修改 项目名称为LightChat
    - 修改 文件夹名
    - 修改 包名
    - 修改 applicationId
    - 修改 个推包名配置
    
- 第六十七次提交 2021.05.09
    - 实现 退出登录
    
- 第六十八次提交 2021.05.10
    - 实现 用户修改密码