package deepstream.ttrack.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class UserOverviewDto {
    private int id;
    private String username;
    private int totalOrder;
    private int totalOrderPending;
    private int totalOrderCompleted;
    private int totalOrderCanceled;
}
