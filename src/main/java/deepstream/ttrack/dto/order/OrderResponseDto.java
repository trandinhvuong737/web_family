package deepstream.ttrack.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponseDto {

    private Integer orderId;

    private LocalDateTime createAt = LocalDateTime.now();

    private String customer;

    private String address;

    private String phoneNumber;

    private String product;

    private Double quantity;

    private String status;

    private String discountCode;

    private String userName;
}
