package deepstream.ttrack.mapper;

import deepstream.ttrack.dto.product.ProductResponseDto;
import deepstream.ttrack.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    ProductResponseDto productToProductResponseDto(Product product);
}
