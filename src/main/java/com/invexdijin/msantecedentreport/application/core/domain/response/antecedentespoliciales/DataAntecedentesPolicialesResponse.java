package com.invexdijin.msantecedentreport.application.core.domain.response.antecedentespoliciales;

import java.util.List;

@lombok.Data
public class DataAntecedentesPolicialesResponse {

    public String documentType;
    public String documentNumber;
    public String details;
    public String fullName;
    public String firstName;
    public String lastName;
    public List<String> arrayName;
    public String legend;
    public String expeditionDate;
    public String expeditionPlace;
    public String dateOfBirth;
}
