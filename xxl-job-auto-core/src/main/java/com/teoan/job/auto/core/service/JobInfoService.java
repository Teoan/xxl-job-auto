package com.teoan.job.auto.core.service;


import com.teoan.job.auto.core.model.XxlJobInfo;

import java.util.List;

/**
 * @author: Teoan
 * @createTime: 2023/04/07 14:27
 * @description: 任务信息操作服务类
 */
public interface JobInfoService {

    /**
     * 获取任务信息
     * @param jobGroupId 执行器ID
     * @param executorHandler 执行Handler
     */
    List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler);

    /**
     * 添加任务
     * @param xxlJobInfo 任务信息
     */
    Integer addJobInfo(XxlJobInfo xxlJobInfo);



    /**
     * 更新任务
     * @param xxlJobInfo 任务信息
     */
    Boolean updateJobInfo(XxlJobInfo xxlJobInfo);
}
