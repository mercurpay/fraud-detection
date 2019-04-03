package fraud.detection.infra

import com.fasterxml.jackson.databind.ObjectMapper
import fraud.detection.domain.fraud.FraudService
import fraud.detection.domain.Transaction
import io.nats.client.Connection
import io.nats.client.Dispatcher
import io.nats.client.Nats
import io.nats.client.Options
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.nio.charset.StandardCharsets

@Configuration
class NatsConfiguration {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @Throws(IOException::class, InterruptedException::class)
    fun createConnection(
            @Value("\${nats.host}") host: String,
            @Value("\${nats.username}") username: String,
            @Value("\${nats.password}") password: String
    ): Connection {
        log.info("Configuring NATS Connection, Host[{}]", host)
        val options = Options.Builder()
                .server("nats://$host:4222")
                .userInfo(username, password)
                .build()

        val connection = Nats.connect(options)
        log.info("Configured NATS Connection, Status=[{}]", connection.status)
        return connection
    }

    @Bean
    fun createDispatcher(connection: Connection, objectMapper: ObjectMapper,
                         fraudService: FraudService): Dispatcher {
        val topic = "fraud-analyze-topic"
        log.info("Configuring Dispatcher, Topic=[{}]", topic)
        val dispatcher = connection.createDispatcher { message ->
            val data = String(message.data, StandardCharsets.UTF_8)
            try {
                log.info("Received message $data")
                val transaction = objectMapper.readValue(data, Transaction::class.java)
                fraudService.analyzeTransaction(transaction)
            } catch (e: Exception) {
                log.error("Error to analyze {}", data, e)
            }
        }
        dispatcher.subscribe(topic)
        log.info("Configured Dispatcher, Topic=[{}]", topic)
        return dispatcher
    }

}