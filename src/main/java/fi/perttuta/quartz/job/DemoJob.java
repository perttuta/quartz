package fi.perttuta.quartz.job;

import org.quartz.*;

/**
 * Quartz job for demoing clustering. This job should have at most one running instance i.e. only one node should execute it in
 * clustered environment at any given moment.
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class DemoJob implements Job {

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    try {
      System.out.println("Executing scheduled jobs on instance " + jobExecutionContext.getScheduler().getSchedulerInstanceId());
      try {
        final int TWO_SECONDS = 2 * 1000;
        Thread.sleep(TWO_SECONDS);
      } catch (InterruptedException e) {
        // ignored
      }
      System.out.println("Job finished");
    } catch (SchedulerException e) {
      System.out.println("Unexpected exception");
      e.printStackTrace();
    }
  }
}
