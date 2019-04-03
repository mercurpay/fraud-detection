package fraud.detection.domain.fraud

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "frauds")
data class Fraud(val id : String = UUID.randomUUID().toString(), var status: FraudStatus = FraudStatus.SUSPECTED)