package com.ricardopassarella.nbrown.GeoLocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReverseGeocoding {

    private final RestTemplate restTemplate;
    private static final String GEOCODE_URl = "https://geocode.xyz/{latitude},{longitude}?json=1";
    private static final int TRIES = 2;

    /**
     * Using service geocode.xyz to do reverse geocoding.
     * Added headers to pretend it is valid user, otherwise geocode only return 403 (Forbidden)
     * <p>
     * Try 2 times to get address details.
     */
    public Optional<String> getFullAddress(BigDecimal latitude, BigDecimal longitude) {
        if (latitude == null || longitude == null)
            return Optional.empty();

        for (int i = 0; i < TRIES; i++) {
            Optional<String> fullAddress = makeRequest(latitude.toString(), longitude.toString());

            if (fullAddress.isPresent())
                return fullAddress;

            sleep();
        }

        return Optional.empty();
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Optional<String> makeRequest(String latitude, String longitude) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:65.0) Gecko/20100101 Firefox/65.0");
            headers.add("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.add("Accept-Language", "en-US,en;q=0.5");
            headers.add("Connection", "keep-alive");
            headers.add("Cookie",
                    "__cfduid=d3f4fd7e0980501154fbb301229441d68155283432432; xyzh=xyzh; geocodes.xyz=605894639");
            headers.add("Upgrade-Insecure-Requests", "1");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<GeoCodeResponse> response = restTemplate.exchange(GEOCODE_URl, HttpMethod.GET, entity,
                    GeoCodeResponse.class, latitude, longitude);

            return Optional.ofNullable(response.getBody())
                    .map(this::getFullAddress);
        } catch (RestClientException e) {
            log.warn("Failed to get Reverse Geolocation for {},{}", latitude, longitude);
        } catch (Exception e){
            log.error("Error when trying to get geolocation", e);
        }
        return Optional.empty();
    }

    private String getFullAddress(GeoCodeResponse geoCodeResponse) {

        return safeToString(geoCodeResponse.getStnumber())
                + safeToString(geoCodeResponse.getStaddress())
                + safeToString(geoCodeResponse.getPostal())
                + safeToString(geoCodeResponse.getCity())
                + safeToString(geoCodeResponse.getRegion())
                + safeToString(geoCodeResponse.getCountry());
    }

    private String safeToString(Object obj) {
        return obj == null ? "" : obj.toString() + " ";
    }
}
