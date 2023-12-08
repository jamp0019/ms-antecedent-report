package com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios;

import lombok.Data;

@Data
public class ResponseAntecedentesDisciplinarios {
    public DataAntecedentesDisciplinariosResponse data;
    public SignatureAntecedentesDisciplinariosResponse signature;
    public String id;
}
