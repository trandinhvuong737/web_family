package deepstream.ttrack.service;


import deepstream.ttrack.common.enums.Status;
import deepstream.ttrack.common.utils.WebUtils;
import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.order.OrderRequestDto;
import deepstream.ttrack.dto.order.OrderResponseDto;
import deepstream.ttrack.dto.overview.ChartOverviewDto;
import deepstream.ttrack.dto.overview.OverviewDto;
import deepstream.ttrack.entity.Order;
import deepstream.ttrack.entity.Product;
import deepstream.ttrack.entity.User;
import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.Errors;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.repository.OrderRepository;
import deepstream.ttrack.repository.ProductRepository;
import deepstream.ttrack.repository.UserRepository;
import org.springframework.data.domain.*;
import deepstream.ttrack.mapper.OrderMapper;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceIml implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderServiceIml(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public boolean addNewOrder(OrderRequestDto orderRequest) {
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );
        Order order = new Order();
        order.setUser(user);
        order.setAddress(orderRequest.getAddress());
        order.setCustomer(orderRequest.getCustomer());
        order.setProduct(orderRequest.getProduct());
        order.setCreateAt(LocalDate.now());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setQuantity(orderRequest.getQuantity());
        order.setStatus(orderRequest.getStatus());
        orderRepository.save(order);
        return false;
    }

    @Override
    public boolean updateOrder(int orderId, OrderRequestDto orderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_ORDER_NOT_FOUND, new ErrorParam(Errors.ORDER_ID)))
        );
        order.setAddress(orderRequest.getAddress());
        order.setCustomer(orderRequest.getCustomer());
        order.setProduct(orderRequest.getProduct());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setQuantity(orderRequest.getQuantity());
        orderRepository.save(order);
        return true;
    }

    @Override
    public boolean deleteOrder(int id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.ID)))
        );
        orderRepository.delete(order);
        return true;
    }

    @Override
    public Page<OrderResponseDto> getAllOrder(deepstream.ttrack.dto.PageRequest pageRequest) {

        Pageable pageable = PageRequest.of(
                pageRequest.getPage() - 1,
                pageRequest.getSize(),
                Sort.by(pageRequest.getSortBy(), pageRequest.getSortField()));

        List<Order> orders = orderRepository.getAllOrder(
                pageable,
                pageRequest.getDateRange().getStartDate(),
                pageRequest.getDateRange().getEndDate());
        int size = orderRepository.findAll().size();

        List<OrderResponseDto> orderResponse = new ArrayList<>();
        orders.stream()
                .map(OrderMapper.INSTANCE::orderToOrderResponseDto)
                .forEach(orderResponse::add);
        return new PageImpl<>(orderResponse, pageable, size);
    }

    @Override
    public void updateStatus(int orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_ORDER_NOT_FOUND, new ErrorParam(Errors.ORDER_ID)))
        );
        if (status.equals(Status.COMPLETED.getDisplayName()) ||
                status.equals(Status.PENDING.getDisplayName()) ||
                status.equals(Status.CANCELED.getDisplayName())) {
            order.setStatus(status);
        } else {
            throw new BadRequestException(
                    new SysError(Errors.ERROR_STATUS_FALSE, new ErrorParam(Errors.STATUS)));
        }
        orderRepository.save(order);
    }

    @Override
    public OverviewDto getOverview(DateRangeDto dateRange) {
        OverviewDto overview = new OverviewDto();
        int totalProduct = 0, totalOrder, totalAmount = 0;
        List<Order> orders = orderRepository.getAllOrder(dateRange.getStartDate(),dateRange.getEndDate());
        for (Order order:orders
             ) {
            Product product = productRepository.getProductByProductName(order.getProduct());
            totalProduct += order.getQuantity();
            totalAmount += order.getQuantity() * product.getUnitPrice();
        }
        totalOrder = orders.size();
        overview.setTotalOrder(totalOrder);
        overview.setTotalProduct(totalProduct);
        overview.setTotalAmount(totalAmount);
        return null;
    }

    @Override
    public List<ChartOverviewDto> getChartOverview() {
        List<ChartOverviewDto> chartOverviews = new ArrayList<>();
        LocalDate date = LocalDate.now();
        for(int i = 0; i<=7; i++){
            ChartOverviewDto overviewDto = new ChartOverviewDto();
            LocalDate minusDays = date.minusDays(i);
            int totalOrder = orderRepository.countOrder(minusDays);
            if(totalOrder > 0){
                overviewDto.setDate(minusDays);
                overviewDto.setTotalOrder(totalOrder);
                chartOverviews.add(overviewDto);
            }
        }
        return chartOverviews;
    }
}
