package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.overview.ChartOverviewDto;
import deepstream.ttrack.dto.overview.OverviewDto;
import deepstream.ttrack.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/overview")
@AllArgsConstructor
public class OverviewController {
    private final OrderService orderService;

    @PostMapping("/get")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<OverviewDto>> createDevice(@RequestBody DateRangeDto dateRange) {
        OverviewDto overviewDto = orderService.getOverview(dateRange);
        return ResponseEntity.ok().body(
                new ResponseJson<>(overviewDto, HttpStatus.OK, Constant.SUCCESS));

    }

    @GetMapping("/get-chart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<ChartOverviewDto>>> getChartOverview() {
        List<ChartOverviewDto> chartOverview = orderService.getChartOverview();
        return ResponseEntity.ok().body(
                new ResponseJson<>(chartOverview, HttpStatus.OK, Constant.SUCCESS));

    }

}
