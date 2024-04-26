package shop.bookbom.batch.coupon.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import shop.bookbom.batch.coupon.dto.BirthMemberDto;

@Slf4j
public class BirthMemberDtoMapper implements RowMapper<BirthMemberDto> {
    @Override
    public BirthMemberDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.info(String.valueOf(rs.getLong("user_id")));
        return BirthMemberDto.builder()
                .memberId(rs.getLong("user_id"))
                .birthDate(rs.getDate("birth_date"))
                .build();
    }
}
