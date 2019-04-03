package fraud.detection.domain.fraud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudRepository extends CrudRepository<Fraud, String> {}