## 百度莱茨狗购买爬虫

[莱茨狗](https://pet-chain.baidu.com/)

如果这个项目对你有帮助，烦请点一下右上角的star，thanks~

效果图：
![](https://ws4.sinaimg.cn/large/006tKfTcly1fochjbt23dj31iu0jin45.jpg)

### 功能
当前版本只是简单地根据预定价格自动买入。

### 特色
- 本地、快速、较为准确的验证码识别机制；
- 健壮的抓狗机制，长时间挂机不会被block；
- 允许失败重抓（主要针对验证码识别错误）；
- 支持多账号同时运行；

### 使用
对于没有接触过java的同学，可以使用 [intellig idea](https://www.jetbrains.com/idea/)，参考网上教程配好[jdk](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)（此工程只能使用jdk8（9也不行））

准备就绪后找到Application.java，点击下图中的运行按钮即可启动应用。
![](https://ws4.sinaimg.cn/large/006tKfTcly1foaizv2a2ej30lo0cq75v.jpg)

1. Application.java 配置端口号；
2. config.json配置用户信息、价格等；
3. 准备就绪后启动应用；
4. enjoy it~


### 说明
你也可以只使用它的验证码识别服务：
 
`POST` `http://localhost:{port}/ocr` 参数为诸如`{"img":"xxxxxxxxxxxx"}`格式的json串，其中img为要识别图片的base64加密数据 返回识别结果字符串。
 
 该工程只有训练好的模型，训练算法近几日开源； 
 

 ### 版本更新
 #### v0.5
 - 项目重构，欢迎review  : )
 - 优化了验证码校验逻辑，生单速度目测提升50%；
 - 更灵活的配置方式，使用config.json即可完成必要配置，开放一些配置项；
 - 更简洁的启动方式；
 - 更加人性化的日志；
 
 
 欢迎交流，不乐意回答如何搭环境、如何运行等非技术问题，下面是我的微信：
 
 ![](https://ws4.sinaimg.cn/large/006tKfTcly1foahsdmz9vj30e80e8t8w.jpg)
