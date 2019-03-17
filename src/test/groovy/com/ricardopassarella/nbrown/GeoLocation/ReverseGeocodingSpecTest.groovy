package com.ricardopassarella.nbrown.GeoLocation


import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.http.HttpStatus.OK

class ReverseGeocodingSpecTest extends Specification {

    def restTemplate = Mock RestTemplate

    @Subject
    static ReverseGeocoding reverseGeocoding;

    def setup() {
        reverseGeocoding = new ReverseGeocoding(restTemplate)
    }

    def "when try to get reverse geolocation from latitude #latitude and longitude #longitude"() {
        given:
        def geoCodeResponse = GeoCodeResponse.builder()
                .stnumber("10")
                .staddress("DOWNING STREET")
                .postal("SW1A 2AA")
                .city("LONDON")
                .region("Greater London, England")
                .country("United Kingdom")
                .build()

        when:
        def address = reverseGeocoding.getFullAddress(latitude, longitude)

        then:
        address == expectedAddress

        and:
        1 * restTemplate.exchange(*_) >> new ResponseEntity<>(geoCodeResponse, OK)

        where:
        latitude | longitude || expectedAddress
        51.50354 | -0.12768  || Optional.of("10 DOWNING STREET SW1A 2AA LONDON Greater London, England United Kingdom ")
    }

    def "return empty when latitude or longitude is null"() {
        when:
        def address = reverseGeocoding.getFullAddress(latitude, longitude)

        then:
        address == expectedAddress

        and:
        0 * restTemplate.exchange(*_)

        where:
        latitude | longitude || expectedAddress
        null     | null      || Optional.empty()
        51.50354 | null      || Optional.empty()
        null     | -0.12768  || Optional.empty()
    }

    def "return empty if geolocation return error status code"() {
        when:
        def address = reverseGeocoding.getFullAddress(latitude, longitude)

        then:
        address == expectedAddress

        and:
        2 * restTemplate.exchange(*_) >> { throw new Exception("") }

        where:
        latitude | longitude || expectedAddress
        51.50354 | -0.12768  || Optional.empty()
    }
}
