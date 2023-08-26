package deepstream.ttrack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeDto {
    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();
}
