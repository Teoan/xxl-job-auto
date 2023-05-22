package com.teoan.job.auto.core;


import cn.hutool.json.JSONUtil;
import com.teoan.job.auto.core.model.XxlJobGroup;
import com.teoan.job.auto.core.model.XxlJobInfo;
import com.teoan.job.auto.core.service.JobGroupService;
import com.teoan.job.auto.core.service.JobInfoService;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Teoan
 * @description xxl-job 自动注册实现
 * @since 2023/04/07 15:57
 */
@Component
@Slf4j
public class JobAutoRegister {

    @Resource
    private JobGroupService jobGroupService;

    @Resource
    private JobInfoService jobInfoService;

    @Resource
    private ApplicationContext applicationContext;


    /**
     * 监听事件实现自动注册逻辑
     * 不影响主线程序启动速度，使用异步执行
     */
    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 注册执行器 失败直接返回
        if(!addJobGroup()) {
            return;
        }
        // 注册任务
        addJobInfo();
        log.info(">>>>>>>>>>> xxl-job auto register success");
    }

    //自动注册执行器
    private Boolean addJobGroup() {
        if (jobGroupService.preciselyCheck())
            return true;

        if (jobGroupService.autoRegisterGroup()){
            log.info(">>>>>>>>>>> xxl-job auto register group success!");
        }else {
            return false;
        }
        return true;
    }

    private void addJobInfo() {
        List<XxlJobGroup> jobGroups = jobGroupService.getJobGroup();
        XxlJobGroup xxlJobGroup = jobGroups.get(0);
        List<Object> beanList = new ArrayList<>(applicationContext.getBeansWithAnnotation(Component.class).values());
        beanList.forEach(bean -> {
            Map<Method, Scheduled> annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                    (MethodIntrospector.MetadataLookup<Scheduled>) method -> AnnotatedElementUtils.findMergedAnnotation(method, Scheduled.class));
            annotatedMethods.forEach((k, v) -> {
                // 停止Spring自带的定时任务
                stopScheduled(k.getDeclaringClass());
                // 自动注册到xxl-job 暂定Handle名称规则beanName#MethodName
                String handlerName = StringUtils.joinWith("#", k.getDeclaringClass().getName(), k.getName());
                // 注册xxl-job的任务
                registJobHandler(handlerName, k);
                //因为是模糊查询，需要再过滤一次
                Optional<XxlJobInfo> first = jobInfoService.getJobInfo(xxlJobGroup.getId(), handlerName).stream()
                        .filter(xxlJobInfo -> xxlJobInfo.getExecutorHandler().equals(handlerName))
                        .findFirst();
                XxlJobInfo xxlJobInfo = createXxlJobInfo(xxlJobGroup, v, handlerName);
                if (first.isEmpty()) {
                    Integer jobInfoId = jobInfoService.addJobInfo(xxlJobInfo);
                    if (ObjectUtils.isNotEmpty(jobInfoId)) {
                        log.info(">>>>>>>>>>> xxl-job auto add jobInfo success! JobInfoId[{}] JobInfo[{}]", jobInfoId,
                                JSONUtil.toJsonStr(xxlJobInfo));
                    }
                }
            });
        });
    }

    /**
     * 构造任务详情对象
     *
     * @param xxlJobGroup 执行器信息
     * @param scheduled   spring注解信息
     * @param handlerName handler名称
     * @return 任务详情对象
     */
    private XxlJobInfo createXxlJobInfo(XxlJobGroup xxlJobGroup, Scheduled scheduled, String handlerName) {
        //目前存在默认配置，后续有需求再修改
        String scheduleType = "CRON";
        String scheduleConf = scheduled.cron();
        // 判断调度类型
        if (StringUtils.isBlank(scheduleConf)) {
            long fixed = scheduled.fixedDelay() == -1L ? scheduled.fixedRate() : scheduled.fixedDelay();
            scheduleType = "FIX_RATE";
            scheduleConf = String.valueOf(fixed / 1000);
        }
        return XxlJobInfo.builder()
                .scheduleType(scheduleType)
                .scheduleConf(scheduleConf)
                .jobGroup(xxlJobGroup.getId())
                .jobDesc(handlerName)
                .author("JobAutoRegister")
                .glueType("BEAN")
                .executorHandler(handlerName)
                .executorRouteStrategy("FIRST")
                .misfireStrategy("DO_NOTHING")
                .executorBlockStrategy("SERIAL_EXECUTION")
                .executorTimeout(0)
                .executorFailRetryCount(0)
                .glueRemark("GLUE代码初始化")
                .triggerStatus(1)
                .build();
    }


    /**
     * 停止Spring自带的定时注解
     *
     * @param clazz 带有定时注解的类
     */
    private void stopScheduled(Class<?> clazz) {
        ScheduledAnnotationBeanPostProcessor processor = (ScheduledAnnotationBeanPostProcessor) applicationContext
                .getBean("org.springframework.context.annotation.internalScheduledAnnotationProcessor");
        processor.postProcessBeforeDestruction(applicationContext.getBean(clazz), "");
    }


    /**
     * 注册任务到xxl-job上
     *
     * @param handlerName   JobHandler名称
     * @param executeMethod 执行定时任务的方法
     */
    private void registJobHandler(String handlerName, Method executeMethod) {
        executeMethod.setAccessible(true);
        // xxl-job初始化和销毁方法对象，后续有需要再赋值
        Method initMethod = null;
        Method destroyMethod = null;
        //获取方法的Bean对象
        Object bean = applicationContext.getBean(executeMethod.getDeclaringClass());
        XxlJobExecutor.registJobHandler(handlerName, new MethodJobHandler(bean, executeMethod, initMethod, destroyMethod));
    }

}
