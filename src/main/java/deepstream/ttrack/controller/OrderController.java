package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.order.OrderRequestDto;
import deepstream.ttrack.dto.order.OrderResponseDto;
import deepstream.ttrack.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<Boolean>> createDevice(@RequestBody OrderRequestDto orderRequestDto) {
        orderService.addNewOrder(orderRequestDto);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.ADD_ORDER_SUCCESS));

    }

    @PostMapping("/update/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<Boolean>> createDevice(
            @PathVariable int orderId,
            @RequestBody OrderRequestDto orderRequestDto) {
        orderService.updateOrder(orderId, orderRequestDto);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.UPDATE_ORDER_SUCCESS));

    }

    @DeleteMapping("/delete/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> createDevice(@PathVariable int orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.DELETE_ORDER_SUCCESS));

    }

    @GetMapping("/get-all-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<OrderResponseDto>>> createDevice() {
        List<OrderResponseDto> orders = orderService.getAllOrder();
        return ResponseEntity.ok().body(
                new ResponseJson<>(orders, HttpStatus.OK, Constant.SUCCESS));

    }

    @GetMapping("/get-order/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<OrderResponseDto>> getOrder(@PathVariable int id) {
        OrderResponseDto orders = orderService.getOrder(id);
        return ResponseEntity.ok().body(
                new ResponseJson<>(orders, HttpStatus.OK, Constant.SUCCESS));

    }

    @PostMapping("/get-order-by-date-range")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<OrderResponseDto>>> filterOrderByDateRange( @RequestBody DateRangeDto dateRangeDto) {
        List<OrderResponseDto> orders = orderService.getAllOrderByFilter(dateRangeDto);
        return ResponseEntity.ok().body(
                new ResponseJson<>(orders, HttpStatus.OK, Constant.SUCCESS));

    }

    @PostMapping("/update-status/{orderId}/{status}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<Boolean>> createDevice(
            @PathVariable int orderId,
            @PathVariable String status) {
        orderService.updateStatus(orderId, status);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.UPDATE_STATUS_ORDER_SUCCESS));

    }

    @GetMapping("/check-order/{phone}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<OrderResponseDto>> checkOrder(
            @PathVariable String phone) {
        ResponseJson<OrderResponseDto> orderResponse = orderService.checkOrder(phone);
        return ResponseEntity.ok().body(orderResponse);

    }
}
