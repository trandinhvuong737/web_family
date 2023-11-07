package deepstream.ttrack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class PageRequest {
    private int page;
    private int size;
    private String sortField;
    private Sort.Direction sortBy;
    private DateRangeDto dateRange;
}
