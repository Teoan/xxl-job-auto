# xxl-job-auto

实现Spring定时任务与XXL-JOB灵活切换

## 使用

### 在POM文件中引入Starter依赖

提供的Starter对XXL-JOB没有强依赖，所以使用方还得引入XXL-JOB的依赖。

```xml
    <!-- xxl-job-core -->
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-job-core</artifactId>
    <version>${xxl-job.version}</version>
</dependency>
<dependency>
    <groupId>com.teoan</groupId>
    <artifactId>xxl-job-auto-spring-boot-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

### SpringBoor配置文件中添加XXL-JOB的配置

除了配置XXL-JOB的基本配置，还需要配置我们自定义实现功能所需要的配置项，具体如下：

```yml
server:
  port: 8080
spring:
  application:
    name: xxlJobAuto
xxl:
  job:
    # 自动注册自定义新增配置项 是否使用Xxl实现定时任务
    enable: true
    accessToken: pass@bingocloud1
    admin:
      addresses: http://localhost:8080/xxl-job-admin
      # 以下admin配置为自动注册自定义新增配置项，必须项
      username: admin                         #admin 用户名
      password: password                      #admin 密码
    executor:
      appname: ${spring.application.name}
      ip:
      address:
      logpath:
      logretentiondays: 3
      port: 0
      # 以下executor配置为自动注册自定义新增配置项，可选
      addressList:    #在addressType为1的情况下，手动录入执行器地址列表，多地址逗号分隔
      addressType: 0      #执行器地址类型：0=自动注册、1=手动录入，默认为0
      title: ${spring.application.name}    #执行器名称
```

### XXL-JOB执行器组件配置

这个是XXL-JOB执行器所需要的配置。

```java

@Configuration
@Slf4j
public class XxlJobConfig {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.executor.address}")
    private String address;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobSpringExecutor;
    }

}
```

### 使用SpringBoot自带的@Scheduled注解开发定时任务

新建一个Job类模拟使用定时任务的场景。

```java
/**
 * @author Teoan
 * @since 2023/04/19 10:14
 */
@Slf4j
@Component
public class XxlJobAutoSamplesJob {

    @Scheduled(fixedRate = 10000)
    public void samplesJob() {
        log.info("samplesJob executor success!");
    }
}
```

### 启动项目验证

先将配置文件中的xxl.job.enable设置为false，使用Spring默认的实现方式。

```txt
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.5)

