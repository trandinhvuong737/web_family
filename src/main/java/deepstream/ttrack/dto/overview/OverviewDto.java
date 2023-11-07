package deepstream.ttrack.dto.overview;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverviewDto {
    private Integer totalOrder;
    private Integer totalProduct;
    private Long totalAmount;
    private Long totalTransportFee;

}
