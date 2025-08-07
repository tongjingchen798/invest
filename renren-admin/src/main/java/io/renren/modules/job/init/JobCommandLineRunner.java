

package io.renren.modules.job.init;

import io.renren.modules.job.dao.ScheduleJobDao;
import io.renren.modules.job.entity.ScheduleJobEntity;
import io.renren.modules.job.utils.ScheduleUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 初始化定时任务数据
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class JobCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(JobCommandLineRunner.class);
    
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ScheduleJobDao scheduleJobDao;

    @Override
    public void run(String... args) {
        try {
            logger.info("开始初始化定时任务...");
            List<ScheduleJobEntity> scheduleJobList = scheduleJobDao.selectList(null);
            logger.info("查询到 {} 个定时任务", scheduleJobList.size());
            
            for(ScheduleJobEntity scheduleJob : scheduleJobList){
                try {
                    CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getId());
                    //如果不存在，则创建
                    if(cronTrigger == null) {
                        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
                        logger.info("创建定时任务: {}", scheduleJob.getBeanName());
                    }else {
                        ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
                        logger.info("更新定时任务: {}", scheduleJob.getBeanName());
                    }
                } catch (Exception e) {
                    logger.error("处理定时任务失败: {}, 错误: {}", scheduleJob.getBeanName(), e.getMessage(), e);
                }
            }
            logger.info("定时任务初始化完成");
        } catch (Exception e) {
            logger.error("初始化定时任务失败: {}", e.getMessage(), e);
            // 不抛出异常，避免应用启动失败
        }
    }
}