package deepstream.ttrack.service;

import deepstream.ttrack.dto.product.ProductRequestDto;
import deepstream.ttrack.dto.product.ProductResponseDto;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> getAllProduct();

    ProductResponseDto getProductById(int productId);

    void updateProductById(int productId, ProductRequestDto productRequestDto);

    void deleteProductById(int productId);

    void addNewProduct(ProductRequestDto productRequestDto);
}
