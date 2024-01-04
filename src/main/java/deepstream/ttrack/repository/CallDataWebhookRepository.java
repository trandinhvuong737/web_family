package deepstream.ttrack.repository;

import deepstream.ttrack.entity.CallHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallDataWebhookRepository extends JpaRepository<CallHistory,Integer> {

    @Query("DELETE FROM CallHistory m WHERE m.createdDate <= :startDate ")
    void deleteMissCallByDate(@Param("startDate") long startDate);

    @Query("SELECT m FROM CallHistory m WHERE m.status = false AND m.typeCall = 'MISSCALL'")
    List<CallHistory> findAllMissCall();

    @Query("SELECT COUNT(m.callId) FROM CallHistory m WHERE m.status = false AND m.typeCall = 'MISSCALL'")
    int getTotalMissCall();
}
