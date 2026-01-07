package com.dailyrecycling.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorCorrectionRequest {
    private List<String> policyNumbers;
    private String errorType;
    private String correctionAction;
}

