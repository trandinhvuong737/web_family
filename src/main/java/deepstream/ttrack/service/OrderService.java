package deepstream.ttrack.service;

import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.PageRequest;
import deepstream.ttrack.dto.order.OrderRequestDto;
import deepstream.ttrack.dto.order.OrderResponseDto;
import deepstream.ttrack.dto.overview.ChartOverviewDto;
import deepstream.ttrack.dto.overview.OverviewDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    public boolean addNewOrder(OrderRequestDto orderRequest);

    public boolean updateOrder(int orderId, OrderRequestDto orderRequest);

    public boolean deleteOrder(int id);

    public Page<OrderResponseDto> getAllOrder(PageRequest pageRequest);

    void updateStatus(int orderId, String status);

    OverviewDto getOverview(DateRangeDto dateRange);

    List<ChartOverviewDto> getChartOverview();
}
