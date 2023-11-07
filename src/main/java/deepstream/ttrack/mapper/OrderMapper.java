package deepstream.ttrack.mapper;

import deepstream.ttrack.dto.order.OrderRequestDto;
import deepstream.ttrack.dto.order.OrderResponseDto;
import deepstream.ttrack.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    OrderResponseDto orderToOrderResponseDto(Order order);
    Order orderResponseDtoToOrder(OrderRequestDto orderRequest);

}
