package com.invexdijin.msantecedentreport.application.core.domain.request;

import lombok.Data;

@Data
public class RequestSearch {
    private String name;
    private String email;
    private String documentType;
    private String documentNumber;
}
