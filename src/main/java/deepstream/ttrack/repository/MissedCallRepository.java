package deepstream.ttrack.repository;

import deepstream.ttrack.entity.MissedCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissedCallRepository extends JpaRepository<MissedCall,Integer> {
}
