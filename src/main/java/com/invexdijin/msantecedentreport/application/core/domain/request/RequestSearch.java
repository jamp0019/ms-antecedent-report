package com.invexdijin.msantecedentreport.application.core.domain.request;

import lombok.Data;

@Data
public class RequestSearch {

    private String paymentName;
    private String paymentEmail;
    private String searchFullName;
    private String searchName;
    private String searchLastName;
    private String documentType;
    private String documentNumber;

}
