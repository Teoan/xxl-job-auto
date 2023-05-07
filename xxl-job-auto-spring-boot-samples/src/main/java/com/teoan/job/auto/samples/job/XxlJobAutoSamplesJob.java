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

    @Scheduled(fixedRate = 10000)
    public void samplesJob(){
        log.info("samplesJob executor success!");
    }
}
