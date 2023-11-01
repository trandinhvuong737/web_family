package deepstream.ttrack.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "missed_call")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissedCall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "call_id")
    private int callId;
    @Column(name = "source_number")
    private String sourceNumber;
    @Column(name = "destination_number")
    private String destinationNumber;
    @Column(name = "created_date")
    private Long createdDate;

}
