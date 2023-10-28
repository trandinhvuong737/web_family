package deepstream.ttrack.repository;

import deepstream.ttrack.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    Product getProductByProductName(String product);
    @Query("SELECT p.productName FROM Product p")
    List<String> findAllProductName();
}
