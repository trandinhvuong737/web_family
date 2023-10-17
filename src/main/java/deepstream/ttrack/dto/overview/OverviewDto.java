package deepstream.ttrack.dto.overview;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OverviewDto {
    private Integer totalOrder;
    private Integer totalProduct;
    private Long totalAmount;
}
