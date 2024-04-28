package com.teoan.job.auto.samples.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Teoan
 * @since 2023/04/19 10:14
 */
@Slf4j
@Component
public class XxlJobAutoSamplesJob {

    /**
     * 固定速度(FIX_RATE)
     */
    @Scheduled(fixedRate = 10000)
    public void fixedRateJob() {
        log.info("fixedRateJob executor success!");
    }


    /**
     * CORN
     */
    @Scheduled(cron = "0/10 * * * * ? ")
    public void cronJob() {
        log.info("cronJob executor success!");
    }


}
