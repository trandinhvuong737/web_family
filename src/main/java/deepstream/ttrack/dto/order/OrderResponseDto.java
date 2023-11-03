package deepstream.ttrack.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderResponseDto {

    private Integer orderId;

    private LocalDate createAt = LocalDate.now();

    private String customer;

    private String address;

    private String phoneNumber;

    private String product;

    private Double quantity;

    private String status;

    private String discountCode;
}
