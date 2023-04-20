package com.teoan.job.auto.core.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.stereotype.Component;

/**
 * @author: Teoan
 * @createTime: 2023/04/11 15:17
 * @description: 处理日志事件
 */
@Component
public class XxlJobLogAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (XxlJobHelper.getJobId() == -1) {
            return;
        }
        if (Level.ERROR.equals(iLoggingEvent.getLevel())) {
            ThrowableProxy throwableProxy = (ThrowableProxy) iLoggingEvent.getThrowableProxy();
            if (throwableProxy != null) {
                XxlJobHelper.log(throwableProxy.getThrowable());
            } else {
                XxlJobHelper.log(iLoggingEvent.getMessage());
            }
        } else {
            XxlJobHelper.log(iLoggingEvent.getMessage());
        }
    }
}
