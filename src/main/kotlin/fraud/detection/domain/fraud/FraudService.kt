package fraud.detection.domain.fraud

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import fraud.detection.domain.Transaction
import fraud.detection.domain.crm.CrmService
import fraud.detection.domain.customer.CustomerService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import fraud.detection.domain.crm.Event

@Service
class FraudService(
        val fraudRepository: FraudRepository,
        val customerService: CustomerService,
        val crmService: CrmService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Throws(JsonProcessingException::class)
    fun analyzeTransaction(transaction: Transaction) {
        log.info("Analyzing transaction {}", transaction)
        val fraud = fraudRepository.save(Fraud(orderId = transaction.orderId))
        notifyFraud(fraud.id, fraud.orderId)
        log.info("Analyzed transaction {}", transaction)
    }

    private fun notifyFraud(fraudId: String, orderId: String) {
        val eventData = mapOf("fraud_id" to fraudId)
        val event = Event("FRAUD", eventData)
        crmService.notifyEvent(orderId, event)
    }

    fun getFrauds() = fraudRepository.findAll()

    fun getFraud(id: String) = fraudRepository.findById(id)

    fun invalidateFraud(fraud: Fraud) {
        fraud.status = FraudStatus.NOT_SUSPECTED
        fraudRepository.save(fraud)
    }

    fun deleteAll() = fraudRepository.deleteAll()

}