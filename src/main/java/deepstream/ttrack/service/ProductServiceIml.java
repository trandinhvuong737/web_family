package deepstream.ttrack.service;

import deepstream.ttrack.dto.product.ProductRequestDto;
import deepstream.ttrack.dto.product.ProductResponseDto;
import deepstream.ttrack.entity.Product;
import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.Errors;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.mapper.ProductMapper;
import deepstream.ttrack.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceIml implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceIml(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponseDto> getAllProduct() {
        List<ProductResponseDto> responseDtos = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products
        ) {
            ProductResponseDto productResponseDto = ProductMapper.INSTANCE.productToProductResponseDto(product);
            responseDtos.add(productResponseDto);
        }

        return responseDtos;
    }

    @Override
    public ProductResponseDto getProductById(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.NOT_FOUND, new ErrorParam(Errors.PRODUCT_ID)))
        );

        return ProductMapper.INSTANCE.productToProductResponseDto(product);
    }

    @Override
    public void updateProductById(int productId, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.NOT_FOUND, new ErrorParam(Errors.PRODUCT_ID)))
        );
        product.setProductName(productRequestDto.getProductName());
        product.setUnitPrice(productRequestDto.getUnitPrice());
        product.setWeight(productRequestDto.getWeight());
        productRepository.save(product);
    }

    @Override
    public void deleteProductById(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.NOT_FOUND, new ErrorParam(Errors.PRODUCT_ID)))
        );
        productRepository.delete(product);
    }

    @Override
    public void addNewProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setProductName(productRequestDto.getProductName());
        product.setUnitPrice(productRequestDto.getUnitPrice());
        product.setWeight(productRequestDto.getWeight());
        productRepository.save(product);
    }
}
