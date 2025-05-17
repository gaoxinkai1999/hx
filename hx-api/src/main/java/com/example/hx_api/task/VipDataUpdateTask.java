package com.example.hx_api.task;

import com.example.hx_api.service.VipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * VIP数据更新定时任务
 */
@Slf4j
@Component
public class VipDataUpdateTask {
    @Autowired
    private VipService vipService;
    
    /**
     * 定时任务，以固定频率处理下一批VIP数据的同步更新。
     * cron表达式在 @Scheduled 注解中定义了执行周期，当前暂定为每6分钟。
     * 您可以根据实际会员总数、期望的日更新完成时间以及外部API的承受能力来调整此cron表达式。
     * 例如，如果会员很多，可以调整为每10分钟或每小时执行对应的cron设置。
     */
    @Scheduled(cron = "0 */6 * * * ?") // 暂定每6分钟执行一次
    public void scheduledVipDataSync() {
        log.info("开始执行VIP数据批量同步定时任务...");
        try {
            vipService.synchronizeNextBatchOfVipsFromExternalApi();
            log.info("VIP数据批量同步定时任务单次执行完成。");
        } catch (Exception e) {
            log.error("VIP数据批量同步定时任务执行失败", e);
        }
    }
} 