package fraud.detection.domain.fraud

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoClient
import fraud.detection.domain.Transaction
import fraud.detection.domain.customer.CustomerService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FraudService(val fraudRepository: FraudRepository, val objectMapper: ObjectMapper,
                   val customerService: CustomerService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Throws(JsonProcessingException::class)
    fun analyzeTransaction(transaction: Transaction) {
        log.info("Analyzing transaction {}", transaction)
        //    val customer = customerService.get(transaction.getCustomerId());
        fraudRepository.save(Fraud())
        log.info("Analyzed transaction {}", transaction)
    }

}