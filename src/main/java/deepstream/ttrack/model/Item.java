package deepstream.ttrack.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @JsonProperty("source_number")
    private String sourceNumber;
    @JsonProperty("destination_number")
    private String destinationNumber;
    @JsonProperty("created_date")
    private Long createdDate;
}
