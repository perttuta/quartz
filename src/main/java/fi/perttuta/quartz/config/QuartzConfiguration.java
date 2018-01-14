package fi.perttuta.quartz.config;

import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Configure quartz.
 */
@Configuration
public class QuartzConfiguration {

  @Bean
  public SchedulerFactoryBean schedulerFactory(PlatformTransactionManager transactionManager,
                                               ApplicationContext applicationContext,
                                               DataSource dataSource)
  throws IOException {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    factory.setApplicationContextSchedulerContextKey("applicationContext");
    factory.setJobFactory(jobFactory(applicationContext));
    factory.setTransactionManager(transactionManager);
    factory.setQuartzProperties(quartzProperties());
    factory.setDataSource(dataSource);
    return factory;
  }

  @Bean
  public JobFactory jobFactory(ApplicationContext applicationContext) {
    AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

  @Bean
  public Properties quartzProperties() throws IOException {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
    propertiesFactoryBean.afterPropertiesSet();
    return propertiesFactoryBean.getObject();
  }
}