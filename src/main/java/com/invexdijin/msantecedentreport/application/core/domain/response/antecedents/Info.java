package com.invexdijin.msantecedentreport.application.core.domain.response.antecedents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Info {
    private String documentType;
    private String documentNumber;
    private String fullName;
    private String firstName;
    private String lastName;
    private List<String> arrayName;
    private String details;
    private String legend;
    private String expeditionDate;
    private String expeditionPlace;
    private String dateOfBirth;
    private List<Antecedente> antecedentes;
    private boolean isRequired;
    private Date searchDate;
    private String pdfBase64;
    @JsonProperty("NUIP")
    private String nuip;
    private String address;
    private String department;
    private String municipality;
    private String pollingTable;
    private String votingStation;

}
