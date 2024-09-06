package com.nisum.userregistration.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Phone {
    private String phoneNumber;
    private String phoneCityCode;
    private String phoneCountryCode;
}