2023-05-07 15:46:19.633 [main] INFO  com.teoan.job.auto.samples.XxlJobAutoApplication - Starting XxlJobAutoApplication using Java 17.0.6 with PID 28253 (/Users/teoan/Project/xxl-job-auto/xxl-job-auto-spring-boot-samples/target/classes started by teoan in /Users/teoan/Project/xxl-job-auto)
2023-05-07 15:46:19.645 [main] INFO  com.teoan.job.auto.samples.XxlJobAutoApplication - No active profile set, falling back to 1 default profile: "default"
2023-05-07 15:46:21.083 [main] INFO  o.s.boot.web.embedded.tomcat.TomcatWebServer - Tomcat initialized with port(s): 8080 (http)
2023-05-07 15:46:21.091 [main] INFO  org.apache.coyote.http11.Http11NioProtocol - Initializing ProtocolHandler ["http-nio-8080"]
2023-05-07 15:46:21.092 [main] INFO  org.apache.catalina.core.StandardService - Starting service [Tomcat]
2023-05-07 15:46:21.092 [main] INFO  org.apache.catalina.core.StandardEngine - Starting Servlet engine: [Apache Tomcat/10.1.7]
2023-05-07 15:46:21.179 [main] INFO  o.a.c.core.ContainerBase.[Tomcat].[localhost].[/] - Initializing Spring embedded WebApplicationContext
2023-05-07 15:46:21.179 [main] INFO  o.s.b.w.s.c.ServletWebServerApplicationContext - Root WebApplicationContext: initialization completed in 1295 ms
2023-05-07 15:46:21.367 [main] INFO  com.teoan.job.auto.samples.config.XxlJobConfig - >>>>>>>>>>> xxl-job config init.
2023-05-07 15:46:21.797 [main] INFO  o.s.b.actuate.endpoint.web.EndpointLinksResolver - Exposing 1 endpoint(s) beneath base path '/actuator'
2023-05-07 15:46:21.954 [main] INFO  org.apache.coyote.http11.Http11NioProtocol - Starting ProtocolHandler ["http-nio-8080"]
2023-05-07 15:46:21.969 [main] INFO  o.s.boot.web.embedded.tomcat.TomcatWebServer - Tomcat started on port(s): 8080 (http) with context path ''
2023-05-07 15:46:21.998 [scheduling-1] INFO  c.teoan.job.auto.samples.job.XxlJobAutoSamplesJob - samplesJob executor success!
2023-05-07 15:46:22.000 [main] INFO  com.teoan.job.auto.samples.XxlJobAutoApplication - Started XxlJobAutoApplication in 3.014 seconds (process running for 3.887)
2023-05-07 15:46:22.020 [Thread-4] INFO  com.xxl.job.core.server.EmbedServer - >>>>>>>>>>> xxl-job remoting server start success, nettype = class com.xxl.job.core.server.EmbedServer, port = 9999
2023-05-07 15:46:22.397 [RMI TCP Connection(2)-192.168.123.139] INFO  o.a.c.core.ContainerBase.[Tomcat].[localhost].[/] - Initializing Spring DispatcherServlet 'dispatcherServlet'
2023-05-07 15:46:22.399 [RMI TCP Connection(2)-192.168.123.139] INFO  org.springframework.web.servlet.DispatcherServlet - Initializing Servlet 'dispatcherServlet'
2023-05-07 15:46:22.402 [RMI TCP Connection(2)-192.168.123.139] INFO  org.springframework.web.servlet.DispatcherServlet - Completed initialization in 3 ms
2023-05-07 15:47:31.997 [scheduling-1] INFO  c.teoan.job.auto.samples.job.XxlJobAutoSamplesJob - samplesJob executor success!
2023-05-07 15:47:41.997 [scheduling-1] INFO  c.teoan.job.auto.samples.job.XxlJobAutoSamplesJob - samplesJob executor success!
2023-05-07 15:47:51.996 [scheduling-1] INFO  c.teoan.job.auto.samples.job.XxlJobAutoSamplesJob - samplesJob executor success!
2023-05-07 15:48:01.994 [scheduling-1] INFO  c.teoan.job.auto.samples.job.XxlJobAutoSamplesJob - samplesJob executor success!
```

嗯，没啥毛病。**scheduling-1** 用的啥Spring自带的scheduling线程池去执行定时任务。
接下来将配置文件中的xxl.job.enable设置为true，再看看日志。

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.5)

2023-05-07 15:56:50.011 [main] INFO  com.teoan.job.auto.samples.XxlJobAutoApplication - Starting XxlJobAutoApplication using Java 17.0.6 with PID 30937 (/Users/teoan/Project/xxl-job-auto/xxl-job-auto-spring-boot-samples/target/classes started by teoan in /Users/teoan/Project/xxl-job-auto)
2023-05-07 15:56:50.025 [main] INFO  com.teoan.job.auto.samples.XxlJobAutoApplication - No active profile set, falling back to 1 default profile: "default"
2023-05-07 15:56:51.538 [main] INFO  o.s.boot.web.embedded.tomcat.TomcatWebServer - Tomcat initialized with port(s): 8080 (http)
2023-05-07 15:56:51.548 [main] INFO  org.apache.coyote.http11.Http11NioProtocol - Initializing ProtocolHandler ["http-nio-8080"]
2023-05-07 15:56:51.549 [main] INFO  org.apache.catalina.core.StandardService - Starting service [Tomcat]
2023-05-07 15:56:51.549 [main] INFO  org.apache.catalina.core.StandardEngine - Starting Servlet engine: [Apache Tomcat/10.1.7]
2023-05-07 15:56:51.642 [main] INFO  o.a.c.core.ContainerBase.[Tomcat].[localhost].[/] - Initializing Spring embedded WebApplicationContext
2023-05-07 15:56:51.642 [main] INFO  o.s.b.w.s.c.ServletWebServerApplicationContext - Root WebApplicationContext: initialization completed in 1351 ms
2023-05-07 15:56:51.835 [main] INFO  com.teoan.job.auto.samples.config.XxlJobConfig - >>>>>>>>>>> xxl-job config init.
2023-05-07 15:56:52.282 [main] INFO  o.s.b.actuate.endpoint.web.EndpointLinksResolver - Exposing 1 endpoint(s) beneath base path '/actuator'
2023-05-07 15:56:52.444 [main] INFO  org.apache.coyote.http11.Http11NioProtocol - Starting ProtocolHandler ["http-nio-8080"]
2023-05-07 15:56:52.457 [main] INFO  o.s.boot.web.embedded.tomcat.TomcatWebServer - Tomcat started on port(s): 8080 (http) with context path ''
2023-05-07 15:56:52.477 [scheduling-1] INFO  c.teoan.job.auto.samples.job.XxlJobAutoSamplesJob - samplesJob executor success!
2023-05-07 15:56:52.480 [main] INFO  com.teoan.job.auto.samples.XxlJobAutoApplication - Started XxlJobAutoApplication in 3.118 seconds (process running for 3.86)
2023-05-07 15:56:52.515 [Thread-4] INFO  com.xxl.job.core.server.EmbedServer - >>>>>>>>>>> xxl-job remoting server start success, nettype = class com.xxl.job.core.server.EmbedServer, port = 9999
2023-05-07 15:56:52.712 [RMI TCP Connection(3)-192.168.123.139] INFO  o.a.c.core.ContainerBase.[Tomcat].[localhost].[/] - Initializing Spring DispatcherServlet 'dispatcherServlet'
2023-05-07 15:56:52.714 [RMI TCP Connection(3)-192.168.123.139] INFO  org.springframework.web.servlet.DispatcherServlet - Initializing Servlet 'dispatcherServlet'
2023-05-07 15:56:52.715 [RMI TCP Connection(3)-192.168.123.139] INFO  org.springframework.web.servlet.DispatcherServlet - Completed initialization in 1 ms
2023-05-07 15:56:53.145 [main] INFO  com.teoan.job.auto.core.JobAutoRegister - >>>>>>>>>>> xxl-job auto register group success!
2023-05-07 15:56:53.490 [main] INFO  com.xxl.job.core.executor.XxlJobExecutor - >>>>>>>>>>> xxl-job register jobhandler success, name:com.teoan.job.auto.samples.job.XxlJobAutoSamplesJob#samplesJob, jobHandler:com.xxl.job.core.handler.impl.MethodJobHandler@223cbf0d[class com.teoan.job.auto.samples.job.XxlJobAutoSamplesJob#samplesJob]
2023-05-07 15:56:53.647 [main] INFO  com.teoan.job.auto.core.JobAutoRegister - >>>>>>>>>>> xxl-job auto add jobInfo success! JobInfoId[11085] JobInfo[{"id":0,"jobGroup":2080,"jobDesc":"com.teoan.job.auto.samples.job.XxlJobAutoSamplesJob#samplesJob","author":"JobAutoRegister","scheduleType":"FIX_RATE","scheduleConf":"10","misfireStrategy":"DO_NOTHING","executorRouteStrategy":"FIRST","executorHandler":"com.teoan.job.auto.samples.job.XxlJobAutoSamplesJob#samplesJob","executorBlockStrategy":"SERIAL_EXECUTION","executorTimeout":0,"executorFailRetryCount":0,"glueType":"BEAN","glueRemark":"GLUE代码初始化","triggerStatus":1,"triggerLastTime":0,"triggerNextTime":0}]
2023-05-07 15:56:53.650 [main] INFO  com.teoan.job.auto.core.JobAutoRegister - >>>>>>>>>>> xxl-job auto register success
2023-05-07 15:57:24.538 [xxl-job, EmbedServer bizThreadPool-123827075] INFO  com.xxl.job.core.executor.XxlJobExecutor - >>>>>>>>>>> xxl-job regist JobThread success, jobId:11085, handler:com.xxl.job.core.handler.impl.MethodJobHandler@223cbf0d[class com.teoan.job.auto.samples.job.XxlJobAutoSamplesJob#samplesJob]
2023-05-07 15:57:24.540 [xxl-job, JobThread-11085-1683446244537] INFO  c.teoan.job.auto.samples.job.XxlJobAutoSamplesJob - samplesJob executor success!
```

日志看起来没啥问题，注册执行器和注册任务信息的相关日志都打印了出来，定时任务的执行日志也有了。我们上调度中心看看。
![执行器管理列表](https://github.com/Teoan/xxl-job-auto/blob/develop/doc/images/119650416278778.png?raw=true)
![执行器任务列表](https://github.com/Teoan/xxl-job-auto/blob/develop/doc/images/267333194899548.png?raw=true)
![任务执行日志](https://github.com/Teoan/xxl-job-auto/blob/develop/doc/images/514241523586190.png?raw=true)
执行器和任务详情都自动添加到调度中心了，任务中心的日志也能在调度中心中查看了，开始愉快地使用吧。