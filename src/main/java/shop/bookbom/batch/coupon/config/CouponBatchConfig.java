package shop.bookbom.batch.coupon.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CouponBatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final Step sendBirthCouponStep;

    @Bean
    public Job birthCouponJob(){
        return jobBuilderFactory.get("birthCouponJob")
                .start(sendBirthCouponStep)
                .build();
    }
}
