package deepstream.ttrack.repository;

import deepstream.ttrack.entity.MissedCall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissedCallRepository extends JpaRepository<MissedCall,Integer> {
}
