package deepstream.ttrack.dto.calldatawebhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CallDTO {
    @JsonProperty("answer_sec")
    private int answerSec;
    @JsonProperty("bill_sec")
    private int billSec;
    @JsonProperty("call_out_price")
    private double callOutPrice;
    @JsonProperty("create_by")
    private CreatedByCall createBy;
    @JsonProperty("created_date")
    private long createdDate;
    @JsonProperty("customer")
    private CustomerCall customer;
    @JsonProperty("destination_number")
    private String destinationNumber;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("domain_fusion")
    private String domainFusion;
    @JsonProperty("duration")
    private int duration;
    @JsonProperty("endby_name")
    private String endByName;
    @JsonProperty("from_number")
    private String fromNumber;
    @JsonProperty("hotline")
    private String hotline;
    @JsonProperty("is_auto_call")
    private boolean isAutoCall;
    @JsonProperty("is_have_forward_out")
    private boolean isHaveForwardOut;
    @JsonProperty("ivr")
    private String ivr;
    @JsonProperty("last_updated_date")
    private long lastUpdatedDate;
    @JsonProperty("note")
    private String note;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("provider")
    private String provider;
    @JsonProperty("record_seconds")
    private int recordSeconds;
    @JsonProperty("recording_file_url")
    private String recordingFileUrl;
    @JsonProperty("send_num_retry")
    private int sendNumRetry;
    @JsonProperty("sip_number")
    private String sipNumber;
    @JsonProperty("sip_user")
    private String sipUser;
    @JsonProperty("source_number")
    private String sourceNumber;
    @JsonProperty("state")
    private String state;
    @JsonProperty("tag")
    private List<String> tag;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("time_end_call")
    private long timeEndCall;
    @JsonProperty("time_start_call")
    private long timeStartCall;
    @JsonProperty("time_start_to_answer")
    private long timeStartToAnswer;
    @JsonProperty("to_number")
    private String toNumber;
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("user")
    private List<UserCall> user;
    @JsonProperty("transfer_histories")
    private List<String> transferHistories;

}
