package deepstream.ttrack.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    private String productName;

    private Long unitPrice;

    private Integer weight;

    private Long transportFee;
}
