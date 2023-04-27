package com.teoan.job.auto.autoconfigure;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 自动装配类
 * 当不启用xxl时启动spring自带定时任务
 */
@Configuration
@ConditionalOnProperty(name = "xxl.job.enable",havingValue = "false")
@EnableScheduling
public class SchedulingAutoConfiguration {
}
