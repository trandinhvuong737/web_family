package deepstream.ttrack.repository;

import deepstream.ttrack.entity.MissedCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallDataWebhookRepository extends JpaRepository<MissedCall,Integer> {

    @Query("DELETE FROM MissedCall m WHERE m.createdDate <= :startDate ")
    void deleteMissCallByDate(@Param("startDate") long startDate);

    @Query("SELECT m FROM MissedCall m WHERE m.status = false AND m.direction = 'inbound'")
    List<MissedCall> findAllMissCall();

    @Query("SELECT COUNT(m.callId) FROM MissedCall m WHERE m.status = false AND m.direction = 'inbound'")
    int getTotalMissCall();
}
