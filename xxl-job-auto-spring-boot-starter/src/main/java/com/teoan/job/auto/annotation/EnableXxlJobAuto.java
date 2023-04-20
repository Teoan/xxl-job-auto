package com.teoan.job.auto.annotation;

import com.teoan.job.auto.autoconfigure.XxlJobAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

/**
 * 启用注解
 * @author zhuangjy
 * @since 2023/04/18 10:20
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({XxlJobAutoConfiguration.class})
@EnableScheduling
@Documented
public @interface EnableXxlJobAuto {
}
