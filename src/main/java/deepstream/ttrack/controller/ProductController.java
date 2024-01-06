package deepstream.ttrack.controller;

import deepstream.ttrack.common.constant.Constant;
import deepstream.ttrack.dto.ResponseJson;
import deepstream.ttrack.dto.product.ProductRequestDto;
import deepstream.ttrack.dto.product.ProductResponseDto;
import deepstream.ttrack.service.ProductService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/get")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseJson<List<ProductResponseDto>>> getAllProduct() {
        logger.info("getAllProduct");
        List<ProductResponseDto> products = productService.getAllProduct();
        return ResponseEntity.ok().body(
                new ResponseJson<>(products, HttpStatus.OK, Constant.SUCCESS));

    }

    @GetMapping("/get/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<ProductResponseDto>> getProductById(@PathVariable int productId) {
        logger.info("getProductById");
        ProductResponseDto product = productService.getProductById(productId);
        return ResponseEntity.ok().body(
                new ResponseJson<>(product, HttpStatus.OK, Constant.SUCCESS));

    }

    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> updateProductById(
            @PathVariable int productId,
            @RequestBody ProductRequestDto product) {
        logger.info("updateProductById");
        productService.updateProductById(productId, product);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.UPDATE_PRODUCT_SUCCESS));

    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> addNewProduct(@RequestBody ProductRequestDto product) {
        logger.info("addNewProduct");
        productService.addNewProduct(product);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.ADD_PRODUCT_SUCCESS));

    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ResponseJson<Boolean>> deleteProductById(@PathVariable int productId) {
        logger.info("deleteProductById");
        productService.deleteProductById(productId);
        return ResponseEntity.ok().body(
                new ResponseJson<>(true, HttpStatus.OK, Constant.DELETE_PRODUCT_SUCCESS));

    }
}
