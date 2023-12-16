package com.invexdijin.msantecedentreport.application.core.domain.response;

import lombok.Data;
@Data
public class ConsolidatedResponse {
    private String fullName;
    private String firstName;
    private String state;
    private String documentNumber;
    private String documentType;
    private String attorneyOfficeLegend;
    private String policeDetail;
    private String publicSpendingWatchdogMessage;
    private String address;
    private String department;
    private String municipality;
    public ConsolidatedResponse(){
        this.state="Vígente";
    }
}
