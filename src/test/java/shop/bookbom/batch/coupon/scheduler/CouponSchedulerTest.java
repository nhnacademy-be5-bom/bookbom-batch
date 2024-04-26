package shop.bookbom.batch.coupon.scheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import shop.bookbom.batch.coupon.config.CouponBatchConfig;

@ExtendWith(MockitoExtension.class)
public class CouponSchedulerTest {
    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private CouponBatchConfig couponBatchConfig;

    @Mock
    private Job birthCouponJob;

    @InjectMocks
    private CouponScheduler couponScheduler;

    @Test
    @DisplayName("scheduler 실행 테스트")
    void testBirthCouponJob() throws Exception {
        Job birthCouponJob = mock(Job.class);

        when(couponBatchConfig.birthCouponJob()).thenReturn(birthCouponJob);
        when(jobLauncher.run(eq(birthCouponJob), any(JobParameters.class)))
                .thenReturn(new JobExecution(1L));

        couponScheduler.runBirthCouponJob();

        verify(jobLauncher, times(1)).run(eq(birthCouponJob), any(JobParameters.class));
    }
}
