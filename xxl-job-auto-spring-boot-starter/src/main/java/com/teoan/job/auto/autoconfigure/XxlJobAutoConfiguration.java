package com.teoan.job.auto.autoconfigure;

import com.teoan.job.auto.annotation.EnableXxlJobAuto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;

/**
 * 自动装配类
 * @author zhuangjy
 * @since 2023/04/18 10:18
 */
@ConditionalOnClass(EnableXxlJobAuto.class)
@ComponentScan("com.teoan.job.auto.core")
public class XxlJobAutoConfiguration {
}
