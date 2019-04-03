package fraud.detection.domain.fraud

import fraud.detection.domain.Transaction
import fraud.detection.domain.crm.CrmService
import fraud.detection.domain.crm.Event
import fraud.detection.domain.customer.CustomerService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FraudService(
        val fraudRepository: FraudRepository,
        val customerService: CustomerService,
        val crmService: CrmService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun analyzeTransaction(transaction: Transaction) {
        log.info("Analyzing transaction {}", transaction)
        val customer = customerService.get(transaction.customerId)
        val fraud = fraudRepository.save(Fraud(orderId = transaction.orderId))
        notifyFraud("FRAUD", fraud)
        log.info("Analyzed transaction {}", transaction)
    }

    fun invalidateFraud(fraud: Fraud) {
        log.info("Invalidating {}", fraud)
        fraud.status = FraudStatus.NOT_SUSPECTED
        fraudRepository.save(fraud)
        notifyFraud("APPROVED", fraud)
        log.info("Invalidated {}", fraud)
    }

    private fun notifyFraud(type: String, fraud: Fraud) {
        val eventData = mapOf("fraudId" to fraud.id)
        val event = Event(type, eventData)
        crmService.notifyEvent(fraud.orderId, event)
    }

    fun getFrauds() = fraudRepository.findAll()

    fun getFraud(id: String) = fraudRepository.findById(id)

    fun deleteAll() = fraudRepository.deleteAll()

}