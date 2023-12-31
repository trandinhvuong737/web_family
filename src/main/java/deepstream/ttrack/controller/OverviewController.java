package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.overview.ChartOverviewDto;
import deepstream.ttrack.dto.overview.OverviewDto;
import deepstream.ttrack.dto.user.UserOverviewDto;
import deepstream.ttrack.service.OrderService;
import deepstream.ttrack.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/overview")
@AllArgsConstructor
public class OverviewController {
    private final OrderService orderService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(OverviewController.class);


    @PostMapping("/get")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<OverviewDto>> createOrderOverview(@RequestBody DateRangeDto dateRange) {
        logger.info("createOrderOverview");
        OverviewDto overviewDto = orderService.getOverview(dateRange);
        return ResponseEntity.ok().body(
                new ResponseJson<>(overviewDto, HttpStatus.OK, Constant.ORDER_OVERVIEW_SUCCESS));

    }

    @GetMapping("/get-chart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<ChartOverviewDto>>> getChartOverview() {
        logger.info("getChartOverview");
        List<ChartOverviewDto> chartOverview = orderService.getChartOverview();
        return ResponseEntity.ok().body(
                new ResponseJson<>(chartOverview, HttpStatus.OK, Constant.CHART_SUCCESS));

    }

    @GetMapping("/get-user-overview")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<List<UserOverviewDto>>> getAllUserOverview() {
        logger.info("getAllUserOverview");
        List<UserOverviewDto> allUserOverview = userService.getAllUserOverview();
        return ResponseEntity.ok().body(
                new ResponseJson<>(allUserOverview, HttpStatus.OK, Constant.SUCCESS));

    }

}
