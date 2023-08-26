package deepstream.ttrack.repository;

import deepstream.ttrack.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    Product getProductByProductName(String product);
}
