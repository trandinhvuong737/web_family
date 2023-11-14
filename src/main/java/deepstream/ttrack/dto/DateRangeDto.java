package deepstream.ttrack.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeDto {
    private LocalDate startDate;
    private LocalDate endDate;
}
