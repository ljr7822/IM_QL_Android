# 轻聊 APP开发文档（更新中...）

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