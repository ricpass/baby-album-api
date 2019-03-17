package com.ricardopassarella.nbrown.baby

import com.ricardopassarella.nbrown.baby.dto.BabyResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.LocalDate

import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

class BabyControllerTest extends Specification {

    def service = Mock BabyService

    @Subject
    static BabyController controller

    void setup() {
        controller = new BabyController(service)
    }

    @Unroll
    def "when getting baby details and receive response #serviceResponse from service, should return #expectedResponse"() {
        given:
        def clientId = 'e0b9e335251a74cad293597d6b0d8341'
        when:
        def response = controller.getBabyDetails(clientId)

        then:
        1 * service.getBabyDetails(clientId) >> serviceResponse

        and:
        response == expectedResponse

        where:
        serviceResponse                                               || expectedResponse
        Optional.of(new BabyResponse("bla", "bla2", LocalDate.now())) || new ResponseEntity<>(new BabyResponse("bla", "bla2", LocalDate.now()), OK)
        Optional.empty()                                              || new ResponseEntity<>(HttpStatus.NOT_FOUND)
    }

    def "when updating baby details from service, should return #expectedResponse"() {
        given:
        def babyRequest = new BabyRequest("bla", "bla2", LocalDate.now())

        def clientId = 'e0b9e335251a74cad293597d6b0d8341'

        when:
        def response = controller.updateBabyDetails(clientId, babyRequest)

        then:
        1 * service.updateBabyDetails(clientId, babyRequest)

        and:
        response == new ResponseEntity<>(NO_CONTENT)
    }
}
