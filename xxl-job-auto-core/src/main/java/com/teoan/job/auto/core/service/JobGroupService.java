package com.teoan.job.auto.core.service;


import com.teoan.job.auto.core.model.XxlJobGroup;

import java.util.List;

/**
 * @author Teoan
 * @description 执行器任务处理服务类
 * @since 2023/04/07 14:17
 */
public interface JobGroupService {

    /**
     * 获取执行器列表
     * @return
     */
    List<XxlJobGroup> getJobGroup();

    /**
     * 自动注册配置执行器
     */
    boolean autoRegisterGroup();

    /**
     * 检查执行器是否已存在
     */
    boolean preciselyCheck();


}
