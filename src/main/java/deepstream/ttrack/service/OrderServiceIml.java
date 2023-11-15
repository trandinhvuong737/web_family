package deepstream.ttrack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.common.enums.Status;
import deepstream.ttrack.common.utils.WebUtils;
import deepstream.ttrack.dto.DateRangeDto;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.order.OrderRequestDto;
import deepstream.ttrack.dto.order.OrderResponseDto;
import deepstream.ttrack.dto.overview.ChartOverviewDto;
import deepstream.ttrack.dto.overview.OverviewDto;
import deepstream.ttrack.entity.MissedCall;
import deepstream.ttrack.entity.Order;
import deepstream.ttrack.entity.Product;
import deepstream.ttrack.entity.User;
import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.Errors;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.model.Item;
import deepstream.ttrack.model.MissedCallModel;
import deepstream.ttrack.model.TokenModel;
import deepstream.ttrack.repository.MissedCallRepository;
import deepstream.ttrack.repository.OrderRepository;
import deepstream.ttrack.repository.ProductRepository;
import deepstream.ttrack.repository.UserRepository;
import deepstream.ttrack.mapper.OrderMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceIml implements OrderService {

    public static final String ASIA_HO_CHI_MINH = "Asia/Ho_Chi_Minh";
    public static final int SUPER_ADMIN_ID = 1;
    public static final int ADMIN_ID = 2;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MissedCallRepository missedCallRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceIml.class);

    public OrderServiceIml(OrderRepository orderRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           MissedCallRepository missedCallRepository,
                           ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.missedCallRepository = missedCallRepository;
        this.objectMapper = objectMapper;
        this.webClient = WebClient.create("");
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
        order.setProduct(orderRequest.getProductName());
        order.setCreateAt(LocalDateTime.now(ZoneId.of(ASIA_HO_CHI_MINH)));
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
        order.setProduct(orderRequest.getProductName());
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
        int roleId = user.getRole().getRoleId();
        if( roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID ){
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
                    responseDto.setUserName(username);
                    return responseDto;
                })
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
        LocalDateTime startDate = dateRange.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRange.getEndDate().atTime(23,59,59);
        OverviewDto overview = new OverviewDto();
        int totalProduct = 0;
        int totalOrder;
        long totalAmount = 0;
        long totalTransportFee = 0L;
        List<Order> orders;
        int roleId = user.getRole().getRoleId();
        if( roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID ){
            orders = orderRepository.getOrderByDateRangeAndUsername(startDate, endDate);
        }else {
            orders = orderRepository.getOrderByDateRangeAndUsername(startDate, endDate, username);
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
        LocalDateTime date = LocalDate.now(ZoneId.of(ASIA_HO_CHI_MINH)).atStartOfDay();

        for (int i = 6; i >= 0; i--) {

            ChartOverviewDto overviewDto = new ChartOverviewDto();
            LocalDateTime startDays = date.toLocalDate().minusDays(i).atStartOfDay();
            LocalDateTime endDays = date.toLocalDate().minusDays(i).atTime(23,59,59);


            int totalOrder;
            Integer totalProduct;
            List<Order> orders;
            long totalTransportFee = 0L;

            int roleId = user.getRole().getRoleId();
            if( roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID){
                totalOrder = orderRepository.countOrder(startDays, endDays);
                totalProduct = orderRepository.sumProduct(startDays, endDays);
                orders = orderRepository.getOrderByDate(startDays, endDays);
            }else {
                totalOrder = orderRepository.countOrder(startDays,endDays, username);
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
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );

        LocalDateTime startDate = dateRangeDto.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRangeDto.getEndDate().atTime(23,59,59);

        if (ObjectUtils.isEmpty(startDate) || ObjectUtils.isEmpty(endDate)) {
            throw new BadRequestException(
                    new SysError(Errors.DATE_RANGE_NULL, new ErrorParam(Errors.DATE_RANGE)));
        }

        List<Order> orders;
        int roleId = user.getRole().getRoleId();
        if( roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID ){
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
    public ResponseJson<OrderResponseDto> checkOrder(String phoneNumber) {
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        List<Product> products = user.getProducts();
        for (Product product:products
             ) {
            List<Order> orders = orderRepository.getOrderByPhoneNumber(phoneNumber, product.getProductName());
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
        }

        return new ResponseJson<>(orderResponseDto, HttpStatus.OK, Constant.SUCCESS);
    }

    @Override
    public OrderResponseDto getOrder(int id) {
        Order order = orderRepository.getOrderByOrderId(id);
        if(ObjectUtils.isEmpty(order)){
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

    //    @Scheduled(fixedRate = 1800000)
    public void testCall() {
        LocalDateTime endDate = LocalDateTime.now(ZoneId.of(ASIA_HO_CHI_MINH));
        LocalDateTime startDate = endDate.minusDays(30);
        Instant endInstant = endDate.atZone(ZoneId.of(ASIA_HO_CHI_MINH)).toInstant();
        Instant startInstant = startDate.atZone(ZoneId.of(ASIA_HO_CHI_MINH)).toInstant();
        TokenModel token = getTokenModel();
        MissedCallModel response = getMissedCallModel(startInstant, endInstant, token);
        List<Item> items = response.getPayload().getItems();
        List<MissedCall> missedCalls = new ArrayList<>();
        if(!items.isEmpty()){
            for (Item item:items
            ) {
                MissedCall missedCall = new MissedCall();
                missedCall.setCreatedDate(item.getCreatedDate());
                missedCall.setDestinationNumber(item.getDestinationNumber());
                missedCall.setSourceNumber(item.getSourceNumber());
                missedCalls.add(missedCall);
            }
            missedCallRepository.saveAll(missedCalls);
        }
    }

    private MissedCallModel getMissedCallModel(Instant startInstant, Instant endInstant, TokenModel token) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("https://public-v1-stg.vcontact.services/api/call_transaction/list")
                .queryParam("disposition", "cancelled")
                .queryParam("direction", "inbound")
                .queryParam("page", 1)
                .queryParam("size", 50)
                .queryParam("from_date", startInstant.toEpochMilli())
                .queryParam("to_date", endInstant.toEpochMilli());

        return webClient.get()
                .uri(builder.toUriString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getPayload().getAccessToken())
                .retrieve()
                .bodyToMono(Object.class)
                .map(jsonNode -> objectMapper.convertValue(jsonNode, MissedCallModel.class))
                .block();
    }

    private TokenModel getTokenModel() {
        UriComponentsBuilder tokenBuilder = UriComponentsBuilder
                .fromHttpUrl("https://public-v1-stg.vcontact.services/api/auth")
                .queryParam("apiKey", "D00018F49A543DA4ED33F5B409C207747C1FE6394FE71020F88FA914D1CF0866");

        return webClient.get()
                .uri(tokenBuilder.toUriString())
                .retrieve()
                .bodyToMono(Object.class)
                .map(jsonNode -> objectMapper.convertValue(jsonNode, TokenModel.class))
                .block();
    }

}


