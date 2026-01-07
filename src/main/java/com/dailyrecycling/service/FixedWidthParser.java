package com.dailyrecycling.service;

import com.dailyrecycling.model.PolicyRecord;

public class FixedWidthParser {

    /**
     * Parse fixed-width record into PolicyRecord object
     * Based on document specifications for column positions
     */
    public PolicyRecord parseRecord(String record) {
        if (record == null || record.length() < 4060) {
            throw new IllegalArgumentException("Record length insufficient");
        }

        PolicyRecord policyRecord = new PolicyRecord();
        policyRecord.setRawRecord(record);

        // Duration of Insurance - Position 299, Length 3
        if (record.length() > 302) {
            policyRecord.setDurationOfInsurance(record.substring(299, 302));
        }

        // Policy Start Date - Positions 310-318, Length 8
        if (record.length() > 318) {
            policyRecord.setPolicyStartDate(record.substring(310, 318));
        }

        // Policy End Date - Positions 318-326, Length 8
        if (record.length() > 326) {
            policyRecord.setPolicyEndDate(record.substring(318, 326));
        }

        // Financing Type - Position 124-125, Length 1
        if (record.length() > 125) {
            policyRecord.setFinancingType(record.substring(124, 125));
        }

        // Financing Rate - Positions 213-218, Length 5
        if (record.length() > 218) {
            policyRecord.setFinancingRate(record.substring(213, 218));
        }

        // Total Premium Amount - Positions 338-349, Length 11
        if (record.length() > 349) {
            policyRecord.setTotalPremiumAmount(record.substring(338, 349));
        }

        // First Line Address - Positions 3928-3960, Length 32
        if (record.length() > 3960) {
            policyRecord.setFirstLineAddress(record.substring(3928, 3960).trim());
        }

        // Second Line Address - Positions 3960-3992, Length 32
        if (record.length() > 3992) {
            policyRecord.setSecondLineAddress(record.substring(3960, 3992).trim());
        }

        // City - Positions 4024-4050, Length 26
        if (record.length() > 4050) {
            policyRecord.setCity(record.substring(4024, 4050).trim());
        }

        // Postal Code - Positions 4050-4060, Length 10
        if (record.length() > 4060) {
            policyRecord.setPostalCode(record.substring(4050, 4060).trim());
        }

        return policyRecord;
    }

    /**
     * Update fixed-width record with new values
     */
    public String updateRecord(String originalRecord, PolicyRecord updates) {
        StringBuilder record = new StringBuilder(originalRecord);

        // Update Duration of Insurance (Position 299, Length 3)
        if (updates.getDurationOfInsurance() != null && record.length() > 302) {
            String duration = String.format("%3s", updates.getDurationOfInsurance()).replace(' ', '0');
            record.replace(299, 302, duration.substring(0, Math.min(3, duration.length())));
        }

        // Update Policy Start Date (Positions 310-318)
        if (updates.getPolicyStartDate() != null && record.length() > 318) {
            String startDate = String.format("%-8s", updates.getPolicyStartDate()).substring(0, 8);
            record.replace(310, 318, startDate);
        }

        // Update Policy End Date (Positions 318-326)
        if (updates.getPolicyEndDate() != null && record.length() > 326) {
            String endDate = String.format("%-8s", updates.getPolicyEndDate()).substring(0, 8);
            record.replace(318, 326, endDate);
        }

        // Update Financing Type (Position 124-125)
        if (updates.getFinancingType() != null && record.length() > 125) {
            record.replace(124, 125, updates.getFinancingType());
        }

        // Update Financing Rate (Positions 213-218)
        if (updates.getFinancingRate() != null && record.length() > 218) {
            String rate = String.format("%-5s", updates.getFinancingRate()).substring(0, 5);
            record.replace(213, 218, rate);
        }

        // Update Total Premium Amount (Positions 338-349)
        if (updates.getTotalPremiumAmount() != null && record.length() > 349) {
            String premium = String.format("%-11s", updates.getTotalPremiumAmount()).substring(0, 11);
            record.replace(338, 349, premium);
        }

        // Update First Line Address (Positions 3928-3960)
        if (updates.getFirstLineAddress() != null && record.length() > 3960) {
            String address1 = String.format("%-32s", updates.getFirstLineAddress()).substring(0, 32);
            record.replace(3928, 3960, address1);
        }

        // Update Second Line Address (Positions 3960-3992)
        if (updates.getSecondLineAddress() != null && record.length() > 3992) {
            String address2 = String.format("%-32s", updates.getSecondLineAddress()).substring(0, 32);
            record.replace(3960, 3992, address2);
        }

        // Update City (Positions 4024-4050)
        if (updates.getCity() != null && record.length() > 4050) {
            String city = String.format("%-26s", updates.getCity()).substring(0, 26);
            record.replace(4024, 4050, city);
        }

        // Update Postal Code (Positions 4050-4060)
        if (updates.getPostalCode() != null && record.length() > 4060) {
            String postal = String.format("%-10s", updates.getPostalCode()).substring(0, 10);
            record.replace(4050, 4060, postal);
        }

        return record.toString();
    }
}
