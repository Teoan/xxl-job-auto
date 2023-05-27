package com.teoan.job.auto.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.teoan.job.auto.core.config.XxlJobAutoConfigProperties;
import com.teoan.job.auto.core.model.XxlJobInfo;
import com.teoan.job.auto.core.service.JobInfoService;
import com.teoan.job.auto.core.service.JobLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Teoan
 * @description 任务信息操作实现类
 * @since 2023/04/07 14:49
 */
@Service
@Slf4j
public class JobInfoServiceImpl implements JobInfoService {

    @Resource
    XxlJobAutoConfigProperties properties;


    private String adminAddresses;

    @PostConstruct
    private void init(){
        this.adminAddresses = properties.getAdmin().getAddresses();
    }


    @Autowired
    private JobLoginService jobLoginService;

    @Override
    public List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler) {
        String url = adminAddresses + "/jobinfo/pageList";
        HttpResponse response = HttpRequest.post(url)
                .form("jobGroup", jobGroupId)
                .form("executorHandler", executorHandler)
                .form("triggerStatus", -1)
                .cookie(jobLoginService.getCookie())
                .execute();

        String body = response.body();
        JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);

        return array.stream()
                .map(o -> JSONUtil.toBean((JSONObject) o, XxlJobInfo.class))
                .collect(Collectors.toList());
    }

    @Override
    public Integer addJobInfo(XxlJobInfo xxlJobInfo) {
        String url = adminAddresses + "/jobinfo/add";
        Map<String, Object> paramMap = BeanUtil.beanToMap(xxlJobInfo);
        HttpResponse response = HttpRequest.post(url)
                .form(paramMap)
                .cookie(jobLoginService.getCookie())
                .execute();

        JSON json = JSONUtil.parse(response.body());
        Object code = json.getByPath("code");
        if (code.equals(200)) {
            return Convert.toInt(json.getByPath("content"));
        }
        log.error("add jobInfo error! massage[{}]", json.getByPath("msg"));
        return null;
    }


    @Override
    public Boolean updateJobInfo(XxlJobInfo xxlJobInfo) {
        String url = adminAddresses + "/jobinfo/update";
        Map<String, Object> paramMap = BeanUtil.beanToMap(xxlJobInfo);
        HttpResponse response = HttpRequest.post(url)
                .form(paramMap)
                .cookie(jobLoginService.getCookie())
                .execute();

        JSON json = JSONUtil.parse(response.body());
        Object code = json.getByPath("code");
        if (code.equals(200)) {
            return true;
        }
        log.error("update jobInfo error! message[{}]", json.getByPath("msg"));
        return false;
    }
}
