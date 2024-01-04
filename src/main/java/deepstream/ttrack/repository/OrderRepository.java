package deepstream.ttrack.repository;

import deepstream.ttrack.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT o FROM Order o order by o.createAt DESC ")
    List<Order> getAllOrder();

    @Query("SELECT o FROM Order o WHERE o.user.username = :username order by o.createAt DESC")
    List<Order> getAllOrderByProduct(String username);

    @Query("SELECT o FROM Order o WHERE o.createAt between :startDate and :endDate " +
            "order by o.createAt DESC")
    List<Order> getOrderByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.createAt between :startDate and :endDate and o.user.username = :username " +
            "order by o.createAt DESC")
    List<Order> getOrderByDateRange(LocalDateTime startDate, LocalDateTime endDate, String username);

    @Query("SELECT o FROM Order o WHERE o.createAt between :startDays and :endDays and o.status = 'completed'")
    List<Order> getOrderByDate(LocalDateTime startDays, LocalDateTime endDays);

    @Query("SELECT o FROM Order o WHERE o.createAt between :startDays and :endDays and o.user.username = :username and o.status = 'completed'")
    List<Order> getOrderByDate(LocalDateTime startDays, LocalDateTime endDays, String username);

    @Query("SELECT count (o.orderId) FROM Order o WHERE o.createAt between :startDays and :endDays"
            + " and o.status = 'completed'")
    int countOrder(LocalDateTime startDays, LocalDateTime endDays);

    @Query("SELECT count (o.orderId) FROM Order o WHERE o.createAt between :startDays and :endDays"
            + " and o.status = 'completed' and o.user.username = :username")
    int countOrder(LocalDateTime startDays, LocalDateTime endDays, String username);

    @Query("SELECT sum (o.quantity) FROM Order o WHERE o.createAt between :startDays and :endDays"
            + " and o.status = 'completed'")
    Integer sumProduct(LocalDateTime startDays, LocalDateTime endDays);

    @Query("SELECT sum (o.quantity) FROM Order o WHERE o.createAt between :startDays and :endDays"
            + " and o.status = 'completed' and o.user.username = :username")
    Integer sumProduct(LocalDateTime startDays, LocalDateTime endDays, String username);

    @Query("SELECT o FROM Order o WHERE o.status = 'pending' and " +
            "o.createAt between :startDate and :endDate order by o.createAt DESC")
    List<Order> getOrdersByStatusEx(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.phoneNumber = :phoneNumber and o.product = :productName")
    List<Order> getOrderByPhoneNumber(String phoneNumber, String productName);

    @Query("SELECT o FROM Order o WHERE o.createAt >= :startDate AND o.createAt <= :endDate " +
            "AND o.status != 'initialization' AND o.user.username = :username " +
            "ORDER BY o.createAt DESC")
    List<Order> getOrderByDateRangeAndUsername(LocalDateTime startDate, LocalDateTime endDate, String username);

    @Query("SELECT o FROM Order o WHERE o.createAt >= :startDate AND o.createAt <= :endDate AND o.status != 'initialization' " +
            "ORDER BY o.createAt DESC ")
    List<Order> getOrderByDateRangeAndUsername(LocalDateTime startDate, LocalDateTime endDate);

    Order getOrderByOrderId(int id);

    @Query("SELECT count (o.orderId) FROM Order o WHERE o.user.id =:id")
    int totalOrOrderByUserOrderId(int id);

    @Query("SELECT count (o.orderId) FROM Order o WHERE o.user.id =:id and o.status = 'completed'")
    int totalOrOrderCompletedByUserOrderId(int id);

    @Query("SELECT count (o.orderId) FROM Order o WHERE o.user.id =:id and o.status = 'pending'")
    int totalOrOrderPendingByUserOrderId(int id);

    @Query("SELECT count (o.orderId) FROM Order o WHERE o.user.id =:id and o.status = 'canceled'")
    int totalOrOrderCanceledByUserOrderId(int id);
}
