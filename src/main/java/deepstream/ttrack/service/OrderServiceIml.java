package deepstream.ttrack.service;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.common.enums.Status;
import deepstream.ttrack.common.utils.WebUtils;
import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.ResponseJson;
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
import deepstream.ttrack.mapper.OrderMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceIml implements OrderService {

    public static final String ASIA_HO_CHI_MINH = "Asia/Ho_Chi_Minh";
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderServiceIml(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addNewOrder(OrderRequestDto orderRequest) {
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );

        if (!Status.getDisplayNames().contains(orderRequest.getStatus())) {
            throw new BadRequestException(
                    new SysError(Errors.ERROR_STATUS_FALSE, new ErrorParam(Errors.STATUS)));
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(orderRequest.getAddress());
        order.setCustomer(orderRequest.getCustomer());
        order.setProduct(user.getProduct().getProductName());
        order.setCreateAt(LocalDate.now(ZoneId.of(ASIA_HO_CHI_MINH)));
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setQuantity(orderRequest.getQuantity());
        order.setStatus(orderRequest.getStatus());
        order.setDiscountCode(orderRequest.getDiscountCode());
        orderRepository.save(order);
    }

    @Override
    public void updateOrder(int orderId, OrderRequestDto orderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_ORDER_NOT_FOUND, new ErrorParam(Errors.ORDER_ID)))
        );
        order.setAddress(orderRequest.getAddress());
        order.setCustomer(orderRequest.getCustomer());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setQuantity(orderRequest.getQuantity());
        order.setStatus(orderRequest.getStatus());
        order.setDiscountCode(orderRequest.getDiscountCode());
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(int id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.ID)))
        );
        orderRepository.delete(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrder() {

        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );
        List<Order> orders;
        if (user.getProduct().getProductName().equals("all")) {
            orders = orderRepository.getAllOrder();
        } else {
            orders = orderRepository.getAllOrderByProduct(user.getProduct().getProductName());
        }

        List<OrderResponseDto> orderResponse = new ArrayList<>();
        orders.stream()
                .map(OrderMapper.INSTANCE::orderToOrderResponseDto)
                .forEach(orderResponse::add);
        return orderResponse;
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
            order.setStatus(status.toLowerCase());
        } else {
            throw new BadRequestException(
                    new SysError(Errors.ERROR_STATUS_FALSE, new ErrorParam(Errors.STATUS)));
        }
        orderRepository.save(order);
    }

    @Override
    public OverviewDto getOverview(DateRangeDto dateRange) {

        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );
        LocalDate startDate = dateRange.getStartDate();
        LocalDate endDate = dateRange.getEndDate();
        String productName = user.getProduct().getProductName();
        OverviewDto overview = new OverviewDto();
        int totalProduct = 0;
        int totalOrder;
        long totalAmount = 0;
        long totalTransportFee = 0L;
        List<Order> orders;

        if (user.getProduct().getProductName().equals("all")) {
            orders = orderRepository.getOrderByDateRange(dateRange.getStartDate(), dateRange.getEndDate());
        } else {
            orders = orderRepository.getOrderByDateRangeAndProduct(startDate, endDate, productName);
        }
        for (Order order : orders) {
            Product product = productRepository.getProductByProductName(order.getProduct());
            totalProduct += (int) order.getQuantity();
            totalAmount += (long) (order.getQuantity() * product.getUnitPrice());
            totalTransportFee += (long) order.getQuantity() * product.getTransportFee();
        }
        totalOrder = orders.size();
        overview.setTotalOrder(totalOrder);
        overview.setTotalProduct(totalProduct);
        overview.setTotalAmount(totalAmount);
        overview.setTotalTransportFee(totalTransportFee);

        return overview;
    }

    @Override
    public List<ChartOverviewDto> getChartOverview() {

        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );

        List<ChartOverviewDto> chartOverviews = new ArrayList<>();
        LocalDate date = LocalDate.now(ZoneId.of(ASIA_HO_CHI_MINH));
        if (user.getProduct().getProductName().equals("all")) {
            for (int i = 6; i >= 0; i--) {
                ChartOverviewDto overviewDto = new ChartOverviewDto();
                LocalDate minusDays = date.minusDays(i);
                int totalOrder = orderRepository.countOrder(minusDays);
                Integer totalProduct = orderRepository.sumProduct(minusDays);
                long totalTransportFee = 0L;
                List<Order> orders = orderRepository.getOrderByDate(minusDays);
                setChart(user, chartOverviews, overviewDto, minusDays, totalOrder, totalProduct, totalTransportFee, orders);
            }
        } else {
            for (int i = 6; i >= 0; i--) {
                ChartOverviewDto overviewDto = new ChartOverviewDto();
                LocalDate minusDays = date.minusDays(i);
                int totalOrder = orderRepository.countOrder(minusDays, user.getProduct().getProductName());
                Integer totalProduct = orderRepository.sumProduct(minusDays, user.getProduct().getProductName());
                long totalTransportFee = 0L;
                List<Order> orders = orderRepository.getOrderByDate(minusDays, user.getProduct().getProductName());
                setChart(user, chartOverviews, overviewDto, minusDays, totalOrder, totalProduct, totalTransportFee, orders);
            }
        }
        return chartOverviews;
    }

    @Override
    public List<OrderResponseDto> getAllOrderByFilter(DateRangeDto dateRangeDto) {

        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );

        ZoneId zoneId = ZoneId.of(ASIA_HO_CHI_MINH);
        // Create a ZonedDateTime with the LocalDate and time zone
        LocalDate startDate = dateRangeDto.getStartDate().atStartOfDay(zoneId).toLocalDate();
        LocalDate endDate = dateRangeDto.getEndDate().atStartOfDay(zoneId).toLocalDate();

        if (ObjectUtils.isEmpty(startDate) || ObjectUtils.isEmpty(endDate)) {
            throw new BadRequestException(
                    new SysError(Errors.DATE_RANGE_NULL, new ErrorParam(Errors.DATE_RANGE)));
        }
        List<Order> orders;
        if (user.getProduct().getProductName().equals("all")) {
            orders = orderRepository.getOrderByDateRange(startDate, endDate);
        }else {
            orders = orderRepository.getOrderByDateRange(startDate, endDate, user.getProduct().getProductName());
        }

        List<OrderResponseDto> orderResponse = new ArrayList<>();
        orders.stream()
                .map(OrderMapper.INSTANCE::orderToOrderResponseDto)
                .forEach(orderResponse::add);
        return orderResponse;
    }

    @Override
    public ResponseJson<OrderResponseDto> checkOrder(String phoneNumber) {
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        List<Order> orders = orderRepository.getOrderByPhoneNumber(phoneNumber,user.getProduct().getProductName());
        if (!orders.isEmpty()) {
            for (Order order : orders) {
                if (order.getStatus().toLowerCase().equals(Status.PENDING.getDisplayName())) {
                    throw new BadRequestException(
                            new SysError("Đã tồn tại đơn hàng với tên: "
                                    .concat(order.getCustomer())
                                    .concat(" và số điện thoại: ")
                                    .concat(order.getPhoneNumber()), new ErrorParam(Errors.PHONE_NUMBER)));
                } else {
                    orderResponseDto = OrderMapper.INSTANCE.orderToOrderResponseDto(order);
                }
            }
        }
        return new ResponseJson<>(orderResponseDto, HttpStatus.OK, Constant.SUCCESS);
    }

    private void setChart(User user, List<ChartOverviewDto> chartOverviews, ChartOverviewDto overviewDto, LocalDate minusDays, int totalOrder, Integer totalProduct, long totalTransportFee, List<Order> orders) {
        for (Order order : orders
        ) {
            Product product = productRepository.getProductByProductName(order.getProduct());
            totalTransportFee += (long) order.getQuantity() * product.getTransportFee();
        }

        if (totalOrder > 0) {
            overviewDto.setDate(minusDays);
            overviewDto.setTotalOrder(totalOrder);
            overviewDto.setTotalProduct(totalProduct);
            overviewDto.setTotalTransportFee(totalTransportFee);
            chartOverviews.add(overviewDto);
        }
    }
}


