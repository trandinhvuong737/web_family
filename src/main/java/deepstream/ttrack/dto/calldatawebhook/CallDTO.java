package deepstream.ttrack.dto.calldatawebhook;

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
    private Hotline data;
    private long time;
    private String eventData;
}
