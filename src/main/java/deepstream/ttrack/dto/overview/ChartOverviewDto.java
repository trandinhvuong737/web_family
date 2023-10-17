package deepstream.ttrack.dto.overview;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ChartOverviewDto {
    private LocalDate date;
    private Integer totalOrder;
}
