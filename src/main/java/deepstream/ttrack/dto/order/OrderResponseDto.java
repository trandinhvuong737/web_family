package deepstream.ttrack.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderResponseDto {

    private int orderId;

    private LocalDate createAt = LocalDate.now();

    private String customer;

    private String address;

    private String phoneNumber;

    private String product;

    private int quantity;

    private String status;
}
