package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.PageRequest;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.order.OrderRequestDto;
import deepstream.ttrack.dto.order.OrderResponseDto;
import deepstream.ttrack.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
                new ResponseJson<>(true, HttpStatus.OK, Constant.SUCCESS));

    }

    @PostMapping("/update/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<Boolean>> createDevice(
            @PathVariable int orderId,
            @RequestBody OrderRequestDto orderRequestDto) {
        orderService.updateOrder(orderId, orderRequestDto);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.SUCCESS));

    }

    @DeleteMapping("/delete/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> createDevice(@PathVariable int orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.SUCCESS));

    }

    @PostMapping("/get-all-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<Page<OrderResponseDto>>> createDevice(@RequestBody PageRequest pageRequest) {
        Page<OrderResponseDto> orders = orderService.getAllOrder(pageRequest);
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
                new ResponseJson<>(true, HttpStatus.OK, Constant.SUCCESS));

    }
}
