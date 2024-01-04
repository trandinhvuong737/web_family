package deepstream.ttrack.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "call_history")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "call_id")
    private int callId;
    @Column(name = "hotline_number")
    private String hotlineNumber;
    @Column(name = "caller_phone")
    private String callerPhone;
    @Column(name = "callee_phone")
    private String calleePhone;
    @Column(name = "type_call")
    private String typeCall;
    @Column(name = "created_date")
    private Long createdDate;
    @Column(name = "status")
    private boolean status;
    @Column(name = "disposition")
    private String disposition;


}
