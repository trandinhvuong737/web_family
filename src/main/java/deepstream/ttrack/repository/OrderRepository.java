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
    @Query("SELECT o FROM Order o WHERE o.product = :productName")
    List<Order> getAllOrderByProduct(String productName);
    @Query("SELECT o FROM Order o WHERE o.createAt between :startDate and :endDate")
    List<Order> getOrderByDateRange(LocalDate startDate, LocalDate endDate);
    @Query("SELECT o FROM Order o WHERE o.createAt between :startDate and :endDate and o.product = :productName")
    List<Order> getOrderByDateRange(LocalDate startDate, LocalDate endDate, String productName);
    @Query("SELECT o FROM Order o WHERE o.createAt = :date and o.status = 'completed'")
    List<Order> getOrderByDate(LocalDate date);
    @Query("SELECT o FROM Order o WHERE o.createAt = :date and o.product = :productName and o.status = 'completed'")
    List<Order> getOrderByDate(LocalDate date, String productName);
    @Query("SELECT count (o.orderId) FROM Order o WHERE o.createAt = :date" + " and o.status = 'completed'")
    int countOrder(LocalDate date);
    @Query("SELECT count (o.orderId) FROM Order o WHERE o.createAt = :date" + " and o.status = 'completed' and o.product = :productName")
    int countOrder(LocalDate date, String productName);
    @Query("SELECT sum (o.quantity) FROM Order o WHERE o.createAt = :date" + " and o.status = 'completed'")
    Integer sumProduct(LocalDate date);
    @Query("SELECT sum (o.quantity) FROM Order o WHERE o.createAt = :date" + " and o.status = 'completed' and o.product = :productName")
    Integer sumProduct(LocalDate date,String productName);
    @Query("SELECT o FROM Order o WHERE o.status = 'pending' and o.createAt between :startDate and :endDate")
    List<Order> getOrdersByStatusEx(LocalDate startDate, LocalDate endDate);
    @Query("SELECT o FROM Order o WHERE o.status = 'pending' and o.createAt between :startDate and :endDate and o.product = :productName")
    List<Order> getOrdersByStatusEx(LocalDate startDate, LocalDate endDate, String productName );
    @Query("SELECT o FROM Order o WHERE o.phoneNumber = :phoneNumber and o.product = :productName")
    List<Order> getOrderByPhoneNumber(String phoneNumber, String productName);
    @Query("SELECT o FROM Order o WHERE o.createAt between :startDate and :endDate and o.product = :productName")
    List<Order> getOrderByDateRangeAndProduct(LocalDate startDate, LocalDate endDate, String productName);
}
