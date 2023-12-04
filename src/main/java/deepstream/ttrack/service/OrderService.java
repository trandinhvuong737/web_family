package deepstream.ttrack.service;

import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.order.OrderRequestDto;
import deepstream.ttrack.dto.order.OrderResponseDto;
import deepstream.ttrack.dto.overview.ChartOverviewDto;
import deepstream.ttrack.dto.overview.OverviewDto;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    public void addNewOrder(OrderRequestDto orderRequest);

    void addNewOrderByDate(OrderRequestDto orderRequest, LocalDate date);

    public void updateOrder(int orderId, OrderRequestDto orderRequest);

    public void deleteOrder(int id);

    public List<OrderResponseDto> getAllOrder();

    void updateStatus(int orderId, String status);

    OverviewDto getOverview(DateRangeDto dateRange);

    List<ChartOverviewDto> getChartOverview();

    List<OrderResponseDto> getAllOrderByFilter(DateRangeDto dateRangeDto);

    OrderResponseDto checkOrder(String phoneNumber);

    OrderResponseDto getOrder(int id);
}
