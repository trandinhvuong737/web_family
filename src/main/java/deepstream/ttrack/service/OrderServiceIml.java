package deepstream.ttrack.service;

import deepstream.ttrack.common.enums.Status;
import deepstream.ttrack.common.utils.WebUtils;
import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.misscall.MissCallResponse;
import deepstream.ttrack.dto.order.OrderRequestDto;
import deepstream.ttrack.dto.order.OrderResponseDto;
import deepstream.ttrack.dto.overview.ChartOverviewDto;
import deepstream.ttrack.dto.overview.OverviewDto;
import deepstream.ttrack.entity.CallHistory;
import deepstream.ttrack.entity.Order;
import deepstream.ttrack.entity.Product;
import deepstream.ttrack.entity.User;
import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.Errors;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.model.BotTelegram;
import deepstream.ttrack.repository.CallDataWebhookRepository;
import deepstream.ttrack.repository.OrderRepository;
import deepstream.ttrack.repository.ProductRepository;
import deepstream.ttrack.repository.UserRepository;
import deepstream.ttrack.mapper.OrderMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceIml implements OrderService {

    public static final String ASIA_HO_CHI_MINH = "Asia/Ho_Chi_Minh";
    public static final int SUPER_ADMIN_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final long GROUP_CHAT_ID = -4182617308L;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CallDataWebhookService callDataWebhookService;
    private final CallDataWebhookRepository callDataWebhookRepository;
    private final BotTelegram botTelegram;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceIml.class);

    public OrderServiceIml(OrderRepository orderRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository, CallDataWebhookService callDataWebhookService, CallDataWebhookRepository callDataWebhookRepository, BotTelegram botTelegram) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.callDataWebhookService = callDataWebhookService;
        this.callDataWebhookRepository = callDataWebhookRepository;
        this.botTelegram = botTelegram;
    }

    @Override
    public void addNewOrder(OrderRequestDto orderRequest) {
        logger.info("addNewOrder");
        OrderResponseDto orderResponseDto = checkOrder(orderRequest.getPhoneNumber());
        if (ObjectUtils.isNotEmpty(orderResponseDto)) {
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
            order.setProduct(orderRequest.getProductName());
            order.setCreateAt(LocalDateTime.now(ZoneId.of(ASIA_HO_CHI_MINH)));
            order.setPhoneNumber(orderRequest.getPhoneNumber());
            order.setQuantity(orderRequest.getQuantity());
            order.setStatus(orderRequest.getStatus());
            order.setDiscountCode(orderRequest.getDiscountCode());
            orderRepository.save(order);
            LocalDateTime startDate = LocalDate.now().atStartOfDay();
            LocalDateTime endDate = LocalDate.now().atTime(23, 59, 59);
            Integer totalOrders = orderRepository.countOrderByDate(startDate, endDate, username);
            Integer totalProduct = orderRepository.sumProductByDate(startDate, endDate, username);
            botTelegram.sendTextAddOder(GROUP_CHAT_ID, order, totalOrders, totalProduct);
        }
    }

    @Override
    public void addNewOrderByDate(OrderRequestDto orderRequest, LocalDate date) {
        logger.info("addNewOrderByDate");
        OrderResponseDto orderResponseDto = checkOrder(orderRequest.getPhoneNumber());
        if (ObjectUtils.isNotEmpty(orderResponseDto)) {
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
            order.setProduct(orderRequest.getProductName());
            order.setCreateAt(date.atStartOfDay());
            order.setPhoneNumber(orderRequest.getPhoneNumber());
            order.setQuantity(orderRequest.getQuantity());
            order.setStatus(orderRequest.getStatus());
            order.setDiscountCode(orderRequest.getDiscountCode());
            LocalDateTime startDate = LocalDate.now().atStartOfDay();
            LocalDateTime endDate = LocalDate.now().atTime(23, 59, 59);
            Integer totalOrders = orderRepository.countOrderByDate(startDate, endDate, username);
            Integer totalProduct = orderRepository.sumProductByDate(startDate, endDate, username);
            botTelegram.sendTextAddOder(GROUP_CHAT_ID, order, totalOrders, totalProduct);
        }

    }

    @Override
    public MissCallResponse addNewOrderByMissCall() {
        logger.info("addNewOrderByMissCall");
        List<CallHistory> callHistories = callDataWebhookService.getAllMissCall();
        if (callHistories.isEmpty()) {
            throw new BadRequestException(
                    new SysError(Errors.NO_MISSED_CALLS, new ErrorParam(Errors.MISS_CALL)));
        }
        MissCallResponse missCallResponse = new MissCallResponse();
        for (CallHistory callHistory : callHistories) {
            if (checkOrderMissCall(callHistory.getCallerPhone())) {
                callHistory.setStatus(true);
                callDataWebhookRepository.save(callHistory);
                this.addNewOrderByMissCall();
            } else {
                String username = WebUtils.getUsername();
                User user = userRepository.findByUsername(username).orElseThrow(
                        () -> new BadRequestException(
                                new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
                );
                Order order = new Order();
                order.setPhoneNumber(callHistory.getCallerPhone());
                order.setStatus(Status.INITIALIZATION.getDisplayName());
                order.setCreateAt(LocalDateTime.now(ZoneId.of(ASIA_HO_CHI_MINH)));
                order.setUser(user);
                Order savedOrder = orderRepository.save(order);
                callHistory.setStatus(true);
                callDataWebhookRepository.save(callHistory);
                int totalMissCall = callDataWebhookRepository.getTotalMissCall();
                missCallResponse.setMissCallId(savedOrder.getOrderId());
                missCallResponse.setTotalMissCall(totalMissCall);
                botTelegram.sendTextAddOderMissCall(GROUP_CHAT_ID, order);
                break;
            }
        }
        if (missCallResponse.getMissCallId() == null) {
            throw new BadRequestException(
                    new SysError(Errors.NO_MISSED_CALLS, new ErrorParam(Errors.MISS_CALL)));
        }
        return missCallResponse;
    }

    @Override
    public void updateOrder(int orderId, OrderRequestDto orderRequest) {
        logger.info("updateOrder");
        String username = WebUtils.getUsername();
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_ORDER_NOT_FOUND, new ErrorParam(Errors.ORDER_ID)))
        );
        String status = orderRequest.getStatus().toLowerCase();
        if (!Status.getDisplayNames().contains(status)) {
            throw new BadRequestException(
                    new SysError(Errors.ERROR_STATUS_FALSE, new ErrorParam(Errors.STATUS)));
        }
        order.setAddress(orderRequest.getAddress());
        order.setCustomer(orderRequest.getCustomer());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setQuantity(orderRequest.getQuantity());
        order.setStatus(orderRequest.getStatus());
        order.setDiscountCode(orderRequest.getDiscountCode());
        order.setProduct(orderRequest.getProductName());
        LocalDateTime startDate = LocalDate.now().atStartOfDay();
        LocalDateTime endDate = LocalDate.now().atTime(23, 59, 59);
        Integer totalOrders = orderRepository.countOrderByDate(startDate, endDate, username);
        Integer totalProduct = orderRepository.sumProductByDate(startDate, endDate, username);
        botTelegram.sendTextUpdateOder(GROUP_CHAT_ID, order, totalOrders, totalProduct);
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(int id) {
        logger.info("deleteOrder");
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.ID)))
        );
        orderRepository.delete(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrder() {
        logger.info("getAllOrder");
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );
        List<Order> orders;
        int roleId = user.getRole().getRoleId();
        if (roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID) {
            orders = orderRepository.getAllOrder();
        } else {
            orders = orderRepository.getAllOrderByProduct(username);
        }

        List<OrderResponseDto> orderResponse = new ArrayList<>();
        orders.stream()
                .map(order -> {
                    OrderResponseDto responseDto = new OrderResponseDto();
                    responseDto.setOrderId(order.getOrderId());
                    responseDto.setProduct(order.getProduct());
                    responseDto.setStatus(order.getStatus());
                    responseDto.setAddress(order.getAddress());
                    responseDto.setCustomer(order.getCustomer());
                    responseDto.setPhoneNumber(order.getPhoneNumber());
                    responseDto.setDiscountCode(order.getDiscountCode());
                    responseDto.setQuantity(order.getQuantity());
                    responseDto.setCreateAt(order.getCreateAt());
                    responseDto.setUserName(order.getUser().getUsername());
                    return responseDto;
                })
                .forEach(orderResponse::add);
        return orderResponse;
    }

    @Override
    public void updateStatus(int orderId, String status) {
        logger.info("updateStatus");
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_ORDER_NOT_FOUND, new ErrorParam(Errors.ORDER_ID)))
        );
        if (Status.getDisplayNames().contains(status.toLowerCase())) {
            order.setStatus(status.toLowerCase());
        } else {
            throw new BadRequestException(
                    new SysError(Errors.ERROR_STATUS_FALSE, new ErrorParam(Errors.STATUS)));
        }
        orderRepository.save(order);
    }

    @Override
    public OverviewDto getOverview(DateRangeDto dateRange) {
        logger.info("getOverview");
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );
        LocalDateTime startDate = dateRange.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRange.getEndDate().atTime(23, 59, 59);

        OverviewDto overview = new OverviewDto();
        int totalProduct = 0;
        long totalAmount = 0;
        long totalTransportFee = 0L;

        List<Order> orders;
        int roleId = user.getRole().getRoleId();
        if (roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID) {
            orders = orderRepository.getOrderByDateRangeAndUsername(startDate, endDate);
        } else {
            orders = orderRepository.getOrderByDateRangeAndUsername(startDate, endDate, username);
        }

        for (Order order : orders) {
            Product product = productRepository.getProductByProductName(order.getProduct());
            totalProduct += (int) order.getQuantity();
            totalAmount += (long) (order.getQuantity() * product.getUnitPrice());
            totalTransportFee += (long) order.getQuantity() * product.getTransportFee();
        }
        int totalOrder = orders.size();
        overview.setTotalOrder(totalOrder);
        overview.setTotalProduct(totalProduct);
        overview.setTotalAmount(totalAmount);
        overview.setTotalTransportFee(totalTransportFee);

        return overview;
    }

    @Override
    public List<ChartOverviewDto> getChartOverview() {
        logger.info("getChartOverview");
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );

        List<ChartOverviewDto> chartOverviews = new ArrayList<>();
        LocalDateTime date = LocalDate.now(ZoneId.of(ASIA_HO_CHI_MINH)).atStartOfDay();

        for (int i = 6; i >= 0; i--) {

            ChartOverviewDto overviewDto = new ChartOverviewDto();
            LocalDateTime startDays = date.toLocalDate().minusDays(i).atStartOfDay();
            LocalDateTime endDays = date.toLocalDate().minusDays(i).atTime(23, 59, 59);


            int totalOrder;
            Integer totalProduct;
            List<Order> orders;
            long totalTransportFee = 0L;

            int roleId = user.getRole().getRoleId();
            if (roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID) {
                totalOrder = orderRepository.countOrder(startDays, endDays);
                totalProduct = orderRepository.sumProduct(startDays, endDays);
                orders = orderRepository.getOrderByDate(startDays, endDays);
            } else {
                totalOrder = orderRepository.countOrder(startDays, endDays, username);
                totalProduct = orderRepository.sumProduct(startDays, endDays, username);
                orders = orderRepository.getOrderByDate(startDays, endDays, username);
            }

            for (Order order : orders
            ) {
                Product product = productRepository.getProductByProductName(order.getProduct());
                totalTransportFee += (long) order.getQuantity() * product.getTransportFee();
            }

            if (totalOrder > 0) {
                overviewDto.setDate(startDays.toLocalDate());
                overviewDto.setTotalOrder(totalOrder);
                overviewDto.setTotalProduct(totalProduct);
                overviewDto.setTotalTransportFee(totalTransportFee);
                chartOverviews.add(overviewDto);
            }

        }

        return chartOverviews;
    }

    @Override
    public List<OrderResponseDto> getAllOrderByFilter(DateRangeDto dateRangeDto) {
        logger.info("getAllOrderByFilter");
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );

        LocalDateTime startDate = dateRangeDto.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRangeDto.getEndDate().atTime(23, 59, 59);

        if (ObjectUtils.isEmpty(startDate) || ObjectUtils.isEmpty(endDate)) {
            throw new BadRequestException(
                    new SysError(Errors.DATE_RANGE_NULL, new ErrorParam(Errors.DATE_RANGE)));
        }

        List<Order> orders;
        int roleId = user.getRole().getRoleId();
        if (roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID) {
            orders = orderRepository.getOrderByDateRange(startDate, endDate);
        } else {
            orders = orderRepository.getOrderByDateRange(startDate, endDate, username);
        }

        List<OrderResponseDto> orderResponse = new ArrayList<>();
        orders.stream()
                .map(OrderMapper.INSTANCE::orderToOrderResponseDto)
                .forEach(orderResponse::add);
        return orderResponse;
    }

    @Override
    public OrderResponseDto checkOrder(String phoneNumber) {
        logger.info("checkOrder");
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        List<Product> products;
        int roleId = user.getRole().getRoleId();
        if (roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID) {
            products = productRepository.findAll();
        } else {
            products = user.getProducts();
        }

        for (Product product : products
        ) {
            List<Order> orders = orderRepository.getOrderByPhoneNumber(phoneNumber, product.getProductName());
            if (!orders.isEmpty()) {
                for (Order order : orders) {
                    String status = order.getStatus().toLowerCase();
                    if (status.equals(Status.PENDING.getDisplayName()) || status.equals(Status.INITIALIZATION.getDisplayName())) {
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
        }

        return orderResponseDto;
    }

    @Override
    public OrderResponseDto getOrder(int id) {
        logger.info("getOrder");
        Order order = orderRepository.getOrderByOrderId(id);
        if (ObjectUtils.isEmpty(order)) {
            throw new BadRequestException(
                    new SysError(Errors.NOT_FOUND_ORDER, new ErrorParam(Errors.ORDER_ID)));
        }
        OrderResponseDto orderResponse = new OrderResponseDto();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setProduct(order.getProduct());
        orderResponse.setCustomer(order.getCustomer());
        orderResponse.setCreateAt(order.getCreateAt());
        orderResponse.setAddress(order.getAddress());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setQuantity(order.getQuantity());
        orderResponse.setDiscountCode(order.getDiscountCode());
        orderResponse.setPhoneNumber(order.getPhoneNumber());
        return orderResponse;
    }

    private boolean checkOrderMissCall(String phoneNumber) {

        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );

        List<Product> products;
        int roleId = user.getRole().getRoleId();
        if (roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID) {
            products = productRepository.findAll();
        } else {
            products = user.getProducts();
        }

        for (Product product : products
        ) {
            List<Order> orders = orderRepository.getOrderByPhoneNumber(phoneNumber, product.getProductName());
            if (!orders.isEmpty()) {
                for (Order order : orders) {
                    boolean statusPending = order.getStatus().toLowerCase().equals(Status.PENDING.getDisplayName());
                    boolean statusInitialization = order.getStatus().toLowerCase().equals(Status.INITIALIZATION.getDisplayName());
                    if (statusPending || statusInitialization) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}


