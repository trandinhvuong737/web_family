package deepstream.ttrack.service;

import deepstream.ttrack.common.utils.WebUtils;
import deepstream.ttrack.dto.product.ProductRequestDto;
import deepstream.ttrack.dto.product.ProductResponseDto;
import deepstream.ttrack.entity.Product;
import deepstream.ttrack.entity.User;
import deepstream.ttrack.exception.BadRequestException;
import deepstream.ttrack.exception.ErrorParam;
import deepstream.ttrack.exception.Errors;
import deepstream.ttrack.exception.SysError;
import deepstream.ttrack.mapper.ProductMapper;
import deepstream.ttrack.repository.ProductRepository;
import deepstream.ttrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceIml implements ProductService{

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public static final int SUPER_ADMIN_ID = 1;
    public static final int ADMIN_ID = 2;

    public ProductServiceIml(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ProductResponseDto> getAllProduct() {
        String username = WebUtils.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException(
                        new SysError(Errors.ERROR_USER_NOT_FOUND, new ErrorParam(Errors.USERNAME)))
        );
        List<ProductResponseDto> responseDtos = new ArrayList<>();
        List<Product> products ;

        int roleId = user.getRole().getRoleId();
        if( roleId == SUPER_ADMIN_ID || roleId == ADMIN_ID){
            products = productRepository.findAll();
        }else {
            products = user.getProducts();
        }

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
        product.setTransportFee(productRequestDto.getTransportFee());

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
        boolean check = productRepository.checkProductName(productRequestDto.getProductName());
        if(check){
            throw new BadRequestException(
                    new SysError(Errors.PRODUCT_ALREADY_EXISTS, new ErrorParam(Errors.PRODUCT_NAME)));
        }
        Product product = new Product();
        product.setProductName(productRequestDto.getProductName());
        product.setUnitPrice(productRequestDto.getUnitPrice());
        product.setWeight(productRequestDto.getWeight());
        product.setTransportFee(productRequestDto.getTransportFee());

        productRepository.save(product);
    }
}
