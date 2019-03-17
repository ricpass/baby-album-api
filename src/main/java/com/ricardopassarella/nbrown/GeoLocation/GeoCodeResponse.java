package com.ricardopassarella.nbrown.GeoLocation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoCodeResponse {

    private String city;
    private String country;
    private String stnumber;
    private String staddress;
    private String region;
    private String postal;

}
