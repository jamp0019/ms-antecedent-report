package com.invexdijin.msantecedentreport.application.core.domain.request;

import lombok.Data;

@Data
public class RequestAntecedentReport {
    private String name;
    private String email;
    private String documentType;
    private String documentNumber;
}
