package com.dailyrecycling.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRecord {
    private String recordId;
    private String policyNumber;
    private String eventType;
    private String policyStartDate;
    private String policyEndDate;
    private String durationOfInsurance;
    private String totalPremiumAmount;
    private String financingType;
    private String financingRate;
    private String firstLineAddress;
    private String secondLineAddress;
    private String city;
    private String postalCode;
    private String bcuNumber;
    private String countryCode;
    private String grossAmount;
    private String rawRecord; // Full fixed-width record
}


