package deepstream.ttrack.repository.address;

import deepstream.ttrack.entity.address.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<Ward,Long> {
}
