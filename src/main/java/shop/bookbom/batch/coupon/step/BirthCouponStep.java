package shop.bookbom.batch.coupon.step;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.bookbom.batch.coupon.dto.BirthMemberDto;
import shop.bookbom.batch.coupon.mapper.BirthMemberDtoMapper;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BirthCouponStep {
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public JdbcPagingItemReader<BirthMemberDto> birthMemberItemReader() throws Exception {
        int chunkSize = 10;
        return new JdbcPagingItemReaderBuilder<BirthMemberDto>()
                .pageSize(chunkSize)
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BirthMemberDtoMapper())
                .queryProvider(queryProvider())
                .name("birthDayDtoItemReader")
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BirthMemberDto> birthCouponItemWriter() {
        return new JdbcBatchItemWriterBuilder<BirthMemberDto>()
                .dataSource(dataSource)
                .sql("insert into member_coupon (status, issue_date, expire_date, coupon_id, user_id) values ('NEW', DATE(now()), LAST_DAY(CURDATE()), 1, :memberId)") //:memberId자리에 쿠폰넘버 들어가야 함
                .beanMapped()
                .build();
    }

    private PagingQueryProvider queryProvider() throws Exception {
        LocalDate nowDate = LocalDate.now();
        int month = nowDate.getMonthValue();

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT user_id, birth_date");
        queryProvider.setFromClause("FROM member");
        queryProvider.setWhereClause("WHERE MONTH(birth_date) = " + month + " and status = 'ACTIVE'");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("user_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider;
    }

    @Bean
    public Step sendBirthCouponStep() throws Exception {
        return stepBuilderFactory.get("sendBirthCouponStep")
                .<BirthMemberDto, BirthMemberDto>chunk(10)
                .reader(birthMemberItemReader())
                .writer(birthCouponItemWriter())
                .build();
    }
}
