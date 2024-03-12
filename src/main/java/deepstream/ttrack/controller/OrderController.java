package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.misscall.MissCallResponse;
import deepstream.ttrack.dto.order.OrderRequestDto;
import deepstream.ttrack.dto.order.OrderResponseDto;
import deepstream.ttrack.service.OrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<Boolean>> createDevice(@RequestBody OrderRequestDto orderRequestDto) throws TelegramApiException {
        logger.info("exportToExcel");
        orderService.addNewOrder(orderRequestDto);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.ADD_ORDER_SUCCESS));

    }

    @PostMapping("/new-order-by-date/{date}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<Boolean>> addNewOrderByDate(
            @RequestBody OrderRequestDto orderRequestDto,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws TelegramApiException {
        logger.info("exportToExcel");
        orderService.addNewOrderByDate(orderRequestDto, date);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.ADD_ORDER_SUCCESS));

    }

    @PostMapping("/new-order-miss-call")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<MissCallResponse>> addNewOrderByMissCall() throws TelegramApiException {
        logger.info("addNewOrderByMissCall");
        MissCallResponse missCallResponse = orderService.addNewOrderByMissCall();
        return ResponseEntity.ok().body(
                new ResponseJson<>(missCallResponse, HttpStatus.OK, Constant.SUCCESS));

    }

    @PostMapping("/update/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<Boolean>> createDevice(
            @PathVariable int orderId,
            @RequestBody OrderRequestDto orderRequestDto) {
        logger.info("createDeviceOrderRequestDto");
        orderService.updateOrder(orderId, orderRequestDto);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.UPDATE_ORDER_SUCCESS));

    }

    @DeleteMapping("/delete/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> createDevice(@PathVariable int orderId) {
        logger.info("createDeviceOrderId");
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.DELETE_ORDER_SUCCESS));

    }

    @GetMapping("/get-all-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<OrderResponseDto>>> createDevice() {
        logger.info("createDevice");
        List<OrderResponseDto> orders = orderService.getAllOrder();
        return ResponseEntity.ok().body(
                new ResponseJson<>(orders, HttpStatus.OK, Constant.SUCCESS));

    }

    @GetMapping("/get-order/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<OrderResponseDto>> getOrder(@PathVariable int id) {
        logger.info("getOrder");
        OrderResponseDto orders = orderService.getOrder(id);
        return ResponseEntity.ok().body(
                new ResponseJson<>(orders, HttpStatus.OK, Constant.SUCCESS));

    }

    @PostMapping("/get-order-by-date-range")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<OrderResponseDto>>> filterOrderByDateRange( @RequestBody DateRangeDto dateRangeDto) {
        logger.info("filterOrderByDateRange");
        List<OrderResponseDto> orders = orderService.getAllOrderByFilter(dateRangeDto);
        return ResponseEntity.ok().body(
                new ResponseJson<>(orders, HttpStatus.OK, Constant.SUCCESS));

    }

    @PostMapping("/update-status/{orderId}/{status}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<Boolean>> createDevice(
            @PathVariable int orderId,
            @PathVariable String status) {
        logger.info("createDevice");
        orderService.updateStatus(orderId, status);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.UPDATE_STATUS_ORDER_SUCCESS));

    }

    @GetMapping("/check-order/{phone}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<OrderResponseDto>> checkOrder(
            @PathVariable String phone) {
        logger.info("checkOrder");
        OrderResponseDto orderResponse = orderService.checkOrder(phone);
        return ResponseEntity.ok()
                .body(new ResponseJson<>(orderResponse, HttpStatus.OK, Constant.SUCCESS));

    }
}
