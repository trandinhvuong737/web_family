package deepstream.ttrack.repository;

import deepstream.ttrack.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT o FROM Order o")
    List<Order> getAllOrder();

    @Query("SELECT o FROM Order o WHERE o.createAt between :startDate and :endDate")
    List<Order> getOrderByDateRange(LocalDate startDate, LocalDate endDate);

    @Query("SELECT o FROM Order o WHERE o.createAt = :date")
    List<Order> getOrderByDate(LocalDate date);

    @Query("SELECT count (o.orderId) FROM Order o WHERE o.createAt = :date" + " and o.status = 'completed'")
    int countOrder(LocalDate date);

    @Query("SELECT sum (o.quantity) FROM Order o WHERE o.createAt = :date" + " and o.status = 'completed'")
    int sumProduct(LocalDate date);

    @Query("SELECT o FROM Order o WHERE o.status = 'pending' and o.createAt between :startDate and :endDate")
    List<Order> getOrdersByStatusEx(LocalDate startDate, LocalDate endDate);

}
