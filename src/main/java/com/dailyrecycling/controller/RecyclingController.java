package com.dailyrecycling.controller;

import com.dailyrecycling.model.PolicyRecord;
import com.dailyrecycling.service.ErrorCorrectionService;
import com.dailyrecycling.service.FixedWidthParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recycling")
@CrossOrigin(origins = "http://localhost:3000")
public class RecyclingController {

    @Autowired
    private FixedWidthParser fixedWidthParser;

    @Autowired
    private ErrorCorrectionService errorCorrectionService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "Daily Recycling Backend");
        result.put("framework", "Spring Boot");
        return ResponseEntity.ok(result);
    }

    /**
     * Parse fixed-width record
     */
    @PostMapping("/parse")
    public ResponseEntity<?> parseRecord(@RequestBody Map<String, String> request) {
        try {
            String record = request.get("record");
            if (record == null || record.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Record is required");
                error.put("message", "Please provide a valid fixed-width record");
                return ResponseEntity.badRequest().body(error);
            }

            PolicyRecord policyRecord = fixedWidthParser.parseRecord(record);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", policyRecord);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Parsing failed");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Correct record errors
     */
    @PostMapping("/correct")
    public ResponseEntity<?> correctRecord(@RequestBody Map<String, String> request) {
        try {
            String record = request.get("record");
            String errorType = request.get("errorType");

            if (record == null || record.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Record is required");
                return ResponseEntity.badRequest().body(error);
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
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Record corrected successfully");
            result.put("correctedRecord", correctedRecord);
            result.put("policyRecord", policyRecord);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Correction failed");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Process batch of records
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> processBatch() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Batch processing initiated");
        return ResponseEntity.ok(result);
    }

    /**
     * Get error types
     */
    @GetMapping("/errorTypes")
    public ResponseEntity<Map<String, Object>> getErrorTypes() {
        Map<String, String> errorTypes = new HashMap<>();
        errorTypes.put("START_DATE_END_DATE", "Start Date - End Date Issue (Event Type 1RA)");
        errorTypes.put("GROSS_AMOUNT_ZERO", "Event Type ≠ 1RA & Gross Amount = 0");
        errorTypes.put("FINANCING_TYPE", "Financing Type Missing");
        errorTypes.put("ADDRESS_FIELDS", "Address Fields (1st Line, 2nd Line, City, Postal Code)");
        errorTypes.put("BCU_NUMBER", "BCU Number Null");
        errorTypes.put("CODE_PAYS_ASSURE", "Code Pays Assure");
        errorTypes.put("DURATION_EXCEEDS_9999", "Duration Exceeds 9999 Days");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("errorTypes", errorTypes);
        return ResponseEntity.ok(result);
    }
}

