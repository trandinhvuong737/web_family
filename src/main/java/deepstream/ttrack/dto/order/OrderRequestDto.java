package deepstream.ttrack.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {

    private String customer;

    private String address;

    private String phoneNumber;

    private String product;

    private int quantity;

    private String status = "pending";
}
