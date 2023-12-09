package com.invexdijin.msantecedentreport.application.core.domain.response.antecedents;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Info {
    public String documentType;
    public String documentNumber;
    public String fullName;
    public String firstName;
    public String lastName;
    public List<String> arrayName;
    public String details;
    public String legend;
    public String expeditionDate;
    public String expeditionPlace;
    public String dateOfBirth;
    public List<Antecedente> antecedentes;
    public boolean isRequired;
    public Date searchDate;
    public String pdfBase64;

}
