package com.dailyrecycling.service;

import com.dailyrecycling.model.PolicyRecord;
import java.util.Map;

public class ErrorCorrectionService {

    private DatabaseService databaseService;
    private FixedWidthParser fixedWidthParser;
    private EmailService emailService;

    public void setDatabaseService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public void setFixedWidthParser(FixedWidthParser fixedWidthParser) {
        this.fixedWidthParser = fixedWidthParser;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Correct Start Date - End Date Issue (Event Type 1RA)
     */
    public PolicyRecord correctStartDateEndDateIssue(PolicyRecord record) {
        // Update Policy Start Date = Policy End Date
        record.setPolicyStartDate(record.getPolicyEndDate());
        
        // Update Duration of Insurance = 0
        record.setDurationOfInsurance("000");
        
        // Update Total Premium Amount = 00000000000
        record.setTotalPremiumAmount("00000000000");
        
        return record;
    }

    /**
     * Correct Event Type other than 1RA & Gross Amount = 0
     */
    public PolicyRecord correctGrossAmountZero(PolicyRecord record) {
        Double grossAmount = databaseService.getGrossAmountByPolicyNumber(record.getPolicyNumber());
        
        if (grossAmount != null && grossAmount > 0 && !"1RA".equals(record.getEventType())) {
            // If gross amount > 0 and event is not 1RA, no modification needed
            return record;
        }
        
        // Update Policy End Date = Policy Start Date
        record.setPolicyEndDate(record.getPolicyStartDate());
        
        // Update Total Premium Amount = 00000000000
        record.setTotalPremiumAmount("00000000000");
        
        return record;
    }

    /**
     * Correct Financing Type
     */
    public PolicyRecord correctFinancingType(PolicyRecord record) {
        // Set Financing Type to "2" if empty
        if (record.getFinancingType() == null || record.getFinancingType().trim().isEmpty()) {
            record.setFinancingType("2");
        }
        
        // Replace dash with "10" in Financing Rate
        if (record.getFinancingRate() != null && record.getFinancingRate().contains("-")) {
            record.setFinancingRate("10");
        }
        
        return record;
    }

    /**
     * Correct Address Fields
     */
    public PolicyRecord correctAddressFields(PolicyRecord record) {
        Long actorId = databaseService.getActorIdByPolicyNumber(record.getPolicyNumber());
        
        if (actorId == null) {
            return record;
        }
        
        Map<String, Object> address = databaseService.getAddressByActorId(actorId);
        
        if (address == null) {
            return record;
        }
        
        // Update address fields from database
        if ((record.getFirstLineAddress() == null || record.getFirstLineAddress().trim().isEmpty()) 
            && address.get("first_line_address") != null) {
            record.setFirstLineAddress(String.valueOf(address.get("first_line_address")));
        }
        
        if ((record.getSecondLineAddress() == null || record.getSecondLineAddress().trim().isEmpty()) 
            && address.get("second_line_address") != null) {
            record.setSecondLineAddress(String.valueOf(address.get("second_line_address")));
        }
        
        if ((record.getCity() == null || record.getCity().trim().isEmpty()) 
            && address.get("city") != null) {
            record.setCity(String.valueOf(address.get("city")));
        }
        
        if ((record.getPostalCode() == null || record.getPostalCode().trim().isEmpty()) 
            && address.get("postal_code") != null) {
            record.setPostalCode(String.valueOf(address.get("postal_code")));
        }
        
        return record;
    }

    /**
     * Correct BCU Number
     */
    public PolicyRecord correctBcuNumber(PolicyRecord record) {
        Long actorId = databaseService.getActorIdByPolicyNumber(record.getPolicyNumber());
        
        if (actorId == null) {
            return record;
        }
        
        String bcuNumber = databaseService.getBcuNumberByActorId(actorId);
        
        if (bcuNumber != null && !bcuNumber.trim().isEmpty()) {
            record.setBcuNumber(bcuNumber);
        } else {
            // Send email to back office
            String appCode = extractAppCode(record.getPolicyNumber());
            emailService.sendBcuNumberMissingEmail(record.getPolicyNumber(), appCode);
        }
        
        return record;
    }

    /**
     * Extract application code from policy number
     */
    private String extractAppCode(String policyNumber) {
        // Extract app code before master policy code
        // Format: {APP_CODE}{MASTER_POLICY_CODE}
        // Example: FR103747091001FR5H01 -> App Code: FR103747091001
        if (policyNumber != null && policyNumber.length() > 5) {
            int lastIndex = policyNumber.lastIndexOf("FR");
            if (lastIndex > 0) {
                return policyNumber.substring(0, lastIndex);
            }
        }
        return "";
    }

    /**
     * Get application code for email routing
     */
    public String getApplicationCode(String policyNumber) {
        return extractAppCode(policyNumber);
    }
}
