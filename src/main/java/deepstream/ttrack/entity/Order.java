package deepstream.ttrack.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name = "customer")
    private String customer;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "product")
    private String product;

    @Column(name = "quantity")
    private double quantity;

    @Column(name = "status")
    private String status;

    @Column(name = "discount_code")
    private String discountCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
