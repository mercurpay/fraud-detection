package fraud.detection.domain.customer

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomerController(val customerService: CustomerService) {

    @PostMapping
    fun post(@RequestBody payload: NewCustomerRequest) : ResponseEntity<*> {
        val customer = customerService.createCustomer(payload)
        return ResponseEntity.status(HttpStatus.CREATED).body(customer)
    }

}