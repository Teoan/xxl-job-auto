package com.teoan.job.auto.core.service.impl;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.teoan.job.auto.core.model.XxlJobGroup;
import com.teoan.job.auto.core.service.JobGroupService;
import com.teoan.job.auto.core.service.JobLoginService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Teoan
 * @description 执行器任务处理实现类
 * @since 2023/04/07 14:50
 */
@Service
@Slf4j
public class JobGroupServiceImpl implements JobGroupService {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.title}")
    private String title;

    /*
     * 执行器地址类型：0=自动注册、1=手动录入
     * */
    @Value("${xxl.job.executor.addressType:0}")
    private Integer addressType;

    /*
     * 执行器地址列表，多地址逗号分隔(手动录入)
     * */
    @Value("${xxl.job.executor.addressList:}")
    private String addressList;

    @Resource
    private JobLoginService jobLoginService;

    @Override
    public List<XxlJobGroup> getJobGroup() {
        String url = adminAddresses + "/jobgroup/pageList";
        HttpResponse response = HttpRequest.post(url)
                .form("appname", appName)
                .form("title", title)
                .cookie(jobLoginService.getCookie())
                .execute();

        String body = response.body();
        JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);

        return array.stream()
                .map(o -> JSONUtil.toBean((JSONObject) o, XxlJobGroup.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean autoRegisterGroup() {
        String url = adminAddresses + "/jobgroup/save";
        HttpRequest httpRequest = HttpRequest.post(url)
                .form("appname", appName)
                .form("title", title);

        httpRequest.form("addressType", addressType);
        if (addressType.equals(1)) {
            if (Strings.isBlank(addressList)) {
                throw new RuntimeException("手动录入模式下,执行器地址列表不能为空");
            }
            httpRequest.form("addressList", addressList);
        }

        HttpResponse response = httpRequest.cookie(jobLoginService.getCookie())
                .execute();
        Object code = JSONUtil.parse(response.body()).getByPath("code");
        if(!code.equals(200)){
            log.error(">>>>>>>>>>> xxl-job auto register group fail!msg[{}]",JSONUtil.parse(response.body()).getByPath("msg"));
            return false;
        }
        return true;
    }

    @Override
    public boolean preciselyCheck() {
        List<XxlJobGroup> jobGroup = getJobGroup();
        Optional<XxlJobGroup> has = jobGroup.stream()
                .filter(xxlJobGroup -> xxlJobGroup.getAppname().equals(appName)
                        && xxlJobGroup.getTitle().equals(title))
                .findAny();
        return has.isPresent();
    }


}
