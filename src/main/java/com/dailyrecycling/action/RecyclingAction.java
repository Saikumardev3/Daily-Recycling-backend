package com.dailyrecycling.action;

import com.dailyrecycling.model.PolicyRecord;
import com.dailyrecycling.service.ErrorCorrectionService;
import com.dailyrecycling.service.FixedWidthParser;
import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class RecyclingAction extends ActionSupport {

    private FixedWidthParser fixedWidthParser;
    private ErrorCorrectionService errorCorrectionService;

    @Getter
    @Setter
    private Map<String, Object> result = new HashMap<>();

    @Getter
    @Setter
    private Map<String, Object> errorResult = new HashMap<>();

    @Getter
    @Setter
    private String record;

    @Getter
    @Setter
    private String errorType;

    @Getter
    @Setter
    private String policyNumber;

    // Setter injection for Struts-Spring integration
    public void setFixedWidthParser(FixedWidthParser fixedWidthParser) {
        this.fixedWidthParser = fixedWidthParser;
    }

    public void setErrorCorrectionService(ErrorCorrectionService errorCorrectionService) {
        this.errorCorrectionService = errorCorrectionService;
    }

    /**
     * Health check endpoint
     */
    public String health() {
        result.put("status", "UP");
        result.put("service", "Daily Recycling Backend");
        result.put("framework", "Apache Struts 2");
        return SUCCESS;
    }

    /**
     * Parse fixed-width record
     */
    public String parseRecord() {
        try {
            if (record == null || record.trim().isEmpty()) {
                errorResult.put("error", "Record is required");
                errorResult.put("message", "Please provide a valid fixed-width record");
                return ERROR;
            }

            PolicyRecord policyRecord = fixedWidthParser.parseRecord(record);
            result.put("success", true);
            result.put("data", policyRecord);
            return SUCCESS;

        } catch (Exception e) {
            errorResult.put("error", "Parsing failed");
            errorResult.put("message", e.getMessage());
            return ERROR;
        }
    }

    /**
     * Correct record errors
     */
    public String correctRecord() {
        try {
            if (record == null || record.trim().isEmpty()) {
                errorResult.put("error", "Record is required");
                return ERROR;
            }

            PolicyRecord policyRecord = fixedWidthParser.parseRecord(record);
            
            // Apply corrections based on error type
            if (errorType != null) {
                switch (errorType.toUpperCase()) {
                    case "START_DATE_END_DATE":
                        policyRecord = errorCorrectionService.correctStartDateEndDateIssue(policyRecord);
                        break;
                    case "GROSS_AMOUNT_ZERO":
                        policyRecord = errorCorrectionService.correctGrossAmountZero(policyRecord);
                        break;
                    case "FINANCING_TYPE":
                        policyRecord = errorCorrectionService.correctFinancingType(policyRecord);
                        break;
                    case "ADDRESS_FIELDS":
                        policyRecord = errorCorrectionService.correctAddressFields(policyRecord);
                        break;
                    case "BCU_NUMBER":
                        policyRecord = errorCorrectionService.correctBcuNumber(policyRecord);
                        break;
                    default:
                        // Apply all corrections
                        policyRecord = errorCorrectionService.correctStartDateEndDateIssue(policyRecord);
                        policyRecord = errorCorrectionService.correctGrossAmountZero(policyRecord);
                        policyRecord = errorCorrectionService.correctFinancingType(policyRecord);
                        policyRecord = errorCorrectionService.correctAddressFields(policyRecord);
                        policyRecord = errorCorrectionService.correctBcuNumber(policyRecord);
                }
            }

            String correctedRecord = fixedWidthParser.updateRecord(record, policyRecord);
            
            result.put("success", true);
            result.put("message", "Record corrected successfully");
            result.put("correctedRecord", correctedRecord);
            result.put("policyRecord", policyRecord);
            return SUCCESS;

        } catch (Exception e) {
            errorResult.put("error", "Correction failed");
            errorResult.put("message", e.getMessage());
            return ERROR;
        }
    }

    /**
     * Process batch of records
     */
    public String processBatch() {
        try {
            // Batch processing logic will be implemented
            result.put("success", true);
            result.put("message", "Batch processing initiated");
            return SUCCESS;
        } catch (Exception e) {
            errorResult.put("error", "Batch processing failed");
            errorResult.put("message", e.getMessage());
            return ERROR;
        }
    }

    /**
     * Get error types
     */
    public String getErrorTypes() {
        Map<String, String> errorTypes = new HashMap<>();
        errorTypes.put("START_DATE_END_DATE", "Start Date - End Date Issue (Event Type 1RA)");
        errorTypes.put("GROSS_AMOUNT_ZERO", "Event Type ≠ 1RA & Gross Amount = 0");
        errorTypes.put("FINANCING_TYPE", "Financing Type Missing");
        errorTypes.put("ADDRESS_FIELDS", "Address Fields (1st Line, 2nd Line, City, Postal Code)");
        errorTypes.put("BCU_NUMBER", "BCU Number Null");
        errorTypes.put("CODE_PAYS_ASSURE", "Code Pays Assure");
        errorTypes.put("DURATION_EXCEEDS_9999", "Duration Exceeds 9999 Days");

        result.put("success", true);
        result.put("errorTypes", errorTypes);
        return SUCCESS;
    }
}
