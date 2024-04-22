package shop.bookbom.batch.coupon.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.bookbom.batch.coupon.config.CouponBatchConfig;

@Component
@RequiredArgsConstructor
public class CouponScheduler {
    private final JobLauncher jobLauncher;
    private final CouponBatchConfig couponBatchConfig;

    //매월 00시 1분에 실행
    //@Scheduled(cron = "0 1 0 1 * *")
    @Scheduled(cron = "0 * 16 * * *")
    public void runBirthCouponJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("CouponJobTime", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(couponBatchConfig.birthCouponJob(), jobParameters);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
