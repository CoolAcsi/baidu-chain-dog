## 百度莱茨狗购买爬虫

[莱茨狗](https://pet-chain.baidu.com/)

如果这个项目对你有帮助，烦请点一下右上角的star，thanks~

v0.6版本使用前请先解压`resources/`下的`svm.model.zip`文件到该目录；

效果图：
![](https://ws4.sinaimg.cn/large/006tKfTcly1fochjbt23dj31iu0jin45.jpg)

### 功能
当前版本只是简单地根据预定价格自动买入。

### 特色
- 本地、快速、准确的验证码识别机制，识别正确率高达96%；
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

config.json 配置
```
{
  // 每次刷新的时间间隔
  "time": 500,
  // 是否显示全量日志
  "logSwitch": false,
  // 是否执行程序
  "isExecutable": true,
  // 验证码策略，目前支持chaojiying/local，local暂不可用
  "verCodeStrategy": "chaojiying",
  // 是否导出验证码到本地
  "exportSwitch": false,
  // 导出验证码路径
  "exportVerCodeImgPath": "",
  // 账号，可以添加多个
  "acounts": [
    {
      // 账号描述，在日志中表示用户名
      "des": "Acsi",
      "cookie":"我的cookie"
    }
  ],
  // 价格配置，目前只有购买价格是生效的
  "amounts":[
    {
      "rareDegree": 0,
      "des":"普通",
      "buyAmount": 250,
      "saleAmount": 999999
    },{
      "rareDegree": 1,
      "des":"稀有",
      "buyAmount": 250,
      "saleAmount": 999999
    },{
      "rareDegree": 2,
      "des":"卓越",
      "buyAmount": 250,
      "saleAmount": 999999
    },{
      "rareDegree": 3,
      "des":"史诗",
      "buyAmount": 500,
      "saleAmount": 999999
    },{
      "rareDegree": 4,
      "des":"神话",
      "buyAmount": 40000,
      "saleAmount": 999999
    },{
      "rareDegree": 5,
      "des":"传说",
      "buyAmount": 40000,
      "saleAmount": 999999
    }
  ]
}
```


### 说明
你也可以只使用它的验证码识别服务：
 
`POST` `http://localhost:{port}/ocr` 参数为诸如`{"img":"xxxxxxxxxxxx"}`格式的json串，其中img为要识别图片的base64加密数据 返回识别结果字符串。
 
 该工程只有训练好的模型，训练算法近几日开源； 
 

 ### 版本更新
 #### v0.7
 - 修复若干bug；
 - 增加了对服务器挂机的支持，可以运行时动态修改配置；
 - 百度更新了验证码，本地识别暂不可用；
 
 #### v0.6
 - 修复若干bug；
 - 优化验证码图像处理算法、重新训练模型，目前正确率提升至96%；
 - 使用前请先解压`resources/`下的`svm.model.zip`文件到该目录；
 
 #### v0.5
 - 项目重构，欢迎review  : )
 - 优化了验证码校验逻辑，生单速度目测提升40%；
 - 更灵活的配置方式，使用config.json即可完成必要配置，开放一些配置项；
 - 更简洁的启动方式；
 - 更加人性化的日志；
 
 
 欢迎交流，不乐意回答如何搭环境、如何运行等非技术问题，下面是我的微信：
 
 ![](https://ws4.sinaimg.cn/large/006tKfTcly1foahsdmz9vj30e80e8t8w.jpg)
