## 百度莱茨狗购买爬虫
[莱茨狗](https://pet-chain.baidu.com/)

效果图：
![](https://ws1.sinaimg.cn/large/006tKfTcly1foajh6yj97j31ge0miteu.jpg)

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
2. RareDegree.java类配置买入价格；
3. PetOperationInvoke.java 可自定义header；
4. TestController.java 配置用户cookie；
5. 准备就绪后启动应用，并访问页面`http://localhost:{port}/start`(port为你配置的端口号，默认8080)启动任务；（该页面只能访问一次，重复访问会加快抓取速度但不保证服务可靠性）
6. enjoy it~


### 说明
你也可以只使用它的验证码识别服务：
 
`POST` `http://localhost:{port}/ocr` 参数为诸如`{"img":"xxxxxxxxxxxx"}`格式的json串，其中img为要识别图片的base64加密数据 返回识别结果字符串。
 
 该工程只有训练好的模型，训练算法近几日开源； 
 
 
 写得比较仓促，代码质量比较差。
 
 可以加我微信交流，拒绝回答如何搭环境、如何运行等非技术问题。
 ![](https://ws4.sinaimg.cn/large/006tKfTcly1foahsdmz9vj30e80e8t8w.jpg)
