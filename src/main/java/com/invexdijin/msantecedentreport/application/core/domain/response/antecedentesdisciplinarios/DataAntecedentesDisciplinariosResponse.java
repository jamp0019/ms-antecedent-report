package com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios;

import java.util.List;

@lombok.Data
public class DataAntecedentesDisciplinariosResponse {

    public String documentType;
    public String documentNumber;
    public String fullName;
    public String firstName;
    public String lastName;
    public List<String> arrayName;
    public List<Antecedente> antecedentes;
    public boolean isRequired;
    public String legend;
    public String expeditionDate;
    public String expeditionPlace;
    public String dateOfBirth;
}
