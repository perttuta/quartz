package fi.perttuta.quartz.job;

import fi.perttuta.quartz.dao.QuartzNodeDao;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Service
public class DemoJobInitializer {

  private static final String DEMO_JOB = "job-demo";
  private static final String DEMO_TRIGGER = "trigger-demo";
  private static final String DEMO_GROUP = "demo";

  private Scheduler scheduler;
  private QuartzNodeDao quartzNodeDao;
  private SimpleTrigger demoTrigger;
  private TriggerKey demoTriggerKey;

  @Autowired
  public DemoJobInitializer(
      Scheduler scheduler,
      QuartzNodeDao quartzNodeDao) {
    this.scheduler = scheduler;
    this.demoTriggerKey = TriggerKey.triggerKey(DEMO_TRIGGER, DEMO_GROUP);
    this.quartzNodeDao = quartzNodeDao;
  }

  @PostConstruct
  public void initialize() throws SchedulerException {
    if (quartzNodeDao.isPrimary()) {
      // only the "lead" process gets to update the jobs. Jobs are updated by removing all job data and recreating it
      scheduler.clear();
      final int SECOND = 1 * 1000;
      this.demoTrigger = createTrigger(SECOND);
      JobDetail importJobDetail = JobBuilder
          .newJob(DemoJob.class)
          .withIdentity(DEMO_JOB, DEMO_GROUP)
          .storeDurably()
          .requestRecovery()
          .build();

      System.out.println("Chosen as master, created new scheduled job");
      scheduler.scheduleJob(importJobDetail, demoTrigger);
    } else {
      System.out.println("Not master, skipping job creation");
    }
  }

  /**
   * Creates a simple millisecond based trigger.
   */
  private SimpleTrigger createTrigger(long millis) {
    return TriggerBuilder.newTrigger()
        .withIdentity(demoTriggerKey)
        .withSchedule(simpleSchedule()
            .withIntervalInMilliseconds(millis)
            .repeatForever())
        .build();
  }
}
