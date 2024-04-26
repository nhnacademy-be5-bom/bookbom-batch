package shop.bookbom.batch.coupon.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BirthMemberDto {
    private Long memberId;
    private Date birthDate;
}
